package com.example.android.forrest.ui.welcome.permissions;

import androidx.lifecycle.ViewModel;

import com.example.android.forrest.utils.SingleLiveEvent;

public class PermissionsViewModel extends ViewModel {
  private final SingleLiveEvent<Boolean> _navigateToGoalScreen = new SingleLiveEvent<>();

  public SingleLiveEvent<Boolean> getNavigateToGoalScreen() {
    return _navigateToGoalScreen;
  }

  public void navigateToGoalScreen() {
    _navigateToGoalScreen.call();
  }
}