package com.example.android.forrest;

import android.app.Application;

import timber.log.Timber;

public class ForrestApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    }
  }
}
