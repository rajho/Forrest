package com.example.android.forrest.data.source.local;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.android.forrest.data.UsersDataSource;
import com.example.android.forrest.data.model.User;
import com.example.android.forrest.framework.AppExecutors;

import javax.inject.Inject;

public class UsersLocalDataSource implements UsersDataSource {
  private UsersDao     mUserDao;
  private AppExecutors mAppExecutors;

  @Inject
  public UsersLocalDataSource(@NonNull AppExecutors appExecutors, @NonNull UsersDao userDao) {
    mAppExecutors = appExecutors;
    mUserDao = userDao;
  }


  @Override
  public void insertUser(@NonNull User user) {
    Runnable insertRunnable = () -> mUserDao.insert(user);
    mAppExecutors.getDiskIO().execute(insertRunnable);
  }

  @Override
  public void updateUser(@NonNull User user) {
    Runnable updateRunnable = () -> mUserDao.update(user);
    mAppExecutors.getDiskIO().execute(updateRunnable);
  }

  @Override
  public LiveData<User> getUserById(@NonNull String id) {
    return mUserDao.getById(id);
  }

}
