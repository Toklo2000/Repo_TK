-- HELMET
INSERT INTO item_template (id, name, sprite, type) VALUES (1, 'Leather Hood', 'leatherHood.png', 'HELMET') ON CONFLICT (id) DO NOTHING;
INSERT INTO item_template (id, name, sprite, type) VALUES (2, 'Soldier Casque', 'soldierCasque.png', 'HELMET') ON CONFLICT (id) DO NOTHING;
INSERT INTO item_template (id, name, sprite, type) VALUES (3, 'Epic Royal Crown', 'epicRoyalCrown.png', 'HELMET') ON CONFLICT (id) DO NOTHING;

-- ARMOR
INSERT INTO item_template (id, name, sprite, type) VALUES (4, 'Warrior Leather Chest', 'warriorLeatherChest.png', 'ARMOR') ON CONFLICT (id) DO NOTHING;
INSERT INTO item_template (id, name, sprite, type) VALUES (5, 'Scale Mail Chest', 'scaleMailChest.png', 'ARMOR') ON CONFLICT (id) DO NOTHING;
INSERT INTO item_template (id, name, sprite, type) VALUES (6, 'Epic Plate Armor', 'epicPlateArmor.png', 'ARMOR') ON CONFLICT (id) DO NOTHING;

-- GLOVES
INSERT INTO item_template (id, name, sprite, type) VALUES (7, 'Old Leather Mitts', 'oldLeatherMitts.png', 'GLOVES') ON CONFLICT (id) DO NOTHING;
INSERT INTO item_template (id, name, sprite, type) VALUES (8, 'Studded Vambraces', 'studdedVambraces.png', 'GLOVES') ON CONFLICT (id) DO NOTHING;
INSERT INTO item_template (id, name, sprite, type) VALUES (9, 'Epic Shadow Grips', 'epicShadowGrips.png', 'GLOVES') ON CONFLICT (id) DO NOTHING;

-- BOOTS
INSERT INTO item_template (id, name, sprite, type) VALUES (10, 'Worn Sandals', 'wornSandals.png', 'BOOTS') ON CONFLICT (id) DO NOTHING;
INSERT INTO item_template (id, name, sprite, type) VALUES (11, 'Heavy Leather Boots', 'heavyLeatherBoots.png', 'BOOTS') ON CONFLICT (id) DO NOTHING;
INSERT INTO item_template (id, name, sprite, type) VALUES (12, 'Epic Swift Boots', 'epicSwiftBoots.png', 'BOOTS') ON CONFLICT (id) DO NOTHING;

-- WEAPON
INSERT INTO item_template (id, name, sprite, type) VALUES (13, 'Rusty Bronze Sword', 'rustyBronzeSword.png', 'WEAPON') ON CONFLICT (id) DO NOTHING;
INSERT INTO item_template (id, name, sprite, type) VALUES (14, 'Iron Broadsword', 'ironBroadsword.png', 'WEAPON') ON CONFLICT (id) DO NOTHING;
INSERT INTO item_template (id, name, sprite, type) VALUES (15, 'Epic Doomblade', 'epicDoomblade.png', 'WEAPON') ON CONFLICT (id) DO NOTHING;

-- NECKLACE
INSERT INTO item_template (id, name, sprite, type) VALUES (16, 'Bone Necklace', 'boneNecklace.png', 'NECKLACE') ON CONFLICT (id) DO NOTHING;
INSERT INTO item_template (id, name, sprite, type) VALUES (17, 'Golden Choker', 'goldenChoker.png', 'NECKLACE') ON CONFLICT (id) DO NOTHING;
INSERT INTO item_template (id, name, sprite, type) VALUES (18, 'Epic Ruby Choker', 'epicRubyChoker.png', 'NECKLACE') ON CONFLICT (id) DO NOTHING;

-- AMULET
INSERT INTO item_template (id, name, sprite, type) VALUES (19, 'Copper Pendant', 'copperPendant.png', 'AMULET') ON CONFLICT (id) DO NOTHING;
INSERT INTO item_template (id, name, sprite, type) VALUES (20, 'Sapphire Talisman', 'sapphireTalisman.png', 'AMULET') ON CONFLICT (id) DO NOTHING;
INSERT INTO item_template (id, name, sprite, type) VALUES (21, 'Epic Prophet Eye', 'epicProphetEye.png', 'AMULET') ON CONFLICT (id) DO NOTHING;

