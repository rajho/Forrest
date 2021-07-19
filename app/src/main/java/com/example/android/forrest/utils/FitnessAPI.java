package com.example.android.forrest.utils;

import androidx.annotation.NonNull;

public class FitnessAPI {

  /**
   * @param durationInMin duration in minutes
   * @param metFactor metabolic equivalent from @{@link METExercise}
   * @param weight in kilograms
   * @return calories burnt in session
   */
  public static double getCaloriesBurnt(@NonNull Integer durationInMin, @NonNull Double metFactor,
      @NonNull Double weight) {
    return durationInMin * metFactor * 3.5 * weight / 200;
  }

  public static double getCaloriesBurnt(@NonNull Float durationInMin, @NonNull Double metFactor,
      @NonNull Double weight) {
    return durationInMin * metFactor * 3.5 * weight / 200;
  }
}
