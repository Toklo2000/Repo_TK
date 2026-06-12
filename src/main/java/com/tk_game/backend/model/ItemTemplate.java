package com.tk_game.backend.model;

import com.tk_game.backend.enume.EquipmentSlot;
import jakarta.persistence.*;

@Entity
public class ItemTemplate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String sprite;

  @Enumerated(EnumType.STRING)
  private EquipmentSlot type;

  public Long getId() { return id; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getSprite() { return sprite; }
  public void setSprite(String sprite) { this.sprite = sprite; }

  public EquipmentSlot getType() { return type; }
  public void setType(EquipmentSlot type) { this.type = type; }
}
