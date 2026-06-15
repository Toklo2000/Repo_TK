async function renderShop() {
  const accountId = localStorage.getItem("accountId");
  const character = await get(`/api/character/${accountId}`);

  const today = new Date().toISOString().split('T')[0];
  const slotKey = `slots_${character.id}_${today}`;

  let slots = JSON.parse(localStorage.getItem(slotKey) || 'null');
  if (!slots) {
    slots = [0, 1, 2, 3, 4, 5];
    localStorage.setItem(slotKey, JSON.stringify(slots));
  }

  const maxIndex = Math.max(...slots);
  const items = await get(`/api/shop/${character.id}?offset=0&count=${maxIndex + 1}`);

  let html = `<h2>Sklep</h2>
  <p>Złoto: <strong>${character.gold}</strong></p>
  <hr>
  <div style="display:flex; flex-wrap:wrap; gap:1rem;">`;

  for (let slotPos = 0; slotPos < slots.length; slotPos++) {
    const idx = slots[slotPos];
    const item = items[idx];
    if (!item) continue;
    const statsText = Object.entries(item.stats).map(([k, v]) => `${k}: +${v}`).join(', ');
    html += `
    <div style="background:#ede0c4; border:1px solid #8b6020; padding:1rem; width:180px; border-radius:2px;">
      <div style="font-family:'Cinzel',serif; font-weight:600; margin-bottom:0.4rem;">${item.name}</div>
      <div style="font-size:0.8rem; color:#6a4818; margin-bottom:0.4rem;">${item.type}</div>
      <div style="font-size:0.8rem; margin-bottom:0.6rem;">${statsText || 'Brak statów'}</div>
      <div style="font-family:'Cinzel',serif; font-size:0.85rem; margin-bottom:0.6rem;">${item.price} złota</div>
      <button onclick="buyItem(${item.templateId}, ${item.price}, ${item.itemSeed}, ${slotPos})">Kup</button>
    </div>`;
  }

  html += `</div>`;
  return html;
}

async function buyItem(templateId, price, itemSeed, slotPos) {
  const accountId = localStorage.getItem("accountId");
  const character = await get(`/api/character/${accountId}`);
  const result = await post(`/api/shop/buy?characterId=${character.id}&templateId=${templateId}&price=${price}&itemSeed=${itemSeed}`);

  if (result.error) {
    alert(result.error);
  } else {
    const today = new Date().toISOString().split('T')[0];
    const slotKey = `slots_${character.id}_${today}`;
    const slots = JSON.parse(localStorage.getItem(slotKey) || '[0,1,2,3,4,5]');
    const nextIndex = Math.max(...slots) + 1;
    slots[slotPos] = nextIndex;
    localStorage.setItem(slotKey, JSON.stringify(slots));
    await render('shop');
    await loadHUD();
  }
}
