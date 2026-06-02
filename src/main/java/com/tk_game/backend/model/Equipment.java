package com.tk_game.backend.model;

import jakarta.persistence.*;

@Entity
public class Equipment {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne
  private Character character;

  @ManyToOne
  private ItemInstance item;

  private String slot;
}
