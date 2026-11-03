\set ON_ERROR_STOP on
-- Disposable PostgreSQL only. Run foundation then correction migration first.
-- All fixtures and pg_temp helpers roll back; never run against the live project.
begin;
insert into auth.users(id) values
 ('71000000-0000-0000-0000-000000000001'),
 ('71000000-0000-0000-0000-000000000002'),
 ('71000000-0000-0000-0000-000000000003');

create function pg_temp.command(op text,b jsonb,base bigint default 0) returns jsonb
language plpgsql as $$ declare r jsonb; begin
 r:=public.questyourlife_apply_batch(jsonb_build_array(jsonb_build_object('event_id',gen_random_uuid(),'operation',op,'base_version',base,'data',b)));
 return r->'receipts'->0;
end $$;
create function pg_temp.applied(op text,b jsonb,base bigint default 0) returns jsonb
language plpgsql as $$ declare r jsonb; begin
 r:=pg_temp.command(op,b,base); assert r->>'result_status'='applied',r::text; return r;
end $$;

set local role authenticated;
do $$
declare
 author uuid:='71000000-0000-0000-0000-000000000001';
 reader uuid:='71000000-0000-0000-0000-000000000002';
 other_user uuid:='71000000-0000-0000-0000-000000000003';
 root uuid:=gen_random_uuid(); private_root uuid:=gen_random_uuid(); v2 uuid:=gen_random_uuid();
 chap1 uuid:=gen_random_uuid(); chap2 uuid:=gen_random_uuid(); main1 uuid:=gen_random_uuid(); main2 uuid:=gen_random_uuid();
 def1 uuid:=gen_random_uuid(); def2 uuid:=gen_random_uuid(); custom uuid:=gen_random_uuid();
 own_join uuid:=gen_random_uuid(); reader_join uuid:=gen_random_uuid(); other_join uuid:=gen_random_uuid();
 execution uuid:=gen_random_uuid(); execution2 uuid:=gen_random_uuid(); cancel_id uuid:=gen_random_uuid(); task uuid;
 r jsonb; tree jsonb; overlay jsonb; nodes jsonb; body jsonb; receipt jsonb; event_request jsonb;
 initial_count bigint; count_now bigint; edit bigint; version_no bigint; amount bigint; grant_id uuid;
 snapshot jsonb; old_snapshot jsonb; original_week date; i integer;
