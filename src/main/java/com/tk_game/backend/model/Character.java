package com.tk_game.backend.model;

import com.tk_game.backend.model.Account;

import jakarta.persistence.*;

@Entity
@Table(name = "characters")
public class Character {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  private Account account;

  private String name;

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
  public int getExp() { return exp; }

  public int getGold() { return gold; }
  public int getEnergy() { return energy; }
}
