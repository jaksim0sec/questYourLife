\set ON_ERROR_STOP on
-- Disposable local PostgreSQL only.
begin;
insert into auth.users(id) values('c1000000-0000-0000-0000-000000000001');
set local role authenticated;
select set_config('request.jwt.claim.sub','c1000000-0000-0000-0000-000000000001',true);
select set_config('request.jwt.claims','{"app_metadata":{"questyourlife_username":"recurrence_check"}}',true);
do $$ declare root uuid:=gen_random_uuid(); j uuid:=gen_random_uuid(); c uuid:=gen_random_uuid(); m uuid:=gen_random_uuid(); d uuid:=gen_random_uuid(); r jsonb; e public.questyourlife_daily_quest; today date:=(now() at time zone 'Asia/Seoul')::date; begin
 r:=public.questyourlife_apply_batch(jsonb_build_array(jsonb_build_object('event_id',gen_random_uuid(),'operation','goal_put','base_version',0,
  'data',jsonb_build_object('id',root,'join_id',j,'title','Recurring book','description','', 'nodes',jsonb_build_array(
   jsonb_build_object('id',c,'head',root,'type','chap','name','Chapter','position',0),
   jsonb_build_object('id',m,'head',c,'type','main','name','Main','position',0),
   jsonb_build_object('id',d,'head',m,'type','daily','name','Read five','position',0,'data',
    '{"criteria":{"method":"self_check","description":"Read five"},"required_count":2,"recurrence":{"frequency":"daily","interval":1}}'::jsonb))))));
 assert r->'receipts'->0->>'result_status'='applied',r::text;
 r:=public.questyourlife_generate_upcoming(j,today+3,25);
 assert r->>'generated'='4',r::text;
 assert (select count(*) from public.questyourlife_daily_quest)=4,'bounded distinct dates';
 assert not exists(select 1 from jsonb_array_elements(r->'receipts') x where x->>'result_status'<>'applied'),'generation rejected';
 r:=public.questyourlife_generate_upcoming(j,today+3,25);
 assert r->>'generated'='0','retry duplicated recurrence';
 select * into e from public.questyourlife_daily_quest where scheduled_date=today;
 r:=public.questyourlife_apply_batch(jsonb_build_array(jsonb_build_object('event_id',gen_random_uuid(),'operation','daily_put','base_version',e.version,
  'data',jsonb_build_object('id',e.id,'join_id',j,'source_id',d,'main_id',m,'occurrence_key',e.occurrence_key,'scheduled_date',today+1,'status','accepted'))));
 assert r->'receipts'->0->>'result_status'='applied',r::text;
 r:=public.questyourlife_generate_upcoming(j,today+3,25);
 assert r->>'generated'='0','defer duplicated original date';
 assert (select content_snapshot->'criteria'->>'description' from public.questyourlife_daily_quest where id=e.id)='Read five','snapshot omitted criteria';
 begin perform public.questyourlife_generate_upcoming(j,today+365);raise exception 'year generation accepted';exception when raise_exception then assert SQLERRM='scheduling_horizon_exceeded',SQLERRM;end;
end $$;
set constraints all immediate;
rollback;
