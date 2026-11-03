-- SOCIAL CONTRACT FOR CORE / ANDROID INTEGRATION (migration 002 must run first).
-- Exactly four new domain tables; keep the existing immutable event ledger.
-- Requires 002 columns: user.cash_debt bigint; event.score_delta bigint,
-- event.clan_id_at_event uuid, event.week_start date. No changes to 001.
-- RPC questyourlife_social_command(p_event_id uuid,p_operation text,p_data jsonb)
-- returns {receipts:[event]}, with requester after-images in event.changes and
-- user.sync_seq. Operation names: clan_create/clan_invite/clan_join/clan_leave/
-- clan_transfer/clan_books/clan_chest_unlock/clan_chest_claim, chat_open_ai/
-- chat_open_direct/chat_create_group/chat_add_member/chat_leave/chat_read,
-- friend_request/friend_accept/friend_block/friend_unblock, post_send/
-- post_edit/post_delete/plan_confirm, alarm_read. IDs are p_data.id;
-- chat IDs are p_data.chat_id; peer is p_data.user_id; invitation is p_data.token.
-- plan_confirm requires {id:<assistant post>,confirmed:true,goal:{id,title,
-- description,nodes:[{id,head,type,name,position,...}]}} and calls core
-- questyourlife_apply_batch([{event_id:<derived>,operation:'goal_put',
-- base_version:0,data:<goal>}]) in the same transaction; rejected core receipt
-- rolls everything back. Goal is editable by the confirming user; no AI SQL.
-- Snapshot RPC questyourlife_social_snapshot() returns an OBJECT with
-- alarms,unread_alarms,clans,chats,posts,public_profiles,my_books,popular_books,
-- daily_inbox,clan_ranking,week_start; all lists bounded, no raw clan invites.
-- questyourlife_posts(p_chat_id uuid,p_before_created_at timestamptz DEFAULT NULL,
-- p_before_id uuid DEFAULT NULL,p_limit int DEFAULT 50) returns
-- {posts:[...],next_cursor:{created_at,id}|null,has_more:boolean}.
-- questyourlife_public_profiles(p_ids uuid[]) returns {profiles:[...]}.
-- Trusted AI persistence RPC questyourlife_assistant_post(p_event_id uuid,
-- p_chat_id uuid,p_content text,p_payload jsonb DEFAULT '{}') is service-role
-- only. It creates assistant posts and owner projection events atomically.
-- Clan member history: current {role,joined_at,left_at} plus history:[interval].
-- No direct table reads for clan/chat: even members use filtered RPCs, because
-- whole-row RLS cannot hide token hashes or other members' private metadata.
-- Android coordination: friend_request/friend_accept/friend_block use {id:chat}
-- (or {chat_id:chat}); open_direct must precede friend_request. Commands return
-- standard {receipts:[...]} rather than top-level IDs: IDs are receipt.result.
-- AI opening is idempotent by server key even when caller supplies a new ID.
-- Snapshot my_books entries are {join:<row>,book:<root>}. Popular entries are
-- {logical_id,active_participants,book:<restricted public root>}. No focus table.
-- Server chest policy v1: settled week, score >=100, reward 50 cash / 0 XP /
-- 0 score. Frozen eligibility: member at week close and positive net contribution.
-- Peers refresh snapshots; requester/AI owner receive ordinary pull_changes
-- after-images. No fanout user locks or cross-account event disclosures.
-- Android parser: clan_ranking rows expose {clan_id,score,rank}; chests remains
-- an object keyed by ISO week date. chest_policy.threshold is the live goal.
-- Alerts use payload {text,chat_id?,clan_id?,from_user_id?}; no private goal text.
-- clan_invite returns its raw capability ONCE at response.invitation.token,
-- outside receipts. No raw token enters event.result/changes, clan JSON or alarms.
-- A retry returns the same redacted receipt WITHOUT invitation/token; a lost
-- response requires a new event ID. This is intentionally not a replayable secret.
-- Current Android has no invitation issuance/display UI: its ordinary outbox
-- consumer ignores response.invitation. A future explicit owner sharing flow must
-- consume this one-time field without persisting it in receipts/logs; no such
-- secure output or clipboard handling is claimed by this SQL migration.
-- Integration ready: social table/RPC contracts above are final. No SQL deployment
-- is performed by the social worker; main owns the migration hash and live apply.

create table public.questyourlife_alarm (
 id uuid primary key default gen_random_uuid(),
 user_id uuid not null references public.questyourlife_user(id),
 type text not null check(length(type) between 1 and 80),
 payload jsonb not null default '{}' check(jsonb_typeof(payload)='object' and octet_length(payload::text)<=8192),
 read smallint not null default 0 check(read in (0,1)),
 created_at timestamptz not null default now()
);
create index questyourlife_alarm_inbox on public.questyourlife_alarm(user_id,read,created_at desc,id desc);

create table public.questyourlife_clan (
 id uuid primary key default gen_random_uuid(), name text not null check(length(btrim(name)) between 1 and 80),
 owner_id uuid not null references public.questyourlife_user(id), icon text not null default 'stamp' check(length(icon)<=100),
 members jsonb not null default '{}' check(jsonb_typeof(members)='object'),
 questbook_ids jsonb not null default '[]' check(jsonb_typeof(questbook_ids)='array' and jsonb_array_length(questbook_ids)<=20),
 invites jsonb not null default '{}' check(jsonb_typeof(invites)='object'),
 chests jsonb not null default '{}' check(jsonb_typeof(chests)='object'),
 settings jsonb not null default '{"discoverable":true}' check(jsonb_typeof(settings)='object'
  and (not(settings ? 'discoverable') or jsonb_typeof(settings->'discoverable')='boolean')),
 version bigint not null default 1, created_at timestamptz not null default now(),
 updated_at timestamptz not null default now(), deleted_at timestamptz
);
create index questyourlife_clan_members on public.questyourlife_clan using gin(members jsonb_ops) where deleted_at is null;

create table public.questyourlife_chat (
 id uuid primary key default gen_random_uuid(), type text not null check(type in ('ai','direct','group')),
 name text check(length(name) between 1 and 120),
 members jsonb not null default '{}' check(jsonb_typeof(members)='object'),
 channel_key text unique, metadata jsonb not null default '{}' check(jsonb_typeof(metadata)='object'),
 created_at timestamptz not null default now(), updated_at timestamptz not null default now(), deleted_at timestamptz
);
create index questyourlife_chat_members on public.questyourlife_chat using gin(members jsonb_ops) where deleted_at is null;

