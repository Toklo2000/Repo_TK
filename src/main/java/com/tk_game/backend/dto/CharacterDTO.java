package com.tk_game.backend.dto;

import java.util.List;
import java.util.Map;

public class CharacterDTO {
  public Long id;
  public String name;

  public int level;
  public int exp;
  public int gold;

  public int strength;
  public int dexterity;
  public int intelligence;
  public int constitution;
  public int luck;

  public Map<String, Integer> totalStats; 

  public List<Map<String, Object>> items;
}
