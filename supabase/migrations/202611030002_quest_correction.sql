-- Core corrective contracts for migration 003 / Android integration:
-- All mutations retain questyourlife_apply_batch(p_events jsonb) -> {receipts:[event]}.
-- New operations: book_join {id:independent_join_uuid,book_id:root_uuid};
-- join_patch {id:join_uuid,overrides:{patch:{logical_uuid:{...}},added:[],hidden:[]}}
--   uses event.base_version = join.edit_version; join_upgrade {id,book_id,accept:true}
--   uses base_version=join.edit_version and returns explicit overlay conflicts.
-- book_publish {id:root_uuid,is_public:true} uses base_version=root.version;
-- book_edition {id:published_root_uuid,new_id:new_root_uuid} creates draft physical
--   nodes with preserved logical IDs; goal_put edits drafts only.
-- daily_put uses logical source_id/main_id and independent join_id (legacy owned
--   root aliases remain accepted); occurrence_key is stable across deferral.
-- daily_undo {id:execution_uuid}; daily_complete/step_complete remain compatible.
-- questyourlife_resolve_tree(p_join uuid) -> effective logical-node JSON array.
-- Shared server-only helpers (no client EXECUTE):
-- questyourlife_week_start(timestamptz) -> Monday date, Asia/Seoul;
-- questyourlife_current_clan(uuid) -> active clan UUID or NULL (dynamic until 003);
-- questyourlife_project_economy(uuid,bigint,bigint) -> void, applies nominal
--   cash/XP deltas while honoring cash_debt. Caller MUST append same ledger deltas.
-- Immutable event gains score_delta, clan_id_at_event, week_start. Only qualified
-- completion events contribute; reversals/restorations retain original clan/week.
-- Integration clarification: quest_join receipt after-images additionally contain
-- derived effective_tree: [{id:logical_uuid,logical_id:logical_uuid,
-- physical_id:uuid|null,book_id:physical_root_uuid,head:logical_uuid|null,
-- type,name,tags:array|null,effective_tags:array|null,data,sort_order,...}].
-- No effective_tree column is stored. questyourlife_joined_books() ->
-- {changes:[{entity,id,version,deleted,data},...]} using the EXISTING questyourlife_change envelope;
-- includes participating joins (all statuses), pinned roots/base descendants,
-- and effective_tree on every join. join_patch and join_upgrade are batch ops.
-- join_edit is an alias of join_patch. data.edit_version overrides base_version
-- for these two operations (legacy base_version remains supported).

alter table public.questyourlife_user add column cash_debt bigint not null default 0 check(cash_debt>=0);
alter table public.questyourlife_event add column score_delta bigint not null default 0,
 add column clan_id_at_event uuid, add column week_start date;
create index questyourlife_event_competition on public.questyourlife_event(week_start,clan_id_at_event,user_id)
 where score_delta<>0 and result_status='applied';

-- Keep the original receipts/ledger byte-for-byte. Only mutable domain projections
-- are rekeyed. This also runs correctly on populated pre-correction databases.
alter table public.questyourlife_daily_quest drop constraint questyourlife_daily_quest_source_id_fkey;
alter table public.questyourlife_daily_quest drop constraint questyourlife_daily_quest_main_id_fkey;
update public.questyourlife_daily_quest d set source_id=q.logical_id from public.questyourlife_questbook q where d.source_id=q.id;
update public.questyourlife_daily_quest d set main_id=q.logical_id from public.questyourlife_questbook q where d.main_id=q.id;
do $$ declare j record; new_id uuid; seq bigint; changes jsonb; begin
 for j in select * from public.questyourlife_quest_join where id=book_id loop
  new_id:=gen_random_uuid();
  insert into public.questyourlife_quest_join select (jsonb_populate_record(null::public.questyourlife_quest_join,to_jsonb(q)||jsonb_build_object('id',new_id,'deleted_at',now(),'status','left'))).*
   from public.questyourlife_quest_join q where q.id=j.id;
  update public.questyourlife_daily_quest set join_id=new_id where join_id=j.id;
  delete from public.questyourlife_quest_join where id=j.id;
  -- The temporary left status avoids the pre-existing active-join unique index.
  update public.questyourlife_quest_join set status=j.status,deleted_at=j.deleted_at where id=new_id;
  -- Existing offline clients must observe both the old join tombstone and the new
  -- identity; never rewrite old sealed receipts or lose their delta-sync history.
  changes:=jsonb_build_array(public.questyourlife_change('quest_join',to_jsonb(j)||jsonb_build_object('deleted_at',now(),'status','left','version',j.version+1)));
  select changes||jsonb_build_array(public.questyourlife_change('quest_join',to_jsonb(q))) into changes from public.questyourlife_quest_join q where id=new_id;
  select changes||coalesce(jsonb_agg(public.questyourlife_change('daily',to_jsonb(d))),'[]') into changes from public.questyourlife_daily_quest d where join_id=new_id;
  update public.questyourlife_user set sync_seq=sync_seq+1 where id=j.user_id returning sync_seq into seq;
  insert into public.questyourlife_event(user_id,event_id,kind,operation,target_id,result_status,changes,sync_seq)
   values(j.user_id,gen_random_uuid(),'projection','join_identity_migration',new_id,'applied',changes,seq);
 end loop;
end $$;

drop index public.questyourlife_execution_once;
-- Old cancelled duplicates remain distinct historical occurrences; neither history
-- nor rewards are discarded to make the new uniqueness constraint fit.
with ranked as (
 select id,row_number() over(partition by join_id,source_id,occurrence_key order by created_at,id) n
 from public.questyourlife_daily_quest where source_id is not null
)
update public.questyourlife_daily_quest d set occurrence_key=coalesce(d.occurrence_key,d.id::text)||':legacy:'||d.id
 from ranked r where r.id=d.id and (r.n>1 or d.occurrence_key is null);
update public.questyourlife_daily_quest set occurrence_key=id::text where occurrence_key is null;
-- Foundation imported historical completions without recording their provenance
-- in the snapshot. Preserve zero-economic imports across undo/recomplete.
update public.questyourlife_daily_quest d set content_snapshot=content_snapshot||'{"legacy":true}'::jsonb
 where d.status='completed' and d.content_snapshot->>'kind' is distinct from 'quick'
 and not exists(select 1 from public.questyourlife_event e where e.user_id=d.user_id and e.target_id=d.id
  and e.operation in ('daily_complete','step_complete') and e.result_status='applied' and e.award_key is not null);
-- Daily completion history can be reconstructed; it must not be copied into the
-- manual-milestone object. Retain entries without a corresponding execution.
update public.questyourlife_quest_join j set node_state=coalesce((select jsonb_object_agg(s.key,s.value)
 from jsonb_each(j.node_state) s where not exists(select 1 from public.questyourlife_daily_quest d
  where d.join_id=j.id and d.source_id::text=s.key and d.status='completed')),'{}');
-- Foundation used 'deferred' as a state; defer now only changes the date.
update public.questyourlife_daily_quest set status='accepted' where status='deferred';
alter table public.questyourlife_daily_quest drop constraint questyourlife_daily_quest_status_check;
alter table public.questyourlife_daily_quest add constraint questyourlife_daily_quest_status_check check(status in ('proposed','accepted','completed','cancelled'));
create unique index questyourlife_execution_once on public.questyourlife_daily_quest(join_id,source_id,occurrence_key)
 where source_id is not null;