create table public.questyourlife_post (
 id uuid primary key default gen_random_uuid(), type text not null default 'chat' check(type='chat'),
 chat_id uuid not null references public.questyourlife_chat(id), author_id uuid references public.questyourlife_user(id),
 actor text not null check(actor in ('user','assistant','system')),
 content text not null check(length(content) between 1 and 16000),
 payload jsonb not null default '{}' check(jsonb_typeof(payload)='object' and octet_length(payload::text)<=65536),
 parent_id uuid references public.questyourlife_post(id),
 created_at timestamptz not null default clock_timestamp(), edited_at timestamptz, deleted_at timestamptz,
 check((actor='user' and author_id is not null) or (actor in ('assistant','system') and author_id is null)),
 check(parent_id is null)
);
create index questyourlife_post_cursor on public.questyourlife_post(chat_id,created_at desc,id desc);

-- Pure predicates are also used by the core completion RPC's clan lookup.
create function public.questyourlife_member_active(p_members jsonb,p_user uuid)
returns boolean language sql immutable set search_path='' as $$
 select coalesce(jsonb_typeof(p_members->p_user::text)='object'
  and (p_members->p_user::text) ? 'joined_at'
  and p_members->p_user::text->>'left_at' is null,false)
$$;

-- Enforce JSON identities, one owner and one active clan even for trusted writes.
create function public.questyourlife_social_validate() returns trigger
language plpgsql set search_path='' as $$
declare m record; segment jsonb; n int:=0; own int:=0; active int:=0; ids uuid[]:='{}'; k text;
begin
 for m in select key,value from jsonb_each(new.members) order by key loop
  if jsonb_typeof(m.value)<>'object' or m.value->>'role' is null
   or not(m.value ? 'joined_at') or (m.value->>'joined_at')::timestamptz is null
   or exists(select 1 from jsonb_object_keys(m.value) f where f not in ('role','joined_at','left_at','last_read_at','history'))
   or not exists(select 1 from public.questyourlife_user where id=m.key::uuid and deleted_at is null)
  then raise exception 'invalid_membership'; end if;
  if m.value->>'left_at' is not null and (m.value->>'left_at')::timestamptz<(m.value->>'joined_at')::timestamptz
  then raise exception 'invalid_membership_interval'; end if;
  if m.value ? 'history' then
   if jsonb_typeof(m.value->'history')<>'array' then raise exception 'invalid_membership_history'; end if;
   for segment in select value from jsonb_array_elements(m.value->'history') loop
    if jsonb_typeof(segment)<>'object' or segment->>'joined_at' is null or segment->>'left_at' is null
     or (segment->>'left_at')::timestamptz<(segment->>'joined_at')::timestamptz
     or segment->>'role' not in ('owner','member','admin')
    then raise exception 'invalid_membership_history'; end if;
   end loop;
  end if;
  n:=n+1; ids:=array_append(ids,m.key::uuid);
  if public.questyourlife_member_active(new.members,m.key::uuid) then active:=active+1; end if;
  if tg_table_name='questyourlife_clan' then
   if m.value->>'role' not in ('owner','member') then raise exception 'invalid_clan_role'; end if;
   if m.value->>'role'='owner' and public.questyourlife_member_active(new.members,m.key::uuid) then
    own:=own+1;
    if m.key::uuid<>new.owner_id then raise exception 'invalid_clan_owner'; end if;
   end if;
   if new.deleted_at is null and public.questyourlife_member_active(new.members,m.key::uuid)
    and (tg_op='INSERT' or not public.questyourlife_member_active(old.members,m.key::uuid)) then
    perform pg_advisory_xact_lock(hashtextextended('questyourlife:clan:'||m.key,0));
    perform 1 from public.questyourlife_user where id=m.key::uuid for update;
    if exists(select 1 from public.questyourlife_clan c where c.id<>new.id and c.deleted_at is null
      and c.members ? m.key and public.questyourlife_member_active(c.members,m.key::uuid))
    then raise exception 'already_in_clan'; end if;
   end if;
  else
   if m.value->>'role' not in ('member','admin') then raise exception 'invalid_chat_role'; end if;
   if m.value->>'last_read_at' is not null then perform (m.value->>'last_read_at')::timestamptz; end if;
  end if;
 end loop;
 if tg_table_name='questyourlife_clan' then
  if (new.deleted_at is null and own<>1) or active>50 or n>1000 then raise exception 'invalid_clan_members'; end if;
  if (select count(*)<>count(distinct value) from jsonb_array_elements(new.questbook_ids)) then raise exception 'duplicate_clan_book'; end if;
  for k in select jsonb_array_elements_text(new.questbook_ids) loop
   if not exists(select 1 from public.questyourlife_questbook where id=k::uuid and type='book'
    and is_public and publication_status='published' and deleted_at is null) then raise exception 'clan_book_must_be_public'; end if;
  end loop;
 else
  if n>100 or n<1 then raise exception 'invalid_chat_members'; end if;
  if new.type='ai' and (n<>1 or active<>1 or new.channel_key is distinct from 'ai:'||ids[1]::text)
  then raise exception 'invalid_ai_channel'; end if;
  if new.type='direct' and (n<>2 or active<>2 or new.channel_key is distinct from 'direct:'||ids[1]::text||':'||ids[2]::text)
  then raise exception 'invalid_direct_channel'; end if;
  if new.type='group' and (new.channel_key is not null or (new.deleted_at is null and active<1)
   or (new.deleted_at is null and not exists(select 1 from jsonb_each(new.members) x
     where x.value->>'role'='admin' and x.value->>'left_at' is null)))
  then raise exception 'invalid_group_channel'; end if;
 end if;
 return new;
end $$;
create trigger questyourlife_clan_validate before insert or update on public.questyourlife_clan
 for each row execute function public.questyourlife_social_validate();
create trigger questyourlife_chat_validate before insert or update on public.questyourlife_chat
 for each row execute function public.questyourlife_social_validate();

