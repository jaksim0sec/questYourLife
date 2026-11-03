#!/usr/bin/env python3
"""Isolated PostgreSQL RPC/RLS tests; no ports, no hosted resources or actual Auth users."""
import pathlib, subprocess, uuid, time
from deploy_supabase import deployment_sql
root = pathlib.Path(__file__).resolve().parents[1]
name = 'qyl-sql-' + uuid.uuid4().hex[:10]
def run(args, **kwargs): return subprocess.run(args, check=True, **kwargs)
try:
    run(['docker', 'run', '-d', '--rm', '--network', 'none', '--name', name,
         '-e', 'POSTGRES_HOST_AUTH_METHOD=trust', 'postgres:17-alpine'], stdout=subprocess.DEVNULL)
    for _ in range(40):
        if subprocess.run(['docker','exec',name,'pg_isready','-h','127.0.0.1','-U','postgres'],stdout=subprocess.DEVNULL,stderr=subprocess.DEVNULL).returncode == 0: break
        time.sleep(.25)
    else: raise RuntimeError("local PostgreSQL readiness timeout")
    def sql(text): run(['docker','exec','-i',name,'psql','-U','postgres','-v','ON_ERROR_STOP=1'],input=text,text=True,stdout=subprocess.DEVNULL)
    sql("CREATE ROLE anon NOLOGIN; CREATE ROLE authenticated NOLOGIN; CREATE ROLE service_role NOLOGIN BYPASSRLS; CREATE SCHEMA auth; CREATE TABLE auth.users(id uuid PRIMARY KEY); CREATE FUNCTION auth.uid() RETURNS uuid LANGUAGE sql STABLE AS $$ SELECT nullif(current_setting('request.jwt.claim.sub',true),'')::uuid $$; CREATE FUNCTION auth.jwt() RETURNS jsonb LANGUAGE sql STABLE AS $$ SELECT coalesce(nullif(current_setting('request.jwt.claims',true),''),'{}')::jsonb $$; GRANT USAGE ON SCHEMA auth TO authenticated, service_role; GRANT EXECUTE ON FUNCTION auth.uid() TO authenticated, service_role;")
    sql("CREATE TABLE public.questyourlife_foreign_namespace(id integer)")
    blocked=subprocess.run(['docker','exec','-i',name,'psql','-U','postgres','-v','ON_ERROR_STOP=1'],input=deployment_sql(),text=True,stdout=subprocess.DEVNULL,stderr=subprocess.PIPE)
    assert blocked.returncode!=0 and 'Existing app namespace differs' in blocked.stderr, 'unsafe deployment preflight'
    sql("DROP TABLE public.questyourlife_foreign_namespace")
    sql(deployment_sql())
    sql(deployment_sql())  # A repeat deployment is a no-op; unknown namespaces never get overwritten.
    sql((root/'supabase/tests/personal_security.sql').read_text())
    # Concurrency matters: SELECT-then-UPSERT must not permit a different owner to win a UUID race.
    sql("INSERT INTO auth.users VALUES('f0000000-0000-0000-0000-000000000001'),('f0000000-0000-0000-0000-000000000002')")
    def actor(uid, name):
        return "SET ROLE authenticated; SELECT set_config('request.jwt.claim.sub','"+uid+"',false); SELECT set_config('request.jwt.claims','{\"app_metadata\":{\"questyourlife_username\":\""+name+"\"}}',false); SELECT public.questyourlife_bootstrap();"
    import json
    for operation in ['goal_put','daily_put','quick_put']:
        target=str(uuid.uuid4())
        def envelope(title):
            data={'id':target,'title':title,'description':'','nodes':[],'detail':'','scheduled_date':'2026-11-03','status':'accepted','completed':False,'created_at':1}
            return json.dumps([{'event_id':str(uuid.uuid4()),'operation':operation,'base_version':0,'data':data}]).replace("'","''")
        a=actor('f0000000-0000-0000-0000-000000000001','race_one')+"BEGIN; SELECT public.questyourlife_apply_batch('"+envelope('owner original')+"');\n\\echo READY\nSELECT pg_sleep(1.5); COMMIT;\n"
        process=subprocess.Popen(['docker','exec','-i',name,'psql','-U','postgres','-At','-v','ON_ERROR_STOP=1'],stdin=subprocess.PIPE,stdout=subprocess.PIPE,stderr=subprocess.PIPE,text=True)
        process.stdin.write(a);process.stdin.close()
        while True:
            line=process.stdout.readline()
            if line.strip()=='READY': break
            if not line: raise RuntimeError('race fixture did not reach barrier: '+process.stderr.read())
        sql(actor('f0000000-0000-0000-0000-000000000002','race_two')+"DO $$ DECLARE r jsonb; BEGIN r:=public.questyourlife_apply_batch('"+envelope('attacker overwrite')+"'); ASSERT r->'receipts'->0->>'result_status'='rejected','cross-owner UPSERT race accepted'; END $$;")
        if process.wait()!=0: raise RuntimeError('race owner failed: '+process.stderr.read())
    print('RPC/RLS tests passed, including three cross-owner UUID races')
finally:
    subprocess.run(['docker','rm','-f',name],stdout=subprocess.DEVNULL,stderr=subprocess.DEVNULL)
