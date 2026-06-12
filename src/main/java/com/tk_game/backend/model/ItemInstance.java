package com.tk_game.backend.model;

import com.tk_game.backend.model.Character;
import com.tk_game.backend.enume.EquipmentSlot;
import jakarta.persistence.*;

@Entity
public class ItemInstance {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private ItemTemplate template;

  @ManyToOne
  private Character owner;

  @Enumerated(EnumType.STRING)
  private EquipmentSlot slot = EquipmentSlot.UNEQUIPPED;

  public Long getId() { return id; }

  public ItemTemplate getTemplate() { return template; }
  public void setTemplate(ItemTemplate template) { this.template = template; }

  public Character getOwner() { return owner; }
  public void setOwner(Character owner) { this.owner = owner; }

  public EquipmentSlot getSlot() { return slot; }
  public void setSlot(EquipmentSlot slot) { this.slot = slot; }
}
