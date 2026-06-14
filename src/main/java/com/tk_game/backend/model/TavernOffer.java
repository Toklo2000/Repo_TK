package com.tk_game.backend.model;
import jakarta.persistence.*;

@Entity
@Table(name = "tavern_offers")
public class TavernOffer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "character_id")
  private Character character;

  @ManyToOne
  @JoinColumn(name = "mission_id")
  private Mission mission;

  public Long getId() { return id; }
  public Character getCharacter() { return character; }
  public void setCharacter(Character character) { this.character = character; }
  public Mission getMission() { return mission; }
  public void setMission(Mission mission) { this.mission = mission; }
}
