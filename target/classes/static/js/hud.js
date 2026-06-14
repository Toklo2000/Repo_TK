async function loadHUD() {
  const accountId = localStorage.getItem("accountId");
  const res = await fetch(`/api/character/${accountId}`);
  if (!res.ok) return;
  const c = await res.json();
  document.getElementById("gold").innerText = c.gold;
  document.getElementById("level").innerText = c.level;
  document.getElementById("exp").innerText = `${c.exp}/${expRequired(c.level)}`;
}

function expRequired(level) {
  if (level === 1) return 10;
  if (level === 2) return 15;
  if (level === 3) return 20;
  if (level === 4) return 25;
  if (level === 5) return 50;
  return 25 * (level - 1);
}
