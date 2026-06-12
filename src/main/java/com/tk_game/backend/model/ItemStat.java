package com.tk_game.backend.model;

import com.tk_game.backend.enume.StatType;
import jakarta.persistence.*;

@Entity
public class ItemStat {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private ItemInstance itemInstance;

  @Enumerated(EnumType.STRING)
  private StatType type;

  private int value;

  public Long getId() { return id; }

  public ItemInstance getItemInstance() { return itemInstance; }
  public void setItemInstance(ItemInstance itemInstance) { this.itemInstance = itemInstance; }

  public StatType getType() { return type; }
  public void setType(StatType type) { this.type = type; }

  public int getValue() { return value; }
  public void setValue(int value) { this.value = value; }
}
