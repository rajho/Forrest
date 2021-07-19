package com.example.android.forrest.ui.ongoing;

import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.core.util.TimeUtils;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.android.forrest.utils.FitnessAPI;
import com.example.android.forrest.utils.METExercise;
import com.example.android.forrest.utils.SingleLiveEvent;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import timber.log.Timber;

@HiltViewModel
public class OngoingViewModel extends ViewModel {
  private final MutableLiveData<Long> _startTime = new MutableLiveData<>();
  private final MutableLiveData<Long> _timeRunning = new MutableLiveData<>();
  private final MutableLiveData<Boolean> _pausedState = new MutableLiveData<>(false);

  public LiveData<Boolean> pauseButtonVisible = Transformations.map(
      getPausedState(),
      paused -> !paused
  );

  public LiveData<Boolean> stopButtonVisible = Transformations.map(
      getPausedState(),
      paused -> paused
  );

  public LiveData<Boolean> resumeButtonVisible = Transformations.map(
      getPausedState(),
      paused -> paused
  );


  public LiveData<String> caloriesBurnt = Transformations.map(
      getTimeRunning(),
      runningTime -> {
        float minutes = runningTime / (60 * 1000f);
        double caloriesBurnt = FitnessAPI.getCaloriesBurnt(
            minutes,
            METExercise.JOGGING.getMETFactor(),
            70d);

        DecimalFormat df = new DecimalFormat("####0.0");
        return df.format(caloriesBurnt);
      }
  );

  public SingleLiveEvent<Boolean> initStartTime = new SingleLiveEvent<>();
  public SingleLiveEvent<Boolean> pauseChronometer = new SingleLiveEvent<>();
  public SingleLiveEvent<Boolean> resumeChronometer = new SingleLiveEvent<>();

  @Inject
  public OngoingViewModel() {
    startChronometer();
  }

  public LiveData<Long> getStartTime() {
    return _startTime;
  }

  public void setStartTime(Long startTime) {
    _startTime.setValue(startTime);
  }

  public void startChronometer() {
    if (_startTime.getValue() == null) {
       initStartTime.call();
    }
  }

  private void resumeChronometer() {
    resumeChronometer.call();
  }

  private void stopChronometer() {
    pauseChronometer.call();
  }

  public LiveData<Long> getTimeRunning() {
    return _timeRunning;
  }

  public void setTimeRunning(Long now) {
    long timeRunning;
    Long startTimeValue = _startTime.getValue();
    if (startTimeValue != null) {
      timeRunning = now - startTimeValue;
    } else {
      timeRunning = 0L;
    }

    _timeRunning.setValue(timeRunning);
  }

  public LiveData<Boolean> getPausedState() {
    return _pausedState;
  }

  public void setPausedState(@NonNull Boolean pause) {
    if(pause) {
      stopChronometer();
    } else {
      resumeChronometer();
    }
    _pausedState.setValue(pause);
  }
}