package com.example.android.forrest.ui.welcome.calories;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.forrest.R;
import com.example.android.forrest.data.UsersDataSource;
import com.example.android.forrest.data.model.User;
import com.example.android.forrest.utils.SingleLiveEvent;
import com.facebook.internal.Mutable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CaloriesViewModel extends ViewModel {
  private final UsersDataSource mUsersLocalDataSource;
  private final FirebaseAuth    mFirebaseAuth;

  private final SingleLiveEvent<Boolean> _openHeightDialog = new SingleLiveEvent<>();
  private final SingleLiveEvent<Boolean> _openWeightDialog = new SingleLiveEvent<>();
  private final SingleLiveEvent<Boolean> _navigateToPermissions = new SingleLiveEvent<>();

  public MutableLiveData<Integer> height = new MutableLiveData<>();
  public MutableLiveData<Double> weight = new MutableLiveData<>();

  public SingleLiveEvent<Integer> showToastInt = new SingleLiveEvent<>();

  @Inject
  public CaloriesViewModel(UsersDataSource usersLocalDataSource, FirebaseAuth firebaseAuth) {
    mUsersLocalDataSource = usersLocalDataSource;
    mFirebaseAuth = firebaseAuth;
  }

  public SingleLiveEvent<Boolean> getOpenHeightDialog() {
    return _openHeightDialog;
  }

  public SingleLiveEvent<Boolean> getOpenWeightDialog() {
    return _openWeightDialog;
  }

  public SingleLiveEvent<Boolean> getNavigateToPermissions() {
    return _navigateToPermissions;
  }

  public void openHeightDialog() {
    _openHeightDialog.call();
  }

  public void openWeightDialog() {
    _openWeightDialog.call();
  }

  public void navigateToPermissions() {
    if (height.getValue() == null || weight.getValue() == null) {
      showToastInt.setValue(R.string.required_calories_field_empty);

      return;
    }

    _navigateToPermissions.call();
  }

  public void saveUser() {
    FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

    User newUser = new User(
        firebaseUser.getUid(),
        firebaseUser.getDisplayName(),
        height.getValue(),
        weight.getValue()
    );

    mUsersLocalDataSource.insertUser(newUser);
  }

}