-- Exposes only a boolean for the current account; no supplied user identity.
create function public.questyourlife_chat_access(p_chat_id uuid) returns boolean
language sql stable security definer set search_path='' as $$
 select auth.uid() is not null and exists(select 1 from public.questyourlife_chat c
  where c.id=p_chat_id and c.deleted_at is null and c.members ? auth.uid()::text
   and public.questyourlife_member_active(c.members,auth.uid()))
$$;
alter table public.questyourlife_alarm enable row level security;
alter table public.questyourlife_clan enable row level security;
alter table public.questyourlife_chat enable row level security;
alter table public.questyourlife_post enable row level security;
create policy questyourlife_alarm_owner on public.questyourlife_alarm for select to authenticated using(user_id=(select auth.uid()));
create policy questyourlife_clan_member on public.questyourlife_clan for select to authenticated
 using(public.questyourlife_member_active(members,(select auth.uid())));
create policy questyourlife_chat_member on public.questyourlife_chat for select to authenticated
 using(public.questyourlife_member_active(members,(select auth.uid())));
create policy questyourlife_post_member on public.questyourlife_post for select to authenticated
 using(public.questyourlife_chat_access(chat_id));
revoke all on public.questyourlife_alarm,public.questyourlife_clan,public.questyourlife_chat,public.questyourlife_post from public,anon,authenticated;
grant select on public.questyourlife_alarm,public.questyourlife_post to authenticated;

-- Projection helpers are NOT client-callable; all identities come from auth.uid().
create function public.questyourlife_clan_projection(p_id uuid,p_viewer uuid) returns jsonb
language plpgsql stable set search_path='' as $$
declare c public.questyourlife_clan; member boolean; eligible boolean;
begin
 select * into c from public.questyourlife_clan where id=p_id;
 if not found then return null; end if;
 member:=public.questyourlife_member_active(c.members,p_viewer);
 eligible:=exists(select 1 from jsonb_each(c.chests) x where x.value->'eligible' ? p_viewer::text);
 if not member and not eligible and (c.deleted_at is not null or c.settings->'discoverable' is distinct from 'true'::jsonb) then return null; end if;
 return jsonb_build_object('id',c.id,'name',c.name,'owner_id',c.owner_id,'icon',c.icon,'version',c.version,
  'created_at',c.created_at,'updated_at',c.updated_at,'deleted_at',c.deleted_at,
  'members',case when member then (select coalesce(jsonb_object_agg(key,value-'history'),'{}') from jsonb_each(c.members)
    where value->>'left_at' is null) else '{}'::jsonb end,
  'questbook_ids',case when member then c.questbook_ids else '[]'::jsonb end,
  'chest_policy',case when member then jsonb_build_object('policy_version',1,'threshold',100,'reward',50) else '{}'::jsonb end,
  'chests',case when member then c.chests else (select coalesce(jsonb_object_agg(key,value),'{}') from jsonb_each(c.chests)
    where value->'eligible' ? p_viewer::text) end,
  'settings',jsonb_build_object('discoverable',c.settings->'discoverable'));
end $$;

create function public.questyourlife_chat_projection(p_id uuid,p_viewer uuid) returns jsonb
language plpgsql stable set search_path='' as $$
declare c public.questyourlife_chat;
begin
 select * into c from public.questyourlife_chat where id=p_id;
 if not found or not public.questyourlife_member_active(c.members,p_viewer) then return null; end if;
 return jsonb_build_object('id',c.id,'type',c.type,'name',c.name,'members',
  (select jsonb_object_agg(key,case when key=p_viewer::text then value-'history'
    else jsonb_build_object('role',value->'role','joined_at',value->'joined_at','left_at',value->'left_at') end)
   from jsonb_each(c.members)),
  'channel_key',c.channel_key,'metadata',case when c.type='direct' then jsonb_build_object(
    'friend_status',c.metadata->'friend_status','requested_by',c.metadata->'requested_by',
    'blocked_by_me',coalesce(c.metadata->'blocked' ? p_viewer::text,false),
    'messaging_blocked',coalesce(c.metadata->'blocked','{}')<>'{}'::jsonb)
    else '{}'::jsonb end,
  'created_at',c.created_at,'updated_at',c.updated_at,'deleted_at',c.deleted_at,
  'latest_post',(select to_jsonb(p) from public.questyourlife_post p where p.chat_id=c.id and p.deleted_at is null
    order by p.created_at desc,p.id desc limit 1),
  'unread_count',(select count(*) from public.questyourlife_post p where p.chat_id=c.id and p.deleted_at is null
    and p.author_id is distinct from p_viewer
    and p.created_at>coalesce((c.members->p_viewer::text->>'last_read_at')::timestamptz,'-infinity'::timestamptz)));
end $$;

create function public.questyourlife_public_profiles(p_ids uuid[]) returns jsonb
language plpgsql stable security definer set search_path='' as $$
declare u uuid:=auth.uid(); profiles jsonb;
begin
 if u is null then raise exception 'authentication_required' using errcode='42501'; end if;
 if p_ids is null or cardinality(p_ids)>100 then raise exception 'invalid_profile_ids'; end if;
 select coalesce(jsonb_agg(jsonb_build_object('id',p.id,'name',p.name,
   'avatar',jsonb_build_object('mascot',p.avatar->'mascot','variant',p.avatar->'variant'),
   'equipped',jsonb_build_object('item_id',p.equipped->'item_id')) order by p.id),'[]') into profiles
 from public.questyourlife_user p where p.id=any(p_ids) and p.deleted_at is null
  and (p.id=u or p.privacy->'profile_public'='true'::jsonb);
 return jsonb_build_object('profiles',profiles);
end $$;

create function public.questyourlife_posts(p_chat_id uuid,p_before_created_at timestamptz default null,
 p_before_id uuid default null,p_limit integer default 50) returns jsonb
