package com.example.android.forrest.ui.welcome.goal;

import androidx.lifecycle.ViewModel;

import com.example.android.forrest.utils.SingleLiveEvent;

public class GoalViewModel extends ViewModel {
  private final SingleLiveEvent<Boolean> _navigateToHomeScreen = new SingleLiveEvent<>();

  public SingleLiveEvent<Boolean> getNavigateToHomeScreen() {
    return _navigateToHomeScreen;
  }

  public void navigateToHomeScreen() {
    _navigateToHomeScreen.call();
  }
}