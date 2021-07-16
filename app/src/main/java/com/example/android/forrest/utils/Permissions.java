package com.example.android.forrest.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class Permissions {
  private static final int FINE_PERMISSIONS_REQUEST_CODE = 11;

  public static boolean isLocationPermissionGranted(@NonNull Context context) {
    int fineLocationPermission = ContextCompat.checkSelfPermission(context,
        Manifest.permission.ACCESS_FINE_LOCATION);

    return fineLocationPermission == PackageManager.PERMISSION_GRANTED;
  }

  public static void requestLocationPermission(@NonNull Fragment fragment) {
    if (Permissions.isLocationPermissionGranted(fragment.requireContext())) {
      return;
    }

    String[] permissionsArray = new String[]{ Manifest.permission.ACCESS_FINE_LOCATION };
    fragment.requestPermissions(permissionsArray, FINE_PERMISSIONS_REQUEST_CODE);
  }
}