create unique index questyourlife_standalone_occurrence on public.questyourlife_daily_quest(user_id,occurrence_key) where source_id is null;
alter table public.questyourlife_daily_quest alter column occurrence_key set not null;

create function public.questyourlife_week_start(p_at timestamptz default now()) returns date
language sql immutable set search_path='' as $$ select date_trunc('week',p_at at time zone 'Asia/Seoul')::date $$;

create function public.questyourlife_current_clan(p_user uuid) returns uuid
language plpgsql stable security definer set search_path='' as $$
declare answer uuid;
begin
 if to_regclass('public.questyourlife_clan') is not null then
  execute 'select id from public.questyourlife_clan where deleted_at is null and members ? $1
    and (members->$1->>''left_at'') is null order by id limit 1' into answer using p_user::text;
 end if;
 return answer;
end $$;

create function public.questyourlife_project_economy(p_user uuid,p_cash bigint,p_xp bigint) returns void
language plpgsql security definer set search_path='' as $$
begin
 -- Net spendable balance is cash-cash_debt; ledger cash_delta records this signed
 -- entitlement. Spend inventory is never revoked by a later completion reversal.
 update public.questyourlife_user set
  cash=greatest(0,cash-cash_debt+p_cash),cash_debt=greatest(0,cash_debt-cash-p_cash),
  xp=xp+p_xp,version=version+1,updated_at=now() where id=p_user;
end $$;

-- A non-recursive, bounded root lookup avoids self-referential RLS recursion.
create function public.questyourlife_root_visible(p_root uuid) returns boolean
language sql stable security definer set search_path='' as $$
 select auth.uid() is not null and exists (
  select 1 from public.questyourlife_questbook q where q.id=p_root and q.type='book'
  and (q.owner_id=auth.uid() or (q.is_public and q.publication_status='published' and q.deleted_at is null)
   or exists(select 1 from public.questyourlife_quest_join j where j.book_id=q.id and j.user_id=auth.uid()))
 )
$$;
drop policy questyourlife_own_book on public.questyourlife_questbook;
create policy questyourlife_own_book on public.questyourlife_questbook for select to authenticated
 using(public.questyourlife_root_visible(book_id));

