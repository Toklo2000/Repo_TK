async function get(url) {
  const res = await fetch(url);
  return res.json();
}

async function post(url) {
  const res = await fetch(url, { method: "POST" });
  return res.json();
}

async function postJson(url, data) {
  const res = await fetch(url, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data)
  });
  return res.json();
}
