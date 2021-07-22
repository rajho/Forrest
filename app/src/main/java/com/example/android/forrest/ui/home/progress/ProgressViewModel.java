package com.example.android.forrest.ui.home.progress;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.forrest.data.ExercisesDataSource;
import com.example.android.forrest.data.model.Exercise;
import com.example.android.forrest.data.source.local.ExercisesLocalDataSource;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ProgressViewModel extends ViewModel {
  private ExercisesDataSource mExercisesLocalDataSource;

  @Inject
  public ProgressViewModel(ExercisesDataSource exercisesDataSource) {
    mExercisesLocalDataSource = exercisesDataSource;
  }

  public LiveData<List<Exercise>> getExercisesByUserId(String userId) {
    return mExercisesLocalDataSource.getExercisesByUserId(userId);
  }


}