async function loadHUD() {
  const accountId = localStorage.getItem("accountId");

  const res = await fetch(`/api/character/${accountId}`);
  if (!res.ok) return;

  const c = await res.json();

  document.getElementById("gold").innerText = c.gold;
  document.getElementById("level").innerText = c.level;
}
