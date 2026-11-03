#!/usr/bin/env python3
"""Run corrective SQL ONLY in a disposable offline PostgreSQL container."""
import pathlib
import subprocess
import uuid
import time
import json
from deploy_supabase import deployment_sql, forward_sql

ROOT = pathlib.Path(__file__).resolve().parents[1]
name = 'qyl-correct-' + uuid.uuid4().hex[:12]

def command(args, **kwargs):
    return subprocess.run(args, check=True, **kwargs)

def sql(text):
    result = subprocess.run(['docker','exec','-i',name,'psql','-U','postgres','-v','ON_ERROR_STOP=1','-q'],
                            input=text,text=True,capture_output=True)
    if result.returncode:
        # The wrapper context repeats the entire migration; keep the actual SQL failure.
        raise RuntimeError(result.stderr.split('CONTEXT:',1)[0])
    return result.stdout

try:
    command(['docker','run','-d','--rm','--network','none','--name',name,
             '-e','POSTGRES_HOST_AUTH_METHOD=trust','postgres:17-alpine'],stdout=subprocess.DEVNULL)
    for _ in range(60):
        if subprocess.run(['docker','exec',name,'pg_isready','-h','127.0.0.1','-U','postgres'],
                          stdout=subprocess.DEVNULL,stderr=subprocess.DEVNULL).returncode==0: break
        time.sleep(.2)
    else: raise RuntimeError('PostgreSQL readiness timeout')
    sql("""CREATE ROLE anon NOLOGIN; CREATE ROLE authenticated NOLOGIN; CREATE ROLE service_role NOLOGIN BYPASSRLS;
    CREATE SCHEMA auth; CREATE TABLE auth.users(id uuid PRIMARY KEY);
    CREATE FUNCTION auth.uid() RETURNS uuid LANGUAGE sql STABLE AS $$ SELECT nullif(current_setting('request.jwt.claim.sub',true),'')::uuid $$;
    CREATE FUNCTION auth.jwt() RETURNS jsonb LANGUAGE sql STABLE AS $$ SELECT coalesce(nullif(current_setting('request.jwt.claims',true),''),'{}')::jsonb $$;
    GRANT USAGE ON SCHEMA auth TO authenticated, service_role;
    GRANT EXECUTE ON FUNCTION auth.uid(),auth.jwt() TO authenticated,service_role;
    CREATE SCHEMA supabase_migrations; CREATE TABLE supabase_migrations.schema_migrations(version text PRIMARY KEY,name text,statements text[]);""")
    sql(deployment_sql())
    sql((ROOT/'supabase/tests/personal_security.sql').read_text())
    # Exercise the actual populated forward migration, not only an empty schema.
    sql("""INSERT INTO auth.users VALUES('b1000000-0000-0000-0000-000000000001');
    SELECT set_config('request.jwt.claim.sub','b1000000-0000-0000-0000-000000000001',false);
    SELECT set_config('request.jwt.claims','{"app_metadata":{"questyourlife_username":"migration_keeper"}}',false);
    SELECT public.questyourlife_bootstrap();
    DO $$ DECLARE r jsonb; BEGIN
      r:=public.questyourlife_apply_batch('[{"event_id":"b2000000-0000-0000-0000-000000000001","operation":"goal_put","base_version":0,"data":{"id":"b3000000-0000-0000-0000-000000000001","title":"Keep history","description":"original","nodes":[{"id":"b3000000-0000-0000-0000-000000000002","head":"b3000000-0000-0000-0000-000000000001","type":"chap","name":"Chapter","position":0},{"id":"b3000000-0000-0000-0000-000000000003","head":"b3000000-0000-0000-0000-000000000002","type":"main","name":"Step","position":0}]}}]');
      ASSERT r->'receipts'->0->>'result_status'='applied',r::text;
      r:=public.questyourlife_apply_batch('[{"event_id":"b2000000-0000-0000-0000-000000000002","operation":"daily_put","base_version":0,"data":{"id":"b4000000-0000-0000-0000-000000000001","join_id":"b3000000-0000-0000-0000-000000000001","source_id":"b3000000-0000-0000-0000-000000000003","title":"Preserved completion","detail":"criteria originally unavailable","scheduled_date":"2026-11-01","status":"completed","legacy":true}}]');
      ASSERT r->'receipts'->0->>'result_status'='applied',r::text;
    END $$;""")
    def records(query):
        return json.loads(sql('COPY ('+query+') TO STDOUT;').strip())
    before=records("SELECT jsonb_agg(to_jsonb(e) ORDER BY id) FROM public.questyourlife_event e WHERE user_id='b1000000-0000-0000-0000-000000000001'")
    for path in sorted((ROOT/'supabase/migrations').glob('*.sql')):
        if path.name.startswith('202611030001_'): continue
        sql(forward_sql(path)); sql(forward_sql(path))
        print('PASS migration and identical reapply: '+path.name,flush=True)
    after=records("SELECT jsonb_agg(to_jsonb(e)-'score_delta'-'clan_id_at_event'-'week_start' ORDER BY id) FROM public.questyourlife_event e WHERE user_id='b1000000-0000-0000-0000-000000000001' AND operation NOT IN ('join_identity_migration','quest_correction_migration')")
    assert before==after,'Forward migration rewrote an existing immutable receipt'
    sql("""DO $$ BEGIN
      ASSERT NOT EXISTS(SELECT 1 FROM public.questyourlife_quest_join WHERE id=book_id),'legacy join remained coupled';
      ASSERT EXISTS(SELECT 1 FROM public.questyourlife_daily_quest d JOIN public.questyourlife_quest_join j ON j.id=d.join_id
        WHERE d.id='b4000000-0000-0000-0000-000000000001' AND d.status='completed' AND d.source_id='b3000000-0000-0000-0000-000000000003'
        AND d.content_snapshot->>'legacy'='true'),'historical execution lost';
    END $$;""")
    print('PASS populated migration, independent join remapping and immutable receipt preservation',flush=True)
    for path in [ROOT/'supabase/tests/quest_correction.sql',ROOT/'supabase/tests/social_correction.sql',ROOT/'supabase/tests/recurrence_correction.sql']:
        if not path.exists(): raise RuntimeError('Missing required test: '+path.name)
        sql(path.read_text())
        print('PASS '+path.name,flush=True)
finally:
    subprocess.run(['docker','rm','-f',name],stdout=subprocess.DEVNULL,stderr=subprocess.DEVNULL)
