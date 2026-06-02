package com.tk_game.backend.model;

import com.tk_game.backend.model.ItemTemplate;
import jakarta.persistence.*;

@Entity
public class ItemInstance {
  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne
  private ItemTemplate template;
}
