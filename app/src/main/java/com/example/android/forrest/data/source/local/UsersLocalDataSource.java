package com.example.android.forrest.data.source.local;

import androidx.annotation.NonNull;

import com.example.android.forrest.data.UsersDataSource;
import com.example.android.forrest.data.model.User;
import com.example.android.forrest.utils.AppExecutors;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

public class UsersLocalDataSource implements UsersDataSource {
  private UserDao mUserDao;
  private AppExecutors mAppExecutors;

  @Inject
  public UsersLocalDataSource(@NonNull AppExecutors appExecutors, @NonNull UserDao userDao) {
    mAppExecutors = appExecutors;
    mUserDao = userDao;
  }


  @Override
  public void insertUser(@NotNull User user) {
    Runnable insertRunnable = () -> mUserDao.insert(user);
    mAppExecutors.getDiskIO().execute(insertRunnable);
  }
}
