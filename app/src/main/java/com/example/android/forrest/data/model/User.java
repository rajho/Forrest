package com.example.android.forrest.data.model;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import javax.annotation.Nullable;

@Entity
public class User {

  @PrimaryKey
  @NonNull
  private final String id;

  @Nullable
  private final String username;

  @Nullable
  private final Integer height;

  @Nullable
  private final Float weight;

  @Nullable
  @ColumnInfo(name = "goal_frequency")
  private String goalFrequency;

  @Nullable
  @ColumnInfo(name = "goal_units")
  private String goalUnits;

  @Nullable
  @ColumnInfo(name = "goal_value")
  private Double goalValue; // milliseconds or kilometers

  @Ignore
  public User(@NonNull String id,
      @Nullable String username,
      @Nullable Integer height,
      @Nullable Float weight) {
    this(id, username, height, weight, null, null, null);
  }

  public User(@NonNull String id,
      @Nullable String username,
      @Nullable Integer height,
      @Nullable Float weight,
      @Nullable String goalFrequency,
      @Nullable String goalUnits,
      @Nullable Double goalValue) {
    this.id            = id;
    this.username      = username;
    this.height        = height;
    this.weight        = weight;
    this.goalFrequency = goalFrequency;
    this.goalUnits     = goalUnits;
    this.goalValue     = goalValue;
  }

  @NonNull
  public String getId() {
    return id;
  }

  @Nullable
  public String getUsername() {
    return username;
  }

  @Nullable
  public Integer getHeight() {
    return height;
  }

  @Nullable
  public Float getWeight() {
    return weight;
  }

  @Nullable
  public String getGoalFrequency() {
    return goalFrequency;
  }

  @Nullable
  public String getGoalUnits() {
    return goalUnits;
  }

  @Nullable
  public Double getGoalValue() {
    return goalValue;
  }

  public void setGoalFrequency(@Nullable String goalFrequency) {
    this.goalFrequency = goalFrequency;
  }

  public void setGoalUnits(@Nullable String goalUnits) {
    this.goalUnits = goalUnits;
  }

  public void setGoalValue(@Nullable Double goalValue) {
    this.goalValue = goalValue;
  }

}
