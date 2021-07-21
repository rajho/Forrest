package com.example.android.forrest.di;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@InstallIn(SingletonComponent.class)
@Module
public class FirebaseModule {

  @Provides
  FirebaseAuth provideFirebaseAuth() {
    return FirebaseAuth.getInstance();
  }
}
