function renderTavern() {
  setTimeout(loadTavernData, 0);
  return `
    <h2>Karczma</h2>
    <hr>
    <div id="tavern-active" style="display:none">
      <h3 id="tavern-mission-name"></h3>
      <p id="tavern-mission-desc" style="margin-bottom:0.5rem; font-style:italic"></p>
      <p id="tavern-timer" style="font-size:1.2rem; color:#6a4818; margin-bottom:1rem"></p>
      <div id="tavern-battle-area"></div>
    </div>
    <div id="tavern-offers" style="display:flex; gap:1rem; flex-wrap:wrap"></div>
    <p id="tavern-status" style="margin-top:1rem; font-style:italic; color:#6a4818"></p>
  `;
}

let tavernTimerInterval = null;
let tavernActiveMission = null;
let tavernCompleting = false;
let battleStarted = false;

async function loadTavernData() {
  const activeRes = await fetch('/api/tavern/mission/active', { credentials: 'same-origin' });
  if (activeRes.ok) {
    tavernActiveMission = await activeRes.json();
    const remaining = tavernActiveMission.endTime - Date.now();
    if (remaining > 0) battleStarted = false;
    showTavernActive();
  } else {
    tavernActiveMission = null;
    battleStarted = false;
    const offersRes = await fetch('/api/tavern/offers', { credentials: 'same-origin' });
    showTavernOffers(await offersRes.json());
  }
}

function showTavernActive() {
  document.getElementById('tavern-active').style.display = 'block';
  document.getElementById('tavern-offers').innerHTML = '';
  document.getElementById('tavern-status').textContent = '';
  document.getElementById('tavern-battle-area').innerHTML = '';
  document.getElementById('tavern-mission-name').textContent = tavernActiveMission.questTemplate.name;
  document.getElementById('tavern-mission-desc').textContent = tavernActiveMission.questTemplate.description;
  if (tavernTimerInterval) clearInterval(tavernTimerInterval);

  const saved = sessionStorage.getItem('battleResult');
  const remaining = tavernActiveMission.endTime - Date.now();

  if (saved) {
    const result = JSON.parse(saved);
    result.skipAnimation = true;
    document.getElementById('tavern-timer').textContent = 'Czas minął! Trwa walka...';
    battleStarted = true;
    renderBattleResult(result);
  } else if (remaining <= 0) {
    document.getElementById('tavern-timer').textContent = 'Czas minął! Trwa walka...';
    if (!battleStarted) {
      battleStarted = true;
      startQuestBattle();
    }
  } else {
    battleStarted = false;
    updateTavernTimer();
    tavernTimerInterval = setInterval(updateTavernTimer, 1000);
  }
}

function updateTavernTimer() {
  const remaining = tavernActiveMission.endTime - Date.now();
  const timerEl = document.getElementById('tavern-timer');
  if (!timerEl) { clearInterval(tavernTimerInterval); return; }
  if (remaining <= 0) {
    timerEl.textContent = 'Czas minął! Trwa walka...';
    clearInterval(tavernTimerInterval);
    if (!battleStarted) {
      battleStarted = true;
      startQuestBattle();
    }
  } else {
    timerEl.textContent = `Pozostało: ${Math.floor(remaining / 1000)}s`;
  }
}

async function startQuestBattle() {
  const accountId = localStorage.getItem("accountId");
  const character = await get(`/api/character/${accountId}`);
  const battleArea = document.getElementById('tavern-battle-area');

  const result = await post(`/api/battle/run-for-quest?characterId=${character.id}`);
  sessionStorage.setItem('battleResult', JSON.stringify(result));

  renderBattleResult(result);
}

