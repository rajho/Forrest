package com.example.android.forrest.utils;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.VisibleForTesting;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppExecutors {
  private static final int THREAD_COUNT = 3;

  private final Executor diskIO;

  private final Executor networkIO;

  private final Executor mainThread;

  @VisibleForTesting
  public AppExecutors(Executor diskIO, Executor networkIO, Executor mainThread) {
    this.diskIO     = diskIO;
    this.networkIO  = networkIO;
    this.mainThread = mainThread;
  }


  @Inject
  public AppExecutors() {
    this(
        Executors.newSingleThreadExecutor(),
        Executors.newFixedThreadPool(THREAD_COUNT),
        new MainThreadExecutor());
  }

  private static class MainThreadExecutor implements Executor{
    private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    @Override
    public void execute(Runnable command) {
      mainThreadHandler.post(command);
    }
  }

  public Executor getDiskIO() {
    return diskIO;
  }

  public Executor getNetworkIO() {
    return networkIO;
  }

  public Executor getMainThread() {
    return mainThread;
  }
}
