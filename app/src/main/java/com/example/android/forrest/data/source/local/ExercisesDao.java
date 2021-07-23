package com.example.android.forrest.data.source.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.android.forrest.data.model.Exercise;

import java.util.List;

@Dao
public interface ExercisesDao {
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  void insert(Exercise exercise);

  @Query("SELECT * FROM Exercise WHERE user_id = :userId ORDER BY id DESC")
  LiveData<List<Exercise>> getAllByUserId(String userId);

  @Query("SELECT * FROM Exercise WHERE user_id = :userId ORDER BY id DESC LIMIT 1")
  Exercise getLastByUserId(String userId);
}
