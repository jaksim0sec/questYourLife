\set ON_ERROR_STOP on
-- Runs only in an isolated local PostgreSQL container, never against a hosted project.
BEGIN;
DO $$ BEGIN
 ASSERT to_regprocedure('public.questyourlife_apply_batch(jsonb)') IS NOT NULL, 'missing transactional RPC';
 ASSERT to_regprocedure('public.questyourlife_pull_changes(bigint,bigint,integer,uuid)') IS NOT NULL, 'missing incremental RPC';
END $$;
ROLLBACK;
BEGIN;
INSERT INTO auth.users VALUES('10000000-0000-0000-0000-000000000001'),('10000000-0000-0000-0000-000000000002');
SET LOCAL ROLE authenticated;
SELECT set_config('request.jwt.claim.sub','10000000-0000-0000-0000-000000000001',true);
SELECT set_config('request.jwt.claims','{"app_metadata":{"questyourlife_username":"test_one"}}',true);
SELECT public.questyourlife_bootstrap();
DO $$ DECLARE r jsonb; body jsonb; BEGIN
 ASSERT (SELECT cash FROM public.questyourlife_user)=0, 'legacy balance must not be imported';
 body:='[{"event_id":"20000000-0000-0000-0000-000000000001","operation":"daily_put","base_version":0,"data":{"id":"30000000-0000-0000-0000-000000000001","title":"offline task","detail":"private","scheduled_date":"2026-11-03","status":"accepted","reward":999999}}]';
 r:=public.questyourlife_apply_batch(body); ASSERT r->'receipts'->0->>'result_status'='applied',r::text;
 r:=public.questyourlife_apply_batch('[{"event_id":"20000000-0000-0000-0000-000000000002","operation":"daily_complete","data":{"id":"30000000-0000-0000-0000-000000000001","reward":999999,"completed_at":"2099-01-01"}}]');
 ASSERT (SELECT cash FROM public.questyourlife_user)=20,'only server reward applies';
 ASSERT r->'receipts'->0->>'cash_delta'='20',r::text;
 ASSERT r->'receipts'->0->'result'->>'user_base_version'='1','own ACK must identify the locked user predecessor version';
 r:=public.questyourlife_apply_batch('[{"event_id":"20000000-0000-0000-0000-000000000002","operation":"daily_complete","data":{"id":"30000000-0000-0000-0000-000000000001","reward":999999,"completed_at":"2099-01-01"}}]');
 ASSERT (SELECT cash FROM public.questyourlife_user)=20,'same UUID delivery twice';
 r:=public.questyourlife_apply_batch('[{"event_id":"20000000-0000-0000-0000-000000000003","operation":"daily_complete","data":{"id":"30000000-0000-0000-0000-000000000001"}}]');
 ASSERT (SELECT cash FROM public.questyourlife_user)=20,'different UUID same execution';
 BEGIN
  perform public.questyourlife_apply_batch('[{"event_id":"20000000-0000-0000-0000-000000000002","operation":"daily_complete","data":{"id":"30000000-0000-0000-0000-000000000001","reward":1}}]');
  raise exception 'changed replay accepted';
 EXCEPTION WHEN invalid_parameter_value THEN NULL; END;
 r:=public.questyourlife_apply_batch('[{"event_id":"20000000-0000-0000-0000-000000000004","operation":"purchase","data":{"id":"40000000-0000-0000-0000-000000000001","item_id":1001,"price":0}}]');
 ASSERT r->'receipts'->0->>'result_status'='rejected',r::text;
 ASSERT (SELECT cash FROM public.questyourlife_user)=20,'failed purchase must roll back';
 r:=public.questyourlife_apply_batch('[{"event_id":"20000000-0000-0000-0000-000000000005","operation":"settings_patch","base_version":0,"data":{"appearance":"dark"}}]');
 ASSERT r->'receipts'->0->>'result_status'='conflict',r::text;
 BEGIN UPDATE public.questyourlife_user SET cash=100000; raise exception 'direct balance write permitted'; EXCEPTION WHEN insufficient_privilege THEN NULL; END;
 r:=public.questyourlife_pull_changes(0,null,2,null);
 ASSERT jsonb_array_length(r->'events')=2,'page size'; ASSERT (r->>'has_more')::boolean,'more pages expected';
 ASSERT (r->>'cursor')::bigint=2,'cursor follows committed events';
