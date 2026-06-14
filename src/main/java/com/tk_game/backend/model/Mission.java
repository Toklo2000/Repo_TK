package com.tk_game.backend.model;
import jakarta.persistence.*;

@Entity
public class Mission {
  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne
  @JoinColumn(name = "character_id")
  private Character character;

  @ManyToOne
  @JoinColumn(name = "quest_template_id")
  private QuestTemplate questTemplate;

  private long startTime;
  private long endTime;
  private boolean completed;
  private boolean success;

  public Long getId() { return id; }
  public Character getCharacter() { return character; }
  public void setCharacter(Character character) { this.character = character; }
  public QuestTemplate getQuestTemplate() { return questTemplate; }
  public void setQuestTemplate(QuestTemplate questTemplate) { this.questTemplate = questTemplate; }
  public long getStartTime() { return startTime; }
  public void setStartTime(long startTime) { this.startTime = startTime; }
  public long getEndTime() { return endTime; }
  public void setEndTime(long endTime) { this.endTime = endTime; }
  public boolean isCompleted() { return completed; }
  public void setCompleted(boolean completed) { this.completed = completed; }
  public boolean isSuccess() { return success; }
  public void setSuccess(boolean success) { this.success = success; }
}
