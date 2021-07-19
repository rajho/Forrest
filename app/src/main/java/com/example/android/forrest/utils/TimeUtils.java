package com.example.android.forrest.utils;

import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.annotation.NonNull;

import java.util.Formatter;
import java.util.Locale;

public class TimeUtils {
  private static final Object sLock = new Object();
  private static Configuration sLastConfig;
  private static String sElapsedFormatMMSS;
  private static String sElapsedFormatHMMSS;

  public static String getFormattedTimeFromNumber(@NonNull Double number) {
    String formattedNumber = String.format(Locale.getDefault(), "%.2f", number);
    String[] timeArray = formattedNumber.split("\\.");
    Integer  hour      = Integer.parseInt(timeArray[0]);
    Integer  minute    = Integer.parseInt(timeArray[1]);

    return hour > 0 ? String.format(Locale.getDefault(), "%sh %02d min", hour, minute) :
           String.format(Locale.getDefault(), "%02d m", minute);
  }

  public static String getFormattedDistanceFromNumber(@NonNull Double number) {
    return String.format("%s km", number);
  }

  /**
   * Formats an elapsed time in the form "MM:SS" or "H:MM:SS"
   * for display on the call-in-progress screen.
   * @param elapsedSeconds the elapsed time in seconds.
   */
  public static String formatElapsedTime(long elapsedSeconds) {
    return formatElapsedTime(null, elapsedSeconds);
  }

  /**
   * Formats an elapsed time in a format like "MM:SS" or "H:MM:SS" (using a form
   * suited to the current locale), similar to that used on the call-in-progress
   * screen.
   *
   * @param recycle {@link StringBuilder} to recycle, or null to use a temporary one.
   * @param elapsedSeconds the elapsed time in seconds.
   */
  public static String formatElapsedTime(StringBuilder recycle, long elapsedSeconds) {
    // Break the elapsed seconds into hours, minutes, and seconds.
    long hours = 0;
    long minutes = 0;
    long seconds = 0;
    if (elapsedSeconds >= 3600) {
      hours = elapsedSeconds / 3600;
      elapsedSeconds -= hours * 3600;
    }
    if (elapsedSeconds >= 60) {
      minutes = elapsedSeconds / 60;
      elapsedSeconds -= minutes * 60;
    }
    seconds = elapsedSeconds;

    // Create a StringBuilder if we weren't given one to recycle.
    // TODO: if we cared, we could have a thread-local temporary StringBuilder.
    StringBuilder sb = recycle;
    if (sb == null) {
      sb = new StringBuilder(8);
    } else {
      sb.setLength(0);
    }

    // Format the broken-down time in a locale-appropriate way.
    // TODO: use icu4c when http://unicode.org/cldr/trac/ticket/3407 is fixed.
    Formatter f = new Formatter(sb, Locale.getDefault());
    initFormatStrings();
    if (hours > 0) {
      return f.format(sElapsedFormatHMMSS, hours, minutes, seconds).toString();
    } else {
      return f.format(sElapsedFormatMMSS, minutes, seconds).toString();
    }
  }

  private static void initFormatStrings() {
    synchronized (sLock) {
      initFormatStringsLocked();
    }
  }

  private static void initFormatStringsLocked() {
    Resources     r   = Resources.getSystem();
    Configuration cfg = r.getConfiguration();
//    if (sLastConfig == null || !sLastConfig.equals(cfg)) {
//      sLastConfig = cfg;
//      sElapsedFormatMMSS = r.getString(com.android.internal.R.string.elapsed_time_short_format_mm_ss);
//      sElapsedFormatHMMSS = r.getString(com.android.internal.R.string.elapsed_time_short_format_h_mm_ss);
//    }
  }
}