create function public.questyourlife_validate_node(p_node jsonb) returns void
language plpgsql immutable set search_path='' as $$
declare d jsonb:=p_node->'data'; r jsonb;
begin
 if p_node->>'type' not in ('book','chap','main','daily') or p_node->>'type' is null
  or jsonb_typeof(p_node->'name') is distinct from 'string'
  or coalesce(length(p_node->>'name'),0) not between 1 and 300 or jsonb_typeof(d) is distinct from 'object'
  or d is null or (p_node->'tags'<>'null'::jsonb and jsonb_typeof(p_node->'tags')<>'array') then raise exception 'invalid_node'; end if;
 if exists(select 1 from jsonb_array_elements(coalesce(nullif(p_node->'tags','null'::jsonb),'[]')) v where jsonb_typeof(v)<>'string' or length(v#>>'{}')>80)
  then raise exception 'invalid_tags'; end if;
 if d ? 'required_count' and (jsonb_typeof(d->'required_count')<>'number' or (d->>'required_count')::numeric not between 1 and 100000 or (d->>'required_count')::numeric<>trunc((d->>'required_count')::numeric)) then raise exception 'invalid_required_count'; end if;
 if d ? 'criteria' and (jsonb_typeof(d->'criteria')<>'object' or coalesce(length(d->'criteria'->>'method'),0)=0 or coalesce(length(d->'criteria'->>'description'),0)=0) then raise exception 'invalid_criteria'; end if;
 if d ? 'estimated_minutes' and (jsonb_typeof(d->'estimated_minutes')<>'number' or (d->>'estimated_minutes')::numeric not between 0 and 10080) then raise exception 'invalid_duration'; end if;
 if d ? 'recurrence' then
  r:=d->'recurrence';
  if jsonb_typeof(r)<>'object' or coalesce(r->>'frequency','') not in ('none','daily','weekly','monthly')
   or coalesce((r->>'interval')::integer,1) not between 1 and 366 then raise exception 'invalid_recurrence'; end if;
 end if;
end $$;

-- Internal resolver accepts a candidate overlay/root for transactional validation
-- before UPDATE. Nothing here materializes a copied quest tree.
create function public.questyourlife_effective_tree(p_root uuid,p_overlay jsonb) returns jsonb
language plpgsql stable security definer set search_path='' as $$
declare nodes jsonb:='[]'; answer jsonb; n jsonb; patch jsonb; a jsonb; k text; root_logical uuid; total integer;
begin
 if jsonb_typeof(p_overlay) is distinct from 'object' or jsonb_typeof(p_overlay->'patch') is distinct from 'object'
  or jsonb_typeof(p_overlay->'added') is distinct from 'array' or jsonb_typeof(p_overlay->'hidden') is distinct from 'array'
  or p_overlay is null or octet_length(p_overlay::text)>131072 then raise exception 'invalid_overrides'; end if;
 select logical_id into root_logical from public.questyourlife_questbook where id=p_root and type='book';
 if not found then raise exception 'invalid_book_root'; end if;
 for n in select to_jsonb(q)||jsonb_build_object('id',q.logical_id,'physical_id',q.id,'head',parent.logical_id)
  from public.questyourlife_questbook q left join public.questyourlife_questbook parent on parent.id=q.head
  where q.book_id=p_root and q.deleted_at is null loop
  nodes:=nodes||jsonb_build_array(n);
 end loop;
 for a in select value from jsonb_array_elements(p_overlay->'added') loop
  if jsonb_typeof(a)<>'object' or a->>'type'='book' or a->>'id' is null then raise exception 'invalid_added_node'; end if;
  perform (a->>'id')::uuid;
  if a ? 'logical_id' and a->>'logical_id'<>a->>'id' then raise exception 'logical_identity_mismatch'; end if;
  if exists(select 1 from jsonb_array_elements(nodes) v where v->>'id'=a->>'id') then raise exception 'duplicate_logical_id'; end if;
  n:=jsonb_build_object('id',(a->>'id')::uuid,'logical_id',(a->>'id')::uuid,'physical_id',null,'book_id',p_root,
   'head',nullif(a->>'head','')::uuid,'type',a->>'type','name',a->>'name','tags',a->'tags','data',coalesce(a->'data','{}'),
   'sort_order',coalesce((a->>'sort_order')::integer,0));
  nodes:=nodes||jsonb_build_array(n);
 end loop;
 if jsonb_array_length(nodes)>500 then raise exception 'tree_limit'; end if;
 for k,patch in select key,value from jsonb_each(p_overlay->'patch') loop
  perform k::uuid;
  if jsonb_typeof(patch)<>'object' or not exists(select 1 from jsonb_array_elements(nodes) v where v->>'id'=k) then raise exception 'overlay_missing_node:%',k; end if;
  if exists(select 1 from jsonb_object_keys(patch) f where f not in ('name','data','tags','head','sort_order')) then raise exception 'invalid_patch_field'; end if;
  if k=root_logical::text and patch ? 'head' then raise exception 'root_parent_forbidden'; end if;
  if patch ? 'head' then perform (patch->>'head')::uuid; end if;
  select jsonb_agg(case when v->>'id'=k then v||patch||
   case when patch ? 'data' then jsonb_build_object('data',(v->'data')||(patch->'data')) else '{}' end else v end)
   into nodes from jsonb_array_elements(nodes) v;
 end loop;
 for n in select value from jsonb_array_elements(nodes) loop
  perform public.questyourlife_validate_node(n);
  perform (n->>'id')::uuid; perform coalesce((n->>'sort_order')::integer,0);
  if n->>'type'='book' then
   if n->>'id'<>root_logical::text or n->>'head' is not null then raise exception 'invalid_book_root'; end if;
  elsif not exists(select 1 from jsonb_array_elements(nodes) p where p->>'id'=n->>'head'
   and p->>'type'=case n->>'type' when 'chap' then 'book' when 'main' then 'chap' when 'daily' then 'main' end) then raise exception 'invalid_effective_parent:%',n->>'id'; end if;
 end loop;
 for n in select value from jsonb_array_elements(p_overlay->'hidden') loop
  if jsonb_typeof(n)<>'string' or n#>>'{}'=root_logical::text
   or not exists(select 1 from jsonb_array_elements(nodes) v where v->>'id'=n#>>'{}') then raise exception 'invalid_hidden_node'; end if;
 end loop;
 -- Strict book→chap→main→daily parent types already exclude cycles; the reachability
 -- count is a second guard and resolves inherited tags and stable sibling ordering.
 with recursive walk as (
  select v node,coalesce(v->'tags','null'::jsonb) effective_tags,array[coalesce((v->>'sort_order')::integer,0)::text||':'||(v->>'id')] path
   from jsonb_array_elements(nodes) v where v->>'id'=root_logical::text
  union all
  select v,case when v->'tags' is null or v->'tags'='null'::jsonb then w.effective_tags else v->'tags' end,
   w.path||(lpad((coalesce((v->>'sort_order')::bigint,0)+2147483648)::text,10,'0')||':'||(v->>'id'))
   from walk w join lateral jsonb_array_elements(nodes) v on v->>'head'=w.node->>'id'
 ) select count(*) into total from walk;
 if total<>jsonb_array_length(nodes) then raise exception 'cyclic_or_disconnected_tree'; end if;
 with recursive hidden as (
  select v->>'id' id from jsonb_array_elements(nodes) v where (p_overlay->'hidden') ? (v->>'id')
  union select v->>'id' from hidden h join lateral jsonb_array_elements(nodes) v on v->>'head'=h.id
 ), walk as (
  select v node,coalesce(v->'tags','null'::jsonb) effective_tags,array['root'] path from jsonb_array_elements(nodes) v where v->>'id'=root_logical::text
  union all
  select v,case when v->'tags' is null or v->'tags'='null'::jsonb then w.effective_tags else v->'tags' end,
   w.path||(lpad((coalesce((v->>'sort_order')::bigint,0)+2147483648)::text,10,'0')||':'||(v->>'id'))
  from walk w join lateral jsonb_array_elements(nodes) v on v->>'head'=w.node->>'id'
 ) select coalesce(jsonb_agg(node||jsonb_build_object('effective_tags',effective_tags) order by path),'[]') into answer
 from walk where node->>'id' not in(select id from hidden);
 return answer;
end $$;

create function public.questyourlife_resolve_tree(p_join uuid) returns jsonb
language plpgsql stable security definer set search_path='' as $$
declare j public.questyourlife_quest_join;
begin
 select * into j from public.questyourlife_quest_join where id=p_join and user_id=auth.uid();
 if not found then raise exception 'join_not_found' using errcode='42501'; end if;
 return public.questyourlife_effective_tree(j.book_id,j.overrides);
end $$;

create function public.questyourlife_join_change(p_join uuid) returns jsonb
language sql stable security definer set search_path='' as $$
 select public.questyourlife_change('quest_join',to_jsonb(j)||jsonb_build_object('effective_tree',
  case when exists(select 1 from public.questyourlife_questbook r where r.id=j.book_id and r.deleted_at is null)
   then public.questyourlife_effective_tree(j.book_id,j.overrides) else '[]'::jsonb end)) from public.questyourlife_quest_join j where j.id=p_join
$$;

create function public.questyourlife_joined_books() returns jsonb
language plpgsql stable security definer set search_path='' as $$
declare changes jsonb; j public.questyourlife_quest_join;
begin
 if auth.uid() is null then raise exception 'authentication_required' using errcode='42501'; end if;
 select coalesce(jsonb_agg(public.questyourlife_change('questbook',to_jsonb(q))),'[]') into changes
  from public.questyourlife_questbook q where exists(select 1 from public.questyourlife_quest_join p where p.user_id=auth.uid() and p.book_id=q.book_id);
 for j in select * from public.questyourlife_quest_join where user_id=auth.uid() loop
  changes:=changes||jsonb_build_array(public.questyourlife_join_change(j.id));
 end loop;
 return jsonb_build_object('changes',changes);
end $$;

create function public.questyourlife_guard_definition() returns trigger
language plpgsql set search_path='' as $$
declare root public.questyourlife_questbook;
begin
 select * into root from public.questyourlife_questbook where id=old.book_id;
 if root.publication_status<>'draft' then raise exception 'published_immutable'; end if;
 if tg_op='DELETE' then return old; end if;
 if new.id<>old.id or new.logical_id<>old.logical_id or new.book_id<>old.book_id or new.owner_id<>old.owner_id or new.type<>old.type then raise exception 'immutable_node_identity'; end if;
 return new;
end $$;
create trigger questyourlife_guard_definition before update or delete on public.questyourlife_questbook
 for each row execute function public.questyourlife_guard_definition();

create function public.questyourlife_check_definition() returns trigger
language plpgsql set search_path='' as $$
declare root public.questyourlife_questbook; parent public.questyourlife_questbook;
begin
 -- Read the final row: deferred triggers may refer to an earlier update image.
 select * into root from public.questyourlife_questbook where id=new.book_id;
 if root.type is distinct from 'book' or root.owner_id<>new.owner_id then raise exception 'invalid_book_root'; end if;
 if new.type<>'book' and new.deleted_at is null then
  select * into parent from public.questyourlife_questbook where id=new.head;
  if parent.type is distinct from (case new.type when 'chap' then 'book' when 'main' then 'chap' when 'daily' then 'main' end)
   or parent.book_id<>new.book_id or parent.deleted_at is not null then raise exception 'invalid_parent'; end if;
 end if;
 perform public.questyourlife_validate_node(to_jsonb(new));
 return null;
end $$;
create constraint trigger questyourlife_check_definition after insert or update on public.questyourlife_questbook
 deferrable initially deferred for each row execute function public.questyourlife_check_definition();

create function public.questyourlife_check_join() returns trigger
language plpgsql set search_path='' as $$
begin
 if new.id=new.book_id or not exists(select 1 from public.questyourlife_questbook where id=new.book_id and type='book') then raise exception 'independent_join_root_required'; end if;
 if jsonb_typeof(new.node_state)<>'object' then raise exception 'invalid_node_state'; end if;
 return new;
end $$;
create trigger questyourlife_check_join before insert or update on public.questyourlife_quest_join
 for each row execute function public.questyourlife_check_join();

alter table public.questyourlife_user alter column avatar set default '{"mascot":"default","variant":null}',
 alter column settings set default '{"appearance":"system","notifications":true,"proposal_time":"07:00","timezone":"Asia/Seoul"}',
 alter column privacy set default '{"profile_public":true,"rank_visible":true}';
create unique index questyourlife_book_edition on public.questyourlife_questbook(logical_id,edition) where type='book';

create function public.questyourlife_upgrade_conflicts(p_old uuid,p_new uuid,p_overlay jsonb) returns jsonb
language plpgsql stable security definer set search_path='' as $$
declare conflicts jsonb:='[]'; k text; patch jsonb; f text; oldnode jsonb; newnode jsonb; n jsonb;
begin
 for k,patch in select key,value from jsonb_each(p_overlay->'patch') loop
  select to_jsonb(q)||jsonb_build_object('head',p.logical_id) into oldnode from public.questyourlife_questbook q
   left join public.questyourlife_questbook p on p.id=q.head where q.book_id=p_old and q.logical_id=k::uuid and q.deleted_at is null;
  select to_jsonb(q)||jsonb_build_object('head',p.logical_id) into newnode from public.questyourlife_questbook q
   left join public.questyourlife_questbook p on p.id=q.head where q.book_id=p_new and q.logical_id=k::uuid and q.deleted_at is null;
  if newnode is null then
   if not exists(select 1 from jsonb_array_elements(p_overlay->'added') a where a->>'id'=k) then conflicts:=conflicts||jsonb_build_array(jsonb_build_object('logical_id',k,'reason','removed_node')); end if;
  else
   for f in select jsonb_object_keys(patch) loop
    if oldnode->f is distinct from newnode->f and patch->f is distinct from newnode->f then
     conflicts:=conflicts||jsonb_build_array(jsonb_build_object('logical_id',k,'field',f,'reason','base_field_changed'));
    end if;
   end loop;
  end if;
 end loop;
 for n in select value from jsonb_array_elements(p_overlay->'hidden') loop
  if not exists(select 1 from public.questyourlife_questbook where book_id=p_new and logical_id=(n#>>'{}')::uuid and deleted_at is null)
   and not exists(select 1 from jsonb_array_elements(p_overlay->'added') a where a->>'id'=n#>>'{}') then
   conflicts:=conflicts||jsonb_build_array(jsonb_build_object('logical_id',n,'reason','hidden_node_removed'));
  end if;
 end loop;
 for n in select value from jsonb_array_elements(p_overlay->'added') loop
  if exists(select 1 from public.questyourlife_questbook where book_id=p_new and logical_id=(n->>'id')::uuid) then
   conflicts:=conflicts||jsonb_build_array(jsonb_build_object('logical_id',n->'id','reason','added_identity_collision'));
  end if;
 end loop;
 return conflicts;
end $$;

create or replace function public.questyourlife_apply_batch(p_events jsonb) returns jsonb
language plpgsql security definer set search_path='' as $$
declare
 u uuid:=auth.uid(); e jsonb; b jsonb; n jsonb; effective jsonb; selected jsonb; candidate_overlay jsonb; conflicts jsonb;
 eid uuid; target uuid; source uuid; joinid uuid; newroot uuid; physical uuid; ids uuid[]; idmap jsonb;
 op text; h text; outcome text; result jsonb; changes jsonb; receipts jsonb:='[]';
 old public.questyourlife_event; grant_event public.questyourlife_event; reversal public.questyourlife_event;
 usr public.questyourlife_user; book public.questyourlife_questbook; q public.questyourlife_questbook;
 daily public.questyourlife_daily_quest; j public.questyourlife_quest_join;
 expected bigint; userbase bigint; delta bigint; xpdelta bigint; score bigint; awarded bigint; cost bigint;
 award text; reverses uuid; clan uuid; week date; restore uuid; item integer; rootver bigint; edition_no integer;
 state text; occurrence text; snapshot jsonb; scheduled date; err text; legacy boolean;
begin
 if u is null then raise exception 'authentication_required' using errcode='42501'; end if;
 if p_events is null or jsonb_typeof(p_events)<>'array' or jsonb_array_length(p_events)>50 or octet_length(p_events::text)>262144 then raise exception 'invalid_batch'; end if;
 perform public.questyourlife_bootstrap();
 select * into usr from public.questyourlife_user where id=u for update;
 if (select count(*) from public.questyourlife_event where user_id=u and server_created_at>now()-interval '1 minute')+jsonb_array_length(p_events)>200 then raise sqlstate 'PT429' using message='sync_rate_limited'; end if;
 for e in select value from jsonb_array_elements(p_events) loop
  eid:=(e->>'event_id')::uuid; op:=e->>'operation'; b:=e->'data';
  if eid is null or jsonb_typeof(b) is distinct from 'object' or op is null then raise exception 'invalid_event'; end if;
  target:=nullif(b->>'id','')::uuid;
  h:=encode(sha256(convert_to(e::text,'UTF8')),'hex');
  select * into old from public.questyourlife_event where user_id=u and event_id=eid;
  if found then
   if old.request_hash<>h then raise exception 'event_payload_mismatch' using errcode='22023'; end if;
   receipts:=receipts||jsonb_build_array(to_jsonb(old)); continue;
  end if;
  select version into userbase from public.questyourlife_user where id=u;
  expected:=coalesce((e->>'base_version')::bigint,0);
  delta:=0; xpdelta:=0; score:=0; award:=null; reverses:=null; clan:=null; week:=null; restore:=null;
  outcome:='applied'; result:='{}'; changes:='[]'; joinid:=null; source:=null;
  begin
   if op='goal_put' then
    select * into book from public.questyourlife_questbook where id=target;
    if found and (book.owner_id<>u or book.type<>'book') then raise exception 'not_owner' using errcode='42501'; end if;
    if coalesce(book.version,0)<>expected then outcome:='conflict';
    elsif coalesce(book.publication_status,'draft')<>'draft' then raise exception 'published_immutable';
    else
     if jsonb_typeof(b->'nodes') is distinct from 'array' or jsonb_array_length(b->'nodes')>100 then raise exception 'invalid_nodes'; end if;
     if book.id is null and (select count(*) from public.questyourlife_questbook where owner_id=u and type='book' and deleted_at is null)>=100 then raise exception 'goal_limit'; end if;
     rootver:=coalesce(book.version,0)+1;
     insert into public.questyourlife_questbook(id,logical_id,book_id,type,owner_id,name,tags,data,version)
     values(target,target,target,'book',u,b->>'title',case when jsonb_typeof(b->'tags')='array' then array(select jsonb_array_elements_text(b->'tags')) end,
      coalesce(b->'data','{}')||jsonb_build_object('description',b->>'description'),rootver)
     on conflict(id) do update set name=excluded.name,data=public.questyourlife_questbook.data||excluded.data,
      tags=case when b ? 'tags' then excluded.tags else public.questyourlife_questbook.tags end,version=rootver,updated_at=now(),deleted_at=null;
     ids:=array[target];
     for n in select value from jsonb_array_elements(b->'nodes') order by case value->>'type' when 'chap' then 0 when 'main' then 1 when 'daily' then 2 else 3 end loop
      if n->>'type' not in ('chap','main','daily') then raise exception 'invalid_node'; end if;
      physical:=(n->>'id')::uuid;
      if physical=any(ids) then raise exception 'duplicate_node'; end if;
      if not exists(select 1 from public.questyourlife_questbook where id=(n->>'head')::uuid and book_id=target and owner_id=u
       and type=case n->>'type' when 'chap' then 'book' when 'main' then 'chap' when 'daily' then 'main' end) then raise exception 'invalid_parent'; end if;
      if exists(select 1 from public.questyourlife_questbook where id=physical and (owner_id<>u or book_id<>target or type<>n->>'type')) then raise exception 'node_identity_collision'; end if;
      insert into public.questyourlife_questbook(id,logical_id,book_id,head,type,owner_id,name,sort_order,tags,data)
      values(physical,coalesce(nullif(n->>'logical_id','')::uuid,physical),target,(n->>'head')::uuid,n->>'type',u,n->>'name',
       coalesce((n->>'sort_order')::integer,(n->>'position')::integer,0),case when jsonb_typeof(n->'tags')='array' then array(select jsonb_array_elements_text(n->'tags')) end,coalesce(n->'data','{}'))
      on conflict(id) do update set name=excluded.name,head=excluded.head,sort_order=excluded.sort_order,
       tags=case when n ? 'tags' then excluded.tags else public.questyourlife_questbook.tags end,
       data=case when n ? 'data' then excluded.data else public.questyourlife_questbook.data end,
       version=public.questyourlife_questbook.version+1,deleted_at=null,updated_at=now();
      ids:=array_append(ids,physical);
     end loop;
     update public.questyourlife_questbook set deleted_at=now(),version=version+1,updated_at=now() where book_id=target and not(id=any(ids)) and deleted_at is null;
     perform public.questyourlife_effective_tree(target,'{"patch":{},"added":[],"hidden":[]}');
     select * into j from public.questyourlife_quest_join where book_id=target and user_id=u and deleted_at is null and status<>'left';
     if not found then
      joinid:=coalesce(nullif(b->>'join_id','')::uuid,gen_random_uuid());
      if joinid=target then joinid:=gen_random_uuid(); end if;
      insert into public.questyourlife_quest_join(id,user_id,book_id) values(joinid,u,target);
     else joinid:=j.id; end if;
    end if;
    select coalesce(jsonb_agg(public.questyourlife_change('questbook',to_jsonb(t))),'[]') into changes from public.questyourlife_questbook t where book_id=target and owner_id=u;
    for j in select * from public.questyourlife_quest_join where book_id=target and user_id=u loop
     changes:=changes||jsonb_build_array(public.questyourlife_join_change(j.id));
    end loop;

   elsif op='book_join' then
    select * into book from public.questyourlife_questbook where id=(b->>'book_id')::uuid and type='book' and deleted_at is null;
    if not found or not (book.owner_id=u or (book.is_public and book.publication_status='published')) then raise exception 'book_not_joinable' using errcode='42501'; end if;
    if target is null or target=book.id then raise exception 'independent_join_id_required'; end if;
    insert into public.questyourlife_quest_join(id,user_id,book_id) values(target,u,book.id);
    changes:=jsonb_build_array(public.questyourlife_join_change(target));
    select changes||coalesce(jsonb_agg(public.questyourlife_change('questbook',to_jsonb(t))),'[]') into changes from public.questyourlife_questbook t where book_id=book.id;

   elsif op in ('join_patch','join_edit','join_upgrade','join_status') then
    select * into j from public.questyourlife_quest_join where id=target and user_id=u for update;
    if not found then raise exception 'join_not_found' using errcode='42501'; end if;
    expected:=coalesce((b->>'edit_version')::bigint,expected);
    if j.edit_version<>expected then outcome:='conflict'; result:=jsonb_build_object('error','edit_version_conflict','edit_version',j.edit_version);
    elsif op in ('join_patch','join_edit') then
     if j.status='left' or j.deleted_at is not null then raise exception 'inactive_join'; end if;
     perform public.questyourlife_effective_tree(j.book_id,b->'overrides');
     update public.questyourlife_quest_join set overrides=b->'overrides',edit_version=edit_version+1,version=version+1,updated_at=now() where id=j.id;
    elsif op='join_status' then
     state:=b->>'status';
     if state not in ('active','paused','completed','left') or state is null then raise exception 'invalid_join_status'; end if;
     update public.questyourlife_quest_join set status=state,deleted_at=case when state='left' then now() else null end,
      edit_version=edit_version+1,version=version+1,updated_at=now() where id=j.id;
    else
     if coalesce((b->>'accept')::boolean,false)=false then raise exception 'upgrade_acceptance_required'; end if;
     select * into book from public.questyourlife_questbook where id=(b->>'book_id')::uuid and type='book' and publication_status='published' and deleted_at is null;
     select * into q from public.questyourlife_questbook where id=j.book_id;
     if book.id is null or book.logical_id<>q.logical_id or book.owner_id<>q.owner_id or book.edition<=q.edition
      or not (book.owner_id=u or book.is_public) then raise exception 'invalid_upgrade_edition'; end if;
     conflicts:=public.questyourlife_upgrade_conflicts(j.book_id,book.id,j.overrides);
     candidate_overlay:=coalesce(b->'overrides',j.overrides);
     if jsonb_array_length(conflicts)>0 and not (b ? 'overrides') then
      outcome:='conflict'; result:=jsonb_build_object('error','overlay_upgrade_conflict','conflicts',conflicts);
     else
      begin perform public.questyourlife_effective_tree(book.id,candidate_overlay);
      exception when raise_exception then outcome:='conflict'; result:=jsonb_build_object('error','overlay_upgrade_conflict','conflicts',conflicts||jsonb_build_array(jsonb_build_object('reason',SQLERRM))); end;
      if outcome='applied' then
       update public.questyourlife_quest_join set book_id=book.id,overrides=candidate_overlay,edit_version=edit_version+1,version=version+1,updated_at=now() where id=j.id;
       select coalesce(jsonb_agg(public.questyourlife_change('questbook',to_jsonb(t))),'[]') into changes from public.questyourlife_questbook t where book_id=book.id;
      end if;
     end if;
    end if;
    changes:=changes||jsonb_build_array(public.questyourlife_join_change(j.id));

   elsif op in ('book_publish','book_edition') then
    select * into book from public.questyourlife_questbook where id=target and owner_id=u and type='book' and deleted_at is null for update;
    if not found then raise exception 'not_owner' using errcode='42501'; end if;
    if book.version<>expected then outcome:='conflict';
    elsif op='book_publish' then
     if book.publication_status<>'draft' then raise exception 'published_immutable'; end if;
     perform public.questyourlife_effective_tree(book.id,'{"patch":{},"added":[],"hidden":[]}');
     update public.questyourlife_questbook set publication_status='published',is_public=coalesce((b->>'is_public')::boolean,false),version=version+1,updated_at=now() where id=book.id;
    else
     if book.publication_status<>'published' then raise exception 'published_source_required'; end if;
     perform pg_advisory_xact_lock(hashtextextended(book.logical_id::text,881));
     newroot:=(b->>'new_id')::uuid;
     if newroot is null then raise exception 'new_root_id_required'; end if;
     select coalesce(max(edition),0)+1 into edition_no from public.questyourlife_questbook where logical_id=book.logical_id and type='book';
     idmap:=jsonb_build_object(book.id::text,newroot);
     for q in select * from public.questyourlife_questbook where book_id=book.id and type<>'book' and deleted_at is null loop
      idmap:=idmap||jsonb_build_object(q.id::text,gen_random_uuid());
     end loop;
     for q in select * from public.questyourlife_questbook where book_id=book.id and deleted_at is null order by case type when 'book' then 0 when 'chap' then 1 when 'main' then 2 else 3 end loop
      insert into public.questyourlife_questbook(id,logical_id,book_id,head,type,owner_id,name,tags,data,sort_order,edition)
      values((idmap->>q.id::text)::uuid,q.logical_id,newroot,(idmap->>q.head::text)::uuid,q.type,u,q.name,q.tags,q.data,q.sort_order,edition_no);
     end loop;
     result:=jsonb_build_object('book_id',newroot,'edition',edition_no);
    end if;
    select coalesce(jsonb_agg(public.questyourlife_change('questbook',to_jsonb(t))),'[]') into changes
     from public.questyourlife_questbook t where book_id=case when op='book_edition' and outcome='applied' then newroot else target end;

   elsif op='quick_put' then
    select * into daily from public.questyourlife_daily_quest where id=target;
    if found and (daily.user_id<>u or coalesce(daily.content_snapshot->>'kind','')<>'quick') then raise exception 'not_quick_owner' using errcode='42501'; end if;
    if coalesce(daily.version,0)<>expected then outcome:='conflict';
    else
     if coalesce(length(b->>'title'),0) not between 1 and 120 then raise exception 'invalid_title'; end if;
     insert into public.questyourlife_daily_quest(id,user_id,scheduled_date,occurrence_key,content_snapshot,status)
     values(target,u,(now() at time zone 'Asia/Seoul')::date,target::text,jsonb_build_object('kind','quick','title',b->>'title','created_at',b->'created_at'),
      case when (b->>'completed')::boolean then 'completed' else 'accepted' end)
     on conflict(id) do update set content_snapshot=excluded.content_snapshot,status=excluded.status,version=public.questyourlife_daily_quest.version+1,updated_at=now();
    end if;
    select jsonb_build_array(public.questyourlife_change('quick',to_jsonb(t))) into changes from public.questyourlife_daily_quest t where id=target and user_id=u;

   elsif op='daily_put' then
    select * into daily from public.questyourlife_daily_quest where id=target;
    if found and (daily.user_id<>u or daily.content_snapshot->>'kind'='quick') then raise exception 'not_owner' using errcode='42501'; end if;
    if coalesce(daily.version,0)<>expected then outcome:='conflict';
    elsif daily.status='completed' then null; -- sealed historical ACK compatibility; undo has its own command
    else
     source:=nullif(b->>'source_id','')::uuid; joinid:=nullif(b->>'join_id','')::uuid;
     if daily.id is not null then
      -- A sealed pre-correction request may still name the owner's root as join.
      if joinid is not null and joinid<>daily.join_id and exists(select 1 from public.questyourlife_quest_join where id=daily.join_id and book_id=joinid and user_id=u) then joinid:=daily.join_id; end if;
      if source is distinct from daily.source_id or joinid is distinct from daily.join_id
       or (b ? 'occurrence_key' and b->>'occurrence_key'<>daily.occurrence_key) then raise exception 'immutable_execution_identity'; end if;
     end if;
     if joinid is not null then
      select * into j from public.questyourlife_quest_join where id=joinid and user_id=u;
      if not found then select * into j from public.questyourlife_quest_join where book_id=joinid and user_id=u and deleted_at is null and status<>'left'; end if;
      if j.id is null or j.status<>'active' or j.deleted_at is not null then raise exception 'invalid_join'; end if;
      joinid:=j.id;
     end if;
     snapshot:=null;
     if source is not null then
      if joinid is null then raise exception 'source_requires_join'; end if;
      effective:=public.questyourlife_effective_tree(j.book_id,j.overrides);
      select value into selected from jsonb_array_elements(effective) where value->>'id'=source::text and value->>'type' in ('main','daily');
      if not found then raise exception 'invalid_logical_source'; end if;
      physical:=case selected->>'type' when 'main' then source else (selected->>'head')::uuid end;
      if b ? 'main_id' and nullif(b->>'main_id','')::uuid is distinct from physical then raise exception 'invalid_logical_main'; end if;
      snapshot:=jsonb_strip_nulls(jsonb_build_object('title',selected->>'name','name',selected->>'name',
       'detail',selected->'data'->>'description','description',selected->'data'->>'description','criteria',selected->'data'->'criteria',
       'required_count',selected->'data'->'required_count','recurrence',selected->'data'->'recurrence',
       'estimated_minutes',selected->'data'->'estimated_minutes','book_id',j.book_id,'source_id',source,'main_id',physical));
     else
      physical:=null;
      if b ? 'main_id' and b->>'main_id' is not null then raise exception 'source_required_for_main'; end if;
      if coalesce(length(b->>'title'),0) not between 1 and 300 then raise exception 'invalid_title'; end if;
      snapshot:=jsonb_strip_nulls(jsonb_build_object('title',b->>'title','name',b->>'title','detail',b->>'detail',
       'description',b->>'detail','criteria',b->'criteria','required_count',b->'required_count','estimated_minutes',b->'estimated_minutes'));
      perform public.questyourlife_validate_node(jsonb_build_object('type','daily','name',b->>'title','data',snapshot-'title'-'name'-'detail','tags',null));
     end if;
     scheduled:=(b->>'scheduled_date')::date; legacy:=coalesce((b->>'legacy')::boolean,false);
     if scheduled>(now() at time zone 'Asia/Seoul')::date+31 and daily.id is null and not legacy then raise exception 'scheduling_horizon_exceeded'; end if;
     state:=b->>'status';
     if state='deferred' then state:=coalesce(daily.status,'accepted'); end if;
     if state='completed' and not legacy then raise exception 'completion_requires_rpc'; end if;
     if daily.status='cancelled' and state<>'cancelled' then raise exception 'cancelled_occurrence_immutable'; end if;
     if daily.id is not null and state='completed' then raise exception 'legacy_import_creation_only'; end if;
     if daily.status='accepted' and state='proposed' then state:='accepted'; end if;
     occurrence:=coalesce(nullif(b->>'occurrence_key',''),daily.occurrence_key,target::text);
     if length(occurrence)>200 then raise exception 'invalid_occurrence_key'; end if;
     insert into public.questyourlife_daily_quest(id,user_id,join_id,source_id,main_id,scheduled_date,occurrence_key,content_snapshot,status,client_occurred_at,completed_at)
     values(target,u,joinid,source,physical,scheduled,occurrence,snapshot||case when legacy then '{"legacy":true}'::jsonb else '{}'::jsonb end,state,
      case when legacy then (b->>'client_occurred_at')::timestamptz end,case when state='completed' then now() end)
     on conflict(id) do update set scheduled_date=excluded.scheduled_date,status=excluded.status,
      -- Snapshot and accumulated condition progress are immutable on deferral.
      version=public.questyourlife_daily_quest.version+1,updated_at=now();
    end if;
    select jsonb_build_array(public.questyourlife_change('daily',to_jsonb(t))) into changes from public.questyourlife_daily_quest t where id=target and user_id=u;

   elsif op in ('daily_complete','step_complete','daily_undo') then
    if op='step_complete' then
     source:=target; joinid:=nullif(b->>'join_id','')::uuid;
     if joinid is null then
      select p.* into j from public.questyourlife_quest_join p join public.questyourlife_questbook t on t.book_id=p.book_id
       where p.user_id=u and p.status='active' and p.deleted_at is null and t.logical_id=source and t.type='main' and t.deleted_at is null;
     else
      select * into j from public.questyourlife_quest_join where user_id=u and (id=joinid or book_id=joinid) and status='active' and deleted_at is null order by (id=joinid) desc limit 1;
     end if;
     if j.id is null then raise exception 'invalid_join'; end if;
     effective:=public.questyourlife_effective_tree(j.book_id,j.overrides);
     select value into selected from jsonb_array_elements(effective) where value->>'id'=source::text and value->>'type'='main';
     if not found then raise exception 'invalid_source'; end if;
     -- Legacy direct step completion reuses an already existing occurrence. It can
     -- never mint an extra reward after completion through the daily endpoint.
     select * into daily from public.questyourlife_daily_quest where user_id=u and join_id=j.id and source_id=source and status<>'cancelled'
      order by (status='completed') desc,created_at,id limit 1;
     if not found then
      insert into public.questyourlife_daily_quest(id,user_id,join_id,source_id,main_id,scheduled_date,occurrence_key,content_snapshot,status)
      values(gen_random_uuid(),u,j.id,source,source,(now() at time zone 'Asia/Seoul')::date,'once',
       jsonb_strip_nulls(jsonb_build_object('title',selected->>'name','name',selected->>'name','description',selected->'data'->>'description',
        'detail',selected->'data'->>'description','criteria',selected->'data'->'criteria','required_count',selected->'data'->'required_count',
        'estimated_minutes',selected->'data'->'estimated_minutes','book_id',j.book_id)),'accepted') returning * into daily;
     end if;
     target:=daily.id;
    else
     select * into daily from public.questyourlife_daily_quest where id=target and user_id=u and deleted_at is null;
     if not found then raise exception 'execution_not_found'; end if;
    end if;
    if daily.content_snapshot->>'kind'='quick' then raise exception 'quick_has_no_reward'; end if;
    if op='daily_undo' then
     if daily.status='completed' then
      select t.* into grant_event from public.questyourlife_event t where user_id=u and target_id=target and result_status='applied'
       and operation in ('daily_complete','step_complete') and award_key is not null
       and not exists(select 1 from public.questyourlife_event r where r.reverses_id=t.id) order by sync_seq desc limit 1;
      if found then
       delta:=-grant_event.cash_delta; xpdelta:=-grant_event.xp_delta; score:=-grant_event.score_delta;
       clan:=grant_event.clan_id_at_event; week:=grant_event.week_start; reverses:=grant_event.id;
       award:='undo:'||grant_event.id;
      end if;
      update public.questyourlife_daily_quest set status='accepted',reward=0,completed_at=null,version=version+1,updated_at=now() where id=target;
     end if;
    else
     if daily.status not in ('accepted','completed') then raise exception 'accept_first'; end if;
     if daily.status<>'completed' then
      -- New completions validate active membership/effective source. A hidden or
      -- removed source leaves its historical snapshots and receipts untouched.
      if daily.join_id is not null then
       select * into j from public.questyourlife_quest_join where id=daily.join_id and user_id=u and status='active' and deleted_at is null;
       if not found then raise exception 'inactive_join'; end if;
       if daily.source_id is not null then
        effective:=public.questyourlife_effective_tree(j.book_id,j.overrides);
        select value into selected from jsonb_array_elements(effective) where value->>'id'=daily.source_id::text;
        if not found then raise exception 'hidden_or_removed_source'; end if;
        -- Preserve sealed main-step ordering without persisting daily histories in node_state.
        if selected->>'type'='main' and exists(
         select 1 from jsonb_array_elements(effective) m join lateral jsonb_array_elements(effective) c on c->>'id'=m->>'head'
         where m->>'type'='main' and ((c->>'sort_order')::integer,(m->>'sort_order')::integer,m->>'id')<
          ((select (a->>'sort_order')::integer from jsonb_array_elements(effective) a where a->>'id'=selected->>'head'),(selected->>'sort_order')::integer,selected->>'id')
         and not exists(select 1 from public.questyourlife_daily_quest d where d.join_id=j.id and d.source_id=(m->>'id')::uuid and d.status='completed')
         and j.node_state->>(m->>'id') is distinct from 'completed') then raise exception 'previous_step_required'; end if;
       end if;
      end if;
      select t.* into reversal from public.questyourlife_event t where user_id=u and target_id=target and operation='daily_undo' and reverses_id is not null
       order by sync_seq desc limit 1;
      if reversal.id is not null then
       -- Restoration inherits its original entitlement, clan and week, even after
       -- midnight, clan changes, spending, or repeated undo/recomplete cycles.
       delta:=-reversal.cash_delta; xpdelta:=-reversal.xp_delta; score:=-reversal.score_delta;
       clan:=reversal.clan_id_at_event; week:=reversal.week_start; restore:=reversal.id;
      elsif coalesce((daily.content_snapshot->>'legacy')::boolean,false) then delta:=0; xpdelta:=0; score:=0;
      else
       select coalesce(sum(t.cash_delta),0) into awarded from public.questyourlife_event t where user_id=u and cash_delta>0
        and operation in ('daily_complete','step_complete') and not (t.result ? 'restores_id')
        and server_created_at>=date_trunc('day',now() at time zone 'Asia/Seoul') at time zone 'Asia/Seoul';
       delta:=least(20,greatest(0,100-awarded)); xpdelta:=delta; score:=delta;
       clan:=public.questyourlife_current_clan(u); week:=public.questyourlife_week_start(now());
      end if;
      award:='daily:'||target||':complete:'||eid;
      update public.questyourlife_daily_quest set status='completed',reward=delta,completed_at=now(),
       client_occurred_at=(b->>'client_occurred_at')::timestamptz,version=version+1,updated_at=now() where id=target;
     end if;
    end if;
    select jsonb_build_array(public.questyourlife_change('daily',to_jsonb(t))) into changes from public.questyourlife_daily_quest t where id=target;
    -- Derived participant view may change, but daily histories stay out of node_state.
    if daily.join_id is not null then changes:=changes||jsonb_build_array(public.questyourlife_join_change(daily.join_id)); end if;
    if restore is not null then result:=result||jsonb_build_object('restores_id',restore); end if;

   elsif op='purchase' then
    item:=(b->>'item_id')::integer;
    cost:=case item when 0 then 0 when 1001 then 120 when 2 then 80 when 1002 then 60 when 1 then 50 when 3 then 100 when 4 then 120 when 5 then 150 when 1003 then 140 when 1004 then 90 else null end;
    if cost is null then raise exception 'unknown_item'; end if;
    select * into usr from public.questyourlife_user where id=u;
    if usr.inventory ? item::text then raise exception 'already_owned'; end if;
    if usr.cash<cost or usr.cash_debt>0 then raise exception 'insufficient_cash'; end if;
    delta:=-cost;
    update public.questyourlife_user set inventory=jsonb_set(inventory,array[item::text],jsonb_build_object('source_event_id',eid,'acquired_at',now())),version=version+1,updated_at=now() where id=u;

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
    if book.version<>expected then outcome:='conflict';
    else
     if book.publication_status='draft' then
      update public.questyourlife_questbook set deleted_at=now(),version=version+1,updated_at=now() where book_id=target and deleted_at is null;
     end if;
     update public.questyourlife_quest_join set deleted_at=now(),status='left',edit_version=edit_version+1,version=version+1,updated_at=now() where book_id=target and user_id=u;
     update public.questyourlife_daily_quest set status='cancelled',version=version+1,updated_at=now()
      where user_id=u and join_id in(select id from public.questyourlife_quest_join where book_id=target and user_id=u) and status not in ('completed','cancelled');
    end if;
    select coalesce(jsonb_agg(public.questyourlife_change('questbook',to_jsonb(t))),'[]') into changes from public.questyourlife_questbook t where book_id=target and owner_id=u;
    for j in select * from public.questyourlife_quest_join where book_id=target and user_id=u loop
     changes:=changes||jsonb_build_array(public.questyourlife_join_change(j.id));
    end loop;
    select changes||coalesce(jsonb_agg(public.questyourlife_change('daily',to_jsonb(t))),'[]') into changes from public.questyourlife_daily_quest t
     where user_id=u and join_id in(select id from public.questyourlife_quest_join where book_id=target and user_id=u);
   else raise exception 'unknown_operation'; end if;

   if outcome='applied' and (delta<>0 or xpdelta<>0) then perform public.questyourlife_project_economy(u,delta,xpdelta); end if;
  exception when check_violation or foreign_key_violation or unique_violation or invalid_text_representation or invalid_parameter_value
   or not_null_violation or raise_exception or insufficient_privilege or numeric_value_out_of_range or invalid_datetime_format or datetime_field_overflow then
   outcome:='rejected'; delta:=0; xpdelta:=0; score:=0; award:=null; reverses:=null; clan:=null; week:=null;
   changes:='[]'; result:=jsonb_build_object('error',SQLERRM);
  end;
  result:=result||jsonb_build_object('user_base_version',userbase);
  update public.questyourlife_user set sync_seq=sync_seq+1 where id=u returning * into usr;
  changes:=coalesce(changes,'[]')||jsonb_build_array(public.questyourlife_change('user',to_jsonb(usr)));
  insert into public.questyourlife_event(user_id,event_id,actor_id,operation,target_id,request_hash,result_status,award_key,cash_delta,xp_delta,
   score_delta,clan_id_at_event,week_start,reverses_id,result,changes,sync_seq)
  values(u,eid,u,op,target,h,outcome,award,delta,xpdelta,score,clan,week,reverses,result,changes,usr.sync_seq) returning * into old;
  receipts:=receipts||jsonb_build_array(to_jsonb(old));
 end loop;
 return jsonb_build_object('receipts',receipts);
end $$;

-- Public API functions are the explicit authenticated entry points. Internal
-- SECURITY DEFINER helpers are deliberately not executable through PostgREST.
revoke all on function public.questyourlife_week_start(timestamptz),public.questyourlife_current_clan(uuid),
 public.questyourlife_project_economy(uuid,bigint,bigint),public.questyourlife_root_visible(uuid),
 public.questyourlife_validate_node(jsonb),public.questyourlife_effective_tree(uuid,jsonb),
 public.questyourlife_join_change(uuid),public.questyourlife_guard_definition(),public.questyourlife_check_definition(),
 public.questyourlife_check_join(),public.questyourlife_upgrade_conflicts(uuid,uuid,jsonb),
 public.questyourlife_resolve_tree(uuid),public.questyourlife_joined_books() from public,anon,authenticated;
grant execute on function public.questyourlife_root_visible(uuid),public.questyourlife_resolve_tree(uuid),public.questyourlife_joined_books() to authenticated;

-- Android's active participation refresh; the broader joined_books endpoint also
-- includes historical/left participations for history views.
create function public.questyourlife_my_books() returns jsonb
language plpgsql stable security definer set search_path='' as $$
declare changes jsonb; j public.questyourlife_quest_join;
begin
 if auth.uid() is null then raise exception 'authentication_required' using errcode='42501'; end if;
 select coalesce(jsonb_agg(public.questyourlife_change('questbook',to_jsonb(q))),'[]') into changes
  from public.questyourlife_questbook q where exists(select 1 from public.questyourlife_quest_join p
   where p.user_id=auth.uid() and p.status='active' and p.deleted_at is null and p.book_id=q.book_id);
 for j in select * from public.questyourlife_quest_join where user_id=auth.uid() and status='active' and deleted_at is null loop
  changes:=changes||jsonb_build_array(public.questyourlife_join_change(j.id));
 end loop;
 return jsonb_build_object('changes',changes);
end $$;
revoke all on function public.questyourlife_my_books() from public,anon,authenticated;
grant execute on function public.questyourlife_my_books() to authenticated;

-- Publish corrected projections for populated installations. Existing immutable
-- commands remain untouched, while old cursors can receive logical source IDs,
-- migrated joins, provenance and effective-tree after-images through normal sync.
do $$
declare p public.questyourlife_user; j public.questyourlife_quest_join; changes jsonb; seq bigint;
begin
 for p in select * from public.questyourlife_user u where
  exists(select 1 from public.questyourlife_questbook q where q.owner_id=u.id)
  or exists(select 1 from public.questyourlife_quest_join q where q.user_id=u.id)
  or exists(select 1 from public.questyourlife_daily_quest q where q.user_id=u.id)
 loop
  select coalesce(jsonb_agg(public.questyourlife_change('questbook',to_jsonb(q))),'[]') into changes
   from public.questyourlife_questbook q where q.owner_id=p.id or exists(select 1 from public.questyourlife_quest_join t where t.user_id=p.id and t.book_id=q.book_id);
  for j in select * from public.questyourlife_quest_join where user_id=p.id loop
   changes:=changes||jsonb_build_array(public.questyourlife_join_change(j.id));
  end loop;
  select changes||coalesce(jsonb_agg(public.questyourlife_change('daily',to_jsonb(q))),'[]') into changes from public.questyourlife_daily_quest q where q.user_id=p.id;
  update public.questyourlife_user set sync_seq=sync_seq+1 where id=p.id returning * into p;
  changes:=changes||jsonb_build_array(public.questyourlife_change('user',to_jsonb(p)));
  insert into public.questyourlife_event(user_id,event_id,kind,operation,result_status,changes,sync_seq)
   values(p.id,gen_random_uuid(),'projection','quest_correction_migration','applied',changes,p.sync_seq);
 end loop;
end $$;
