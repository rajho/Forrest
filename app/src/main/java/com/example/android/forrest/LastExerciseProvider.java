package com.example.android.forrest;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.android.forrest.data.model.Exercise;
import com.example.android.forrest.ui.MainActivity;
import com.example.android.forrest.utils.FitnessOperations;
import com.example.android.forrest.utils.TimeUtils;

import java.text.DecimalFormat;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Implementation of App Widget functionality.
 */
public class LastExerciseProvider extends AppWidgetProvider {

  static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, Exercise exercise,
      int appWidgetId) {

//    CharSequence widgetText = context.getString(R.string.appwidget_text);
    // Construct the RemoteViews object
    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.last_exercise_provider);

    String rhythm = FitnessOperations.getFormattedRhythm(exercise.getDuration(),
        exercise.getDistance());
    String distance = (new DecimalFormat("####0.0#")).format(exercise.getDistance());
    String duration = TimeUtils.getFormattedTime(exercise.getDuration(), "%02d:%02d:%02d");

    views.setTextViewText(R.id.rhytm_value, rhythm);
    views.setTextViewText(R.id.distance_value, distance);
    views.setTextViewText(R.id.duration_value, duration);

    // Open app from widget
    Intent intent = new Intent(context, MainActivity.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
    views.setOnClickPendingIntent(R.id.widget_last_session, pendingIntent);

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views);
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    // There may be multiple widgets active, so update all of them
    LastExerciseService.startActionGetLastExercise(context);
  }

  public static void updateExerciseWidgets(Context context, AppWidgetManager appWidgetManager,
      Exercise exercise, int[] appWidgetIds) {
    for (int appWidgetId : appWidgetIds) {
      updateAppWidget(context, appWidgetManager, exercise, appWidgetId);
    }
  }

  @Override
  public void onEnabled(Context context) {
    // Enter relevant functionality for when the first widget is created
  }

  @Override
  public void onDisabled(Context context) {
    // Enter relevant functionality for when the last widget is disabled
  }
}