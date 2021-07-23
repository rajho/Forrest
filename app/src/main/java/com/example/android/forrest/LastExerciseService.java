package com.example.android.forrest;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.example.android.forrest.data.ExercisesDataSource;
import com.example.android.forrest.data.model.Exercise;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LastExerciseService extends IntentService {
  private static final String ACTION_GET_LAST_EXERCISE = "com.example.android.forrest.action" +
                                                        ".get_last_exercise";

  @Inject
  ExercisesDataSource mExercisesLocalDataSource;

  @Inject
  FirebaseAuth mFirebaseAuth;

  public LastExerciseService() {
    super(LastExerciseService.class.getSimpleName());
  }

  public static void startActionGetLastExercise(Context context) {
    Intent intent = new Intent(context, LastExerciseService.class);
    intent.setAction(ACTION_GET_LAST_EXERCISE);
    context.startService(intent);
  }

  @Override
  protected void onHandleIntent(@Nullable Intent intent) {
    if (intent != null) {
      final String action = intent.getAction();
      if (ACTION_GET_LAST_EXERCISE.equals(action)) {
        handleActionGetLastExercise();
      }
    }
  }

  private void handleActionGetLastExercise() {
    FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
    if (mFirebaseUser != null) {
      Exercise lastExercise = mExercisesLocalDataSource.getLastExerciseByUserId(mFirebaseUser.getUid());

      AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
      int[] appWidgetIds = appWidgetManager
          .getAppWidgetIds(new ComponentName(this, LastExerciseProvider.class));

      LastExerciseProvider.updateExerciseWidgets(this, appWidgetManager, lastExercise, appWidgetIds);
    }
  }
}
