package com.example.android.forrest.data.source.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.android.forrest.data.model.User;

@Dao
public interface UsersDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(User user);

  @Query("SELECT * FROM User WHERE id = :id")
  LiveData<User> getById(String id);

  @Update(onConflict = OnConflictStrategy.REPLACE)
  void update(User user);

  @Query("UPDATE user " +
         "SET goal_frequency = :goalFrequency, goal_units = :goalUnits, goal_value = :goalValue " +
         "WHERE id = :userId")
  void updateGoal(String userId, String goalFrequency, String goalUnits, Long goalValue);
}