language plpgsql stable security definer set search_path='' as $$
declare rows jsonb; more boolean; cursor jsonb;
begin
 if auth.uid() is null or not public.questyourlife_chat_access(p_chat_id) then raise exception 'not_chat_member' using errcode='42501'; end if;
 if p_limit is null or p_limit not between 1 and 100 or (p_before_created_at is null)<>(p_before_id is null)
 then raise exception 'invalid_post_cursor'; end if;
 select coalesce(jsonb_agg(to_jsonb(p) order by p.created_at desc,p.id desc),'[]') into rows
 from (select * from public.questyourlife_post where chat_id=p_chat_id
  and (p_before_created_at is null or (created_at,id)<(p_before_created_at,p_before_id))
  order by created_at desc,id desc limit p_limit+1) p;
 more:=jsonb_array_length(rows)>p_limit;
 if more then rows:=rows-p_limit; end if;
 cursor:=case when more then jsonb_build_object('created_at',rows->(p_limit-1)->'created_at','id',rows->(p_limit-1)->'id') else null end;
 return jsonb_build_object('posts',rows,'next_cursor',cursor,'has_more',more);
end $$;

create function public.questyourlife_social_snapshot() returns jsonb
language plpgsql stable security definer set search_path='' as $$
declare u uuid:=auth.uid(); week date:=(date_trunc('week',now() at time zone 'Asia/Seoul'))::date;
 alarms jsonb; clans jsonb; chats jsonb; posts jsonb; books jsonb; popular jsonb; daily jsonb; ranks jsonb; profiles jsonb; profile_ids uuid[];
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
 select array_agg(distinct id) into profile_ids from (
  select u id union select (x->>'owner_id')::uuid from jsonb_array_elements(clans) x
  union select key::uuid from jsonb_array_elements(clans) x cross join lateral jsonb_each(x->'members')
  union select key::uuid from jsonb_array_elements(chats) x cross join lateral jsonb_each(x->'members')
 ) p;
 -- Profile lookup is bounded independently even when many group members exist.
 profiles:=public.questyourlife_public_profiles((coalesce(profile_ids,'{}'::uuid[]))[1:100])->'profiles';
 return jsonb_build_object('alarms',alarms,'unread_alarms',(select count(*) from public.questyourlife_alarm where user_id=u and read=0),
  'clans',clans,'chats',chats,'posts',posts,'public_profiles',profiles,'my_books',books,'popular_books',popular,
  'daily_inbox',daily,'clan_ranking',ranks,'week_start',week);
end $$;

-- Deterministic recipient-specific IDs deduplicate delivery. Recipients query
-- their own inbox; compact payloads never include private goals or AI text.
create function public.questyourlife_social_notify(p_user uuid,p_event uuid,p_type text,p_payload jsonb)
returns void language sql set search_path='' as $$
 insert into public.questyourlife_alarm(id,user_id,type,payload)
 values(md5('questyourlife:alarm:'||p_event::text||':'||p_user::text||':'||p_type)::uuid,p_user,p_type,p_payload)
 on conflict(id) do nothing
$$;

-- Only call under the owner's user row lock. Projection events have zero economy.
create function public.questyourlife_social_receipt(p_user uuid,p_event uuid,p_operation text,p_hash text,
 p_status text,p_result jsonb,p_changes jsonb,p_cash bigint default 0,p_award text default null)
returns jsonb language plpgsql set search_path='' as $$
declare usr public.questyourlife_user; receipt public.questyourlife_event;
begin
 if p_cash<0 then raise exception 'invalid_social_award'; end if;
 if p_cash<>0 then perform public.questyourlife_project_economy(p_user,p_cash,0); end if;
 update public.questyourlife_user set sync_seq=sync_seq+1 where id=p_user returning * into usr;
 if not found then raise exception 'unknown_user'; end if;
 insert into public.questyourlife_event(user_id,event_id,actor_id,operation,request_hash,result_status,
  award_key,cash_delta,xp_delta,score_delta,result,changes,sync_seq)
 values(p_user,p_event,p_user,p_operation,p_hash,p_status,p_award,p_cash,0,0,p_result,
  coalesce(p_changes,'[]')||jsonb_build_array(public.questyourlife_change('user',to_jsonb(usr))),usr.sync_seq)
 returning * into receipt;
 return jsonb_build_object('receipts',jsonb_build_array(to_jsonb(receipt)));
end $$;

create function public.questyourlife_social_command(p_event_id uuid,p_operation text,p_data jsonb)
returns jsonb language plpgsql security definer set search_path='' as $$
#variable_conflict use_column
<<questyourlife_social_command>>
declare u uuid:=auth.uid(); usr public.questyourlife_user; prior public.questyourlife_event; hash text;
 id uuid; peer uuid; chatid uuid; c public.questyourlife_clan; ch public.questyourlife_chat; p public.questyourlife_post;
 oldmember jsonb; members jsonb; metadata jsonb; changes jsonb:='[]'; result jsonb:='{}'; status text:='applied';
 token text; tokenhash text; invitation jsonb; stamp timestamptz:=clock_timestamp(); award text; delta bigint:=0;
 week date; weekend timestamptz; chest jsonb; eligible jsonb; total bigint; k text; other uuid;
 nested jsonb; goal jsonb; command_id uuid; userbase bigint; delivery jsonb; response jsonb;
