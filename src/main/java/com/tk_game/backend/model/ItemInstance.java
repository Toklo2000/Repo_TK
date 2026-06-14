package com.tk_game.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tk_game.backend.enume.EquipmentSlot;
import jakarta.persistence.*;

@Entity
public class ItemInstance {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "template_id")
  private ItemTemplate template;

  @ManyToOne
  @JoinColumn(name = "character_id")
  @JsonIgnore
  private Character character;

  @Enumerated(EnumType.STRING)
  private EquipmentSlot slot;

  public Long getId() { return id; }

  public ItemTemplate getTemplate() { return template; }
  public void setTemplate(ItemTemplate template) { this.template = template; }

  public Character getCharacter() { return character; }
  public void setCharacter(Character character) { this.character = character; }

  public EquipmentSlot getSlot() { return slot; }
  public void setSlot(EquipmentSlot slot) { this.slot = slot; }
}
