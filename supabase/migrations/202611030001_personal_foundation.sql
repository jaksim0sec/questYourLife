-- App-scoped only. Auth itself owns password hashes; no password columns are created.
create table public.questyourlife_user (
 id uuid primary key references auth.users(id), login_name text unique not null check(login_name ~ '^[a-z0-9_]{3,24}$'),
 name text not null, avatar jsonb not null default '{}', cash bigint not null default 0 check(cash>=0), xp bigint not null default 0 check(xp>=0),
 inventory jsonb not null default '{}', equipped jsonb not null default '{}', settings jsonb not null default '{}', privacy jsonb not null default '{}',
 version bigint not null default 1, sync_seq bigint not null default 0, sync_epoch uuid not null default gen_random_uuid(),
 created_at timestamptz not null default now(), updated_at timestamptz not null default now(), deleted_at timestamptz
);
create table public.questyourlife_questbook (
 id uuid primary key, logical_id uuid not null, book_id uuid not null, head uuid,
 type text not null check(type in ('book','chap','main','daily')), owner_id uuid not null references public.questyourlife_user(id),
 is_public boolean not null default false, name text not null check(length(name) between 1 and 300), tags text[], data jsonb not null default '{}', sort_order integer not null default 0,
 edition integer not null default 1, publication_status text not null default 'draft' check(publication_status in ('draft','published','retired')),
 version bigint not null default 1, created_at timestamptz not null default now(), updated_at timestamptz not null default now(), deleted_at timestamptz,
 unique(id,book_id), unique(book_id,logical_id),
 foreign key(book_id) references public.questyourlife_questbook(id) deferrable initially deferred,
 foreign key(head,book_id) references public.questyourlife_questbook(id,book_id) deferrable initially deferred,
 check((type='book' and id=book_id and head is null) or (type<>'book' and head is not null))
);
create index questyourlife_book_tree on public.questyourlife_questbook(book_id,head,sort_order,id);
create index questyourlife_book_owner on public.questyourlife_questbook(owner_id,id) where type='book' and deleted_at is null;
create table public.questyourlife_quest_join (
 id uuid primary key, user_id uuid not null references public.questyourlife_user(id), book_id uuid not null references public.questyourlife_questbook(id),
 status text not null default 'active' check(status in ('active','paused','completed','left')),
 overrides jsonb not null default '{"patch":{},"added":[],"hidden":[]}', node_state jsonb not null default '{}', edit_version bigint not null default 1, version bigint not null default 1,
 created_at timestamptz not null default now(), updated_at timestamptz not null default now(), deleted_at timestamptz
);
create unique index questyourlife_join_active on public.questyourlife_quest_join(user_id,book_id) where deleted_at is null and status<>'left';
create index questyourlife_join_owner on public.questyourlife_quest_join(user_id,status,id);
create table public.questyourlife_daily_quest (
 id uuid primary key, user_id uuid not null references public.questyourlife_user(id), join_id uuid references public.questyourlife_quest_join(id),
 source_id uuid references public.questyourlife_questbook(id), main_id uuid references public.questyourlife_questbook(id), scheduled_date date not null,
 occurrence_key text, content_snapshot jsonb not null, status text not null default 'proposed' check(status in ('proposed','accepted','completed','deferred','cancelled')),
 reward bigint not null default 0 check(reward>=0), reward_policy_version integer not null default 1, completed_at timestamptz, client_occurred_at timestamptz,
 version bigint not null default 1, created_at timestamptz not null default now(), updated_at timestamptz not null default now(), deleted_at timestamptz
);
create unique index questyourlife_execution_once on public.questyourlife_daily_quest(user_id,source_id,occurrence_key) where source_id is not null and occurrence_key is not null and status<>'cancelled';
create index questyourlife_daily_owner on public.questyourlife_daily_quest(user_id,scheduled_date,status,id);
-- This single append-only structure holds command receipts, immutable ledger deltas and delta-sync after-images.
-- Signup audit entries have no owner and are visible only to the service-role signup gate.
create table public.questyourlife_event (
 id uuid primary key default gen_random_uuid(), user_id uuid references public.questyourlife_user(id), event_id uuid not null, actor_id uuid,
 kind text not null default 'command' check(kind in ('command','projection','signup')),
 operation text not null, target_id uuid, request_hash text, result_status text not null check(result_status in ('applied','conflict','rejected')),
 award_key text, cash_delta bigint not null default 0, xp_delta bigint not null default 0, reverses_id uuid references public.questyourlife_event(id),
 result jsonb not null default '{}', changes jsonb not null default '[]', sync_seq bigint, server_created_at timestamptz not null default now(),
 unique(user_id,event_id), unique(user_id,sync_seq), check((kind='signup' and user_id is null and sync_seq is null) or (kind<>'signup' and user_id is not null and sync_seq is not null)),
 check(kind='command' or (cash_delta=0 and xp_delta=0))
);
create unique index questyourlife_award_once on public.questyourlife_event(user_id,award_key) where award_key is not null and result_status='applied';
create unique index questyourlife_reverse_once on public.questyourlife_event(reverses_id) where reverses_id is not null;
create index questyourlife_event_window on public.questyourlife_event(user_id,server_created_at);
create index questyourlife_signup_window on public.questyourlife_event(server_created_at,request_hash) where kind='signup';

