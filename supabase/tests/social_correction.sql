\set ON_ERROR_STOP on
-- Independent, rollback-only fixtures. Run after 001+002+003 on isolated local
-- PostgreSQL with the same minimal Auth stubs as tools/test_supabase.py.
BEGIN;
DO $$ BEGIN
 ASSERT (select count(*) from pg_tables where schemaname='public' and tablename like 'questyourlife\_%' escape '\')=9,
  'eight domain tables plus the preserved event ledger';
 ASSERT (select count(*) from pg_class where oid in ('public.questyourlife_alarm'::regclass,'public.questyourlife_clan'::regclass,
  'public.questyourlife_chat'::regclass,'public.questyourlife_post'::regclass) and relrowsecurity)=4,'social RLS required';
 ASSERT not has_function_privilege('anon','public.questyourlife_social_command(uuid,text,jsonb)','EXECUTE'),'anonymous command';
 ASSERT not has_function_privilege('authenticated','public.questyourlife_assistant_post(uuid,uuid,text,jsonb)','EXECUTE'),'assistant impersonation';
 ASSERT not has_function_privilege('authenticated','public.questyourlife_social_receipt(uuid,uuid,text,text,text,jsonb,jsonb,bigint,text)','EXECUTE'),'ledger helper exposed';
 ASSERT not has_table_privilege('authenticated','public.questyourlife_clan','SELECT'),'raw clan invites exposed';
 ASSERT not has_table_privilege('authenticated','public.questyourlife_chat','SELECT'),'raw friend/block metadata exposed';
END $$;
INSERT INTO auth.users(id) VALUES
 ('a1000000-0000-0000-0000-000000000001'),('a1000000-0000-0000-0000-000000000002'),('a1000000-0000-0000-0000-000000000003');
SET LOCAL ROLE authenticated;
SELECT set_config('request.jwt.claim.sub','a1000000-0000-0000-0000-000000000001',true);
SELECT set_config('request.jwt.claims','{"app_metadata":{"questyourlife_username":"social_one"}}',true);
SELECT public.questyourlife_bootstrap();
SELECT set_config('request.jwt.claim.sub','a1000000-0000-0000-0000-000000000002',true);
SELECT set_config('request.jwt.claims','{"app_metadata":{"questyourlife_username":"social_two"}}',true);
SELECT public.questyourlife_bootstrap();
SELECT set_config('request.jwt.claim.sub','a1000000-0000-0000-0000-000000000003',true);
SELECT set_config('request.jwt.claims','{"app_metadata":{"questyourlife_username":"social_three"}}',true);
SELECT public.questyourlife_bootstrap();
RESET ROLE;
UPDATE public.questyourlife_user SET privacy='{"profile_public":false,"rank_visible":false}'
 WHERE id='a1000000-0000-0000-0000-000000000001';
UPDATE public.questyourlife_user SET privacy='{"profile_public":true,"rank_visible":true}',avatar='{"mascot":"stamp","variant":"red","secret":"never_project"}'
 WHERE id='a1000000-0000-0000-0000-000000000002';
SET LOCAL ROLE authenticated;
SELECT set_config('request.jwt.claim.sub','a1000000-0000-0000-0000-000000000001',true);
SELECT set_config('request.jwt.claims','{"app_metadata":{"questyourlife_username":"social_one"}}',true);
DO $$ DECLARE r jsonb; replay jsonb; invite_event uuid:=gen_random_uuid(); body jsonb:='{"id":"a2000000-0000-0000-0000-000000000001","name":"one clan","cash":9999}'; BEGIN
 r:=public.questyourlife_social_command('a3000000-0000-0000-0000-000000000001','clan_create',body);
 ASSERT r->'receipts'->0->>'result_status'='applied',r::text;
 replay:=public.questyourlife_social_command('a3000000-0000-0000-0000-000000000001','clan_create',body);
 ASSERT replay=r,'replayed social command changed receipt';
 BEGIN
  perform public.questyourlife_social_command('a3000000-0000-0000-0000-000000000001','clan_create',body||'{"name":"changed"}');
  raise exception 'changed replay accepted';
 EXCEPTION WHEN invalid_parameter_value THEN NULL; END;
 ASSERT (select cash from public.questyourlife_user)=0,'client balance accepted';
 r:=public.questyourlife_social_command(gen_random_uuid(),'clan_create','{"name":"second clan"}');
 ASSERT r->'receipts'->0->>'result_status'='rejected','multiple active clans';
 r:=public.questyourlife_social_command(invite_event,'clan_invite',
  '{"id":"a2000000-0000-0000-0000-000000000001","user_id":"a1000000-0000-0000-0000-000000000002"}');
 ASSERT r->'receipts'->0->>'result_status'='applied',r::text;
 perform set_config('qyl_test.invite',r->'invitation'->>'token',true);
 ASSERT length(current_setting('qyl_test.invite'))=64,'unguessable invitation';
 ASSERT position(current_setting('qyl_test.invite') in (r->'receipts')::text)=0,'raw token retained in immutable receipt';
 replay:=public.questyourlife_social_command(invite_event,'clan_invite',
  '{"id":"a2000000-0000-0000-0000-000000000001","user_id":"a1000000-0000-0000-0000-000000000002"}');
 ASSERT replay->'receipts'=r->'receipts' and not(replay ? 'invitation'),'token replayed from persisted receipt';
 ASSERT not ((r->'receipts'->0->'changes'->0->'data') ? 'invites'),'invites in after-images';
 BEGIN INSERT INTO public.questyourlife_clan(id,name,owner_id) VALUES(gen_random_uuid(),'bypass',auth.uid());
  raise exception 'client mutated membership'; EXCEPTION WHEN insufficient_privilege THEN NULL; END;
END $$;
SELECT set_config('request.jwt.claim.sub','a1000000-0000-0000-0000-000000000003',true);
DO $$ DECLARE r jsonb; s jsonb; BEGIN
 r:=public.questyourlife_social_command(gen_random_uuid(),'clan_join',jsonb_build_object('id','a2000000-0000-0000-0000-000000000001','token',current_setting('qyl_test.invite')));
 ASSERT r->'receipts'->0->>'result_status'='rejected','targeted invitation stolen';
 s:=public.questyourlife_social_snapshot();
 ASSERT s->'clans'->0->'members'='{}'::jsonb,'unrelated user saw registry';
 ASSERT s->'clans'->0->'chests'='{}'::jsonb,'unrelated user saw chests';
 ASSERT not(s->'clans'->0 ? 'invites'),'unrelated user saw invitation hashes';
 ASSERT (select count(*) from public.questyourlife_alarm)=0,'other account inbox leaked';
 r:=public.questyourlife_public_profiles(array['a1000000-0000-0000-0000-000000000001'::uuid,'a1000000-0000-0000-0000-000000000002'::uuid]);
 ASSERT jsonb_array_length(r->'profiles')=1,'private profile exposed';
 ASSERT not(r->'profiles'->0 ?|array['login_name','cash','settings','privacy','inventory']),'private profile columns exposed';
 ASSERT not(r->'profiles'->0->'avatar' ? 'secret'),'arbitrary avatar keys exposed';
END $$;
SELECT set_config('request.jwt.claim.sub','a1000000-0000-0000-0000-000000000002',true);
SELECT set_config('request.jwt.claims','{"app_metadata":{"questyourlife_username":"social_two"}}',true);
DO $$ DECLARE r jsonb; BEGIN
 r:=public.questyourlife_social_command(gen_random_uuid(),'clan_join',jsonb_build_object('id','a2000000-0000-0000-0000-000000000001','token',current_setting('qyl_test.invite')));
 ASSERT r->'receipts'->0->>'result_status'='applied',r::text;
 r:=public.questyourlife_social_command(gen_random_uuid(),'clan_invite','{"id":"a2000000-0000-0000-0000-000000000001"}');
 ASSERT r->'receipts'->0->>'result_status'='rejected','nonowner minted invitations';
 ASSERT (select count(*) from public.questyourlife_alarm)=1,'targeted inbox missing';
 r:=public.questyourlife_social_command(gen_random_uuid(),'alarm_read',jsonb_build_object('id',(select id from public.questyourlife_alarm limit 1)));
 ASSERT r->'receipts'->0->>'result_status'='applied',r::text;
 ASSERT (select sum(read) from public.questyourlife_alarm)=1,'recipient read state missing';
END $$;
SELECT set_config('request.jwt.claim.sub','a1000000-0000-0000-0000-000000000001',true);
DO $$ DECLARE r jsonb; BEGIN
 r:=public.questyourlife_social_command(gen_random_uuid(),'clan_leave','{"id":"a2000000-0000-0000-0000-000000000001"}');
 ASSERT r->'receipts'->0->>'result_status'='rejected','owner abandoned remaining members';
 r:=public.questyourlife_social_command(gen_random_uuid(),'clan_transfer','{"id":"a2000000-0000-0000-0000-000000000001","user_id":"a1000000-0000-0000-0000-000000000002"}');
 ASSERT r->'receipts'->0->>'result_status'='applied',r::text;
 r:=public.questyourlife_social_command(gen_random_uuid(),'clan_leave','{"id":"a2000000-0000-0000-0000-000000000001"}');
 ASSERT r->'receipts'->0->>'result_status'='applied',r::text;
 ASSERT jsonb_typeof(r->'receipts'->0->'changes'->0->'data')='object','Room cannot apply null tombstones';
END $$;
SELECT set_config('request.jwt.claim.sub','a1000000-0000-0000-0000-000000000002',true);
DO $$ DECLARE r jsonb; BEGIN
 r:=public.questyourlife_social_command(gen_random_uuid(),'clan_invite','{"id":"a2000000-0000-0000-0000-000000000001","user_id":"a1000000-0000-0000-0000-000000000001"}');
 ASSERT r->'receipts'->0->>'result_status'='applied',r::text;
 perform set_config('qyl_test.reinvite',r->'invitation'->>'token',true);
END $$;
SELECT set_config('request.jwt.claim.sub','a1000000-0000-0000-0000-000000000001',true);
DO $$ DECLARE r jsonb; BEGIN
 r:=public.questyourlife_social_command(gen_random_uuid(),'clan_join',jsonb_build_object('id','a2000000-0000-0000-0000-000000000001','token',current_setting('qyl_test.invite')));
 ASSERT r->'receipts'->0->>'result_status'='rejected','used invitation reused';
 r:=public.questyourlife_social_command(gen_random_uuid(),'clan_join',jsonb_build_object('id','a2000000-0000-0000-0000-000000000001','token',current_setting('qyl_test.reinvite')));
 ASSERT r->'receipts'->0->>'result_status'='applied',r::text;
END $$;
RESET ROLE;
DO $$ DECLARE c public.questyourlife_clan; w date:=public.questyourlife_week_start()-7; BEGIN
 select * into c from public.questyourlife_clan where id='a2000000-0000-0000-0000-000000000001';
 ASSERT jsonb_array_length(c.members->'a1000000-0000-0000-0000-000000000001'->'history')=1,'leave/rejoin erased membership history';
 ASSERT c.members->'a1000000-0000-0000-0000-000000000001'->'history'->0->>'left_at' is not null,'missing historical leave';
 ASSERT (select count(*) from jsonb_each(c.invites) x where length(x.key)=64)=2,'hashed invites not persisted';
 ASSERT not(c.invites ? current_setting('qyl_test.invite')),'raw invitation token stored';
 ASSERT not exists(select 1 from public.questyourlife_event e where
  position(current_setting('qyl_test.invite') in to_jsonb(e)::text)>0
  or position(current_setting('qyl_test.reinvite') in to_jsonb(e)::text)>0),'raw invitation capability persisted anywhere in event ledger';
 ASSERT not exists(select 1 from public.questyourlife_alarm a where
  position(current_setting('qyl_test.invite') in to_jsonb(a)::text)>0
  or position(current_setting('qyl_test.reinvite') in to_jsonb(a)::text)>0),'raw invitation capability persisted in inbox';
 -- Freeze a completed week's real membership intervals and qualified ledger
 -- contributions without mocking the public APIs or rewriting immutable events.
 update public.questyourlife_clan set created_at=(w-1)::timestamp at time zone 'Asia/Seoul',
  members=jsonb_set(jsonb_set(members,array['a1000000-0000-0000-0000-000000000001','joined_at'],to_jsonb(w::timestamp at time zone 'Asia/Seoul')),
   array['a1000000-0000-0000-0000-000000000002','joined_at'],to_jsonb(w::timestamp at time zone 'Asia/Seoul')) where id=c.id;
 insert into public.questyourlife_event(user_id,event_id,actor_id,operation,result_status,score_delta,clan_id_at_event,week_start,sync_seq)
 select id,gen_random_uuid(),id,'daily_complete','applied',100,c.id,w,sync_seq+1 from public.questyourlife_user
 where id in ('a1000000-0000-0000-0000-000000000001','a1000000-0000-0000-0000-000000000002');
 update public.questyourlife_user set sync_seq=sync_seq+1 where id in ('a1000000-0000-0000-0000-000000000001','a1000000-0000-0000-0000-000000000002');
 -- Outstanding reversal debt consumes future chest cash without deleting items.
 update public.questyourlife_user set cash_debt=30,inventory='{"2":{"source_event_id":"a3000000-0000-0000-0000-000000000099"}}'
 where id='a1000000-0000-0000-0000-000000000001';
 insert into public.questyourlife_event(user_id,event_id,actor_id,operation,result_status,cash_delta,sync_seq)
 select id,gen_random_uuid(),id,'daily_undo','applied',-30,sync_seq+1 from public.questyourlife_user where id='a1000000-0000-0000-0000-000000000001';
 update public.questyourlife_user set sync_seq=sync_seq+1 where id='a1000000-0000-0000-0000-000000000001';
END $$;
SET LOCAL ROLE authenticated;
DO $$ DECLARE r jsonb; request uuid:=gen_random_uuid(); body jsonb; BEGIN
 body:=jsonb_build_object('id','a2000000-0000-0000-0000-000000000001','week_start',date_trunc('week',now() at time zone 'Asia/Seoul')::date-7,'reward',99999);
 r:=public.questyourlife_social_command(gen_random_uuid(),'clan_chest_unlock',body);
 ASSERT r->'receipts'->0->>'result_status'='applied',r::text;
 r:=public.questyourlife_social_command(request,'clan_chest_claim',body);
 ASSERT r->'receipts'->0->>'cash_delta'='50',r::text;
 ASSERT r->'receipts'->0->>'xp_delta'='0' and r->'receipts'->0->>'score_delta'='0','chest inflated competition';
 ASSERT (select cash from public.questyourlife_user)=20 and (select cash_debt from public.questyourlife_user)=0,'chest did not pay debt first';
 ASSERT (select inventory ? '2' from public.questyourlife_user),'debt erased purchase history';
 ASSERT public.questyourlife_social_command(request,'clan_chest_claim',body)=r,'chest replay receipt changed';
 r:=public.questyourlife_social_command(gen_random_uuid(),'clan_chest_claim',body);
 ASSERT r->'receipts'->0->>'cash_delta'='0','new request double-awarded chest';
 r:=public.questyourlife_social_command(gen_random_uuid(),'clan_leave','{"id":"a2000000-0000-0000-0000-000000000001"}');
 ASSERT r->'receipts'->0->>'result_status'='applied',r::text;
 r:=public.questyourlife_social_command(gen_random_uuid(),'clan_chest_claim',body);
 ASSERT r->'receipts'->0->>'cash_delta'='0','leave reset frozen eligibility claim';
 ASSERT (select cash from public.questyourlife_user)=20,'chest replay changed projection';
 ASSERT (select cash-cash_debt from public.questyourlife_user)=(select sum(cash_delta) from public.questyourlife_event),'ledger and net cash diverged';
END $$;
-- A second clan cannot manufacture a second entitlement for the same account
-- and week, even if a trusted historical import contains both frozen snapshots.
DO $$ DECLARE r jsonb; BEGIN
 r:=public.questyourlife_social_command(gen_random_uuid(),'clan_create',
  '{"id":"a2000000-0000-0000-0000-000000000002","name":"new clan after leaving","discoverable":false}');
 ASSERT r->'receipts'->0->>'result_status'='applied',r::text;
END $$;
RESET ROLE;
UPDATE public.questyourlife_clan n SET created_at=o.created_at,chests=o.chests
 FROM public.questyourlife_clan o WHERE n.id='a2000000-0000-0000-0000-000000000002' AND o.id='a2000000-0000-0000-0000-000000000001';
SET LOCAL ROLE authenticated;
DO $$ DECLARE r jsonb; BEGIN
 r:=public.questyourlife_social_command(gen_random_uuid(),'clan_chest_claim',jsonb_build_object(
  'id','a2000000-0000-0000-0000-000000000002','week_start',date_trunc('week',now() at time zone 'Asia/Seoul')::date-7));
 ASSERT r->'receipts'->0->>'result_status'='applied' AND r->'receipts'->0->>'cash_delta'='0',r::text;
 ASSERT (select cash from public.questyourlife_user)=20,'group change bypassed account/week chest key';
END $$;
SELECT set_config('request.jwt.claim.sub','a1000000-0000-0000-0000-000000000003',true);
DO $$ DECLARE r jsonb; BEGIN
 r:=public.questyourlife_social_command(gen_random_uuid(),'clan_chest_claim',jsonb_build_object('id','a2000000-0000-0000-0000-000000000001','week_start',date_trunc('week',now() at time zone 'Asia/Seoul')::date-7));
 ASSERT r->'receipts'->0->>'result_status'='rejected','unrelated chest claimant accepted';
 ASSERT public.questyourlife_social_snapshot()->'clans'->0->'chests'='{}'::jsonb,'frozen eligibility leaked to outsider';
END $$;

-- Chat, friend metadata, author validation and private AI channels.
SELECT set_config('request.jwt.claim.sub','a1000000-0000-0000-0000-000000000001',true);
DO $$ DECLARE r jsonb; BEGIN
 r:=public.questyourlife_social_command(gen_random_uuid(),'chat_open_ai','{"id":"a4000000-0000-0000-0000-000000000001"}');
 ASSERT r->'receipts'->0->>'result_status'='applied',r::text;
 r:=public.questyourlife_social_command(gen_random_uuid(),'chat_open_ai','{"id":"a4000000-0000-0000-0000-000000000099"}');
 ASSERT r->'receipts'->0->'result'->>'chat_id'='a4000000-0000-0000-0000-000000000001','AI channel duplicated';
 r:=public.questyourlife_social_command(gen_random_uuid(),'post_send','{"id":"a5000000-0000-0000-0000-000000000001","chat_id":"a4000000-0000-0000-0000-000000000001","content":"private AI request"}');
 ASSERT r->'receipts'->0->>'result_status'='applied',r::text;
 r:=public.questyourlife_social_command(gen_random_uuid(),'post_send','{"chat_id":"a4000000-0000-0000-0000-000000000001","actor":"assistant","content":"forged"}');
 ASSERT r->'receipts'->0->>'result_status'='rejected','client impersonated assistant';
 r:=public.questyourlife_social_command(gen_random_uuid(),'post_send','{"chat_id":"a4000000-0000-0000-0000-000000000001","author_id":"a1000000-0000-0000-0000-000000000002","content":"forged"}');
 ASSERT r->'receipts'->0->>'result_status'='rejected','client impersonated other author';
 r:=public.questyourlife_social_command(gen_random_uuid(),'chat_open_direct','{"id":"a4000000-0000-0000-0000-000000000002","user_id":"a1000000-0000-0000-0000-000000000002"}');
 ASSERT r->'receipts'->0->>'result_status'='applied',r::text;
 r:=public.questyourlife_social_command(gen_random_uuid(),'friend_request','{"id":"a4000000-0000-0000-0000-000000000002"}');
 ASSERT r->'receipts'->0->>'result_status'='applied',r::text;
 r:=public.questyourlife_social_command(gen_random_uuid(),'friend_accept','{"id":"a4000000-0000-0000-0000-000000000002"}');
 ASSERT r->'receipts'->0->>'result_status'='rejected','sender accepted own friend request';
 r:=public.questyourlife_social_command(gen_random_uuid(),'chat_create_group','{"id":"a4000000-0000-0000-0000-000000000003","name":"group","user_ids":["a1000000-0000-0000-0000-000000000002"]}');
 ASSERT r->'receipts'->0->>'result_status'='applied',r::text;
 r:=public.questyourlife_social_command(gen_random_uuid(),'chat_create_group','{"name":"duplicate","user_ids":["a1000000-0000-0000-0000-000000000001"]}');
 ASSERT r->'receipts'->0->>'result_status'='rejected','duplicate group participant accepted';
END $$;
SELECT set_config('request.jwt.claim.sub','a1000000-0000-0000-0000-000000000002',true);
SELECT set_config('request.jwt.claims','{"app_metadata":{"questyourlife_username":"social_two"}}',true);
DO $$ DECLARE r jsonb; BEGIN
 ASSERT (select count(*) from public.questyourlife_post where chat_id='a4000000-0000-0000-0000-000000000001')=0,'private AI posts leaked';
 BEGIN perform public.questyourlife_posts('a4000000-0000-0000-0000-000000000001'); raise exception 'private AI cursor leaked';
 EXCEPTION WHEN insufficient_privilege THEN NULL; END;
 r:=public.questyourlife_social_command(gen_random_uuid(),'post_send','{"chat_id":"a4000000-0000-0000-0000-000000000001","content":"intrusion"}');
 ASSERT r->'receipts'->0->>'result_status'='rejected','nonmember posted to AI';
 r:=public.questyourlife_social_command(gen_random_uuid(),'chat_open_direct','{"user_id":"a1000000-0000-0000-0000-000000000001"}');
 ASSERT r->'receipts'->0->'result'->>'chat_id'='a4000000-0000-0000-0000-000000000002','reverse direct key duplicated';
 r:=public.questyourlife_social_command(gen_random_uuid(),'friend_accept','{"id":"a4000000-0000-0000-0000-000000000002"}');
 ASSERT r->'receipts'->0->>'result_status'='applied',r::text;
 r:=public.questyourlife_social_command(gen_random_uuid(),'friend_block','{"id":"a4000000-0000-0000-0000-000000000002"}');
 ASSERT r->'receipts'->0->>'result_status'='applied',r::text;
 r:=public.questyourlife_social_command(gen_random_uuid(),'chat_add_member','{"chat_id":"a4000000-0000-0000-0000-000000000003","user_id":"a1000000-0000-0000-0000-000000000003"}');
 ASSERT r->'receipts'->0->>'result_status'='rejected','group member promoted self to admin';
END $$;
SELECT set_config('request.jwt.claim.sub','a1000000-0000-0000-0000-000000000001',true);
SELECT set_config('request.jwt.claims','{"app_metadata":{"questyourlife_username":"social_one"}}',true);
DO $$ DECLARE r jsonb; i int; page jsonb; nextpage jsonb; BEGIN
 r:=public.questyourlife_social_command(gen_random_uuid(),'post_send','{"chat_id":"a4000000-0000-0000-0000-000000000002","content":"blocked"}');
 ASSERT r->'receipts'->0->>'result_status'='rejected','blocked user sent direct message';
 r:=public.questyourlife_social_command(gen_random_uuid(),'friend_unblock','{"id":"a4000000-0000-0000-0000-000000000002"}');
 ASSERT r->'receipts'->0->>'result_status'='applied',r::text;
 r:=public.questyourlife_social_command(gen_random_uuid(),'post_send','{"chat_id":"a4000000-0000-0000-0000-000000000002","content":"still blocked"}');
 ASSERT r->'receipts'->0->>'result_status'='rejected','user removed peers block';
 for i in 1..3 loop
  r:=public.questyourlife_social_command(gen_random_uuid(),'post_send',jsonb_build_object('chat_id','a4000000-0000-0000-0000-000000000003','content','group message '||i));
  ASSERT r->'receipts'->0->>'result_status'='applied',r::text;
 end loop;
 page:=public.questyourlife_posts('a4000000-0000-0000-0000-000000000003',null,null,2);
 ASSERT jsonb_array_length(page->'posts')=2 and (page->>'has_more')::boolean,'first cursor page';
 nextpage:=public.questyourlife_posts('a4000000-0000-0000-0000-000000000003',(page->'next_cursor'->>'created_at')::timestamptz,(page->'next_cursor'->>'id')::uuid,2);
 ASSERT jsonb_array_length(nextpage->'posts')=1 and not(nextpage->>'has_more')::boolean,'second cursor page';
 ASSERT not exists(select 1 from jsonb_array_elements(page->'posts') a cross join jsonb_array_elements(nextpage->'posts') b where a->>'id'=b->>'id'),'cursor repeated row';
 r:=public.questyourlife_social_command(gen_random_uuid(),'chat_add_member','{"chat_id":"a4000000-0000-0000-0000-000000000003","user_id":"a1000000-0000-0000-0000-000000000003"}');
 ASSERT r->'receipts'->0->>'result_status'='applied',r::text;
 r:=public.questyourlife_social_command(gen_random_uuid(),'chat_leave','{"id":"a4000000-0000-0000-0000-000000000003"}');
 ASSERT r->'receipts'->0->>'result_status'='applied',r::text;
 ASSERT not public.questyourlife_chat_access('a4000000-0000-0000-0000-000000000003'),'left group retained message access';
END $$;

-- Only a trusted service role can persist an assistant draft. Confirmation calls
-- the actual core goal API and must roll back both domain writes and receipts.
SET LOCAL ROLE service_role;
DO $$ DECLARE r jsonb; BEGIN
 r:=public.questyourlife_assistant_post('a5000000-0000-0000-0000-000000000010','a4000000-0000-0000-0000-000000000001',
  'AI plan proposal','{"plan_draft":{"title":"proposal"}}');
 ASSERT public.questyourlife_assistant_post('a5000000-0000-0000-0000-000000000010','a4000000-0000-0000-0000-000000000001',
  'AI plan proposal','{"plan_draft":{"title":"proposal"}}')=r,'assistant retry changed durable receipt';
END $$;
SET LOCAL ROLE authenticated;
DO $$ DECLARE r jsonb; v_before bigint; count_before bigint; body jsonb; BEGIN
 ASSERT (select count(*) from public.questyourlife_questbook)=0,'AI auto-applied unconfirmed plan';
 body:='{"id":"a5000000-0000-0000-0000-000000000010","goal":{"id":"a6000000-0000-0000-0000-000000000001","title":"confirmed title","description":"private","nodes":[]}}';
 r:=public.questyourlife_social_command(gen_random_uuid(),'plan_confirm',body);
 ASSERT r->'receipts'->0->>'result_status'='rejected','implicit AI plan executed';
 select count(*) into count_before from public.questyourlife_event;
 r:=public.questyourlife_social_command(gen_random_uuid(),'plan_confirm',body||jsonb_build_object('confirmed',true,'goal',
  jsonb_build_object('id','a6000000-0000-0000-0000-000000000001','title','bad plan','nodes',jsonb_build_array(jsonb_build_object('type','invalid')))));
 ASSERT r->'receipts'->0->>'result_status'='rejected','invalid AI plan accepted';
 ASSERT (select count(*) from public.questyourlife_questbook)=0,'invalid plan left partial goal';
 ASSERT (select count(*) from public.questyourlife_event)=count_before+1,'invalid plan retained nested core receipt';
 r:=public.questyourlife_social_command(gen_random_uuid(),'plan_confirm',body||'{"confirmed":true}');
 ASSERT r->'receipts'->0->>'result_status'='applied',r::text;
 ASSERT (select count(*) from public.questyourlife_questbook where type='book')=1,'confirmed plan missing';
 ASSERT (select count(*) from public.questyourlife_quest_join)=1,'confirmed plan lacks independent participation';
 ASSERT not exists(select 1 from public.questyourlife_quest_join where id=book_id),'AI plan recoupled join ID';
 r:=public.questyourlife_social_command(gen_random_uuid(),'plan_confirm',body||'{"confirmed":true}');
 ASSERT r->'receipts'->0->'result'->>'already_applied'='true','confirmation retry applied twice';
 r:=public.questyourlife_social_snapshot();
 ASSERT jsonb_typeof(r)='object' and jsonb_array_length(r->'my_books')=1,'snapshot did not expose persistent participating books';
 ASSERT r ?&array['alarms','unread_alarms','clans','chats','posts','public_profiles','my_books','popular_books','daily_inbox','clan_ranking','week_start'],'incomplete page contract';
 ASSERT jsonb_array_length(r->'chats')=2,'left group appeared in active chats';
 r:=public.questyourlife_pull_changes();
 ASSERT exists(select 1 from jsonb_array_elements(r->'events') e cross join jsonb_array_elements(e->'changes') c
  where c->>'entity'='post' and c->'data'->>'actor'='assistant'),'assistant post missing durable sync event';
END $$;
SELECT set_config('request.jwt.claim.sub','a1000000-0000-0000-0000-000000000003',true);
DO $$ DECLARE r jsonb; BEGIN
 ASSERT (select count(*) from public.questyourlife_questbook)=0,'private confirmed goal leaked';
 r:=public.questyourlife_social_command(gen_random_uuid(),'post_edit','{"id":"a5000000-0000-0000-0000-000000000001","content":"stolen edit"}');
 ASSERT r->'receipts'->0->>'result_status'='rejected','other author edited private message';
 ASSERT jsonb_array_length(public.questyourlife_social_snapshot()->'chats')=1,'unrelated AI/DM leaked in chat list';
END $$;
SET LOCAL ROLE anon;
DO $$ BEGIN
 BEGIN perform public.questyourlife_social_snapshot(); raise exception 'anonymous snapshot'; EXCEPTION WHEN insufficient_privilege THEN NULL; END;
 BEGIN perform public.questyourlife_social_command(gen_random_uuid(),'clan_create','{}'); raise exception 'anonymous mutation'; EXCEPTION WHEN insufficient_privilege THEN NULL; END;
END $$;
RESET ROLE;
DO $$ BEGIN
 ASSERT (select count(*) from public.questyourlife_event e join public.questyourlife_user u on u.id=e.user_id where e.sync_seq>u.sync_seq)=0,'uncommitted watermark';
 ASSERT not exists(select 1 from public.questyourlife_user u where u.id::text like 'a1000000%'
  and (select max(e.sync_seq) from public.questyourlife_event e where e.user_id=u.id)<>u.sync_seq),'social cursor diverged';
 BEGIN UPDATE public.questyourlife_event SET score_delta=999; raise exception 'immutable event overwritten'; EXCEPTION WHEN insufficient_privilege THEN NULL; END;
END $$;
ROLLBACK;
