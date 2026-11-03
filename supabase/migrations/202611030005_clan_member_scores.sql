-- Read-only member contribution extension to existing social snapshot.
-- No new tables, no unrelated services and no exposure of personal goal details.
-- For nonmembers / guest, the field is an empty array.
create or replace function public.questyourlife_social_snapshot() returns jsonb
language plpgsql stable security definer set search_path='' as $$
declare u uuid:=auth.uid(); week date:=(date_trunc('week',now() at time zone 'Asia/Seoul'))::date;
 alarms jsonb; clans jsonb; chats jsonb; posts jsonb; books jsonb; popular jsonb; daily jsonb; ranks jsonb; profiles jsonb; profile_ids uuid[]; member_scores jsonb; active_clan uuid;
begin
 if u is null or not exists(select 1 from public.questyourlife_user where id=u and deleted_at is null)
 then raise exception 'authentication_required' using errcode='42501'; end if;
 select coalesce(jsonb_agg(to_jsonb(a) order by a.created_at desc,a.id desc),'[]') into alarms
 from (select * from public.questyourlife_alarm where user_id=u order by read,created_at desc,id desc limit 100) a;
 select coalesce(jsonb_agg(public.questyourlife_clan_projection(c.id,u) order by c.priority,c.updated_at desc,c.id),'[]') into clans
 from (select c.*,case when public.questyourlife_member_active(c.members,u) then 0 else 1 end priority
  from public.questyourlife_clan c where (c.deleted_at is null and (public.questyourlife_member_active(c.members,u)
   or c.settings->'discoverable'='true'::jsonb))
   or exists(select 1 from jsonb_each(c.chests) x where x.value->'eligible' ? u::text)
  order by priority,c.updated_at desc,c.id limit 50) c;
 select coalesce(jsonb_agg(public.questyourlife_chat_projection(c.id,u) order by c.updated_at desc,c.id),'[]') into chats
 from (select * from public.questyourlife_chat where deleted_at is null and members ? u::text
  and public.questyourlife_member_active(members,u) order by updated_at desc,id limit 50) c;
 -- One latest post per visible chat; message history uses the cursor RPC.
 select coalesce(jsonb_agg(x->'latest_post'),'[]') into posts from jsonb_array_elements(chats) x
 where x->'latest_post'<>'null'::jsonb;
 select coalesce(jsonb_agg(jsonb_build_object('join',to_jsonb(j),'book',to_jsonb(b)) order by j.updated_at desc,j.id),'[]') into books
 from (select * from public.questyourlife_quest_join where user_id=u and status='active' and deleted_at is null
  order by updated_at desc,id limit 100) j join public.questyourlife_questbook b on b.id=j.book_id;
 select coalesce(jsonb_agg(to_jsonb(t) order by t.active_participants desc,t.logical_id),'[]') into popular
 from (select b.logical_id,count(distinct j.user_id) active_participants,
  (select jsonb_build_object('id',v.id,'logical_id',v.logical_id,'name',v.name,'tags',v.tags,'edition',v.edition,'owner_id',v.owner_id)
   from public.questyourlife_questbook v where v.logical_id=b.logical_id and v.type='book'
    and v.is_public and v.publication_status='published' and v.deleted_at is null
   order by v.edition desc,v.id limit 1) book
  from public.questyourlife_questbook b left join public.questyourlife_quest_join j
   on j.book_id=b.id and j.status='active' and j.deleted_at is null
  where b.type='book' and b.is_public and b.publication_status='published' and b.deleted_at is null
  group by b.logical_id order by active_participants desc,b.logical_id limit 30) t;
 select coalesce(jsonb_agg(to_jsonb(d) order by d.scheduled_date,d.id),'[]') into daily
 from (select * from public.questyourlife_daily_quest where user_id=u and deleted_at is null
  and scheduled_date between (now() at time zone 'Asia/Seoul')::date-7 and (now() at time zone 'Asia/Seoul')::date+14
  order by scheduled_date,id limit 200) d;
 select coalesce(jsonb_agg(to_jsonb(r) order by r.score desc,r.clan_id),'[]') into ranks
 from (select e.clan_id_at_event clan_id,sum(e.score_delta) score,rank() over(order by sum(e.score_delta) desc) rank
  from public.questyourlife_event e join public.questyourlife_clan c on c.id=e.clan_id_at_event
  where e.week_start=week and e.result_status='applied' and e.score_delta<>0 and e.kind='command'
   and c.deleted_at is null and (public.questyourlife_member_active(c.members,u) or c.settings->'discoverable'='true'::jsonb)
  group by e.clan_id_at_event order by score desc,e.clan_id_at_event limit 50) r;
 -- Only active members may see their own clan's aggregate scores.
 -- No titles, goal details, or other private event fields leave the database.
 select c.id into active_clan
 from public.questyourlife_clan c
 where c.deleted_at is null and public.questyourlife_member_active(c.members,u)
 order by c.id limit 1;
 select coalesce(jsonb_agg(jsonb_build_object('user_id',s.user_id,'score',s.score)
   order by s.score desc,s.user_id),'[]'::jsonb) into member_scores
 from (
  select m.key::uuid user_id,coalesce(sum(e.score_delta),0) score
  from public.questyourlife_clan c
  cross join lateral jsonb_each(c.members) m
  left join public.questyourlife_event e on e.user_id=m.key::uuid
   and e.week_start=week and e.clan_id_at_event=c.id
   and e.result_status='applied' and e.kind='command' and e.score_delta<>0
  where c.id=active_clan and c.deleted_at is null
   and public.questyourlife_member_active(c.members,m.key::uuid)
  group by m.key
  order by score desc,m.key limit 50
 ) s;
 select array_agg(distinct id) into profile_ids from (
  select u id union select (x->>'owner_id')::uuid from jsonb_array_elements(clans) x
  union select key::uuid from jsonb_array_elements(clans) x cross join lateral jsonb_each(x->'members')
  union select key::uuid from jsonb_array_elements(chats) x cross join lateral jsonb_each(x->'members')
 ) p;
 -- Profile lookup is bounded independently even when many group members exist.
 profiles:=public.questyourlife_public_profiles((coalesce(profile_ids,'{}'::uuid[]))[1:100])->'profiles';
 return jsonb_build_object('alarms',alarms,'unread_alarms',(select count(*) from public.questyourlife_alarm where user_id=u and read=0),
  'clans',clans,'chats',chats,'posts',posts,'public_profiles',profiles,'my_books',books,'popular_books',popular,
  'daily_inbox',daily,'clan_ranking',ranks,'clan_member_scores',member_scores,'week_start',week);
end $$;
