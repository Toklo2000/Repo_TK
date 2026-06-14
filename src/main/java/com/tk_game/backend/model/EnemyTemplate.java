package com.tk_game.backend.model;

import jakarta.persistence.*;

@Entity
public class EnemyTemplate {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String sprite;
  private String type;

  private int baseHp;
  private int baseDmgMin;
  private int baseDmgMax;

  private int baseStr;
  private int baseDex;
  private int baseInt;
  private int baseCon;
  private int baseLck;

  public Long getId() { return id; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getSprite() { return sprite; }
  public void setSprite(String sprite) { this.sprite = sprite; }

  public String getType() { return type; }
  public void setType(String type) { this.type = type; }

  public int getBaseHp() { return baseHp; }
  public void setBaseHp(int baseHp) { this.baseHp = baseHp; }

  public int getBaseDmgMin() { return baseDmgMin; }
  public void setBaseDmgMin(int baseDmgMin) { this.baseDmgMin = baseDmgMin; }

  public int getBaseDmgMax() { return baseDmgMax; }
  public void setBaseDmgMax(int baseDmgMax) { this.baseDmgMax = baseDmgMax; }

  public int getBaseStr() { return baseStr; }
  public void setBaseStr(int baseStr) { this.baseStr = baseStr; }

  public int getBaseDex() { return baseDex; }
  public void setBaseDex(int baseDex) { this.baseDex = baseDex; }

  public int getBaseInt() { return baseInt; }
  public void setBaseInt(int baseInt) { this.baseInt = baseInt; }

  public int getBaseCon() { return baseCon; }
  public void setBaseCon(int baseCon) { this.baseCon = baseCon; }

  public int getBaseLck() { return baseLck; }
  public void setBaseLck(int baseLck) { this.baseLck = baseLck; }
}
