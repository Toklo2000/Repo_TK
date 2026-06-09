async function upgrade(stat) {
  const accountId = localStorage.getItem("accountId");

  await fetch(`/api/character/${accountId}/upgrade?stat=${stat}`, {
    method: "POST"
  });

  await loadHUD();
  await render("character");
}
