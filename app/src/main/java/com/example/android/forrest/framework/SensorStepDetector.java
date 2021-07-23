package com.example.android.forrest.framework;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

public class SensorStepDetector {

  public static void registerListener(FragmentActivity fragmentActivity,
      SensorEventListener listener) {
    SensorManager sensorManager = (SensorManager) fragmentActivity.getSystemService(Activity.SENSOR_SERVICE);
    Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

    sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL, 0);
  }

  public static void unregisterListener(@NonNull FragmentActivity fragmentActivity,
      SensorEventListener listener) {
    SensorManager sensorManager = (SensorManager) fragmentActivity.getSystemService(Activity.SENSOR_SERVICE);
    sensorManager.unregisterListener(listener);
  }
}
