function renderTavern() {
  setTimeout(loadTavernData, 0);
  return `
    <h3>Tawerna</h3>
    <hr>
    <div id="tavern-active" style="display:none">
      <h3 id="tavern-mission-name"></h3>
      <p id="tavern-mission-desc" style="margin-bottom:0.5rem; font-style:italic"></p>
      <p id="tavern-timer" style="font-size:1.2rem; color:#6a4818; margin-bottom:1rem"></p>
      <div id="tavern-battle-btns" style="display:none">
        <button onclick="tavernComplete(true)">Wygraj walkę</button>
        <button onclick="tavernComplete(false)">Przegraj walkę</button>
      </div>
    </div>
    <div id="tavern-offers" style="display:flex; gap:1rem; flex-wrap:wrap"></div>
    <p id="tavern-status" style="margin-top:1rem; font-style:italic; color:#6a4818"></p>
  `;
}

let tavernTimerInterval = null;
let tavernActiveMission = null;

async function loadTavernData() {
  const activeRes = await fetch('/api/tavern/mission/active', { credentials: 'same-origin' });
  if (activeRes.ok) {
    tavernActiveMission = await activeRes.json();
    showTavernActive();
  } else {
    const offersRes = await fetch('/api/tavern/offers', { credentials: 'same-origin' });
    const offers = await offersRes.json();
    showTavernOffers(offers);
  }
}

function showTavernActive() {
  document.getElementById('tavern-active').style.display = 'block';
  document.getElementById('tavern-offers').innerHTML = '';
  document.getElementById('tavern-mission-name').textContent = tavernActiveMission.questTemplate.name;
  document.getElementById('tavern-mission-desc').textContent = tavernActiveMission.questTemplate.description;
  if (tavernTimerInterval) clearInterval(tavernTimerInterval);
  updateTavernTimer();
  tavernTimerInterval = setInterval(updateTavernTimer, 1000);
}

function updateTavernTimer() {
  const remaining = tavernActiveMission.endTime - Date.now();
  const timerEl = document.getElementById('tavern-timer');
  const btnsEl = document.getElementById('tavern-battle-btns');
  if (!timerEl) { clearInterval(tavernTimerInterval); return; }
  if (remaining <= 0) {
    timerEl.textContent = 'Czas minął! Możesz stoczyć walkę.';
    btnsEl.style.display = 'block';
    clearInterval(tavernTimerInterval);
  } else {
    timerEl.textContent = `Pozostało: ${Math.floor(remaining / 1000)}s`;
    btnsEl.style.display = 'none';
  }
}

function showTavernOffers(offers) {
  document.getElementById('tavern-active').style.display = 'none';
  const container = document.getElementById('tavern-offers');
  container.innerHTML = '';
  offers.forEach(offer => {
    const qt = offer.mission.questTemplate;
    const div = document.createElement('div');
    div.style.cssText = 'background:#fffbf0; border:1px solid #c8a060; border-bottom:2px solid #8b6020; padding:1rem; width:220px; border-radius:1px';
    div.innerHTML = `
      <strong style="font-family:'Cinzel',serif; font-size:0.85rem; color:#4a2c08">${qt.name}</strong>
      <p style="font-style:italic; font-size:0.85rem; margin:0.5rem 0; color:#5a3c18">${qt.description}</p>
      <p style="font-size:0.8rem">Poziom: ${qt.level}</p>
      <p style="font-size:0.8rem">EXP: ${qt.expReward} | Gold: ${qt.goldReward}</p>
      <p style="font-size:0.8rem; margin-bottom:0.75rem">Czas: ${qt.duration}s</p>
      <button onclick="tavernAccept(${offer.id})">Przyjmij</button>
    `;
    container.appendChild(div);
  });
}

async function tavernAccept(offerId) {
  const res = await fetch(`/api/tavern/accept/${offerId}`, { method: 'POST', credentials: 'same-origin' });
  if (res.ok) {
    tavernActiveMission = await res.json();
    showTavernActive();
  }
}

async function tavernComplete(success) {
  await fetch(`/api/tavern/complete?success=${success}`, { method: 'POST', credentials: 'same-origin' });
  tavernActiveMission = null;
  clearInterval(tavernTimerInterval);
  document.getElementById('tavern-status').textContent = success ? 'Misja zakończona sukcesem!' : 'Misja nieudana.';
  const offersRes = await fetch('/api/tavern/offers', { credentials: 'same-origin' });
  showTavernOffers(await offersRes.json());
  await loadHUD();
}
