package com.example.android.forrest.utils;

import androidx.annotation.NonNull;

/**
 *  Metabolic equivalent for a task per each exercise
 */
public enum METExercise {
  JOGGING(7d);

  Double mMETFactor;

  METExercise(@NonNull Double met) {
    mMETFactor = met;
  }

  public Double getMETFactor() {
    return mMETFactor;
  }
}
