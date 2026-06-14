function renderCharacter(c) {
  if (!c.itemsLoaded && !c.isLoadingItems) {
    c.isLoadingItems = true; 

    fetch(`/api/items/${c.id}`)
      .then(res => {
        if (!res.ok) throw new Error("Fetch failed");
        return res.json();
      })
      .then(items => {
        c.itemsLoaded = true;
        c.isLoadingItems = false;
        c.equippedItems = items.filter(i => i.slot !== 'UNEQUIPPED');
        c.inventoryItems = items.filter(i => i.slot === 'UNEQUIPPED');
        
        if (typeof router === 'function') {
          router();
        } else if (typeof renderApp === 'function') {
          renderApp(); 
        } else {
          const contentDiv = document.getElementById('content') || document.getElementById('app') || document.body;
          if (contentDiv) {
            contentDiv.innerHTML = renderCharacter(c);
          }
        }
      })
      .catch(err => {
        c.isLoadingItems = false;
        console.error("Error loading items:", err);
      });
  }

  window.showItemTooltip = function(e, name, type, statsStr) {
    let tooltip = document.getElementById("item-tooltip-box");
    if (!tooltip) {
      tooltip = document.createElement("div");
      tooltip.id = "item-tooltip-box";
      tooltip.style.position = "absolute";
      tooltip.style.background = "rgba(20, 14, 8, 0.96)";
      tooltip.style.border = "2px solid #8b6020";
      tooltip.style.color = "#f5e6c8";
      tooltip.style.padding = "10px";
      tooltip.style.fontFamily = "'Times New Roman', Georgia, serif";
      tooltip.style.fontSize = "0.85rem";
      tooltip.style.pointerEvents = "none";
      tooltip.style.zIndex = "9999";
      tooltip.style.boxShadow = "3px 3px 8px rgba(0,0,0,0.7)";
      document.body.appendChild(tooltip);
    }

    let statsObj = JSON.parse(decodeURIComponent(statsStr));
    let statsHtml = "";
    for (let [statName, val] of Object.entries(statsObj)) {
      if (val > 0) {
        statsHtml += `<div style="color: #5cb85c; margin-top: 3px;">+${val} ${statName}</div>`;
      }
    }
    if (!statsHtml) statsHtml = `<div style="color: #bfa38a; font-style: italic; margin-top: 3px;">No attributes</div>`;

    tooltip.innerHTML = `
      <div style="font-weight: bold; font-size: 0.95rem; color: #fff; border-bottom: 1px solid #8b6020; padding-bottom: 2px; margin-bottom: 4px;">${name}</div>
      <div style="color: #a07446; font-size: 0.75rem; text-transform: uppercase;">Slot: ${type}</div>
      ${statsHtml}
    `;
    tooltip.style.display = "block";
    window.moveItemTooltip(e);
  };

  window.moveItemTooltip = function(e) {
    let tooltip = document.getElementById("item-tooltip-box");
    if (tooltip) {
      tooltip.style.left = (e.pageX + 12) + "px";
      tooltip.style.top = (e.pageY + 12) + "px";
    }
  };

  window.hideItemTooltip = function() {
    let tooltip = document.getElementById("item-tooltip-box");
    if (tooltip) tooltip.style.display = "none";
  };

  const equipped = c.equippedItems || [];
  const inventory = c.inventoryItems || [];

  const equipmentSlots = [
    { key: 'HELMET', label: 'Helmet' },
    { key: 'NECKLACE', label: 'Necklace' },
    { key: 'ARMOR', label: 'Armor' },
    { key: 'BELT', label: 'Belt' },
    { key: 'BOOTS', label: 'Boots' },
    { key: 'GLOVES', label: 'Gloves' },
    { key: 'RING', label: 'Ring' },
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
          <span onclick="hideItemTooltip(); unequipItemRequest(${item.id})" style="cursor: pointer; font-weight: bold; font-size: 0.75rem; text-decoration: underline; color: #5a3510;">
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

  const formatStatValue = (statKey, baseValue) => {
    let total = (c.totalStats && c.totalStats[statKey.toUpperCase()]) ? c.totalStats[statKey.toUpperCase()] : baseValue;
    let bonus = total - baseValue;
    return `${total} (${baseValue}) +${bonus}`;
  };

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
          
          ${!c.itemsLoaded ? '<div style="color: #7a5828; font-size: 0.8rem; text-align: center; margin-top: 20px;">Loading items...</div>' : 
            (inventory.length === 0 ? '<div style="color: #bfa38a; font-style: italic; font-size: 0.8rem; text-align: center; margin-top: 20px;">Backpack is empty</div>' : inventory.map(item => {
              let statsEncoded = encodeURIComponent(JSON.stringify(item.stats || {}));
              return `
                <div onmouseenter="showItemTooltip(event, '${item.name}', '${item.type || 'Item'}', '${statsEncoded}')"
                     onmousemove="moveItemTooltip(event)"
                     onmouseleave="hideItemTooltip()"
                     style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 6px; padding: 4px; border-bottom: 1px solid #e8d5b0; font-size: 0.85rem;">
                  <span style="font-weight: 600; color: #2b1d0e;">${item.name}</span>
                  <button onclick="hideItemTooltip(); equipItemRequest(${item.id})" style="background: #8b6020; color: #fff; border: none; padding: 2px 6px; font-size: 0.75rem; cursor: pointer;">Equip</button>
                </div>
              `;
            }).join(''))}
        </div>

      </div>

      <div style="box-sizing: border-box; border-top: 2px solid #8b6020; padding-top: 20px; display: grid; grid-template-columns: repeat(5, 1fr); gap: 12px; text-align: center;">
        <div style="background: #fff8eb; padding: 8px; border: 1px solid #e8d5b0;">
          <div style="font-size: 0.75rem; color: #7a5828; font-weight: bold;">STR</div>
          <div style="font-size: 0.95rem; margin: 4px 0; font-weight: bold; color: #4a2c08;">${formatStatValue('strength', c.strength)}</div>
          <button onclick="upgrade('strength')" style="width: 100%; cursor: pointer; background: #8b6020; color: white; border: none; padding: 2px 0;">+</button>
        </div>
        <div style="background: #fff8eb; padding: 8px; border: 1px solid #e8d5b0;">
          <div style="font-size: 0.75rem; color: #7a5828; font-weight: bold;">DEX</div>
          <div style="font-size: 0.95rem; margin: 4px 0; font-weight: bold; color: #4a2c08;">${formatStatValue('dexterity', c.dexterity)}</div>
          <button onclick="upgrade('dexterity')" style="width: 100%; cursor: pointer; background: #8b6020; color: white; border: none; padding: 2px 0;">+</button>
        </div>
        <div style="background: #fff8eb; padding: 8px; border: 1px solid #e8d5b0;">
          <div style="font-size: 0.75rem; color: #7a5828; font-weight: bold;">INT</div>
          <div style="font-size: 0.95rem; margin: 4px 0; font-weight: bold; color: #4a2c08;">${formatStatValue('intelligence', c.intelligence)}</div>
          <button onclick="upgrade('intelligence')" style="width: 100%; cursor: pointer; background: #8b6020; color: white; border: none; padding: 2px 0;">+</button>
        </div>
        <div style="background: #fff8eb; padding: 8px; border: 1px solid #e8d5b0;">
          <div style="font-size: 0.75rem; color: #7a5828; font-weight: bold;">CON</div>
          <div style="font-size: 0.95rem; margin: 4px 0; font-weight: bold; color: #4a2c08;">${formatStatValue('constitution', c.constitution)}</div>
          <button onclick="upgrade('constitution')" style="width: 100%; cursor: pointer; background: #8b6020; color: white; border: none; padding: 2px 0;">+</button>
        </div>
        <div style="background: #fff8eb; padding: 8px; border: 1px solid #e8d5b0;">
          <div style="font-size: 0.75rem; color: #7a5828; font-weight: bold;">LCK</div>
          <div style="font-size: 0.95rem; margin: 4px 0; font-weight: bold; color: #4a2c08;">${formatStatValue('luck', c.luck)}</div>
          <button onclick="upgrade('luck')" style="width: 100%; cursor: pointer; background: #8b6020; color: white; border: none; padding: 2px 0;">+</button>
        </div>
      </div>

    </div>
  `;
}

function equipItemRequest(itemId) {
  fetch(`/api/items/equip?itemId=${itemId}`, { method: 'POST' })
    .then(() => {
      if (typeof character !== 'undefined') character.itemsLoaded = false;
      if (typeof currentCharacter !== 'undefined') currentCharacter.itemsLoaded = false;
      
      if (typeof router === 'function') router();
      else if (typeof renderApp === 'function') renderApp();
      else location.reload();
    });
}

function unequipItemRequest(itemId) {
  fetch(`/api/items/unequip?itemId=${itemId}`, { method: 'POST' })
    .then(() => {
      if (typeof character !== 'undefined') character.itemsLoaded = false;
      if (typeof currentCharacter !== 'undefined') currentCharacter.itemsLoaded = false;

      if (typeof router === 'function') router();
      else if (typeof renderApp === 'function') renderApp();
      else location.reload();
    });
}
