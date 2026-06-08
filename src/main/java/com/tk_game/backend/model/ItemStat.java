package com.tk_game.backend.model;

import com.tk_game.backend.model.ItemTemplate;
import com.tk_game.backend.model.ItemInstance;
import com.tk_game.backend.enume.StatType;
import jakarta.persistence.*;

@Entity
public class ItemStat {
  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne
  private ItemInstance itemInstance;

  @Enumerated(EnumType.STRING)
  private StatType type;

  private int value;
}
