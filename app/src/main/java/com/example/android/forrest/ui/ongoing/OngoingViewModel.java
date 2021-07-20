package com.example.android.forrest.ui.ongoing;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.android.forrest.data.UsersDataSource;
import com.example.android.forrest.data.model.User;
import com.example.android.forrest.utils.FitnessOperations;
import com.example.android.forrest.utils.METExercise;
import com.example.android.forrest.utils.SingleLiveEvent;

import java.nio.DoubleBuffer;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;


@HiltViewModel
public class OngoingViewModel extends ViewModel {
  public final MutableLiveData<User> user = new MutableLiveData<>();

  private final MutableLiveData<Integer> _stepsNumber = new MutableLiveData<>(0);
  private final MutableLiveData<Long>    _startTime   = new MutableLiveData<>();
  private final MutableLiveData<Long>    _timeRunning = new MutableLiveData<>();
  private final MutableLiveData<Boolean> _pausedState = new MutableLiveData<>(false);

  private final UsersDataSource mUsersLocalDataSource;

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

  private final LiveData<Double> _caloriesBurnt = Transformations.map(
      getTimeRunning(),
      runningTime -> {
        if (runningTime < 1000 || user.getValue() == null || user.getValue().getWeight() == null || user.getValue().getWeight() == 0) {
          return 0d;
        }
        float minutes = runningTime / (60 * 1000f);

        return FitnessOperations.getCaloriesBurnt(
            minutes,
            METExercise.JOGGING.getMETFactor(),
            70d
        );
      }
  );

  public LiveData<String> caloriesBurntFormatted = Transformations.map(
      _caloriesBurnt,
      caloriesBurnt -> {
        DecimalFormat df = new DecimalFormat("####0.0");
        return df.format(caloriesBurnt);
      }
  );

  private final LiveData<Double> _kilometers = Transformations.map(
      getStepsNumber(),
      steps -> {
        User u = user.getValue();
        if (u == null) {
          return 0d;
        }
        Integer height = u.getHeight();

        if (height == null) {
          return 0d;
        }

        return FitnessOperations.convertStepsToKm(
            steps,
            Objects.requireNonNull(Objects.requireNonNull(user.getValue()).getHeight())
        );
      }
  );

  public LiveData<String> kilometersFormatted = Transformations.map(
      _kilometers,
      kilometers -> {
        DecimalFormat df = new DecimalFormat("####0.0#");
        return df.format(kilometers);
      }
  );

  public LiveData<String> rhythmFormatted = Transformations.map(
      getTimeRunning(),
      runningTime -> {
        if (runningTime < 1000 || _kilometers.getValue() == null ||  _kilometers.getValue() == 0) {
          return "00:00";
        }
        double  rhytmInMillis = runningTime / _kilometers.getValue();
        Integer minutes       = (int) (rhytmInMillis / (60 * 1000));
        Integer remaining     = (int) (runningTime % (60 * 1000));
        Integer seconds       = remaining / 1000;

        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
      }
  );

  public SingleLiveEvent<Boolean> initStartTime     = new SingleLiveEvent<>();
  public SingleLiveEvent<Boolean> pauseChronometer  = new SingleLiveEvent<>();
  public SingleLiveEvent<Boolean> resumeChronometer = new SingleLiveEvent<>();

  @Inject
  public OngoingViewModel(UsersDataSource usersLocalDataSource) {
    mUsersLocalDataSource = usersLocalDataSource;

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
    if (pause) {
      stopChronometer();
    } else {
      resumeChronometer();
    }
    _pausedState.setValue(pause);
  }

  public LiveData<Integer> getStepsNumber() {
    return _stepsNumber;
  }

  public LiveData<User> getUserById(@NonNull String id) {
    return mUsersLocalDataSource.getUserById(id);
  }

  public void incrementStepsNumber(Integer moreSteps) {
    Integer currentSteps = _stepsNumber.getValue();
    currentSteps = currentSteps == null ? 0 : currentSteps;
    _stepsNumber.setValue(currentSteps + moreSteps);
  }

  public void saveReport() {

  }
}