begin
 if u is null then raise exception 'authentication_required' using errcode='42501'; end if;
 if p_event_id is null or p_operation is null or jsonb_typeof(p_data) is distinct from 'object'
  or octet_length(p_data::text)>131072 then raise exception 'invalid_social_command'; end if;
 -- Use the same lock order as core: account row before any social row. Each
 -- command only appends the requester's after-images; peers poll the snapshot.
 perform public.questyourlife_bootstrap();
 select * into usr from public.questyourlife_user where id=u and deleted_at is null for update;
 if not found then raise exception 'inactive_user' using errcode='42501'; end if;
 userbase:=usr.version;
 hash:=encode(sha256(convert_to(jsonb_build_object('operation',p_operation,'data',p_data)::text,'UTF8')),'hex');
 select * into prior from public.questyourlife_event where user_id=u and event_id=p_event_id;
 if found then
  if prior.request_hash is distinct from hash then raise exception 'event_payload_mismatch' using errcode='22023'; end if;
  return jsonb_build_object('receipts',jsonb_build_array(to_jsonb(prior)));
 end if;
 if (select count(*) from public.questyourlife_event where user_id=u and server_created_at>now()-interval '1 minute')>=200
 then raise sqlstate 'PT429' using message='social_rate_limited'; end if;
 begin
  id:=nullif(p_data->>'id','')::uuid; peer:=nullif(p_data->>'user_id','')::uuid;
  chatid:=nullif(p_data->>'chat_id','')::uuid;
  if p_operation='clan_create' then
   if exists(select 1 from public.questyourlife_clan where deleted_at is null and members ? u::text
    and public.questyourlife_member_active(members,u)) then raise exception 'already_in_clan'; end if;
   id:=coalesce(id,gen_random_uuid());
   members:=jsonb_build_object(u::text,jsonb_build_object('role','owner','joined_at',stamp,'left_at',null));
   insert into public.questyourlife_clan(id,name,owner_id,icon,members,settings)
    values(id,p_data->>'name',u,coalesce(p_data->>'icon','stamp'),members,
     jsonb_build_object('discoverable',coalesce(p_data->'discoverable','true'::jsonb))) returning * into c;
  elsif p_operation in ('clan_invite','clan_join','clan_leave','clan_transfer','clan_books','clan_chest_unlock','clan_chest_claim') then
   select * into c from public.questyourlife_clan where questyourlife_clan.id=questyourlife_social_command.id for update;
   stamp:=clock_timestamp();
   if not found or (c.deleted_at is not null and p_operation<>'clan_chest_claim') then raise exception 'clan_not_found'; end if;
   if p_operation='clan_join' then
    if public.questyourlife_member_active(c.members,u) or exists(select 1 from public.questyourlife_clan q
     where q.deleted_at is null and q.members ? u::text and public.questyourlife_member_active(q.members,u))
    then raise exception 'already_in_clan'; end if;
    token:=p_data->>'token';
    if token is null or length(token) not between 32 and 128 then raise exception 'invalid_invitation'; end if;
    tokenhash:=encode(sha256(convert_to(token,'UTF8')),'hex'); invitation:=c.invites->tokenhash;
    if invitation is null or (invitation->>'expires_at')::timestamptz<=stamp
     or (invitation->>'uses')::int>=(invitation->>'max_uses')::int
     or (invitation->>'user_id' is not null and invitation->>'user_id'<>u::text)
    then raise exception 'invalid_invitation'; end if;
    if (select count(*) from jsonb_each(c.members) x where x.value->>'left_at' is null)>=50 then raise exception 'clan_full'; end if;
    oldmember:=c.members->u::text;
    members:=jsonb_set(c.members,array[u::text],jsonb_build_object('role','member','joined_at',stamp,'left_at',null,
     'history',coalesce(oldmember->'history','[]')||case when oldmember is not null then jsonb_build_array(oldmember-'history') else '[]'::jsonb end));
    update public.questyourlife_clan q set members=questyourlife_social_command.members,
     invites=jsonb_set(q.invites,array[tokenhash,'uses'],to_jsonb((invitation->>'uses')::int+1)),
     version=q.version+1,updated_at=stamp where q.id=questyourlife_social_command.id returning * into c;
   else
    if p_operation<>'clan_chest_claim' and not public.questyourlife_member_active(c.members,u)
    then raise exception 'not_clan_member' using errcode='42501'; end if;
    if p_operation='clan_invite' then
     if c.owner_id<>u then raise exception 'clan_owner_required' using errcode='42501'; end if;
     if peer is not null and not exists(select 1 from public.questyourlife_user where questyourlife_user.id=peer and deleted_at is null)
     then raise exception 'unknown_invitee'; end if;
     if (select count(*) from jsonb_each(c.invites) x where (x.value->>'expires_at')::timestamptz>stamp)>=100
     then raise exception 'invitation_limit'; end if;
     token:=replace(gen_random_uuid()::text,'-','')||replace(gen_random_uuid()::text,'-','');
     tokenhash:=encode(sha256(convert_to(token,'UTF8')),'hex');
     invitation:=jsonb_build_object('created_by',u,'user_id',peer,'created_at',stamp,'expires_at',stamp+interval '7 days','uses',0,'max_uses',1);
     update public.questyourlife_clan q set invites=jsonb_set(q.invites,array[tokenhash],invitation),version=q.version+1,updated_at=stamp
      where q.id=questyourlife_social_command.id returning * into c;
     result:=jsonb_build_object('clan_id',id,'expires_at',invitation->'expires_at','token_delivery','response_once');
     delivery:=jsonb_build_object('token',token,'clan_id',id,'expires_at',invitation->'expires_at');
     if peer is not null then perform public.questyourlife_social_notify(peer,p_event_id,'clan_invite',
      jsonb_build_object('clan_id',id,'from_user_id',u,'text','클랜 초대가 도착했어요')); end if;
    elsif p_operation='clan_leave' then
     if c.owner_id=u and exists(select 1 from jsonb_each(c.members) x where x.key<>u::text and x.value->>'left_at' is null)
     then raise exception 'transfer_owner_before_leaving'; end if;
     update public.questyourlife_clan q set members=jsonb_set(q.members,array[u::text,'left_at'],to_jsonb(stamp)),
      deleted_at=case when c.owner_id=u then stamp else q.deleted_at end,version=q.version+1,updated_at=stamp
      where q.id=questyourlife_social_command.id returning * into c;
    elsif p_operation='clan_transfer' then
     if c.owner_id<>u or peer is null or peer=u or not public.questyourlife_member_active(c.members,peer)
     then raise exception 'invalid_owner_transfer' using errcode='42501'; end if;
     update public.questyourlife_clan q set owner_id=peer,members=jsonb_set(jsonb_set(q.members,
      array[u::text,'role'],'"member"'),array[peer::text,'role'],'"owner"'),version=q.version+1,updated_at=stamp
      where q.id=questyourlife_social_command.id returning * into c;
    elsif p_operation='clan_books' then
     if c.owner_id<>u or jsonb_typeof(p_data->'questbook_ids') is distinct from 'array' then raise exception 'invalid_clan_books'; end if;
     update public.questyourlife_clan q set questbook_ids=p_data->'questbook_ids',version=q.version+1,updated_at=stamp
      where q.id=questyourlife_social_command.id returning * into c;
    else
     week:=nullif(p_data->>'week_start','')::date;
     if week is null or extract(isodow from week)<>1 or week>=(date_trunc('week',stamp at time zone 'Asia/Seoul'))::date
      or week<(c.created_at at time zone 'Asia/Seoul')::date-7 then raise exception 'invalid_settled_week'; end if;
     k:=week::text; chest:=c.chests->k;
     if p_operation='clan_chest_unlock' then
      if chest is null then
       select coalesce(sum(e.score_delta),0) into total from public.questyourlife_event e
        where e.clan_id_at_event=questyourlife_social_command.id and e.week_start=week and e.result_status='applied' and e.kind='command';
       -- Fixed server policy v1. Neither request data nor owner settings set economics.
       if total<100 then raise exception 'chest_threshold_not_met'; end if;
       weekend:=(week+7)::timestamp at time zone 'Asia/Seoul';
       select coalesce(jsonb_object_agg(x.key,jsonb_build_object('eligible_at',weekend)),'{}') into eligible
        from jsonb_each(c.members) x where exists(
         select 1 from jsonb_array_elements(coalesce(x.value->'history','[]')||jsonb_build_array(x.value-'history')) m
         where (m->>'joined_at')::timestamptz<weekend and (m->>'left_at' is null or (m->>'left_at')::timestamptz>=weekend))
         and (select coalesce(sum(e.score_delta),0) from public.questyourlife_event e where e.user_id=x.key::uuid
          and e.clan_id_at_event=questyourlife_social_command.id and e.week_start=week and e.result_status='applied' and e.kind='command')>0;
       chest:=jsonb_build_object('week_start',week,'policy_version',1,'threshold',100,'reward',50,
        'unlocked_at',stamp,'eligible',eligible);
       update public.questyourlife_clan q set chests=jsonb_set(q.chests,array[k],chest),version=q.version+1,updated_at=stamp
        where q.id=questyourlife_social_command.id returning * into c;
      end if;
     else
      if chest is null or not(chest->'eligible' ? u::text) then raise exception 'not_chest_eligible' using errcode='42501'; end if;
      -- Account/week key deliberately excludes clan ID: switching groups cannot pay twice.
      award:='clan_chest:'||week::text;
      if exists(select 1 from public.questyourlife_event where user_id=u and award_key=award and result_status='applied')
      then award:=null; result:=jsonb_build_object('already_claimed',true);
      else delta:=(chest->>'reward')::bigint; result:=jsonb_build_object('reward',delta,'week_start',week); end if;
     end if;
    end if;
   end if;
  elsif p_operation in ('chat_open_ai','chat_open_direct','chat_create_group') then
   if p_operation='chat_open_ai' then
    k:='ai:'||u::text; members:=jsonb_build_object(u::text,jsonb_build_object('role','member','joined_at',stamp,'last_read_at',stamp));
   elsif p_operation='chat_open_direct' then
    if peer is null or peer=u or not exists(select 1 from public.questyourlife_user where questyourlife_user.id=peer and deleted_at is null)
    then raise exception 'invalid_direct_peer'; end if;
    k:='direct:'||least(u::text,peer::text)||':'||greatest(u::text,peer::text);
    members:=jsonb_build_object(u::text,jsonb_build_object('role','member','joined_at',stamp,'last_read_at',stamp),
     peer::text,jsonb_build_object('role','member','joined_at',stamp,'last_read_at',stamp));
   else
    if jsonb_typeof(p_data->'user_ids') is distinct from 'array' or jsonb_array_length(p_data->'user_ids') not between 1 and 49
    then raise exception 'invalid_group_members'; end if;
    k:=null; members:=jsonb_build_object(u::text,jsonb_build_object('role','admin','joined_at',stamp,'last_read_at',stamp));
    for other in select value::uuid from jsonb_array_elements_text(p_data->'user_ids') loop
     if members ? other::text then raise exception 'duplicate_group_member'; end if;
     members:=members||jsonb_build_object(other::text,jsonb_build_object('role','member','joined_at',stamp,'last_read_at',stamp));
    end loop;
   end if;
   if k is not null then
    perform pg_advisory_xact_lock(hashtextextended('questyourlife:chat:'||k,0));
    select * into ch from public.questyourlife_chat where channel_key=k and deleted_at is null;
   end if;
   if ch.id is null then
    insert into public.questyourlife_chat(id,type,name,members,channel_key,metadata)
    values(coalesce(id,gen_random_uuid()),case p_operation when 'chat_open_ai' then 'ai' when 'chat_open_direct' then 'direct' else 'group' end,
     case when p_operation='chat_create_group' then p_data->>'name' else null end,members,k,
     case when p_operation='chat_open_direct' then '{"friend_status":"none","blocked":{}}'::jsonb else '{}'::jsonb end)
    returning * into ch;
   end if;
   id:=ch.id; result:=jsonb_build_object('chat_id',ch.id);
  elsif p_operation in ('chat_add_member','chat_leave','chat_read','friend_request','friend_accept','friend_block','friend_unblock','post_send','post_edit','post_delete','plan_confirm') then
   if p_operation in ('post_edit','post_delete','plan_confirm') then
    select * into p from public.questyourlife_post where questyourlife_post.id=questyourlife_social_command.id;
    if not found then raise exception 'post_not_found'; end if;
    chatid:=p.chat_id;
   end if;
   chatid:=coalesce(chatid,id);
   select * into ch from public.questyourlife_chat where questyourlife_chat.id=chatid and deleted_at is null for update;
   stamp:=clock_timestamp();
   if not found or not public.questyourlife_member_active(ch.members,u) then raise exception 'not_chat_member' using errcode='42501'; end if;
   if p_operation like 'friend_%' then
    if ch.type<>'direct' then raise exception 'direct_channel_required'; end if;
    metadata:=ch.metadata;
    if p_operation='friend_block' then metadata:=jsonb_set(metadata,array['blocked',u::text],to_jsonb(stamp));
    elsif p_operation='friend_unblock' then metadata:=metadata#-array['blocked',u::text];
    else
     if coalesce(metadata->'blocked','{}')<>'{}'::jsonb then raise exception 'direct_blocked'; end if;
     if p_operation='friend_request' then
      if metadata->>'friend_status'='accepted' then raise exception 'already_friends'; end if;
      if metadata->>'friend_status'='pending' and metadata->>'requested_by'<>u::text then raise exception 'accept_pending_request'; end if;
      metadata:=metadata||jsonb_build_object('friend_status','pending','requested_by',u,'requested_at',stamp);
      select key::uuid into other from jsonb_each(ch.members) x where x.key<>u::text;
      perform public.questyourlife_social_notify(other,p_event_id,'friend_request',
       jsonb_build_object('chat_id',chatid,'from_user_id',u,'text','친구 요청이 도착했어요'));
     else
      if metadata->>'friend_status' is distinct from 'pending' or metadata->>'requested_by'=u::text
       or metadata->>'requested_by' is null then raise exception 'no_incoming_request'; end if;
      metadata:=metadata||jsonb_build_object('friend_status','accepted','accepted_at',stamp);
     end if;
    end if;
    update public.questyourlife_chat q set metadata=questyourlife_social_command.metadata,updated_at=stamp
     where q.id=chatid returning * into ch;
   elsif p_operation='chat_add_member' then
    if ch.type<>'group' or ch.members->u::text->>'role'<>'admin' then raise exception 'group_admin_required' using errcode='42501'; end if;
    if peer is null or public.questyourlife_member_active(ch.members,peer) then raise exception 'invalid_new_member'; end if;
    update public.questyourlife_chat q set members=jsonb_set(q.members,array[peer::text],
     jsonb_build_object('role','member','joined_at',stamp,'last_read_at',stamp)),updated_at=stamp where q.id=chatid returning * into ch;
   elsif p_operation='chat_leave' then
    if ch.type<>'group' then raise exception 'group_channel_required'; end if;
    members:=jsonb_set(ch.members,array[u::text,'left_at'],to_jsonb(stamp));
    select key::uuid into other from jsonb_each(members) x where x.key<>u::text and x.value->>'left_at' is null order by x.key limit 1;
    if other is not null and ch.members->u::text->>'role'='admin' then members:=jsonb_set(members,array[other::text,'role'],'"admin"'); end if;
    update public.questyourlife_chat q set members=questyourlife_social_command.members,deleted_at=case when other is null then stamp else null end,
     updated_at=stamp where q.id=chatid returning * into ch;
    changes:=changes||jsonb_build_array(jsonb_build_object('entity','chat','id',chatid,
     'version',(extract(epoch from stamp)*1000000)::bigint,'deleted',true,'data',jsonb_build_object('id',chatid,'deleted_at',stamp)));
   elsif p_operation='chat_read' then
    update public.questyourlife_chat q set members=jsonb_set(q.members,array[u::text,'last_read_at'],to_jsonb(stamp)),updated_at=stamp
     where q.id=chatid returning * into ch;
   elsif p_operation='post_send' then
    if (p_data ? 'actor' and p_data->>'actor' is distinct from 'user')
     or (p_data ? 'author_id' and p_data->>'author_id' is distinct from u::text) or p_data->>'parent_id' is not null
    then raise exception 'invalid_message_author' using errcode='42501'; end if;
    if ch.type='direct' and coalesce(ch.metadata->'blocked','{}')<>'{}'::jsonb then raise exception 'direct_blocked'; end if;
    -- User payload is intentionally restricted. AI drafts are accepted only by the trusted RPC.
    metadata:=coalesce(p_data->'payload','{}');
    if jsonb_typeof(metadata)<>'object' or metadata<>'{}'::jsonb then raise exception 'unsupported_user_payload'; end if;
    insert into public.questyourlife_post(id,chat_id,author_id,actor,content,payload)
     values(coalesce(id,gen_random_uuid()),chatid,u,'user',p_data->>'content',metadata) returning * into p;
    if ch.type<>'ai' then
     for other in select key::uuid from jsonb_each(ch.members) x where x.key<>u::text and x.value->>'left_at' is null loop
      perform public.questyourlife_social_notify(other,p_event_id,'chat_message',
       jsonb_build_object('chat_id',chatid,'post_id',p.id,'from_user_id',u,'text','새 메시지가 도착했어요'));
     end loop;
    end if;
    update public.questyourlife_chat q set updated_at=stamp where q.id=chatid returning * into ch;
   elsif p_operation in ('post_edit','post_delete') then
    if p.actor<>'user' or p.author_id<>u or p.deleted_at is not null then raise exception 'not_post_author' using errcode='42501'; end if;
    if p_operation='post_edit' then
     update public.questyourlife_post q set content=p_data->>'content',edited_at=stamp where q.id=questyourlife_social_command.id returning * into p;
    else
     update public.questyourlife_post q set content='[deleted]',payload='{}',deleted_at=stamp where q.id=questyourlife_social_command.id returning * into p;
    end if;
    update public.questyourlife_chat q set updated_at=stamp where q.id=chatid returning * into ch;
   else
    select * into p from public.questyourlife_post q where q.id=questyourlife_social_command.id for update;
    if ch.type<>'ai' or p.actor<>'assistant' or p.deleted_at is not null
     or jsonb_typeof(p.payload->'plan_draft') is distinct from 'object' or p_data->'confirmed' is distinct from 'true'::jsonb
    then raise exception 'explicit_plan_confirmation_required'; end if;
    if p.payload ? 'applied_goal_id' then result:=jsonb_build_object('goal_id',p.payload->'applied_goal_id','already_applied',true);
    else
     goal:=p_data->'goal';
     if jsonb_typeof(goal) is distinct from 'object' or goal->>'id' is null
      or exists(select 1 from public.questyourlife_questbook where questyourlife_questbook.id=(goal->>'id')::uuid)
     then raise exception 'new_goal_required'; end if;
     command_id:=(md5('questyourlife:plan:'||u::text||':'||p.id::text))::uuid;
     nested:=public.questyourlife_apply_batch(jsonb_build_array(jsonb_build_object('event_id',command_id,
      'operation','goal_put','base_version',0,'data',goal)));
     if nested->'receipts'->0->>'result_status' is distinct from 'applied' then raise exception 'plan_validation_failed: %',nested->'receipts'->0->'result'; end if;
     changes:=changes||coalesce(nested->'receipts'->0->'changes','[]');
     update public.questyourlife_post q set payload=q.payload||jsonb_build_object('applied_goal_id',goal->'id','confirmed_by',u,'confirmed_at',stamp),edited_at=stamp
      where q.id=questyourlife_social_command.id returning * into p;
     result:=jsonb_build_object('goal_id',goal->'id');
    end if;
   end if;
   if p.id is not null then changes:=changes||jsonb_build_array(jsonb_build_object('entity','post','id',p.id,
    'version',(extract(epoch from coalesce(p.deleted_at,p.edited_at,p.created_at))*1000000)::bigint,
    'deleted',p.deleted_at is not null,'data',to_jsonb(p))); end if;
  elsif p_operation='alarm_read' then
   update public.questyourlife_alarm a set read=1 where a.id=questyourlife_social_command.id and a.user_id=u;
   if not found then raise exception 'alarm_not_found'; end if;
   select jsonb_build_array(jsonb_build_object('entity','alarm','id',a.id,'version',1,'deleted',false,'data',to_jsonb(a))) into changes
    from public.questyourlife_alarm a where a.id=questyourlife_social_command.id;
  else raise exception 'unknown_social_operation'; end if;
  if c.id is not null then
   changes:=changes||jsonb_build_array(jsonb_build_object('entity','clan','id',c.id,'version',c.version,
    'deleted',p_operation='clan_leave','data',case when p_operation='clan_leave' then
     jsonb_build_object('id',c.id,'deleted_at',stamp) else public.questyourlife_clan_projection(c.id,u) end));
   result:=result||jsonb_build_object('clan_id',c.id);
  end if;
  if ch.id is not null and p_operation<>'chat_leave' then
   changes:=changes||jsonb_build_array(jsonb_build_object('entity','chat','id',ch.id,
    'version',(extract(epoch from ch.updated_at)*1000000)::bigint,
    'deleted',ch.deleted_at is not null,'data',public.questyourlife_chat_projection(ch.id,u)));
   result:=result||jsonb_build_object('chat_id',ch.id);
  end if;
 exception when check_violation or foreign_key_violation or unique_violation or invalid_text_representation
  or invalid_parameter_value or not_null_violation or raise_exception or insufficient_privilege or datetime_field_overflow then
  status:='rejected'; delta:=0; award:=null; changes:='[]'; delivery:=null; result:=jsonb_build_object('error',SQLERRM);
 end;
 response:=public.questyourlife_social_receipt(u,p_event_id,p_operation,hash,status,
  result||jsonb_build_object('user_base_version',userbase),changes,delta,award);
 if delivery is not null then response:=response||jsonb_build_object('invitation',delivery); end if;
 return response;
