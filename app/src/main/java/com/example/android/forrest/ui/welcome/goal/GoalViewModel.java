package com.example.android.forrest.ui.welcome.goal;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.forrest.utils.SingleLiveEvent;

public class GoalViewModel extends ViewModel {
  private final SingleLiveEvent<Boolean> _navigateToHomeScreen = new SingleLiveEvent<>();
  private final SingleLiveEvent<Boolean> _openSetGoalDialog = new SingleLiveEvent<>();

  public final MutableLiveData<String[]> frequencyOptions = new MutableLiveData<>();
  public final MutableLiveData<String[]> unitOptions = new MutableLiveData<>();

  public final MutableLiveData<String> frequencySelected = new MutableLiveData<>();
  public final MutableLiveData<String> unitSelected = new MutableLiveData<>();
  public final MutableLiveData<Double> goal = new MutableLiveData<>();

  public SingleLiveEvent<Boolean> getNavigateToHomeScreen() {
    return _navigateToHomeScreen;
  }

  public SingleLiveEvent<Boolean> getOpenSetGoalDialog() {
    return _openSetGoalDialog;
  }

  public void navigateToHomeScreen() {
    _navigateToHomeScreen.call();
  }

  public void openSetGoalDialog() {
    _openSetGoalDialog.call();
  }
}