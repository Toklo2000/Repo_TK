async function render(view) {
  const app = document.getElementById("app");
  const accountId = localStorage.getItem("accountId");

  if (view === "city") {
    app.innerHTML = renderCity();
  }

  if (view === "shop") {
    app.innerHTML = await renderShop();
  }

  if (view === "tavern") {
    app.innerHTML = await renderTavern();
  }

  if (view === "character") {
    const c = await get(`/api/character/${accountId}`);
    app.innerHTML = renderCharacter(c);
  }
}
