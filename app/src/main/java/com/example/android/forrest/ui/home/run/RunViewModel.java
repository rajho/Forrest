package com.example.android.forrest.ui.home.run;

import androidx.lifecycle.ViewModel;

import com.example.android.forrest.utils.SingleLiveEvent;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RunViewModel extends ViewModel {
  private final SingleLiveEvent<Boolean> _verifyLocationEnabled = new SingleLiveEvent<>();
  public SingleLiveEvent<Boolean> verifyLocationEnabled() {
    return _verifyLocationEnabled;
  }

  private final SingleLiveEvent<Boolean> _navigateToCountDown = new SingleLiveEvent<>();
  public SingleLiveEvent<Boolean> getNavigateToCountDown() {
    return _navigateToCountDown;
  }

  @Inject
  public RunViewModel() {
  }

  public void verifyLocationAndNavigate() {
    _verifyLocationEnabled.call();
  }

  public void navigateToCountDown() {
    _navigateToCountDown.call();
  }


}