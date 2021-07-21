package com.example.android.forrest.di;

import android.content.Context;

import androidx.room.Room;

import com.example.android.forrest.data.source.local.ExercisesDao;
import com.example.android.forrest.data.source.local.ForrestDatabase;
import com.example.android.forrest.data.source.local.UsersDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@InstallIn(SingletonComponent.class)
@Module
public class DatabaseModule {

  @Provides
  @Singleton
  ForrestDatabase provideDatabase(@ApplicationContext Context appContext) {
    return Room.databaseBuilder(
        appContext,
        ForrestDatabase.class,
        "Forrest.db"
    ).fallbackToDestructiveMigration().build();
  }

  @Provides
  UsersDao provideUserDao(ForrestDatabase database) {
    return database.usersDao();
  }

  @Provides
  ExercisesDao provideExercisesDao(ForrestDatabase database) {
    return database.exercisesDao();
  }
}
