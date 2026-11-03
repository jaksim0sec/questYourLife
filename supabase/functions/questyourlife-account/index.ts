// Supabase-hosted signup only. GoTrue owns hashing; passwords never enter business RPCs/logs.
const headers = { 'Content-Type': 'application/json', 'Access-Control-Allow-Origin': '*', 'Access-Control-Allow-Headers': 'authorization, apikey, content-type, x-client-info' };
async function boundedBody(req: Request): Promise<string | null> {
  if (Number(req.headers.get('content-length') || '0') > 4096) return null;
  const reader = req.body?.getReader();
  if (!reader) return null;
  const chunks: Uint8Array[] = []; let size = 0;
  while (true) {
    const { value, done } = await reader.read();
    if (done) break;
    size += value.length;
    if (size > 4096) { await reader.cancel(); return null; }
    chunks.push(value);
  }
  const bytes = new Uint8Array(size); let offset = 0;
  for (const chunk of chunks) { bytes.set(chunk, offset); offset += chunk.length; }
  return new TextDecoder().decode(bytes);
}
export async function signup(req: Request): Promise<Response> {
  if (req.method === 'OPTIONS') return new Response(null, { headers });
  if (req.method !== 'POST') return new Response('{}', { status: 405, headers });
  try {
    const text = await boundedBody(req);
    if (text === null) return new Response('{"error":"invalid_input"}', { status: 400, headers });
    const body = JSON.parse(text);
    const username = typeof body.username === 'string' ? body.username.trim().toLowerCase() : '';
    const password = body.password;
    const action = body.action ?? "signup";
    if (!['signup','signin'].includes(action) || !/^[a-z0-9_]{3,24}$/.test(username) || typeof password !== 'string' || password.length < 8 || password.length > 128)
      return new Response('{"error":"invalid_input"}', { status: 400, headers });
    const url = Deno.env.get('SUPABASE_URL'); const serviceKey = Deno.env.get('SUPABASE_SERVICE_ROLE_KEY');
    const idSecret = Deno.env.get('QUESTYOURLIFE_ID_SECRET');
    const publicKey = Deno.env.get('SUPABASE_ANON_KEY');
    if (!url || !serviceKey || !publicKey || !idSecret || idSecret.length < 32) return new Response('{"error":"signup_unavailable"}', { status: 503, headers });
    const authHeaders = { apikey: serviceKey, Authorization: `Bearer ${serviceKey}`, 'Content-Type': 'application/json' };
    const gate = await fetch(`${url}/rest/v1/rpc/questyourlife_signup_gate`, { method: 'POST', headers: authHeaders, body: JSON.stringify({ p_name: username }) });
    if (!gate.ok) { await gate.body?.cancel(); return new Response('{"error":"signup_temporarily_unavailable"}', { status: 429, headers }); }
    await gate.body?.cancel();
    // A server-only stable HMAC prevents direct public Auth signups from reserving predictable ID aliases.
    // Keep this secret across deployments; rotating it without an identity migration would break ID login.
    const key = await crypto.subtle.importKey('raw', new TextEncoder().encode(idSecret), { name: 'HMAC', hash: 'SHA-256' }, false, ['sign']);
    const digest = new Uint8Array(await crypto.subtle.sign('HMAC', key, new TextEncoder().encode(`questyourlife:login:${username}`)));
    const localPart = Array.from(digest, b => b.toString(16).padStart(2, '0')).join('');
    const email = `${localPart}@xpqjjfcyiirospxzykqd.accounts.invalid`;
    if (action === 'signup') {
      const created = await fetch(`${url}/auth/v1/admin/users`, { method: 'POST', headers: authHeaders, body: JSON.stringify({
        email, password, email_confirm: true, app_metadata: { questyourlife_username: username },
      }) });
      const ok = created.ok; await created.body?.cancel();
      if (!ok) return new Response('{"error":"signup_unavailable"}', { status: 400, headers });
    }
    const signed = await fetch(`${url}/auth/v1/token?grant_type=password`, { method: 'POST',
      headers: { apikey: publicKey, 'Content-Type': 'application/json' }, body: JSON.stringify({ email, password }) });
    if (!signed.ok) { await signed.body?.cancel(); return new Response('{"error":"invalid_credentials"}', { status: 401, headers }); }
    const session = await signed.json();
    return new Response(JSON.stringify({ access_token: session.access_token, refresh_token: session.refresh_token,
      expires_in: session.expires_in, user: { id: session.user.id } }), { status: 200, headers });
  } catch {
    return new Response('{"error":"signup_unavailable"}', { status: 400, headers });
  }
}
if (import.meta.main) Deno.serve(signup);
