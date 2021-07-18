package com.example.android.forrest.ui.welcome.goal;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.forrest.data.UsersDataSource;
import com.example.android.forrest.data.model.User;
import com.example.android.forrest.utils.FitnessAPI;
import com.example.android.forrest.utils.METExercise;
import com.example.android.forrest.utils.SingleLiveEvent;

import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class GoalViewModel extends ViewModel {
  public final MutableLiveData<String[]> frequencyOptions  = new MutableLiveData<>();
  public final MutableLiveData<String[]> unitOptions       = new MutableLiveData<>();
  public final MutableLiveData<String>   frequencySelected = new MutableLiveData<>();
  public final MutableLiveData<String>   unitSelected      = new MutableLiveData<>();
  public final MutableLiveData<Double>   goal              = new MutableLiveData<>();
  public final MutableLiveData<User>     user              = new MutableLiveData<>();
  private final UsersDataSource mUsersLocalDataSource;
  private final SingleLiveEvent<Boolean> _navigateToHomeScreen = new SingleLiveEvent<>();
  private final SingleLiveEvent<Boolean> _openSetGoalDialog    = new SingleLiveEvent<>();
  private final MutableLiveData<Double>  _caloriesBurnt        = new MutableLiveData<>();

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
    User updateUser = user.getValue();
    if (updateUser != null) {
      updateUser.setGoalFrequency(frequencySelected.getValue());
      updateUser.setGoalUnits(unitSelected.getValue());
      updateUser.setGoalValue(goal.getValue());
      mUsersLocalDataSource.updateUser(updateUser);

      _navigateToHomeScreen.call();
    }
  }

  public void openSetGoalDialog() {
    _openSetGoalDialog.call();
  }

  public LiveData<Double> getCaloriesBurnt() {
    return _caloriesBurnt;
  }

  public void setCaloriesBurnt() {
    Integer goalInMinutes = getGoalInMinutes(Objects.requireNonNull(goal.getValue()));
    Double userWeight    = Objects.requireNonNull(Objects.requireNonNull(user.getValue())
                                                         .getWeight());

    Double caloriesBurnt = FitnessAPI.getCaloriesBurnt(
        goalInMinutes,
        METExercise.JOGGING.getMETFactor(),
        userWeight
    );

    _caloriesBurnt.setValue(caloriesBurnt);
  }

  public LiveData<User> getUserById(@NonNull String id) {
    return mUsersLocalDataSource.getUserById(id);
  }

  private Integer getGoalInMinutes(@NonNull Double goalValue) {
    String   formattedGoal = String.format(Locale.getDefault(), "%.2f", goalValue);
    String[] timeArray     = formattedGoal.split("\\.");
    int      hour          = Integer.parseInt(timeArray[0]);
    int      minute        = Integer.parseInt(timeArray[1]);

    return hour * 60 + minute;
  }
}