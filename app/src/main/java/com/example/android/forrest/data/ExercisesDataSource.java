package com.example.android.forrest.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.android.forrest.data.model.Exercise;

import java.util.List;


public interface ExercisesDataSource {
  void insertExercise(@NonNull Exercise exercise);

  LiveData<List<Exercise>> getExercisesByUserId(@NonNull String userId);

  Exercise getLastExerciseByUserId(@NonNull String userId);
}
