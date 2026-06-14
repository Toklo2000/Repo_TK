function renderCharacter(c) {
  
  window.showItemTooltip = function(e, name, type, statsStr) {
    let tooltip = document.getElementById('item-tooltip');
    if (!tooltip) {
      tooltip = document.createElement('div');
      tooltip.id = 'item-tooltip';
      tooltip.style.position = 'absolute';
      tooltip.style.zIndex = '9999';
      tooltip.style.background = '#1a120b';
      tooltip.style.color = '#f5e6c8';
      tooltip.style.border = '2px solid #8b6020';
      tooltip.style.padding = '8px';
      tooltip.style.fontFamily = "'Times New Roman', Georgia, serif";
      tooltip.style.fontSize = '0.85rem';
      tooltip.style.boxShadow = '3px 3px 10px rgba(0,0,0,0.6)';
      tooltip.style.pointerEvents = 'none';
      tooltip.style.minWidth = '140px';
      document.body.appendChild(tooltip);
    }
    
    let statsHtml = '';
    try {
      let stats = JSON.parse(decodeURIComponent(statsStr));
      for (let [sName, sVal] of Object.entries(stats)) {
        if (sVal > 0) {
          statsHtml += `<div style="color: #60a060; margin-top: 2px;">+${sVal} ${sName.toUpperCase()}</div>`;
        }
      }
    } catch(err) {
      console.error(err);
    }

    tooltip.innerHTML = `
      <div style="font-weight: bold; border-bottom: 1px solid #8b6020; padding-bottom: 2px; color: #fff8eb;">${name}</div>
      <div style="font-style: italic; font-size: 0.75rem; color: #a08060; margin-bottom: 4px;">${type}</div>
      ${statsHtml}
    `;
    
    tooltip.style.display = 'block';
    window.moveItemTooltip(e);
  };

  window.moveItemTooltip = function(e) {
    let tooltip = document.getElementById('item-tooltip');
    if (tooltip) {
      tooltip.style.left = (e.pageX + 12) + 'px';
      tooltip.style.top = (e.pageY + 12) + 'px';
    }
  };

  window.hideItemTooltip = function() {
    let tooltip = document.getElementById('item-tooltip');
    if (tooltip) {
      tooltip.style.display = 'none';
    }
  };

  const statMapping = {
    'strength': 'STR',
    'dexterity': 'DEX',
    'intelligence': 'INT',
    'constitution': 'CON',
    'luck': 'LCK'
  };

  c.totalStats = { 
    STR: c.strength || 1, 
    DEX: c.dexterity || 1, 
    INT: c.intelligence || 1, 
    CON: c.constitution || 1, 
    LCK: c.luck || 1 
  };

  if (c.equippedItems) {
    c.equippedItems.forEach(item => {
      if (item.stats) {
        for (let [statName, value] of Object.entries(item.stats)) {
          let key = statName.toUpperCase();
          if (c.totalStats[key] !== undefined) {
            c.totalStats[key] += value;
          }
        }
      }
    });
  }

  const formatStatValue = (statKey, baseValue) => {
    let key = statMapping[statKey.toLowerCase()] || statKey.toUpperCase();
    let total = (c.totalStats && c.totalStats[key] !== undefined) ? c.totalStats[key] : baseValue;
    return `${total} (${baseValue})`;
  };

  if (!c.itemsLoaded && !c.isLoadingItems) {
    c.isLoadingItems = true;
    
    const accountId = c.id || localStorage.getItem("accountId") || 1;

    Promise.all([
      fetch(`/api/items/${accountId}`).then(res => res.json()),
      fetch(`/api/character/${accountId}`).then(res => res.json()).catch(() => ({ error: true }))
    ]).then(([items, updatedChar]) => {
      c.itemsLoaded = true;
      c.isLoadingItems = false;
      
      if (!updatedChar.error) {
        Object.assign(c, updatedChar);
      }
      
      c.equippedItems = items.filter(i => i.slot !== 'UNEQUIPPED');
      c.inventoryItems = items.filter(i => i.slot === 'UNEQUIPPED');
      
      const contentDiv = document.getElementById('content') 
        || document.getElementById('app') || document.body;
      if (contentDiv) {
        contentDiv.innerHTML = renderCharacter(c);
      }
    }).catch(err => {
      console.error("Błąd podczas ładowania profilu postaci:", err);
      c.isLoadingItems = false;
    });

    return `<div style="text-align: center; padding: 50px; font-family: 'Times New Roman'; color: #8b6020; font-size: 1.2rem; background: #f5e6c8; min-height: 100vh;">Loading Character Profile...</div>`;
  }

  const equipped = c.equippedItems || [];
  const inventory = c.inventoryItems || [];

  const equipmentSlots = [
    { key: 'HELMET', label: 'Helmet' },
    { key: 'NECKLACE', label: 'Necklace' },
    { key: 'ARMOR', label: 'Armor' },
    { key: 'BELT', label: 'Belt' },
    { key: 'GLOVES', label: 'Gloves' },
    { key: 'RING', label: 'Ring' },
    { key: 'BOOTS', label: 'Boots' },
    { key: 'AMULET', label: 'Amulet' },
    { key: 'WEAPON', label: 'Weapon' }
  ];

  const drawEquipmentSlot = (s) => {
    const item = equipped.find(i => i.slot === s.key);
    if (item) {
      let statsEncoded = encodeURIComponent(JSON.stringify(item.stats || {}));
      return `
        <div onmouseenter="showItemTooltip(event, '${item.name}', '${s.label}', '${statsEncoded}')" 
             onmousemove="moveItemTooltip(event)" 
             onmouseleave="hideItemTooltip()"
             style="box-sizing: border-box; border: 1px solid #8b6020; padding: 4px; background: #fff8eb; height: 50px; display: flex; flex-direction: column; justify-content: center; align-items: center; position: relative;">
          <span style="font-size: 0.65rem; color: #7a5828; text-transform: uppercase;">${s.label}</span>
          <span style="font-size: 0.75rem; font-weight: bold; text-decoration: underline; color: #5a3510; cursor: pointer;" onclick="hideItemTooltip(); unequipItemRequest(${item.id})">
            ${item.name}
          </span>
        </div>
      `;
    } else {
      return `
        <div style="box-sizing: border-box; border: 1px solid #c8a060; opacity: 0.6; padding: 4px; background: #fff8eb; height: 50px; display: flex; flex-direction: column; justify-content: center; align-items: center;">
          <span style="font-size: 0.65rem; color: #7a5828; text-transform: uppercase;">${s.label}</span>
          <span style="color: #bfa38a; font-size: 0.75rem; font-style: italic;">Empty</span>
        </div>
      `;
    }
  };

  // 4. GENEROWANIE KODU HTML WIDOKU
  return `
    <div style="box-sizing: border-box; max-width: 900px; margin: 0 auto; font-family: 'Times New Roman', Georgia, serif; color: #2b1d0e; background: #f5e6c8; padding: 20px; border: 2px solid #8b6020;">
      <div style="display: grid; grid-template-columns: 240px 180px 1fr; gap: 20px; align-items: start; margin-bottom: 25px;">
        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 6px;">
          ${equipmentSlots.map(s => drawEquipmentSlot(s)).join('')}
        </div>
        <div style="box-sizing: border-box; border: 1px dashed #8b6020; background: #eee3cb; height: 246px; width: 100%; display: flex; align-items: center; justify-content: center;">
          <span style="font-size: 0.8rem; color: #7a5828; font-style: italic;">Character Preview</span>
        </div>
        <div style="box-sizing: border-box; border: 1px solid #8b6020; background: #fff8eb; padding: 12px; height: 246px; overflow-y: auto;">
          <h4 style="margin: 0 0 10px 0; border-bottom: 1px solid #8b6020; padding-bottom: 4px; font-size: 0.85rem; text-transform: uppercase; color: #7a5828;">Inventory</h4>
          ${inventory.length === 0 ? '<div style="color: #bfa38a; font-style: italic; font-size: 0.8rem; text-align: center; margin-top: 20px;">Backpack is empty</div>' : inventory.map(item => {
              let statsEncoded = encodeURIComponent(JSON.stringify(item.stats || {}));
              return `
                <div onmouseenter="showItemTooltip(event, '${item.name}', '${item.type || 'Item'}', '${statsEncoded}')"
                     onmousemove="moveItemTooltip(event)"
                     onmouseleave="hideItemTooltip()"
                     style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 6px; padding: 4px; border-bottom: 1px solid #e8d5b0; font-size: 0.85rem;">
                  <span style="font-weight: 600; color: #2b1d0e;">${item.name}</span>
                  <button onclick="hideItemTooltip(); equipItemRequest(${item.id})" style="background: #8b6020; color: white; border: none; padding: 2px 6px; font-size: 0.75rem; cursor: pointer;">Equip</button>
                </div>
              `;
            }).join('')}
        </div>
      </div>
      <div style="box-sizing: border-box; border-top: 2px solid #8b6020; padding-top: 20px; display: grid; grid-template-columns: repeat(5, 1fr); gap: 12px; text-align: center;">
        <div style="background: #fff8eb; padding: 8px; border: 1px solid #e8d5b0;">
          <div style="font-size: 0.75rem; color: #7a5828; font-weight: bold;">STR</div>
          <div style="font-size: 0.95rem; margin: 4px 0; font-weight: bold; color: #4a2c08;">${formatStatValue('strength', c.strength)}</div>
          <button onclick="upgrade('STR')" style="width: 100%; cursor: pointer; background: #8b6020; color: white; border: none; padding: 2px 0;">+</button>
        </div>
        <div style="background: #fff8eb; padding: 8px; border: 1px solid #e8d5b0;">
          <div style="font-size: 0.75rem; color: #7a5828; font-weight: bold;">DEX</div>
          <div style="font-size: 0.95rem; margin: 4px 0; font-weight: bold; color: #4a2c08;">${formatStatValue('dexterity', c.dexterity)}</div>
          <button onclick="upgrade('DEX')" style="width: 100%; cursor: pointer; background: #8b6020; color: white; border: none; padding: 2px 0;">+</button>
        </div>
        <div style="background: #fff8eb; padding: 8px; border: 1px solid #e8d5b0;">
          <div style="font-size: 0.75rem; color: #7a5828; font-weight: bold;">INT</div>
          <div style="font-size: 0.95rem; margin: 4px 0; font-weight: bold; color: #4a2c08;">${formatStatValue('intelligence', c.intelligence)}</div>
          <button onclick="upgrade('INT')" style="width: 100%; cursor: pointer; background: #8b6020; color: white; border: none; padding: 2px 0;">+</button>
        </div>
        <div style="background: #fff8eb; padding: 8px; border: 1px solid #e8d5b0;">
          <div style="font-size: 0.75rem; color: #7a5828; font-weight: bold;">CON</div>
          <div style="font-size: 0.95rem; margin: 4px 0; font-weight: bold; color: #4a2c08;">${formatStatValue('constitution', c.constitution)}</div>
          <button onclick="upgrade('CON')" style="width: 100%; cursor: pointer; background: #8b6020; color: white; border: none; padding: 2px 0;">+</button>
        </div>
        <div style="background: #fff8eb; padding: 8px; border: 1px solid #e8d5b0;">
          <div style="font-size: 0.75rem; color: #7a5828; font-weight: bold;">LCK</div>
          <div style="font-size: 0.95rem; margin: 4px 0; font-weight: bold; color: #4a2c08;">${formatStatValue('luck', c.luck)}</div>
          <button onclick="upgrade('LCK')" style="width: 100%; cursor: pointer; background: #8b6020; color: white; border: none; padding: 2px 0;">+</button>
        </div>
      </div>
    </div>
  `;
}