async function renderBattleResult(result) {
  const battleArea = document.getElementById('tavern-battle-area');
  if (!battleArea) return;

  let playerHp = result.maxPlayerHp;
  let enemyHp = result.maxEnemyHp;

  battleArea.innerHTML = `
    <h3 style="margin-bottom:0.8rem">Walka z: ${result.enemyName}</h3>
    <div style="display:flex; gap:2rem; margin-bottom:1rem; align-items:center;">
      <div style="flex:1">
        <div style="font-family:'Cinzel',serif; font-size:0.8rem; margin-bottom:0.3rem;">${result.playerName}</div>
        <div style="background:#c8a060; border-radius:3px; height:18px; position:relative;">
          <div id="player-hp-bar" style="background:#4a7a20; height:100%; border-radius:3px; width:100%; transition:width 0.3s;"></div>
        </div>
        <div id="player-hp-text" style="font-size:0.75rem; margin-top:0.2rem;">${playerHp} / ${playerHp}</div>
      </div>
      <div style="font-family:'Cinzel',serif; font-size:1rem; color:#8b6020;">VS</div>
      <div style="flex:1">
        <div style="font-family:'Cinzel',serif; font-size:0.8rem; margin-bottom:0.3rem;">${result.enemyName}</div>
        <div style="background:#c8a060; border-radius:3px; height:18px; position:relative;">
          <div id="enemy-hp-bar" style="background:#b8340a; height:100%; border-radius:3px; width:100%; transition:width 0.3s;"></div>
        </div>
        <div id="enemy-hp-text" style="font-size:0.75rem; margin-top:0.2rem;">${enemyHp} / ${enemyHp}</div>
      </div>
    </div>
    <div id="battle-lines" style="background:#ede0c4; border:1px solid #8b6020; padding:1rem; margin-top:0.5rem; font-size:0.85rem; line-height:1.8; max-height:250px; overflow-y:auto;"></div>
    <div id="battle-result" style="margin-top:0.8rem;"></div>
  `;

  const linesDiv = document.getElementById('battle-lines');
  const resultDiv = document.getElementById('battle-result');
  const skipAnimation = result.skipAnimation;

  for (let i = 0; i < result.log.length; i++) {
    if (!skipAnimation) await delay(400);
    const line = document.createElement('div');
    line.textContent = result.log[i];
    if (result.log[i].includes('CRIT')) line.style.color = '#b8340a';
    else if (result.log[i].includes('uniknął')) line.style.color = '#4a7a20';
    else if (result.log[i].includes('Runda')) line.style.fontWeight = '600';

    const playerHpMatch = result.log[i].match(/HP gracza: (\d+)/);
    const enemyHpMatch = result.log[i].match(/HP wroga: (\d+)/);
    if (playerHpMatch) {
      playerHp = parseInt(playerHpMatch[1]);
      const pct = Math.max(0, playerHp / result.maxPlayerHp * 100);
      document.getElementById('player-hp-bar').style.width = pct + '%';
      document.getElementById('player-hp-text').textContent = playerHp + ' / ' + result.maxPlayerHp;
    }
    if (enemyHpMatch) {
      enemyHp = parseInt(enemyHpMatch[1]);
      const pct = Math.max(0, enemyHp / result.maxEnemyHp * 100);
      document.getElementById('enemy-hp-bar').style.width = pct + '%';
      document.getElementById('enemy-hp-text').textContent = enemyHp + ' / ' + result.maxEnemyHp;
    }
    linesDiv.appendChild(line);
    linesDiv.scrollTop = linesDiv.scrollHeight;
  }

  await delay(skipAnimation ? 0 : 600);
  const won = result.won;
  resultDiv.innerHTML = `
    <strong style="font-size:1.1rem;">${won ? 'Zwycięstwo!' : 'Przegrana!'}</strong>
    <br><br>
    <button onclick="tavernComplete(${won})">Zakończ misję</button>
  `;
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
  if (tavernCompleting) return;
  tavernCompleting = true;
  battleStarted = false;
  sessionStorage.removeItem('battleResult');
  await fetch(`/api/tavern/complete?success=${success}`, { method: 'POST', credentials: 'same-origin' });
  tavernActiveMission = null;
  clearInterval(tavernTimerInterval);
  const statusEl = document.getElementById('tavern-status');
  if (statusEl) statusEl.textContent = success ? 'Misja zakończona sukcesem!' : 'Misja nieudana.';
  const offersRes = await fetch('/api/tavern/offers', { credentials: 'same-origin' });
  showTavernOffers(await offersRes.json());
  await loadHUD();
  tavernCompleting = false;
}

function delay(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}