alter table public.questyourlife_user enable row level security;
alter table public.questyourlife_questbook enable row level security;
alter table public.questyourlife_quest_join enable row level security;
alter table public.questyourlife_daily_quest enable row level security;
alter table public.questyourlife_event enable row level security;
create policy questyourlife_own_user on public.questyourlife_user for select to authenticated using(id=auth.uid());
create policy questyourlife_own_book on public.questyourlife_questbook for select to authenticated using(owner_id=auth.uid() or (is_public and publication_status='published' and deleted_at is null));
create policy questyourlife_own_join on public.questyourlife_quest_join for select to authenticated using(user_id=auth.uid());
create policy questyourlife_own_daily on public.questyourlife_daily_quest for select to authenticated using(user_id=auth.uid());
create policy questyourlife_own_event on public.questyourlife_event for select to authenticated using(user_id=auth.uid());
revoke all on public.questyourlife_user,public.questyourlife_questbook,public.questyourlife_quest_join,public.questyourlife_daily_quest,public.questyourlife_event from anon,authenticated;
grant select on public.questyourlife_user,public.questyourlife_questbook,public.questyourlife_quest_join,public.questyourlife_daily_quest,public.questyourlife_event to authenticated;

create function public.questyourlife_immutable_event() returns trigger language plpgsql set search_path='' as $$
begin raise exception 'immutable_event' using errcode='42501'; end $$;
create trigger questyourlife_no_event_changes before update or delete on public.questyourlife_event for each row execute function public.questyourlife_immutable_event();

create function public.questyourlife_change(p_type text,p_record jsonb) returns jsonb language sql immutable set search_path='' as $$
 select jsonb_build_object('entity',p_type,'id',p_record->>'id','version',(p_record->>'version')::bigint,'deleted',p_record->>'deleted_at' is not null,'data',p_record)
$$;

create function public.questyourlife_bootstrap() returns jsonb language plpgsql security definer set search_path='' as $$
declare u uuid:=auth.uid(); n text; p public.questyourlife_user; c jsonb; s bigint;
begin
 if u is null then raise exception 'authentication_required' using errcode='42501'; end if;
 n:=auth.jwt()->'app_metadata'->>'questyourlife_username';
 if n is null or n !~ '^[a-z0-9_]{3,24}$' then raise exception 'app_account_required' using errcode='42501'; end if;
 insert into public.questyourlife_user(id,login_name,name) values(u,n,n) on conflict(id) do nothing;
 select * into p from public.questyourlife_user where id=u for update;
 if p.sync_seq=0 then
  update public.questyourlife_user set sync_seq=1 where id=u returning * into p;
  c:=jsonb_build_array(public.questyourlife_change('user',to_jsonb(p)));
  insert into public.questyourlife_event(user_id,event_id,actor_id,operation,request_hash,result_status,result,changes,sync_seq)
  values(u,gen_random_uuid(),u,'bootstrap','server','applied','{}',c,1);
 end if;
 return jsonb_build_object('user',to_jsonb(p),'epoch',p.sync_epoch,'watermark',p.sync_seq);
