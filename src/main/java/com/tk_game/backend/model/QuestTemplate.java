package com.tk_game.backend.model;
import jakarta.persistence.*;

@Entity
public class QuestTemplate {
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String description;
  private int level;
  private int expReward;
  private int goldReward;
  private int duration;

  public Long getId() { return id; }
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }
  public int getLevel() { return level; }
  public void setLevel(int level) { this.level = level; }
  public int getExpReward() { return expReward; }
  public void setExpReward(int expReward) { this.expReward = expReward; }
  public int getGoldReward() { return goldReward; }
  public void setGoldReward(int goldReward) { this.goldReward = goldReward; }
  public int getDuration() { return duration; }
  public void setDuration(int duration) { this.duration = duration; }
}
