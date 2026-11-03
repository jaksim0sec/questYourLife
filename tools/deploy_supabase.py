#!/usr/bin/env python3
"""Deploy only this app's foundation and Edge function; credentials stay in environment."""
import argparse
import hashlib
import json
import os
from pathlib import Path
import secrets
import subprocess
import tempfile
import urllib.error
import urllib.request

PROJECT = 'xpqjjfcyiirospxzykqd'
ROOT = Path(__file__).resolve().parents[1]
MIGRATION = ROOT / 'supabase/migrations/202611030001_personal_foundation.sql'


def project_url(project):
    if project != PROJECT:
        raise ValueError('This deployment is restricted to the authorized project.')
    return 'https://api.supabase.com/v1/projects/' + project


def deployment_sql():
    migration = MIGRATION.read_text()
    marker = 'questyourlife:202611030001:sha256:' + hashlib.sha256(migration.encode()).hexdigest()
    # The advisory lock and namespace assertion run in the same transaction as all DDL.
    # A matching marker is the only allowed no-op. Existing unmarked app objects abort.
    return """BEGIN;
DO $qyl_deploy$
DECLARE marker text;
BEGIN
 PERFORM pg_advisory_xact_lock(81399210002);
 SELECT obj_description(c.oid,'pg_class') INTO marker FROM pg_class c JOIN pg_namespace n ON n.oid=c.relnamespace
 WHERE n.nspname='public' AND c.relname='questyourlife_user';
 IF marker='%s' THEN RETURN; END IF;
 IF EXISTS(SELECT 1 FROM pg_class c JOIN pg_namespace n ON n.oid=c.relnamespace
            WHERE n.nspname='public' AND c.relname LIKE 'questyourlife\\_%%' ESCAPE '\\')
 OR EXISTS(SELECT 1 FROM pg_proc p JOIN pg_namespace n ON n.oid=p.pronamespace
            WHERE n.nspname='public' AND p.proname LIKE 'questyourlife\\_%%' ESCAPE '\\') THEN
  RAISE EXCEPTION 'Existing app namespace differs; inspect and create an explicit forward migration.';
 END IF;
 EXECUTE $qyl_foundation$%s$qyl_foundation$;
 EXECUTE 'COMMENT ON TABLE public.questyourlife_user IS ''%s''';
END $qyl_deploy$;
NOTIFY pgrst, 'reload schema';
COMMIT;""" % (marker, migration, marker)


class NoRedirect(urllib.request.HTTPRedirectHandler):
    def redirect_request(self, *args, **kwargs):
        return None


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--apply', action='store_true', help='Apply app DDL, preserve/provision ID secret, deploy Edge function.')
    args = parser.parse_args()
    token = os.environ.get('SUPABASE_ACCESS_TOKEN')
    if not token:
        raise RuntimeError('Set SUPABASE_ACCESS_TOKEN through environment secrets; do not paste it into source or chat.')
    opener = urllib.request.build_opener(NoRedirect())

    def api(path, body=None):
        request = urllib.request.Request(project_url(PROJECT) + path,
            data=None if body is None else json.dumps(body).encode(),
            headers={'Authorization': 'Bearer ' + token, 'Content-Type': 'application/json'})
        try:
            with opener.open(request, timeout=60) as response:
                raw = response.read()
                return json.loads(raw) if raw else None
        except urllib.error.HTTPError as error:
            # Do not print remote response bodies (may contain project data or credentials).
            raise RuntimeError('Supabase management request failed (HTTP %s).' % error.code) from None
        except urllib.error.URLError:
            raise RuntimeError('Supabase management endpoint unavailable; check configured outbound hosts.') from None

    objects = api('/database/query', {'query': "SELECT tablename FROM pg_tables WHERE schemaname='public' AND tablename LIKE 'questyourlife\\_%' ESCAPE '\\' ORDER BY tablename;", 'read_only': True})
    if not isinstance(objects, list): raise RuntimeError('Unexpected preflight response.')
    print('Authorized project reachable; %d existing app tables.' % len(objects))
    if not args.apply:
        print('Preflight only; no remote changes. Use --apply for deployment.')
        return
    api('/database/query', {'query': deployment_sql()})
    print('Foundation applied or verified as the identical migration.')
    registered = api('/secrets')
    if not isinstance(registered, list): raise RuntimeError('Unexpected secret metadata response.')
    if not any(item.get('name') == 'QUESTYOURLIFE_ID_SECRET' for item in registered):
        api('/secrets', [{'name': 'QUESTYOURLIFE_ID_SECRET', 'value': secrets.token_hex(32)}])
        print('Provisioned stable server-only ID secret; retain it across all later deployments.')
    else:
        print('Existing stable ID secret preserved.')
    # CLI consumes only management token from the inherited environment. No secret in argv or logs.
    with tempfile.TemporaryFile() as output:
        result = subprocess.run(['npx', '--yes', 'supabase@3.0.0', 'functions', 'deploy', 'questyourlife-account',
            '--remote=' + PROJECT, '--no-verify-jwt', '--use-api', '--yes'], cwd=ROOT, stdout=output, stderr=output)
    if result.returncode:
        raise RuntimeError('Edge deployment failed; foundation is intact. Rerun with the same migration and preserved secret.')
    print('Edge function deployed. Hosted Auth/RPC and an actual device still require smoke testing.')


if __name__ == '__main__':
    try:
        main()
    except (RuntimeError, ValueError) as error:
        raise SystemExit(str(error)) from None
