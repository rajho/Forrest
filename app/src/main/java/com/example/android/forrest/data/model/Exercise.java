package com.example.android.forrest.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Exercise {

  @PrimaryKey
  @NonNull
  private final String id;

  /**
   * Duration in milliseconds
   */
  @NonNull
  private final Long duration;

  /**
   * Distance in kilometers
   */
  @NonNull
  private final Double distance;

  @ColumnInfo(name = "calories_burnt")
  @NonNull
  private final Double caloriesBurnt;

  @ColumnInfo(name = "user_id")
  @NonNull
  private final String userId;

  public Exercise(@NonNull String id,
      @NonNull Long duration,
      @NonNull Double distance,
      @NonNull Double caloriesBurnt, @NonNull String userId) {
    this.id            = id;
    this.duration      = duration;
    this.distance      = distance;
    this.caloriesBurnt = caloriesBurnt;
    this.userId        = userId;
  }

  @NonNull
  public String getId() {
    return id;
  }

  @NonNull
  public Long getDuration() {
    return duration;
  }

  @NonNull
  public Double getDistance() {
    return distance;
  }

  @NonNull
  public Double getCaloriesBurnt() {
    return caloriesBurnt;
  }

  @NonNull
  public String getUserId() {
    return userId;
  }
}