function equipItemRequest(itemId) {
  fetch(`/api/items/equip?itemId=${itemId}`, { method: 'POST' })
    .then(() => {
      if (typeof render === 'function') {
        render('character');
      } else {
        location.reload();
      }
    })
    .catch(err => console.error("Error equipping item:", err));
}

function unequipItemRequest(itemId) {
  fetch(`/api/items/unequip?itemId=${itemId}`, { method: 'POST' })
    .then(() => {
      if (typeof render === 'function') {
        render('character');
      } else {
        location.reload();
      }
    })
    .catch(err => console.error("Error unequipping item:", err));
}

function upgrade(statName) {
  const accountId = localStorage.getItem("accountId") || 1; 

  const params = new URLSearchParams();
  params.append('stat', statName);

  fetch(`/api/character/${accountId}/upgrade`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    },
    body: params
  })
  .then(res => {
    if (!res.ok) {
      throw new Error("Brak wystarczającej ilości złota lub błąd serwera");
    }
    return res.json();
  })
  .then(updatedCharacter => {
    console.log("Statystyka ulepszona:", updatedCharacter);
    
    if (typeof loadHUD === 'function') {
      loadHUD();
    }
    
    if (typeof render === 'function') {
      render('character');
    } else {
      location.reload();
    }
  })
  .catch(err => {
    console.error("Błąd podczas ulepszania statystyki:", err);
    alert(err.message);
  });
}