END $$;
SELECT set_config('request.jwt.claim.sub','10000000-0000-0000-0000-000000000002',true);
SELECT set_config('request.jwt.claims','{"app_metadata":{"questyourlife_username":"test_two"}}',true);
SELECT public.questyourlife_bootstrap();
DO $$ DECLARE r jsonb; BEGIN
 ASSERT (SELECT count(*) FROM public.questyourlife_daily_quest)=0,'RLS leaked private quest';
 ASSERT (SELECT count(*) FROM public.questyourlife_user)=1,'RLS leaked profile';
 r:=public.questyourlife_apply_batch('[{"event_id":"20000000-0000-0000-0000-000000000006","operation":"daily_complete","data":{"id":"30000000-0000-0000-0000-000000000001"}}]');
 ASSERT r->'receipts'->0->>'result_status'='rejected','cross-account completion allowed';
 ASSERT (SELECT cash FROM public.questyourlife_user)=0,'cross-account award';
END $$;
SET LOCAL ROLE anon;
DO $$ BEGIN
 BEGIN perform public.questyourlife_bootstrap(); raise exception 'anonymous SQL role allowed'; EXCEPTION WHEN insufficient_privilege THEN NULL; END;
END $$;
ROLLBACK;
BEGIN;
INSERT INTO auth.users VALUES('10000000-0000-0000-0000-000000000003');
SET LOCAL ROLE authenticated;
SELECT set_config('request.jwt.claim.sub','10000000-0000-0000-0000-000000000003',true);
SELECT set_config('request.jwt.claims','{"app_metadata":{"questyourlife_username":"test_three"}}',true);
SELECT public.questyourlife_bootstrap();
DO $$ DECLARE r jsonb; BEGIN
 r:=public.questyourlife_apply_batch('[{"event_id":"20000000-0000-0000-0000-000000000010","operation":"quick_put","base_version":0,"data":{"id":"30000000-0000-0000-0000-000000000010","title":"quick checklist","completed":true,"created_at":100}}]');
 ASSERT r->'receipts'->0->>'result_status'='applied',r::text;
 r:=public.questyourlife_apply_batch('[{"event_id":"20000000-0000-0000-0000-000000000011","operation":"quick_put","base_version":1,"data":{"id":"30000000-0000-0000-0000-000000000010","title":"quick checklist","completed":false,"created_at":100}}]');
 ASSERT r->'receipts'->0->>'result_status'='applied',r::text;
 ASSERT (SELECT cash FROM public.questyourlife_user)=0,'legacy checklist toggles must not farm cash';
 r:=public.questyourlife_apply_batch('[{"event_id":"20000000-0000-0000-0000-000000000012","operation":"daily_complete","data":{"id":"30000000-0000-0000-0000-000000000010"}}]');
 ASSERT r->'receipts'->0->>'result_status'='rejected','quick checklist cannot become a reward execution';
