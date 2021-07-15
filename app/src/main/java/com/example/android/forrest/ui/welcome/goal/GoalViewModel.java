package com.example.android.forrest.ui.welcome.goal;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.forrest.data.UsersDataSource;
import com.example.android.forrest.data.model.User;
import com.example.android.forrest.utils.FitnessAPI;
import com.example.android.forrest.utils.METExercise;
import com.example.android.forrest.utils.SingleLiveEvent;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class GoalViewModel extends ViewModel {
  private final UsersDataSource mUsersLocalDataSource;


  private final SingleLiveEvent<Boolean> _navigateToHomeScreen = new SingleLiveEvent<>();
  private final SingleLiveEvent<Boolean> _openSetGoalDialog = new SingleLiveEvent<>();
  private final MutableLiveData<Double> _caloriesBurnt = new MutableLiveData<>();

  public final MutableLiveData<String[]> frequencyOptions = new MutableLiveData<>();
  public final MutableLiveData<String[]> unitOptions = new MutableLiveData<>();
  public final MutableLiveData<String> frequencySelected = new MutableLiveData<>();
  public final MutableLiveData<String> unitSelected = new MutableLiveData<>();
  public final MutableLiveData<Double> goal = new MutableLiveData<>();
  public final MutableLiveData<User> user = new MutableLiveData<>();

  @Inject
  public GoalViewModel(UsersDataSource usersLocalDataSource) {
    mUsersLocalDataSource = usersLocalDataSource;
  }

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

  public LiveData<Double> getCaloriesBurnt() {
    return _caloriesBurnt;
  }

  public void setCaloriesBurnt() {
    Double caloriesBurnt = FitnessAPI.getCaloriesBurnt(
        Objects.requireNonNull(goal.getValue()).intValue(),
        METExercise.JOGGING.getMETFactor(),
        Objects.requireNonNull(Objects.requireNonNull(user.getValue()).getWeight())
    );

    _caloriesBurnt.setValue(caloriesBurnt);
  }

  public LiveData<User> getUserById(@NonNull String id) {
    return mUsersLocalDataSource.getUserById(id);
  }
}