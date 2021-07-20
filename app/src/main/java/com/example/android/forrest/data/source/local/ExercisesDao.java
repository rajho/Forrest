package com.example.android.forrest.data.source.local;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.android.forrest.data.model.Exercise;

import java.util.List;

public interface ExercisesDao {
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  void insert(Exercise exercise);

  @Query("SELECT * FROM Exercise WHERE user_id = :userId")
  LiveData<List<Exercise>> getAllByUserId(String userId);
}