begin
 perform set_config('request.jwt.claim.sub',author::text,true);
 perform set_config('request.jwt.claims','{"app_metadata":{"questyourlife_username":"correction_author"}}',true);
 perform public.questyourlife_bootstrap();
 nodes:=jsonb_build_array(
  jsonb_build_object('id',chap1,'head',root,'type','chap','name','First chapter','position',0),
  jsonb_build_object('id',chap2,'head',root,'type','chap','name','Second chapter','position',1,'tags','[]'::jsonb),
  jsonb_build_object('id',main1,'head',chap1,'type','main','name','First main','position',0),
  jsonb_build_object('id',main2,'head',chap2,'type','main','name','Second main','position',1),
  jsonb_build_object('id',def1,'head',main1,'type','daily','name','Read five characters','position',0,'data',
   '{"description":"Original description","criteria":{"method":"self_check","description":"Read all five characters","target":5,"unit":"characters"},"required_count":2,"recurrence":{"frequency":"daily","interval":1},"estimated_minutes":10}'::jsonb),
  jsonb_build_object('id',def2,'head',main2,'type','daily','name','Review','position',0,'data',
   '{"criteria":{"method":"self_check","description":"Review characters"},"required_count":1}'::jsonb));
 r:=pg_temp.applied('goal_put',jsonb_build_object('id',root,'join_id',own_join,'title','Reusable book','description','Shared description','tags',jsonb_build_array('study'),'nodes',nodes));
 assert exists(select 1 from public.questyourlife_quest_join where id=own_join and book_id=root and id<>book_id),'creator join must be independent';
 assert exists(select 1 from jsonb_array_elements(r->'changes') c where c->>'entity'='quest_join' and jsonb_typeof(c->'data'->'effective_tree')='array'),'derived join after-image missing';
 tree:=public.questyourlife_resolve_tree(own_join);
 assert jsonb_array_length(tree)=7,'initial shared tree';
 assert (select v->'effective_tags' from jsonb_array_elements(tree) v where v->>'id'=def1::text)='["study"]','NULL tags inherit';
 assert (select v->'effective_tags' from jsonb_array_elements(tree) v where v->>'id'=def2::text)='[]','empty tags clear';
 perform pg_temp.applied('goal_put',jsonb_build_object('id',private_root,'title','Private','description','Never public','nodes','[]'::jsonb));
 perform pg_temp.applied('book_publish',jsonb_build_object('id',root,'is_public',true),1);
 r:=pg_temp.command('goal_put',jsonb_build_object('id',root,'title','Tamper','nodes',nodes),2);
 assert r->>'result_status'='rejected' and r->'result'->>'error'='published_immutable','published edition mutated';

 perform set_config('request.jwt.claim.sub',reader::text,true);
 perform set_config('request.jwt.claims','{"app_metadata":{"questyourlife_username":"correction_reader"}}',true);
 perform public.questyourlife_bootstrap();
 assert (select count(*) from public.questyourlife_questbook)=7,'root inherited RLS must expose all public descendants and no private drafts';
 perform pg_temp.applied('book_join',jsonb_build_object('id',reader_join,'book_id',root));
 r:=pg_temp.command('book_join',jsonb_build_object('id',gen_random_uuid(),'book_id',root));
 assert r->>'result_status'='rejected','duplicate active participation';
 r:=pg_temp.command('book_join',jsonb_build_object('id',gen_random_uuid(),'book_id',private_root));
 assert r->>'result_status'='rejected','unrelated private book joined';
 initial_count:=(select count(*) from public.questyourlife_questbook);
 overlay:=jsonb_build_object('patch',jsonb_build_object(main1::text,jsonb_build_object('name','My main title')),'added','[]'::jsonb,'hidden','[]'::jsonb);
 r:=pg_temp.applied('join_edit',jsonb_build_object('id',reader_join,'edit_version',1,'overrides',overlay));
 tree:=public.questyourlife_resolve_tree(reader_join);
 assert (select v->>'name' from jsonb_array_elements(tree) v where v->>'id'=main1::text)='My main title','logical title patch';
 assert (select (v->>'physical_id')::uuid from jsonb_array_elements(tree) v where v->>'id'=def1::text)=def1,'unchanged descendant remains shared';
 assert (select (v->>'head')::uuid from jsonb_array_elements(tree) v where v->>'id'=def1::text)=main1,'logical descendant parent retained';
 assert (select count(*) from public.questyourlife_questbook)=initial_count,'overlay copied base rows';
 r:=pg_temp.command('join_edit',jsonb_build_object('id',reader_join,'edit_version',1,'overrides',overlay));
 assert r->>'result_status'='conflict','stale overlay write accepted';
 overlay:=jsonb_set(overlay,'{added}',jsonb_build_array(jsonb_build_object('id',custom,'head',main1,'type','daily','name','Custom','sort_order',1,
  'data','{"criteria":{"method":"self_check","description":"Custom condition"},"required_count":1}'::jsonb)));
 perform pg_temp.applied('join_edit',jsonb_build_object('id',reader_join,'edit_version',2,'overrides',overlay));
 overlay:=jsonb_set(overlay,array['patch',custom::text],jsonb_build_object('name','Moved custom','head',main2,'sort_order',-1,'tags',jsonb_build_array('custom')));
 perform pg_temp.applied('join_patch',jsonb_build_object('id',reader_join,'overrides',overlay),3);
 tree:=public.questyourlife_resolve_tree(reader_join);
 assert (select v->>'head' from jsonb_array_elements(tree) v where v->>'id'=custom::text)=main2::text,'custom reparenting';
 assert (select v->'effective_tags' from jsonb_array_elements(tree) v where v->>'id'=custom::text)='["custom"]','custom tags';
 overlay:=jsonb_set(overlay,'{hidden}',jsonb_build_array(main2));
 perform pg_temp.applied('join_edit',jsonb_build_object('id',reader_join,'edit_version',4,'overrides',overlay));
 tree:=public.questyourlife_resolve_tree(reader_join);
 assert not exists(select 1 from jsonb_array_elements(tree) v where v->>'id' in(main2::text,def2::text,custom::text)),'hide must exclude dependent descendants';
 overlay:=jsonb_set(overlay,'{hidden}','[]');
 perform pg_temp.applied('join_edit',jsonb_build_object('id',reader_join,'edit_version',5,'overrides',overlay));
 r:=pg_temp.command('join_edit',jsonb_build_object('id',reader_join,'edit_version',6,'overrides',jsonb_set(overlay,array['patch',main1::text,'head'],to_jsonb(def1))));
 assert r->>'result_status'='rejected','invalid hierarchy/reparenting accepted';
 assert (select edit_version from public.questyourlife_quest_join where id=reader_join)=6,'rejected overlay partially committed';
 r:=pg_temp.command('join_edit',jsonb_build_object('id',reader_join,'edit_version',6,'overrides','{}'::jsonb));
 assert r->>'result_status'='rejected','missing overlay shape fields accepted';
 r:=pg_temp.command('join_edit',jsonb_build_object('id',reader_join,'edit_version',6,'overrides',jsonb_build_object('patch','null'::jsonb,'added','[]'::jsonb,'hidden','[]'::jsonb)));
 assert r->>'result_status'='rejected','JSON null overlay accepted';
 assert (select count(*) from public.questyourlife_questbook)=initial_count,'add/move/hide copied base nodes';

 body:=jsonb_build_object('id',execution,'join_id',reader_join,'source_id',def1,'main_id',main1,'occurrence_key','day-one',
  'title','CLIENT SPOOF','criteria',jsonb_build_object('description','CLIENT SPOOF'),'scheduled_date',current_date,'status','accepted','reward',999999);
 perform pg_temp.applied('daily_put',body);
 select content_snapshot into snapshot from public.questyourlife_daily_quest where id=execution;
 assert snapshot->>'title'='Read five characters' and snapshot->'criteria'->>'description'='Read all five characters','source snapshot trusted client';
 assert snapshot->>'required_count'='2' and snapshot->>'estimated_minutes'='10','condition snapshot missing';
 old_snapshot:=snapshot;
 perform pg_temp.applied('daily_put',body||jsonb_build_object('scheduled_date',current_date+1,'status','deferred'),1);
 assert exists(select 1 from public.questyourlife_daily_quest where id=execution and occurrence_key='day-one' and status='accepted' and scheduled_date=current_date+1 and content_snapshot=old_snapshot),'deferral changed occurrence/acceptance/conditions';
 perform pg_temp.applied('daily_put',body||jsonb_build_object('id',execution2,'occurrence_key','day-two','scheduled_date',current_date+1));
 r:=pg_temp.command('daily_put',body||jsonb_build_object('id',gen_random_uuid()));
 assert r->>'result_status'='rejected','duplicate occurrence accepted';
 perform pg_temp.applied('daily_put',body||jsonb_build_object('id',cancel_id,'occurrence_key','cancelled-one','status','cancelled'));
 r:=pg_temp.command('daily_put',body||jsonb_build_object('id',gen_random_uuid(),'occurrence_key','cancelled-one'));
 assert r->>'result_status'='rejected','cancelled occurrence reused';
 r:=pg_temp.command('daily_put',body||jsonb_build_object('id',gen_random_uuid(),'occurrence_key','too-far','scheduled_date',current_date+32));
 assert r->>'result_status'='rejected','unbounded scheduling';
 r:=pg_temp.command('daily_put',body||jsonb_build_object('source_id',main2,'main_id',main2),2);
 assert r->>'result_status'='rejected','source/occurrence identity mutated';
 r:=pg_temp.command('daily_put',body||jsonb_build_object('legacy',true,'status','completed'),2);
 assert r->>'result_status'='rejected','legacy flag bypassed normal completion of an existing execution';

 event_request:=jsonb_build_array(jsonb_build_object('event_id',gen_random_uuid(),'operation','daily_complete','data',jsonb_build_object('id',execution,'cash',99999,'score',99999)));
 receipt:=public.questyourlife_apply_batch(event_request)->'receipts'->0;
 assert receipt->>'cash_delta'='20' and receipt->>'xp_delta'='20' and receipt->>'score_delta'='20','server 20 reward policy';
 grant_id:=(receipt->>'id')::uuid; original_week:=(receipt->>'week_start')::date;
 assert original_week=date_trunc('week',now() at time zone 'Asia/Seoul')::date,'Seoul week boundary';
 r:=public.questyourlife_apply_batch(event_request)->'receipts'->0;
 assert r=receipt,'sealed replay changed receipt';
 r:=pg_temp.applied('daily_complete',jsonb_build_object('id',execution));
 assert r->>'cash_delta'='0','different event duplicate completion rewarded';
 begin
  perform public.questyourlife_apply_batch(jsonb_set(event_request,'{0,data,cash}','1'));
  raise exception 'changed sealed replay accepted';
 exception when invalid_parameter_value then null; end;
 r:=pg_temp.applied('daily_undo',jsonb_build_object('id',execution));
 assert r->>'cash_delta'='-20' and r->>'score_delta'='-20' and r->>'reverses_id'=grant_id::text,'immutable economic reversal';
 r:=pg_temp.applied('daily_undo',jsonb_build_object('id',execution));
 assert r->>'cash_delta'='0','duplicate undo reversed twice';
 for i in 1..3 loop
  r:=pg_temp.applied('daily_complete',jsonb_build_object('id',execution));
  assert r->>'cash_delta'='20' and (r->>'week_start')::date=original_week and r->'result' ? 'restores_id','restore original entitlement';
  perform pg_temp.applied('daily_undo',jsonb_build_object('id',execution));
 end loop;
 perform pg_temp.applied('daily_complete',jsonb_build_object('id',execution));
 perform pg_temp.applied('daily_complete',jsonb_build_object('id',execution2));
 assert (select cash from public.questyourlife_user)=40,'independent recurring executions earn separately';
 assert (select sum(score_delta) from public.questyourlife_event)=40,'net ranking must match qualified completions';
 assert (select node_state from public.questyourlife_quest_join where id=reader_join)='{}','daily histories duplicated in node_state';

 -- Existing main title overlay conflicts with the author's v2 title edit.
 perform set_config('request.jwt.claim.sub',author::text,true);
 perform set_config('request.jwt.claims','{"app_metadata":{"questyourlife_username":"correction_author"}}',true);
 perform pg_temp.applied('book_edition',jsonb_build_object('id',root,'new_id',v2),2);
 select jsonb_agg(jsonb_build_object('id',id,'logical_id',logical_id,'head',head,'type',type,'name',case when logical_id=main1 then 'Author v2 main' else name end,'position',sort_order,'tags',to_jsonb(tags),'data',data))
  into nodes from public.questyourlife_questbook where book_id=v2 and type<>'book';
 perform pg_temp.applied('goal_put',jsonb_build_object('id',v2,'title','Edition two','description','New edition','nodes',nodes),1);
 perform pg_temp.applied('book_publish',jsonb_build_object('id',v2,'is_public',true),2);
 assert (select name from public.questyourlife_questbook where id=main1)='First main','v1 mutated by edition edit';

 perform set_config('request.jwt.claim.sub',other_user::text,true);
 perform set_config('request.jwt.claims','{"app_metadata":{"questyourlife_username":"correction_other"}}',true);
 perform public.questyourlife_bootstrap();
 perform pg_temp.applied('book_join',jsonb_build_object('id',other_join,'book_id',root));
 assert exists(select 1 from public.questyourlife_quest_join where id=other_join and id<>reader_join and book_id=root),'independent participant IDs';
 assert (select count(*) from public.questyourlife_daily_quest)=0,'private execution leaked';
 assert (select count(*) from public.questyourlife_user)=1,'private user columns leaked';
 begin perform public.questyourlife_resolve_tree(reader_join); raise exception 'private overlay leaked'; exception when insufficient_privilege then null; end;
 r:=pg_temp.command('daily_complete',jsonb_build_object('id',execution));
 assert r->>'result_status'='rejected','cross-user reward allowed';
 tree:=public.questyourlife_resolve_tree(other_join);
 assert (select v->>'name' from jsonb_array_elements(tree) v where v->>'id'=main1::text)='First main','participant overlay affected another user';

 perform set_config('request.jwt.claim.sub',reader::text,true);
 perform set_config('request.jwt.claims','{"app_metadata":{"questyourlife_username":"correction_reader"}}',true);
 assert (select book_id from public.questyourlife_quest_join where id=reader_join)=root,'published v2 silently upgraded participant';
 r:=pg_temp.command('join_upgrade',jsonb_build_object('id',reader_join,'book_id',v2,'accept',true),6);
 assert r->>'result_status'='conflict' and jsonb_array_length(r->'result'->'conflicts')>0,'base/overlay upgrade conflict not explicit';
 r:=pg_temp.command('join_upgrade',jsonb_build_object('id',reader_join,'book_id',v2),6);
 assert r->>'result_status'='rejected','upgrade without explicit acceptance';
 perform pg_temp.applied('join_upgrade',jsonb_build_object('id',reader_join,'book_id',v2,'accept',true,'overrides',overlay),6);
 tree:=public.questyourlife_resolve_tree(reader_join);
 assert (select v->>'physical_id' from jsonb_array_elements(tree) v where v->>'id'=main1::text)<>main1::text,'edition physical IDs reused';
 assert (select v->>'name' from jsonb_array_elements(tree) v where v->>'id'=main1::text)='My main title','explicit conflict resolution lost overlay';
 assert (select content_snapshot from public.questyourlife_daily_quest where id=execution)=old_snapshot,'upgrade changed historical snapshot';
 overlay:=jsonb_set(overlay,'{hidden}',jsonb_build_array(def1));
 perform pg_temp.applied('join_edit',jsonb_build_object('id',reader_join,'edit_version',7,'overrides',overlay));
 assert exists(select 1 from public.questyourlife_daily_quest where id=execution and status='completed' and content_snapshot=old_snapshot),'hide removed historical completion';
 assert exists(select 1 from jsonb_array_elements(public.questyourlife_my_books()->'changes') c where c->>'entity'='quest_join' and c->'data'->>'id'=reader_join::text and jsonb_typeof(c->'data'->'effective_tree')='array'),'active books snapshot missing effective tree';

 -- Fill to 100 using three fresh standalone grants, buy the 80 item, then undo
 -- a 20 reward after spending to require debt. Restore and future earnings repay it.
 for i in 1..3 loop
  task:=gen_random_uuid();
  perform pg_temp.applied('daily_put',jsonb_build_object('id',task,'title','Standalone','scheduled_date',current_date,'status','accepted'));
  perform pg_temp.applied('daily_complete',jsonb_build_object('id',task));
 end loop;
 assert (select cash from public.questyourlife_user)=100,'existing 100 daily cap altered';
 perform pg_temp.applied('purchase',jsonb_build_object('id',gen_random_uuid(),'item_id',2,'price',0));
 assert (select cash from public.questyourlife_user)=20 and (select xp from public.questyourlife_user)=100,'purchase changed XP or trusted price';
 perform pg_temp.applied('daily_undo',jsonb_build_object('id',execution));
 perform pg_temp.applied('daily_undo',jsonb_build_object('id',execution2));
 assert (select cash from public.questyourlife_user)=0 and (select cash_debt from public.questyourlife_user)=20,'spent reversal must create debt';
 assert (select inventory ? '2' from public.questyourlife_user),'spent reversal revoked inventory';
 r:=pg_temp.applied('daily_complete',jsonb_build_object('id',task));
 assert r->>'cash_delta'='0','already complete paid twice';
 task:=gen_random_uuid();
 perform pg_temp.applied('daily_put',jsonb_build_object('id',task,'title','Over cap','scheduled_date',current_date,'status','accepted'));
 r:=pg_temp.applied('daily_complete',jsonb_build_object('id',task));
 assert r->>'cash_delta'='0','undo bypassed gross 100 daily cap';
 overlay:=jsonb_set(overlay,'{hidden}','[]');
 perform pg_temp.applied('join_edit',jsonb_build_object('id',reader_join,'edit_version',8,'overrides',overlay));
 perform pg_temp.applied('daily_complete',jsonb_build_object('id',execution));
 assert (select cash from public.questyourlife_user)=0 and (select cash_debt from public.questyourlife_user)=0,'future earning failed to repay outstanding reversal debt';
 perform pg_temp.applied('daily_complete',jsonb_build_object('id',execution2));
 assert (select cash from public.questyourlife_user)=20 and (select xp from public.questyourlife_user)=100,'recompletion exceeded original net rewards';
 assert (select cash-cash_debt from public.questyourlife_user)=(select sum(cash_delta) from public.questyourlife_event),'cash/debt projection differs from ledger';
 assert (select xp from public.questyourlife_user)=(select sum(xp_delta) from public.questyourlife_event),'XP projection differs from ledger';
 assert (select sum(score_delta) from public.questyourlife_event where operation='purchase')=0,'purchase inflates rankings';
 begin update public.questyourlife_user set cash=999; raise exception 'balance directly writable'; exception when insufficient_privilege then null; end;
 begin perform public.questyourlife_project_economy(reader,999,999); raise exception 'economy helper publicly callable'; exception when insufficient_privilege then null; end;
end $$;

reset role;
do $$ begin
 assert public.questyourlife_week_start('2026-11-01 14:59:59+00')='2026-10-26'::date,'Sunday Seoul cutoff';
 assert public.questyourlife_week_start('2026-11-01 15:00:00+00')='2026-11-02'::date,'Monday Seoul cutoff';
 begin update public.questyourlife_event set cash_delta=999; raise exception 'ledger mutable'; exception when insufficient_privilege then null; end;
 assert (select count(*) from pg_class c join pg_namespace n on n.oid=c.relnamespace where n.nspname='public' and c.relkind='r' and c.relname like 'questyourlife_%') in (5,9),'unexpected extra domain/ledger tables';
end $$;
set constraints all immediate;
rollback;
