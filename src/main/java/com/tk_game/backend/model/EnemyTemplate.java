package com.tk_game.backend.model;

import jakarta.persistence.*;

@Entity
public class EnemyTemplate {
  @Id
  @GeneratedValue
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
}