end $$;

create function public.questyourlife_signup_gate(p_name text) returns void language plpgsql security definer set search_path='' as $$
declare h text:=encode(sha256(convert_to(p_name,'UTF8')),'hex');
begin
 if p_name !~ '^[a-z0-9_]{3,24}$' then raise exception 'invalid_username'; end if;
 perform pg_advisory_xact_lock(81399210001);
 if (select count(*) from public.questyourlife_event where kind='signup' and server_created_at>now()-interval '1 minute')>=20
 or (select count(*) from public.questyourlife_event where kind='signup' and request_hash=h and server_created_at>now()-interval '10 minutes')>=3 then
  raise exception 'signup_rate_limited' using errcode='P0001';
 end if;
 insert into public.questyourlife_event(event_id,kind,operation,request_hash,result_status) values(gen_random_uuid(),'signup','signup_attempt',h,'applied');
end $$;

create function public.questyourlife_apply_batch(p_events jsonb) returns jsonb language plpgsql security definer set search_path='' as $$
declare
 u uuid:=auth.uid(); e jsonb; b jsonb; node jsonb; eid uuid; target uuid; op text; h text; old public.questyourlife_event;
 usr public.questyourlife_user; book public.questyourlife_questbook; daily public.questyourlife_daily_quest; main public.questyourlife_questbook;
 userbase bigint; expected bigint; delta bigint; cost bigint; item integer; awarded bigint; seq bigint; award text; outcome text; result jsonb; changes jsonb; receipts jsonb:='[]';
 source uuid; joinid uuid; ids uuid[]; rootver bigint; snapshot jsonb; affected uuid;
