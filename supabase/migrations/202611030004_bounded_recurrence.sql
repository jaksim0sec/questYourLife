-- Generate dated executions from reusable daily definitions, never copied nodes.
-- Re-running or deferring an occurrence cannot create a second instance.
-- Deferred triggers run at the request's outer role after the RPC has returned.
-- Their internal validation helper is deliberately not client executable.
alter function public.questyourlife_check_definition() security definer;
create function public.questyourlife_generate_upcoming(p_join uuid,p_until date default null,p_limit integer default 25)
returns jsonb language plpgsql security definer set search_path='' as $$
declare u uuid:=auth.uid(); j public.questyourlife_quest_join; tree jsonb; node jsonb;
 today date:=(now() at time zone 'Asia/Seoul')::date; last_day date; anchor date; day date;
 frequency text; every integer; occurrence text; execution uuid; request uuid; commands jsonb:='[]';
 more boolean:=false; due boolean; result jsonb; month_distance integer;
begin
 if u is null then raise exception 'authentication_required' using errcode='42501'; end if;
 if p_limit is null or p_limit not between 1 and 25 then raise exception 'invalid_generation_limit'; end if;
 last_day:=coalesce(p_until,today+7);
 if last_day<today or last_day>today+14 then raise exception 'scheduling_horizon_exceeded'; end if;
 perform 1 from public.questyourlife_user where id=u for update;
 select * into j from public.questyourlife_quest_join where id=p_join and user_id=u and status='active' and deleted_at is null for update;
 if not found then raise exception 'active_join_required' using errcode='42501'; end if;
 tree:=public.questyourlife_effective_tree(j.book_id,j.overrides);
 anchor:=(j.created_at at time zone 'Asia/Seoul')::date;
 for day in select d::date from generate_series(today::timestamp,last_day::timestamp,interval '1 day') d loop
  for node in select value from jsonb_array_elements(tree) where value->>'type'='daily' order by value->>'id' loop
   frequency:=coalesce(node->'data'->'recurrence'->>'frequency','none');
   every:=coalesce((node->'data'->'recurrence'->>'interval')::integer,1);
   due:=false;
   if frequency='none' then due:=day=today;
   elsif frequency='daily' then due:=(day-anchor)%every=0;
   elsif frequency='weekly' then due:=(day-anchor)%(7*every)=0;
   elsif frequency='monthly' then
    month_distance:=(extract(year from day)::int-extract(year from anchor)::int)*12+extract(month from day)::int-extract(month from anchor)::int;
    due:=month_distance%every=0 and extract(day from day)=least(extract(day from anchor),extract(day from (date_trunc('month',day)+interval '1 month - 1 day')));
   end if;
   if not due then continue; end if;
   occurrence:=case when frequency='none' then 'once' else 'recurrence:'||day::text end;
   if exists(select 1 from public.questyourlife_daily_quest d where d.join_id=j.id and d.source_id=(node->>'id')::uuid and d.occurrence_key=occurrence) then continue; end if;
   if jsonb_array_length(commands)>=p_limit then more:=true; exit; end if;
   execution:=md5('questyourlife:occurrence:'||j.id||':'||(node->>'id')||':'||occurrence)::uuid;
   request:=md5('questyourlife:generate:'||execution)::uuid;
   commands:=commands||jsonb_build_array(jsonb_build_object('event_id',request,'operation','daily_put','base_version',0,
    'data',jsonb_build_object('id',execution,'join_id',j.id,'source_id',node->>'id','main_id',node->>'head',
     'occurrence_key',occurrence,'scheduled_date',day,'status','proposed')));
  end loop;
  if more then exit; end if;
 end loop;
 result:=public.questyourlife_apply_batch(commands);
 return result||jsonb_build_object('generated',jsonb_array_length(commands),'has_more',more,'through',last_day);
end $$;
revoke all on function public.questyourlife_generate_upcoming(uuid,date,integer) from public,anon,authenticated;
grant execute on function public.questyourlife_generate_upcoming(uuid,date,integer) to authenticated;
