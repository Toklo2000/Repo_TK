package com.tk_game.backend.model;

import jakarta.persistence.*;

@Entity
public class QuestTemplate {
  @Id
  @GeneratedValue
  private Long id;

  private String name;
  private String description;

  private String enemyType; 

  private int baseExp;
  private int baseGold;

  private int duration; //w sekundach
}
