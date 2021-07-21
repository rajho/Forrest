package com.example.android.forrest.di;

import com.example.android.forrest.data.ExercisesDataSource;
import com.example.android.forrest.data.source.local.ExercisesLocalDataSource;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@InstallIn(SingletonComponent.class)
@Module
abstract class ExercisesSourceModule {

  @Binds
  abstract ExercisesDataSource bindExercisesLocalDataSource(ExercisesLocalDataSource impl);
}
