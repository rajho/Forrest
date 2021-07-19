package com.example.android.forrest.ui.countdown;

import android.os.CountDownTimer;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.android.forrest.utils.SingleLiveEvent;

import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import timber.log.Timber;

@HiltViewModel
public class CountdownViewModel extends ViewModel {
  private static final long DONE           = 0L;
  private static final long ONE_SECOND     = 1000L;
  private static final long COUNTDOWN_TIME = 6000L;

  private final CountDownTimer timer;

  private final SingleLiveEvent<Boolean> _countDownFinish = new SingleLiveEvent<>();
  private final MutableLiveData<Long>    _currentTime     = new MutableLiveData<>();
  private final LiveData<Long> currentTime = _currentTime;
  public final LiveData<String> currentTimeString = Transformations.map(
      currentTime,
      time -> {
        if (time == 0L) {
          return "Go!";
        }

        return String.format(Locale.getDefault(), "%s", time);
      }
  );

  @Inject
  public CountdownViewModel() {
    timer = new CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
      @Override
      public void onTick(long millisUntilFinished) {
        _currentTime.setValue(millisUntilFinished / ONE_SECOND);
      }

      @Override
      public void onFinish() {
        _currentTime.setValue(DONE);
        _countDownFinish.setValue(true);
      }
    };

    timer.start();
  }

  public SingleLiveEvent<Boolean> getCountDownFinish() {
    return _countDownFinish;
  }

  @Override
  protected void onCleared() {
    super.onCleared();
    timer.cancel();
  }
}