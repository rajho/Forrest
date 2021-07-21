package com.example.android.forrest.ui.report;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.forrest.data.model.Exercise;
import com.example.android.forrest.utils.SingleLiveEvent;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ReportViewModel extends ViewModel {

  public final SingleLiveEvent<Boolean> _goHome = new SingleLiveEvent<>();
  private final MutableLiveData<Exercise> _exercise = new MutableLiveData<>();

  @Inject
  public ReportViewModel() {
  }

  public LiveData<Exercise> getExercise() {
    return _exercise;
  }

  public void setExercise(Exercise exercise) {
    _exercise.setValue(exercise);
  }

  public void goHome() {
    _goHome.call();
  }


}