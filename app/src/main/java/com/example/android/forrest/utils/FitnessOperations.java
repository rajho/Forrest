package com.example.android.forrest.utils;

import androidx.annotation.NonNull;

public class FitnessOperations {
  /**
   * @param durationInMin duration in minutes
   * @param metFactor     metabolic equivalent from @{@link METExercise}
   * @param weight        in kilograms
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


  /**
   * @param steps  number
   * @param height in centimeters
   * @return distance in kilometers
   */
  public static double convertStepsToKm(@NonNull Integer steps, @NonNull Integer height) {
    double strideInCm = 0.43 * height;
    double strideInKm = strideInCm / 100 / 1000;
    return steps * strideInKm;
  }
}