end $$;

-- Trusted Edge Functions persist generated text/drafts, never executable actions.
create function public.questyourlife_assistant_post(p_event_id uuid,p_chat_id uuid,p_content text,p_payload jsonb default '{}')
returns jsonb language plpgsql security definer set search_path='' as $$
declare ch public.questyourlife_chat; owner uuid; hash text; prior public.questyourlife_event; p public.questyourlife_post; changes jsonb;
begin
 if p_event_id is null then raise exception 'event_id_required'; end if;
 select * into ch from public.questyourlife_chat where id=p_chat_id and type='ai' and deleted_at is null;
 if not found then raise exception 'private_ai_channel_required'; end if;
 select key::uuid into owner from jsonb_each(ch.members);
 perform 1 from public.questyourlife_user where id=owner and deleted_at is null for update;
 if not found then raise exception 'inactive_ai_owner'; end if;
 select * into ch from public.questyourlife_chat where id=p_chat_id for update;
 hash:=encode(sha256(convert_to(jsonb_build_object('chat_id',p_chat_id,'content',p_content,'payload',p_payload)::text,'UTF8')),'hex');
 select * into prior from public.questyourlife_event where user_id=owner and event_id=p_event_id;
 if found then
  if prior.request_hash is distinct from hash then raise exception 'event_payload_mismatch' using errcode='22023'; end if;
  return jsonb_build_object('receipts',jsonb_build_array(to_jsonb(prior)));
 end if;
 if jsonb_typeof(p_payload) is distinct from 'object' or p_payload ?|array['applied_goal_id','confirmed_by','confirmed_at']
  or (p_payload ? 'plan_draft' and jsonb_typeof(p_payload->'plan_draft') is distinct from 'object')
 then raise exception 'invalid_assistant_payload'; end if;
 insert into public.questyourlife_post(id,chat_id,actor,content,payload) values(p_event_id,p_chat_id,'assistant',p_content,p_payload) returning * into p;
 update public.questyourlife_chat set updated_at=clock_timestamp() where id=p_chat_id;
 select * into ch from public.questyourlife_chat where id=p_chat_id;
 changes:=jsonb_build_array(jsonb_build_object('entity','post','id',p.id,
   'version',(extract(epoch from p.created_at)*1000000)::bigint,'deleted',false,'data',to_jsonb(p)),
  jsonb_build_object('entity','chat','id',p_chat_id,'version',(extract(epoch from ch.updated_at)*1000000)::bigint,
   'deleted',false,'data',public.questyourlife_chat_projection(p_chat_id,owner)));
 return public.questyourlife_social_receipt(owner,p_event_id,'assistant_post',hash,'applied',jsonb_build_object('post_id',p.id),changes);
end $$;

revoke all on function public.questyourlife_member_active(jsonb,uuid),public.questyourlife_social_validate(),
 public.questyourlife_clan_projection(uuid,uuid),public.questyourlife_chat_projection(uuid,uuid),
 public.questyourlife_social_notify(uuid,uuid,text,jsonb),
 public.questyourlife_social_receipt(uuid,uuid,text,text,text,jsonb,jsonb,bigint,text),
 public.questyourlife_chat_access(uuid),public.questyourlife_public_profiles(uuid[]),
 public.questyourlife_posts(uuid,timestamptz,uuid,integer),public.questyourlife_social_snapshot(),
 public.questyourlife_social_command(uuid,text,jsonb),public.questyourlife_assistant_post(uuid,uuid,text,jsonb)
 from public,anon,authenticated;
grant execute on function public.questyourlife_member_active(jsonb,uuid),public.questyourlife_chat_access(uuid),
 public.questyourlife_public_profiles(uuid[]),public.questyourlife_posts(uuid,timestamptz,uuid,integer),
 public.questyourlife_social_snapshot(),public.questyourlife_social_command(uuid,text,jsonb) to authenticated;
grant execute on function public.questyourlife_assistant_post(uuid,uuid,text,jsonb) to service_role;
