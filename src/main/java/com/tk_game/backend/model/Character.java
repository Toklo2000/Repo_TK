package com.tk_game.backend.model;

import com.tk_game.backend.model.Account;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "characters")
public class Character {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "account_id", nullable = false)
  private Account account;
  
  private String name;

  @OneToMany(mappedBy = "character")
  private List<Equipment> equipment;

  private int level = 1;
  private int exp = 0;

  private int gold = 100;
  private int energy = 100;

  private int strength = 1;
  private int dexterity = 1;
  private int intelligence = 1;
  private int constitution = 1; //ex. hp = 5 * lvl * con + con * 10
  private int luck = 1;

  public Long getId() { return id; }

  public Account getAccount() { return account; }
  public void setAccount(Account account) { this.account = account; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public int getLevel() { return level; }
  public void setLevel(int level) { this.level = level; }
  public int getExp() { return exp; }
  public void setExp(int exp) { this.exp = exp; }

  public int getGold() { return gold; }
  public void setGold(int gold) { this.gold = gold; }
  public int getEnergy() { return energy; }
  public void setEnergy(int energy) { this.energy = energy; }

  public int getStrength() { return strength; }
  public void setStrength(int strength) { this.strength = strength; }
  public int getDexterity() { return dexterity; }
  public void setDexterity(int dexterity) { this.dexterity = dexterity; }
  public int getIntelligence() { return intelligence; }
  public void setIntelligence(int intelligence) { this.intelligence = intelligence; }
  public int getConstitution() { return constitution; }
  public void setConstitution(int constitution) { this.constitution = constitution; }
  public int getLuck() { return luck; }
  public void setLuck(int luck) { this.luck = luck; }
}
