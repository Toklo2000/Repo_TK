package com.tk_game.backend.model;

import jakarta.persistence.*;

@Entity
public class Mission {
  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne
  private Character character;

  private Long questTemplateId;

  private long startTime;
  private long endTime;

  private boolean completed;
}
