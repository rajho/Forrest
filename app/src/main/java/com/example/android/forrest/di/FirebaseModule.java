package com.example.android.forrest.di;

import android.content.Context;

import androidx.room.Room;

import com.example.android.forrest.data.source.local.ForrestDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@InstallIn(SingletonComponent.class)
@Module
public class FirebaseModule {

  @Provides
  FirebaseAuth provideFirebaseAuth() {
    return FirebaseAuth.getInstance();
  }

  @Provides
  FirebaseUser provideFirebaseUser(FirebaseAuth firebaseAuth) {
    return firebaseAuth.getCurrentUser();
  }
}