begin
 if u is null then raise exception 'authentication_required' using errcode='42501'; end if;
 if jsonb_typeof(p_events)<>'array' or jsonb_array_length(p_events)>50 or octet_length(p_events::text)>262144 then raise exception 'invalid_batch'; end if;
 perform public.questyourlife_bootstrap();
 -- Per-user row lock is held until commit: sequence assignment cannot skip an uncommitted predecessor.
 select * into usr from public.questyourlife_user where id=u for update;
 if (select count(*) from public.questyourlife_event where user_id=u and server_created_at>now()-interval '1 minute')+jsonb_array_length(p_events)>200 then raise sqlstate 'PT429' using message='sync_rate_limited'; end if;
 for e in select value from jsonb_array_elements(p_events) loop
  eid:=(e->>'event_id')::uuid; op:=e->>'operation'; b:=e->'data'; target:=(b->>'id')::uuid;
  h:=encode(sha256(convert_to(e::text,'UTF8')),'hex');
  select * into old from public.questyourlife_event where user_id=u and event_id=eid;
  if found then
   if old.request_hash<>h then raise exception 'event_payload_mismatch' using errcode='22023'; end if;
   receipts:=receipts||jsonb_build_array(to_jsonb(old)); continue;
  end if;
  select version into userbase from public.questyourlife_user where id=u;
  expected:=coalesce((e->>'base_version')::bigint,0); delta:=0; award:=null; outcome:='applied'; result:='{}'; changes:='[]';
  begin
   if op='goal_put' then
    select * into book from public.questyourlife_questbook where id=target;
    if found and book.owner_id<>u then raise exception 'not_owner' using errcode='42501'; end if;
    if coalesce(book.version,0)<>expected then
     outcome:='conflict';
    elsif coalesce(book.publication_status,'draft')<>'draft' then raise exception 'published_immutable';
    else
     if jsonb_array_length(b->'nodes')>100 or (book.id is null and (select count(*) from public.questyourlife_questbook where owner_id=u and type='book' and deleted_at is null)>=100) then raise exception 'goal_limit'; end if;
     rootver:=coalesce(book.version,0)+1;
     insert into public.questyourlife_questbook(id,logical_id,book_id,type,owner_id,name,data,version)
     values(target,target,target,'book',u,b->>'title',jsonb_build_object('description',b->>'description'),rootver)
     on conflict(id) do update set name=excluded.name,data=excluded.data,version=rootver,updated_at=now()
      where public.questyourlife_questbook.owner_id=u and public.questyourlife_questbook.type='book' and public.questyourlife_questbook.version=expected
      returning id into affected;
     if not found then raise exception 'record_owner_or_version_changed' using errcode='42501'; end if;
     ids:=array[target];
     -- Input is limited to chap/main; parent must already exist in this same private book.
     for node in select value from jsonb_array_elements(b->'nodes') order by case value->>'type' when 'chap' then 0 else 1 end loop
      if node->>'type' not in ('chap','main') then raise exception 'invalid_node'; end if;
      if not exists(select 1 from public.questyourlife_questbook where id=(node->>'head')::uuid and book_id=target and owner_id=u and type=case node->>'type' when 'chap' then 'book' else 'chap' end) then raise exception 'invalid_parent'; end if;
      if exists(select 1 from public.questyourlife_questbook where id=(node->>'id')::uuid and (owner_id<>u or book_id<>target or type<>node->>'type')) then raise exception 'node_identity_collision'; end if;
      insert into public.questyourlife_questbook(id,logical_id,book_id,head,type,owner_id,name,sort_order,data)
      values((node->>'id')::uuid,(node->>'id')::uuid,target,(node->>'head')::uuid,node->>'type',u,node->>'name',coalesce((node->>'position')::int,0),'{}')
      on conflict(id) do update set name=excluded.name,head=excluded.head,sort_order=excluded.sort_order,version=public.questyourlife_questbook.version+1,deleted_at=null,updated_at=now()
       where public.questyourlife_questbook.owner_id=u and public.questyourlife_questbook.book_id=target and public.questyourlife_questbook.type=node->>'type'
       returning id into affected;
      if not found then raise exception 'node_identity_collision' using errcode='42501'; end if;
      ids:=array_append(ids,(node->>'id')::uuid);
     end loop;
     update public.questyourlife_questbook set deleted_at=now(),version=version+1 where book_id=target and not(id=any(ids)) and deleted_at is null;
     if (select count(*)<>count(distinct sort_order) from public.questyourlife_questbook where book_id=target and type='main' and deleted_at is null) then raise exception 'duplicate_step_position'; end if;
     insert into public.questyourlife_quest_join(id,user_id,book_id) values(target,u,target) on conflict(id) do nothing;
    end if;
    select coalesce(jsonb_agg(public.questyourlife_change('questbook',to_jsonb(q))),'[]') into changes from public.questyourlife_questbook q where book_id=target and owner_id=u;
    select changes||jsonb_build_array(public.questyourlife_change('quest_join',to_jsonb(q))) into changes from public.questyourlife_quest_join q where id=target and user_id=u;
   elsif op='quick_put' then
    select * into daily from public.questyourlife_daily_quest where id=target;
    if found and (daily.user_id<>u or coalesce(daily.content_snapshot->>'kind','')<>'quick') then raise exception 'not_quick_owner' using errcode='42501'; end if;
    if coalesce(daily.version,0)<>expected then outcome:='conflict';
    else
     if length(b->>'title') not between 1 and 120 then raise exception 'invalid_title'; end if;
     insert into public.questyourlife_daily_quest(id,user_id,scheduled_date,content_snapshot,status)
     values(target,u,current_date,jsonb_build_object('kind','quick','title',b->>'title','created_at',b->'created_at'),case when (b->>'completed')::boolean then 'completed' else 'accepted' end)
     on conflict(id) do update set content_snapshot=excluded.content_snapshot,status=excluded.status,version=public.questyourlife_daily_quest.version+1,updated_at=now()
      where public.questyourlife_daily_quest.user_id=u and public.questyourlife_daily_quest.version=expected and public.questyourlife_daily_quest.content_snapshot->>'kind'='quick'
      returning id into affected;
     if not found then raise exception 'record_owner_or_version_changed' using errcode='42501'; end if;
    end if;
    select jsonb_build_array(public.questyourlife_change('quick',to_jsonb(q))) into changes from public.questyourlife_daily_quest q where id=target and user_id=u;
   elsif op='daily_put' then
    select * into daily from public.questyourlife_daily_quest where id=target;
    if found and (daily.user_id<>u or daily.content_snapshot->>'kind'='quick') then raise exception 'not_owner' using errcode='42501'; end if;
    source:=nullif(b->>'source_id','')::uuid; joinid:=nullif(b->>'join_id','')::uuid;
    if source is not null and not exists(select 1 from public.questyourlife_questbook where id=source and owner_id=u and type='main') then raise exception 'invalid_source'; end if;
    if joinid is not null and not exists(select 1 from public.questyourlife_quest_join where id=joinid and user_id=u) then raise exception 'invalid_join'; end if;
    if source is not null and (joinid is null or not exists(select 1 from public.questyourlife_questbook where id=source and book_id=joinid)) then raise exception 'source_join_mismatch'; end if;
    if coalesce(daily.version,0)<>expected then outcome:='conflict';
    elsif daily.status='completed' then null;
    else
     if b->>'status'='completed' and coalesce((b->>'legacy')::boolean,false)=false then raise exception 'completion_requires_rpc'; end if;
     insert into public.questyourlife_daily_quest(id,user_id,join_id,source_id,main_id,scheduled_date,occurrence_key,content_snapshot,status,client_occurred_at)
     values(target,u,joinid,source,source,(b->>'scheduled_date')::date,case when source is not null then 'once' else null end,
      jsonb_build_object('title',b->>'title','detail',b->>'detail'),b->>'status',case when coalesce((b->>'legacy')::boolean,false) then (b->>'client_occurred_at')::timestamptz else null end)
     on conflict(id) do update set scheduled_date=excluded.scheduled_date,content_snapshot=excluded.content_snapshot,status=excluded.status,client_occurred_at=excluded.client_occurred_at,version=public.questyourlife_daily_quest.version+1,updated_at=now()
      where public.questyourlife_daily_quest.user_id=u and public.questyourlife_daily_quest.version=expected and public.questyourlife_daily_quest.content_snapshot->>'kind' is distinct from 'quick'
      returning id into affected;
     if not found then raise exception 'record_owner_or_version_changed' using errcode='42501'; end if;
    end if;
    select jsonb_build_array(public.questyourlife_change('daily',to_jsonb(q))) into changes from public.questyourlife_daily_quest q where id=target and user_id=u;
   elsif op in ('daily_complete','step_complete') then
    if op='step_complete' then
     source:=target;
     select * into main from public.questyourlife_questbook where id=source and owner_id=u and type='main' and deleted_at is null;
     if not found then raise exception 'invalid_source'; end if;
     -- Server checks all earlier steps, independently of the client's position/status.
     if exists(select 1 from public.questyourlife_questbook q join public.questyourlife_questbook c on c.id=q.head
        where q.book_id=main.book_id and q.type='main' and q.deleted_at is null
        and (c.sort_order,q.sort_order,q.id)<((select sort_order from public.questyourlife_questbook where id=main.head),main.sort_order,main.id)
        and not exists(select 1 from public.questyourlife_daily_quest d where d.user_id=u and d.source_id=q.id and d.status='completed')) then raise exception 'previous_step_required'; end if;
     select * into daily from public.questyourlife_daily_quest where user_id=u and source_id=source and occurrence_key='once' and status<>'cancelled';
     if not found then
      insert into public.questyourlife_daily_quest(id,user_id,join_id,source_id,main_id,scheduled_date,occurrence_key,content_snapshot,status)
      values(gen_random_uuid(),u,main.book_id,source,source,current_date,'once',jsonb_build_object('title',main.name,'detail',''),'accepted') returning * into daily;
     end if;
     target:=daily.id;
    else
     select * into daily from public.questyourlife_daily_quest where id=target and user_id=u and deleted_at is null;
     if not found then raise exception 'execution_not_found'; end if;
     if daily.content_snapshot->>'kind'='quick' then raise exception 'quick_has_no_reward'; end if;
     if daily.status not in ('accepted','completed') then raise exception 'accept_first'; end if;
     if daily.source_id is not null then
      select * into main from public.questyourlife_questbook where id=daily.source_id and owner_id=u and deleted_at is null;
      if not found then raise exception 'invalid_source'; end if;
      if exists(select 1 from public.questyourlife_questbook q join public.questyourlife_questbook c on c.id=q.head where q.book_id=main.book_id and q.type='main' and q.deleted_at is null
       and (c.sort_order,q.sort_order,q.id)<((select sort_order from public.questyourlife_questbook where id=main.head),main.sort_order,main.id)
       and not exists(select 1 from public.questyourlife_daily_quest d where d.user_id=u and d.source_id=q.id and d.status='completed')) then raise exception 'previous_step_required'; end if;
     end if;
    end if;
    award:=case when daily.source_id is null then 'daily:'||target else 'step:'||daily.source_id end;
    if daily.status<>'completed' then
     select coalesce(sum(cash_delta),0) into awarded from public.questyourlife_event where user_id=u and cash_delta>0 and server_created_at>=date_trunc('day',now() at time zone 'Asia/Seoul') at time zone 'Asia/Seoul';
     delta:=least(20,greatest(0,100-awarded));
     update public.questyourlife_daily_quest set status='completed',reward=delta,completed_at=now(),client_occurred_at=(b->>'client_occurred_at')::timestamptz,version=version+1,updated_at=now() where id=target;
    else award:=null; end if;
    select jsonb_build_array(public.questyourlife_change('daily',to_jsonb(q))) into changes from public.questyourlife_daily_quest q where id=target;
    if daily.source_id is not null then
     update public.questyourlife_quest_join set node_state=jsonb_set(node_state,array[daily.source_id::text],'"completed"'),version=version+1,updated_at=now() where id=daily.join_id and node_state->>daily.source_id::text is distinct from 'completed';
     select changes||jsonb_build_array(public.questyourlife_change('quest_join',to_jsonb(q))) into changes from public.questyourlife_quest_join q where id=daily.join_id;
    end if;
   elsif op='purchase' then
    item:=(b->>'item_id')::integer;
    cost:=case item when 0 then 0 when 1001 then 120 when 2 then 80 when 1002 then 60 when 1 then 50 when 3 then 100 when 4 then 120 when 5 then 150 when 1003 then 140 when 1004 then 90 else null end;
    if cost is null then raise exception 'unknown_item'; end if;
    select * into usr from public.questyourlife_user where id=u;
    if usr.inventory ? item::text then raise exception 'already_owned'; end if;
    if usr.cash<cost then raise exception 'insufficient_cash'; end if;
    delta:=-cost;
    update public.questyourlife_user set inventory=jsonb_set(inventory,array[item::text],jsonb_build_object('event_id',eid,'acquired_at',now())),version=version+1,updated_at=now() where id=u;
   elsif op='settings_patch' then
    select * into usr from public.questyourlife_user where id=u;
    if usr.version<>expected then outcome:='conflict';
    else
     if b ? 'appearance' and b->>'appearance' not in ('system','light','dark') then raise exception 'invalid_appearance'; end if;
     update public.questyourlife_user set settings=settings||case when b ? 'appearance' then jsonb_build_object('appearance',b->>'appearance') else '{}' end,
      version=version+1,updated_at=now() where id=u;
    end if;
   elsif op='equip' then
    item:=(b->>'item_id')::integer;
    if item<>0 and not exists(select 1 from public.questyourlife_user where id=u and inventory ? item::text) then raise exception 'not_owned'; end if;
    update public.questyourlife_user set equipped=jsonb_build_object('item_id',item),version=version+1,updated_at=now() where id=u;
   elsif op='goal_delete' then
    select * into book from public.questyourlife_questbook where id=target and owner_id=u and type='book';
    if not found then raise exception 'not_owner' using errcode='42501'; end if;
    if book.version<>expected then outcome:='conflict'; else
     update public.questyourlife_questbook set deleted_at=now(),version=version+1,updated_at=now() where book_id=target and deleted_at is null;
     update public.questyourlife_quest_join set deleted_at=now(),status='left',version=version+1,updated_at=now() where book_id=target and user_id=u;
    end if;
    select coalesce(jsonb_agg(public.questyourlife_change('questbook',to_jsonb(q))),'[]') into changes from public.questyourlife_questbook q where book_id=target and owner_id=u;
    select changes||jsonb_build_array(public.questyourlife_change('quest_join',to_jsonb(q))) into changes from public.questyourlife_quest_join q where book_id=target and user_id=u;
    if outcome='applied' then
     update public.questyourlife_daily_quest set status='cancelled',version=version+1,updated_at=now() where join_id=target and user_id=u and status not in ('completed','cancelled');
     select changes||coalesce(jsonb_agg(public.questyourlife_change('daily',to_jsonb(q))),'[]') into changes from public.questyourlife_daily_quest q where join_id=target and user_id=u and status='cancelled';
    end if;
   else raise exception 'unknown_operation'; end if;
  exception when check_violation or foreign_key_violation or unique_violation or invalid_text_representation or invalid_parameter_value or not_null_violation or raise_exception or insufficient_privilege then
   outcome:='rejected'; delta:=0; award:=null; changes:='[]'; result:=jsonb_build_object('error',SQLERRM);
  end;
  -- A rejected subtransaction rolls back all its domain writes, then receives an immutable receipt.
  result:=result||jsonb_build_object('user_base_version',userbase);
  if outcome='applied' then
   update public.questyourlife_user set cash=cash+delta,xp=xp+greatest(delta,0),version=version+case when delta<>0 then 1 else 0 end,updated_at=case when delta<>0 then now() else updated_at end where id=u;
  end if;
  update public.questyourlife_user set sync_seq=sync_seq+1 where id=u returning * into usr;
  changes:=coalesce(changes,'[]')||jsonb_build_array(public.questyourlife_change('user',to_jsonb(usr)));
  insert into public.questyourlife_event(user_id,event_id,actor_id,operation,target_id,request_hash,result_status,award_key,cash_delta,xp_delta,result,changes,sync_seq)
  values(u,eid,u,op,target,h,outcome,award,delta,greatest(delta,0),result,changes,usr.sync_seq) returning * into old;
  receipts:=receipts||jsonb_build_array(to_jsonb(old));
 end loop;
 return jsonb_build_object('receipts',receipts);