-- RING
INSERT INTO item_template (id, name, sprite, type) VALUES (22, 'Lead Signet', 'leadSignet.png', 'RING') ON CONFLICT (id) DO NOTHING;
INSERT INTO item_template (id, name, sprite, type) VALUES (23, 'Silver Ring', 'silverRing.png', 'RING') ON CONFLICT (id) DO NOTHING;
INSERT INTO item_template (id, name, sprite, type) VALUES (24, 'Epic Ring of Power', 'epicRingOfPower.png', 'RING') ON CONFLICT (id) DO NOTHING;

-- BELT
INSERT INTO item_template (id, name, sprite, type) VALUES (25, 'Rope Belt', 'ropeBelt.png', 'BELT') ON CONFLICT (id) DO NOTHING;
INSERT INTO item_template (id, name, sprite, type) VALUES (26, 'Military Leather Belt', 'militaryLeatherBelt.png', 'BELT') ON CONFLICT (id) DO NOTHING;
INSERT INTO item_template (id, name, sprite, type) VALUES (27, 'Epic Titan Girdle', 'epicTitanGirdle.png', 'BELT') ON CONFLICT (id) DO NOTHING;

-- ENEMIES
INSERT INTO enemy_template (id, name, sprite, type, base_hp, base_dmg_min, base_dmg_max, base_str, base_dex, base_int, base_con, base_lck) VALUES (1, 'Goblin', 'goblin.png', 'BEAST', 20, 3, 6, 2, 3, 1, 2, 1) ON CONFLICT (id) DO NOTHING;
INSERT INTO enemy_template (id, name, sprite, type, base_hp, base_dmg_min, base_dmg_max, base_str, base_dex, base_int, base_con, base_lck) VALUES (2, 'Wolf', 'wolf.png', 'BEAST', 25, 4, 8, 3, 4, 1, 3, 1) ON CONFLICT (id) DO NOTHING;
INSERT INTO enemy_template (id, name, sprite, type, base_hp, base_dmg_min, base_dmg_max, base_str, base_dex, base_int, base_con, base_lck) VALUES (3, 'Bandit', 'bandit.png', 'HUMAN', 30, 5, 10, 4, 3, 2, 3, 2) ON CONFLICT (id) DO NOTHING;
INSERT INTO enemy_template (id, name, sprite, type, base_hp, base_dmg_min, base_dmg_max, base_str, base_dex, base_int, base_con, base_lck) VALUES (4, 'Skeleton', 'skeleton.png', 'UNDEAD', 22, 4, 7, 3, 2, 1, 2, 1) ON CONFLICT (id) DO NOTHING;
INSERT INTO enemy_template (id, name, sprite, type, base_hp, base_dmg_min, base_dmg_max, base_str, base_dex, base_int, base_con, base_lck) VALUES (5, 'Troll', 'troll.png', 'BEAST', 60, 8, 15, 7, 2, 1, 6, 1) ON CONFLICT (id) DO NOTHING;
INSERT INTO enemy_template (id, name, sprite, type, base_hp, base_dmg_min, base_dmg_max, base_str, base_dex, base_int, base_con, base_lck) VALUES (6, 'Vampire', 'vampire.png', 'UNDEAD', 35, 6, 12, 5, 5, 4, 4, 3) ON CONFLICT (id) DO NOTHING;
INSERT INTO enemy_template (id, name, sprite, type, base_hp, base_dmg_min, base_dmg_max, base_str, base_dex, base_int, base_con, base_lck) VALUES (7, 'Lich', 'lich.png', 'UNDEAD', 40, 8, 16, 3, 2, 8, 4, 2) ON CONFLICT (id) DO NOTHING;
INSERT INTO enemy_template (id, name, sprite, type, base_hp, base_dmg_min, base_dmg_max, base_str, base_dex, base_int, base_con, base_lck) VALUES (8, 'Wyvern', 'wyvern.png', 'BEAST', 50, 10, 18, 7, 6, 3, 5, 2) ON CONFLICT (id) DO NOTHING;
INSERT INTO enemy_template (id, name, sprite, type, base_hp, base_dmg_min, base_dmg_max, base_str, base_dex, base_int, base_con, base_lck) VALUES (9, 'Hobgoblin', 'hobgoblin.png', 'BEAST', 28, 5, 9, 4, 3, 2, 3, 2) ON CONFLICT (id) DO NOTHING;
INSERT INTO enemy_template (id, name, sprite, type, base_hp, base_dmg_min, base_dmg_max, base_str, base_dex, base_int, base_con, base_lck) VALUES (10, 'Rat', 'rat.png', 'BEAST', 10, 1, 3, 1, 5, 1, 1, 4) ON CONFLICT (id) DO NOTHING;
