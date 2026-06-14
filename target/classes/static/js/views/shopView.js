async function renderShop() {
  const accountId = localStorage.getItem("accountId");
  const character = await get(`/api/character/${accountId}`);
  const items = await get(`/api/shop/${character.id}`);

  let html = `<h2>Sklep</h2>
  <p>Złoto: <strong>${character.gold}</strong></p>
  <hr>
  <div style="display:flex; flex-wrap:wrap; gap:1rem;">`;

  for (const item of items) {
    const statsText = Object.entries(item.stats).map(([k, v]) => `${k}: +${v}`).join(', ');

    html += `
    <div style="background:#ede0c4; border:1px solid #8b6020; padding:1rem; width:180px; border-radius:2px;">
    <div style="font-family:'Cinzel',serif; font-weight:600; margin-bottom:0.4rem;">${item.name}</div>
    <div style="font-size:0.8rem; color:#6a4818; margin-bottom:0.4rem;">${item.type}</div>
    <div style="font-size:0.8rem; margin-bottom:0.6rem;">${statsText || 'Brak statów'}</div>
    <div style="font-family:'Cinzel',serif; font-size:0.85rem; margin-bottom:0.6rem;">${item.price} złota</div>
    <button onclick="buyItem(${item.templateId}, ${item.price}, ${item.itemSeed})">Kup</button>
    </div>`;
  }

  html += `</div>`;
  return html;
}

async function buyItem(templateId, price, itemSeed) {
  const accountId = localStorage.getItem("accountId");
  const character = await get(`/api/character/${accountId}`);
  const result = await post(`/api/shop/buy?characterId=${character.id}&templateId=${templateId}&price=${price}&itemSeed=${itemSeed}`);
  if (result.error) {
    alert(result.error);
  } else {
    await render('shop');
    await loadHUD();
  }
}