end $$;

create function public.questyourlife_pull_changes(p_cursor bigint default 0,p_watermark bigint default null,p_limit integer default 100,p_epoch uuid default null)
returns jsonb language plpgsql security definer set search_path='' as $$
declare u uuid:=auth.uid(); p public.questyourlife_user; w bigint; events jsonb; n bigint;
begin
 if u is null then raise exception 'authentication_required' using errcode='42501'; end if;
 select * into p from public.questyourlife_user where id=u;
 if not found then raise exception 'bootstrap_required'; end if;
 if p_epoch is not null and p_epoch<>p.sync_epoch then raise exception 'cursor_epoch_mismatch'; end if;
 w:=coalesce(p_watermark,p.sync_seq);
 if p_cursor<0 or w>p.sync_seq or w<p_cursor or p_limit not between 1 and 200 then raise exception 'invalid_cursor'; end if;
 select coalesce(jsonb_agg(to_jsonb(q) order by q.sync_seq),'[]'),coalesce(max(q.sync_seq),p_cursor) into events,n
 from (select * from public.questyourlife_event where user_id=u and sync_seq>p_cursor and sync_seq<=w order by sync_seq limit p_limit) q;
 return jsonb_build_object('events',events,'cursor',n,'watermark',w,'epoch',p.sync_epoch,'has_more',n<w);
end $$;

revoke all on function public.questyourlife_change(text,jsonb),public.questyourlife_immutable_event() from public,anon,authenticated;
revoke all on function public.questyourlife_bootstrap(),public.questyourlife_apply_batch(jsonb),public.questyourlife_pull_changes(bigint,bigint,integer,uuid),public.questyourlife_signup_gate(text) from public,anon,authenticated;
grant execute on function public.questyourlife_bootstrap(),public.questyourlife_apply_batch(jsonb),public.questyourlife_pull_changes(bigint,bigint,integer,uuid) to authenticated;
grant execute on function public.questyourlife_signup_gate(text) to service_role;
