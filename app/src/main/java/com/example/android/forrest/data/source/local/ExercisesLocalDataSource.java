package com.example.android.forrest.data.source.local;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.android.forrest.data.ExercisesDataSource;
import com.example.android.forrest.data.model.Exercise;
import com.example.android.forrest.utils.AppExecutors;

import java.util.List;

import javax.inject.Inject;

public class ExercisesLocalDataSource implements ExercisesDataSource {
  private ExercisesDao mExercisesDao;
  private AppExecutors mAppExecutors;

  @Inject
  public ExercisesLocalDataSource(ExercisesDao exercisesDao,
      AppExecutors appExecutors) {
    mExercisesDao = exercisesDao;
    mAppExecutors = appExecutors;
  }

  @Override
  public void insertExercise(@NonNull Exercise exercise) {
    Runnable insertRunnable = () -> mExercisesDao.insert(exercise);
    mAppExecutors.getDiskIO().execute(insertRunnable);
  }

  @Override
  public LiveData<List<Exercise>> getExercisesByUserId(@NonNull String userId) {
    return mExercisesDao.getAllByUserId(userId);
  }
}
