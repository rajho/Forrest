package com.example.android.forrest.utils;

import androidx.annotation.NonNull;

import java.util.Locale;

public class TimeUtils {

  public static String getFormattedTimeFromNumber(@NonNull Double number) {
    String[] timeArray = String.valueOf(number).split("\\.");
    Integer  hour      = Integer.parseInt(timeArray[0]);
    Integer  minute    = Integer.parseInt(timeArray[1]);

    return hour > 0 ? String.format(Locale.getDefault(), "%s:%02d h", hour, minute) :
           String.format(Locale.getDefault(), "%02d m", minute);
  }
}
