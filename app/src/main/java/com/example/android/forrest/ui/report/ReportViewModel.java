package com.example.android.forrest.ui.report;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.android.forrest.data.model.Exercise;
import com.example.android.forrest.utils.SingleLiveEvent;
import com.example.android.forrest.utils.TimeUtils;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ReportViewModel extends ViewModel {

  public final SingleLiveEvent<Boolean> _goHome = new SingleLiveEvent<>();
  private final MutableLiveData<Exercise> _exercise = new MutableLiveData<>();

  private final LiveData<String> _durationFormatted = Transformations.map(
      _exercise,
      exercise -> TimeUtils.getFormattedTimeForReport(exercise.getDuration())
  );

  @Inject
  public ReportViewModel() {
  }

  public LiveData<Exercise> getExercise() {
    return _exercise;
  }

  public void setExercise(Exercise exercise) {
    _exercise.setValue(exercise);
  }

  public LiveData<String> getDurationFormatted() {
    return _durationFormatted;
  }

  public void goHome() {
    _goHome.call();
  }


}