END $$;
ROLLBACK;
BEGIN;
INSERT INTO auth.users VALUES('10000000-0000-0000-0000-000000000004');
SET LOCAL ROLE authenticated;
SELECT set_config('request.jwt.claim.sub','10000000-0000-0000-0000-000000000004',true);
SELECT set_config('request.jwt.claims','{"app_metadata":{"questyourlife_username":"test_four"}}',true);
SELECT public.questyourlife_bootstrap();
DO $$ DECLARE r jsonb; BEGIN
 r:=public.questyourlife_apply_batch('[{"event_id":"20000000-0000-0000-0000-000000000020","operation":"goal_put","base_version":0,"data":{"id":"40000000-0000-0000-0000-000000000020","title":"private book","description":"private","nodes":[{"id":"40000000-0000-0000-0000-000000000021","head":"40000000-0000-0000-0000-000000000020","type":"chap","name":"chapter","position":1},{"id":"40000000-0000-0000-0000-000000000022","head":"40000000-0000-0000-0000-000000000021","type":"main","name":"main","position":0}]}}]');
 ASSERT r->'receipts'->0->>'result_status'='applied',r::text;
 r:=public.questyourlife_apply_batch('[{"event_id":"20000000-0000-0000-0000-000000000021","operation":"daily_put","base_version":0,"data":{"id":"30000000-0000-0000-0000-000000000020","source_id":"40000000-0000-0000-0000-000000000022","join_id":"40000000-0000-0000-0000-000000000020","title":"attempt one","detail":"","scheduled_date":"2026-11-03","status":"accepted"}},{"event_id":"20000000-0000-0000-0000-000000000022","operation":"daily_put","base_version":1,"data":{"id":"30000000-0000-0000-0000-000000000020","source_id":"40000000-0000-0000-0000-000000000022","join_id":"40000000-0000-0000-0000-000000000020","title":"attempt one","detail":"","scheduled_date":"2026-11-03","status":"cancelled"}}]');
 ASSERT r->'receipts'->1->>'result_status'='applied',r::text;
 r:=public.questyourlife_apply_batch('[{"event_id":"20000000-0000-0000-0000-000000000023","operation":"daily_put","base_version":0,"data":{"id":"30000000-0000-0000-0000-000000000021","source_id":"40000000-0000-0000-0000-000000000022","join_id":"40000000-0000-0000-0000-000000000020","title":"attempt two","detail":"","scheduled_date":"2026-11-03","status":"accepted"}},{"event_id":"20000000-0000-0000-0000-000000000024","operation":"daily_complete","data":{"id":"30000000-0000-0000-0000-000000000021"}}]');
 ASSERT r->'receipts'->0->>'result_status'='applied',r::text;
 ASSERT r->'receipts'->1->>'result_status'='applied',r::text;
 ASSERT (SELECT cash FROM public.questyourlife_user)=20,'linked execution earns once';
 r:=public.questyourlife_apply_batch('[{"event_id":"20000000-0000-0000-0000-000000000025","operation":"step_complete","data":{"id":"40000000-0000-0000-0000-000000000022"}}]');
 ASSERT (SELECT cash FROM public.questyourlife_user)=20,'direct step cannot pay again';
END $$;
ROLLBACK;
BEGIN;
INSERT INTO auth.users VALUES('10000000-0000-0000-0000-000000000005');
SET LOCAL ROLE authenticated;
SELECT set_config('request.jwt.claim.sub','10000000-0000-0000-0000-000000000005',true);
SELECT set_config('request.jwt.claims','{"app_metadata":{"questyourlife_username":"test_five"}}',true);
SELECT public.questyourlife_bootstrap();
DO $$ DECLARE r jsonb; i integer; task uuid; BEGIN
 FOR i IN 1..4 LOOP
  task:=gen_random_uuid();
  r:=public.questyourlife_apply_batch(jsonb_build_array(
   jsonb_build_object('event_id',gen_random_uuid(),'operation','daily_put','base_version',0,'data',jsonb_build_object('id',task,'title','eligible','detail','','scheduled_date','2026-11-03','status','accepted')),
   jsonb_build_object('event_id',gen_random_uuid(),'operation','daily_complete','data',jsonb_build_object('id',task))));
  ASSERT r->'receipts'->1->>'cash_delta'='20',r::text;
 END LOOP;
 ASSERT (SELECT cash FROM public.questyourlife_user)=80,'awards confirmed';
 r:=public.questyourlife_apply_batch(jsonb_build_array(jsonb_build_object('event_id',gen_random_uuid(),'operation','purchase','data',jsonb_build_object('id',gen_random_uuid(),'item_id',2,'price',0))));
 ASSERT r->'receipts'->0->>'result_status'='applied',r::text;
 ASSERT (SELECT cash FROM public.questyourlife_user)=0,'server catalog price must be deducted';
 ASSERT (SELECT xp FROM public.questyourlife_user)=80,'purchase must not remove XP';
 ASSERT (SELECT inventory ? '2' FROM public.questyourlife_user),'ownership and debit must commit together';
 r:=public.questyourlife_apply_batch(jsonb_build_array(jsonb_build_object('event_id',gen_random_uuid(),'operation','purchase','data',jsonb_build_object('id',gen_random_uuid(),'item_id',2))));
 ASSERT r->'receipts'->0->>'result_status'='rejected','double purchase must be rejected';
 ASSERT (SELECT sum(cash_delta) FROM public.questyourlife_event)=0,'projection agrees with immutable ledger';
 BEGIN perform public.questyourlife_signup_gate('test_bad'); raise exception 'public signup gate permitted'; EXCEPTION WHEN insufficient_privilege THEN NULL; END;
END $$;
RESET ROLE;
DO $$ BEGIN
 BEGIN UPDATE public.questyourlife_event SET cash_delta=99; raise exception 'ledger updated'; EXCEPTION WHEN insufficient_privilege THEN NULL; END;
END $$;
ROLLBACK;
