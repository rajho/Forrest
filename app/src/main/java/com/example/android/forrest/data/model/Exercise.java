package com.example.android.forrest.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Exercise implements Parcelable {

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

  @PrimaryKey(autoGenerate = true)
  private Long id;

  @Ignore
  public Exercise(
      @NonNull Long duration,
      @NonNull Double distance,
      @NonNull Double caloriesBurnt,
      @NonNull String userId) {
    this.duration      = duration;
    this.distance      = distance;
    this.caloriesBurnt = caloriesBurnt;
    this.userId        = userId;
  }

  public Exercise(@NonNull Long id,
      @NonNull Long duration,
      @NonNull Double distance,
      @NonNull Double caloriesBurnt, @NonNull String userId) {
    this.id            = id;
    this.duration      = duration;
    this.distance      = distance;
    this.caloriesBurnt = caloriesBurnt;
    this.userId        = userId;
  }

  @Ignore
  protected Exercise(Parcel in) {
    if (in.readByte() == 0) {
      duration = null;
    } else {
      duration = in.readLong();
    }
    if (in.readByte() == 0) {
      distance = null;
    } else {
      distance = in.readDouble();
    }
    if (in.readByte() == 0) {
      caloriesBurnt = null;
    } else {
      caloriesBurnt = in.readDouble();
    }
    userId = in.readString();
    id     = in.readLong();
  }

  @Ignore
  public static final Creator<Exercise> CREATOR = new Creator<Exercise>() {
    @Override
    public Exercise createFromParcel(Parcel in) {
      return new Exercise(in);
    }

    @Override
    public Exercise[] newArray(int size) {
      return new Exercise[size];
    }
  };

  @NonNull
  public Long getId() {
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

  @Ignore
  @Override
  public int describeContents() {
    return 0;
  }

  @Ignore
  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeLong(duration);
    dest.writeDouble(distance);
    dest.writeDouble(caloriesBurnt);
    dest.writeString(userId);
    dest.writeLong(id);
  }
}
