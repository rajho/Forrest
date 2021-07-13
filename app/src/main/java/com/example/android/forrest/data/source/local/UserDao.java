package com.example.android.forrest.data.source.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.android.forrest.data.model.User;

@Dao
public interface UserDao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  void insert(User user);

  @Query("SELECT * FROM User WHERE id = :id")
  User getById(String id);

  @Update
  int update(User user);

  @Query("UPDATE user " +
         "SET goal_frequency = :goalFrequency, goal_units = :goalUnits, goal_value = :goalValue " +
         "WHERE id = :userId")
  void updateGoal(String userId, String goalFrequency, String goalUnits, Long goalValue);
}
