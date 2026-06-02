package com.tk_game.backend.model;

import jakarta.persistence.*;

@Entity
public class ItemTemplate {
  @Id
  @GeneratedValue
  private Long id;

  private String name;
  private String type;
  private String sprite;
}
