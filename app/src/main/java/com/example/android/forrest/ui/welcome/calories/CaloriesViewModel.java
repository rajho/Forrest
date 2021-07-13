package com.example.android.forrest.ui.welcome.calories;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.forrest.data.UsersDataSource;
import com.example.android.forrest.data.model.User;
import com.example.android.forrest.utils.SingleLiveEvent;
import com.facebook.internal.Mutable;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CaloriesViewModel extends ViewModel {
  private final UsersDataSource mUsersLocalDataSource;
  private final FirebaseUser mFirebaseUser;

  private final SingleLiveEvent<Void> _openHeightDialog = new SingleLiveEvent();
  private final SingleLiveEvent<Void> _openWeightDialog = new SingleLiveEvent();
  private final SingleLiveEvent<Void> _navigateToPermissions = new SingleLiveEvent();

  public MutableLiveData<Integer> height = new MutableLiveData<>();
  public MutableLiveData<Float> weight = new MutableLiveData<>();

  @Inject
  public CaloriesViewModel(UsersDataSource usersLocalDataSource, FirebaseUser firebaseUser) {
    mUsersLocalDataSource = usersLocalDataSource;
    mFirebaseUser = firebaseUser;
  }

  public SingleLiveEvent getOpenHeightDialog() {
    return _openHeightDialog;
  }

  public SingleLiveEvent getOpenWeightDialog() {
    return _openWeightDialog;
  }

  public SingleLiveEvent getNavigateToPermissions() {
    return _navigateToPermissions;
  }

  public void openHeightDialog() {
    _openHeightDialog.call();
  }

  public void openWeightDialog() {
    _openWeightDialog.call();
  }

  public void navigateToPermissions() {
    _navigateToPermissions.call();
  }

  public void saveUser() {
    User newUser = new User(
        mFirebaseUser.getUid(),
        mFirebaseUser.getDisplayName(),
        height.getValue(),
        weight.getValue()
    );

    mUsersLocalDataSource.insertUser(newUser);
  }

}