package com.example.android.forrest.utils;

import androidx.annotation.NonNull;

import java.util.Locale;

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

  public static String getFormattedRhythm(Long duration, Double distance) {
    if (duration < 1000 || distance == null || distance == 0.0) {
      return String.format(Locale.getDefault(), "%02d:%02d min/km", 0, 0);
    }

    double  rhytmInMillis = duration / distance;
    Integer minutes       = (int) (rhytmInMillis / (60 * 1000));
    Integer remaining     = (int) (duration % (60 * 1000));
    Integer seconds       = remaining / 1000;

    return String.format(Locale.getDefault(), "%02d:%02d min/km", minutes, seconds);
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
