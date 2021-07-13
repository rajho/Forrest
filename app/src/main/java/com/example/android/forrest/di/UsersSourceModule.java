package com.example.android.forrest.di;

import com.example.android.forrest.data.UsersDataSource;
import com.example.android.forrest.data.source.local.UsersLocalDataSource;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@InstallIn(SingletonComponent.class)
@Module
abstract class UsersSourceModule {

  @Binds
  abstract UsersDataSource bindUsersLocalDataSource(UsersLocalDataSource impl);
}
