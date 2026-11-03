import { signup } from './index.ts';
function assert(value: unknown, message: string) { if (!value) throw new Error(message); }
Deno.test('invalid signup never reaches privileged endpoints', async () => {
  const old = globalThis.fetch;
  globalThis.fetch = () => { throw new Error('network forbidden'); };
  try {
    const r = await signup(new Request('https://test.invalid', { method: 'POST', body: JSON.stringify({ username: '../admin', password: 'strong password' }) }));
    assert(r.status === 400, 'invalid name accepted');
    const huge = await signup(new Request('https://test.invalid', { method: 'POST', body: 'x'.repeat(4097) }));
    assert(huge.status === 400, 'oversize body accepted');
  } finally { globalThis.fetch = old; }
});
Deno.test('only Auth receives the unchanged password, not RPC or response', async () => {
  Deno.env.set('SUPABASE_URL', 'https://test.invalid'); Deno.env.set('SUPABASE_SERVICE_ROLE_KEY', 'test-only-not-a-credential'); Deno.env.set('SUPABASE_ANON_KEY', 'test-public-key'); Deno.env.set('QUESTYOURLIFE_ID_SECRET', 'test-only-stable-id-secret-minimum-32-characters');
  const old = globalThis.fetch; const calls: {url: string, body: Record<string, unknown>}[] = [];
  globalThis.fetch = (input, init) => { calls.push({ url: String(input), body: JSON.parse(String(init?.body)) }); return Promise.resolve(new Response(JSON.stringify({access_token:'access',refresh_token:'refresh',expires_in:3600,user:{id:'user'}}), { status: 200 })); };
  try {
    const password = '  keep my spaces  ';
    const r = await signup(new Request('https://test.invalid', { method: 'POST', body: JSON.stringify({ username: ' My_ID ', password }) }));
    assert(r.status === 200, 'creation failed'); assert(calls.length === 3, 'gate must precede Auth');
    assert(!('password' in calls[0].body), 'RPC received password');
    assert(calls[1].body.password === password, 'password altered');
    assert(/^[0-9a-f]{64}@xpqjjfcyiirospxzykqd\.accounts\.invalid$/.test(String(calls[1].body.email)), 'alias must be opaque and app-scoped');
    assert(calls[2].body.password === password && calls[1].body.email === calls[2].body.email, 'Auth identity mismatch');
    const response = await r.text(); assert(!response.includes(password) && !response.includes('credential'), 'response leaked credentials');
  } finally { globalThis.fetch = old; }
});
Deno.test('rate gate failure prevents Auth account creation', async () => {
  Deno.env.set('SUPABASE_URL', 'https://test.invalid'); Deno.env.set('SUPABASE_SERVICE_ROLE_KEY', 'test-only-not-a-credential'); Deno.env.set('SUPABASE_ANON_KEY', 'test-public-key'); Deno.env.set('QUESTYOURLIFE_ID_SECRET', 'test-only-stable-id-secret-minimum-32-characters');
  const old = globalThis.fetch; let count = 0;
  globalThis.fetch = () => { count++; return Promise.resolve(new Response('{}', { status: 429 })); };
  try {
    const r = await signup(new Request('https://test.invalid', { method: 'POST', body: JSON.stringify({ username: 'test_one', password: 'test password' }) }));
    assert(r.status === 429 && count === 1, 'rate limit bypassed');
  } finally { globalThis.fetch = old; }
});
