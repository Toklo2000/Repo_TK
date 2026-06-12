function renderCharacter(c) {
  return `
    <h2>${c.name}</h2>

    STR ${c.strength} <button onclick="upgrade('strength')">+</button><br>
    DEX ${c.dexterity} <button onclick="upgrade('dexterity')">+</button><br>
    INT ${c.intelligence} <button onclick="upgrade('intelligence')">+</button><br>
    CON ${c.constitution} <button onclick="upgrade('constitution')">+</button><br>
    LCK ${c.luck} <button onclick="upgrade('luck')">+</button><br>

    <hr>
    Gold: ${c.gold}
  `;
}
