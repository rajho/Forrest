package com.example.android.forrest.utils;

import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.annotation.NonNull;

import com.example.android.forrest.R;

import java.util.Formatter;
import java.util.Locale;

public class TimeUtils {
  private static final Object sLock = new Object();
  private static Configuration sLastConfig;
  private static String sElapsedFormatHMMSS;

  private static void initFormatStrings() {
    synchronized (sLock) {
      initFormatStringsLocked();
    }
  }

  private static void initFormatStringsLocked() {
    Resources r = Resources.getSystem();
    Configuration cfg = r.getConfiguration();
    if (sLastConfig == null || !sLastConfig.equals(cfg)) {
      sLastConfig = cfg;
      sElapsedFormatHMMSS = r.getString(R.string.elapsed_time_short_format_h_mm_ss);
    }
  }

  public static String getFormattedTimeFromNumber(@NonNull Double number) {
    String formattedNumber = String.format(Locale.getDefault(), "%.2f", number);
    String[] timeArray = formattedNumber.split("\\.");
    Integer  hour      = Integer.parseInt(timeArray[0]);
    Integer  minute    = Integer.parseInt(timeArray[1]);

    return hour > 0 ? String.format(Locale.getDefault(), "%sh %02d min", hour, minute) :
           String.format(Locale.getDefault(), "%02d m", minute);
  }

  public static String getFormattedTimeForReport(@NonNull Long millisTime) {
    long totalSeconds = millisTime / 1000;

    long hours = 0;
    long minutes = 0;
    long seconds;

    if (totalSeconds >= 3600) {
      hours = totalSeconds / 3600;
      totalSeconds -= hours * 3600;
    }
    if (totalSeconds >= 60) {
      minutes = totalSeconds / 60;
      totalSeconds -= minutes * 60;
    }
    seconds = totalSeconds;

    return hours > 0 ?
           String.format(Locale.getDefault(), "%dh %02dm %02ds", hours, minutes, seconds) :
           String.format(Locale.getDefault(), "%02dm %02ds", minutes, seconds);
  }

  public static String getFormattedTime(@NonNull Long millisTime, String format) {
    long totalSeconds = millisTime / 1000;

    long hours = 0;
    long minutes = 0;
    long seconds;

    if (totalSeconds >= 3600) {
      hours = totalSeconds / 3600;
      totalSeconds -= hours * 3600;
    }
    if (totalSeconds >= 60) {
      minutes = totalSeconds / 60;
      totalSeconds -= minutes * 60;
    }
    seconds = totalSeconds;

    return String.format(Locale.getDefault(), format, hours, minutes, seconds);
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

    return f.format(sElapsedFormatHMMSS, hours, minutes, seconds).toString();
  }
}
