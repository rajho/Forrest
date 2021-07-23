package com.example.android.forrest.ui.ongoing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.example.android.forrest.BuildConfig;
import com.example.android.forrest.LastExerciseService;
import com.example.android.forrest.R;
import com.example.android.forrest.databinding.FragmentOngoingBinding;
import com.example.android.forrest.framework.Permissions;
import com.example.android.forrest.framework.SensorStepDetector;
import com.example.android.forrest.utils.LocationConstants;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class OngoingFragment extends Fragment implements OnMapReadyCallback {
  @Inject
  FirebaseAuth mFirebaseAuth;
  private FragmentOngoingBinding mBinding;
  private OngoingViewModel       mViewModel;
  private FirebaseUser           mFirebaseUser;
  private GoogleMap              mMap;
  private Snackbar                    mSnackbar;
  private FusedLocationProviderClient mFusedLocationProviderClient;


  private SensorEventListener mStepDetectorListener;

  public static OngoingFragment newInstance() {
    return new OngoingFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    mBinding = FragmentOngoingBinding.inflate(inflater, container, false);
    mBinding.setLifecycleOwner(this);

    mFirebaseUser = mFirebaseAuth.getCurrentUser();

    // Getting locationProvider and initializing map
    mFusedLocationProviderClient = LocationServices
        .getFusedLocationProviderClient(requireContext());
    SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
        .findFragmentById(R.id.map);
    Objects.requireNonNull(mapFragment).getMapAsync(this);

    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull @NotNull View view,
      @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mViewModel = new ViewModelProvider(this).get(OngoingViewModel.class);
    mBinding.setViewmodel(mViewModel);

    setUpObservers();
    setUpListeners();
    mBinding.timeRunningText.start();
    SensorStepDetector.registerListener(requireActivity(), mStepDetectorListener);
  }

  @Override
  public void onPause() {
    super.onPause();
    SensorStepDetector.unregisterListener(requireActivity(), mStepDetectorListener);
  }

  private void setUpListeners() {
    mBinding.timeRunningText.setOnChronometerTickListener(chronometer -> {
      mViewModel.setTimeRunning(SystemClock.elapsedRealtime());
    });
  }

  private void setUpObservers() {
    mViewModel.initStartTime.observe(getViewLifecycleOwner(), aBoolean -> {
      long startTime = SystemClock.elapsedRealtime();
      mViewModel.setStartTime(startTime);
    });

    mViewModel.getStartTime().observe(getViewLifecycleOwner(), startTime -> {
      mBinding.timeRunningText.setBase(startTime);
    });

    mViewModel.pauseChronometer.observe(getViewLifecycleOwner(), aBoolean -> {
      mBinding.timeRunningText.stop();
    });

    mViewModel.resumeChronometer.observe(getViewLifecycleOwner(), aBoolean -> {
      mBinding.timeRunningText.start();
    });

    mStepDetectorListener = new SensorEventListener() {
      @Override
      public void onSensorChanged(SensorEvent event) {
        mViewModel.incrementStepsNumber(event.values.length);
      }

      @Override
      public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // ignore
      }
    };

    mViewModel.getUserById(mFirebaseUser.getUid()).observe(
        getViewLifecycleOwner(),
        user -> mViewModel.user.setValue(user)
    );

    mViewModel.openStopDialog.observe(
        getViewLifecycleOwner(),
        aBoolean -> openStopConfirmationDialog()
    );

    mViewModel.goReportScren.observe(getViewLifecycleOwner(), exercise -> {
      if (exercise != null) {
        // Updating widget
        LastExerciseService.startActionGetLastExercise(requireContext());

        NavDirections reportDirection =
            OngoingFragmentDirections.actionOngoingFragmentToReportFragment(
                exercise);
        NavHostFragment.findNavController(this).navigate(reportDirection);
      }
    });
  }

  private void openStopConfirmationDialog() {
    new MaterialAlertDialogBuilder(requireContext())
        .setTitle(R.string.is_running_over)
        .setNegativeButton(R.string.no, (dialog, which) -> {
          mViewModel.setPausedState(false);
          dialog.dismiss();
        })
        .setPositiveButton(
            R.string.yes,
            (dialog, which) -> mViewModel.saveReportAndGoReportScreen()
        ).show();
  }

  @Override
  public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
    mMap = googleMap;
    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
        LocationConstants.defaultLocation,
        LocationConstants.DEFAULT_ZOOM
    ));

    checkLocationPermissionsAndEnableMyLocation();
  }

  @SuppressLint("MissingPermission")
  private void checkLocationPermissionsAndEnableMyLocation() {
    if (Permissions.isLocationPermissionGranted(requireContext())) {
      checkDeviceLocationEnabledAndEnableLocation(true);
    } else {
      Permissions.requestLocationPermission(this);
    }
  }

  @SuppressLint("MissingPermission")
  private void checkDeviceLocationEnabledAndEnableLocation(boolean resolve) {
    LocationRequest locationRequest = LocationRequest.create();
    locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);

    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
        .addLocationRequest(locationRequest);

    SettingsClient settingsClient = LocationServices
        .getSettingsClient(requireContext());
    Task<LocationSettingsResponse> locationSettingsResponseTask = settingsClient
        .checkLocationSettings(builder.build());

    locationSettingsResponseTask.addOnCompleteListener(task -> {
      if (task.isSuccessful()) {
        mMap.setMyLocationEnabled(true);
        moveCameraInMap();
      }
    });

    locationSettingsResponseTask.addOnFailureListener(exception -> {
      if (exception instanceof ResolvableApiException && resolve) {
        try {
          startIntentSenderForResult(
              ((ResolvableApiException) exception).getResolution().getIntentSender(),
              LocationConstants.REQUEST_TURN_DEVICE_LOCATION_ON,
              null,
              0,
              0,
              0,
              null
          );
        } catch (IntentSender.SendIntentException sendEx) {
          Timber.d("Error getting location settings resolution: %s", sendEx.getMessage());
        }
      } else {
        Snackbar
            .make(
                mBinding.pauseFab,
                R.string.location_required_error,
                Snackbar.LENGTH_INDEFINITE
            )
            .setAction(android.R.string.ok, v -> {
              checkDeviceLocationEnabledAndEnableLocation(true);
              v.setVisibility(View.GONE);
            })
            .show();
      }
    });
  }

  @SuppressLint("MissingPermission")
  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == LocationConstants.REQUEST_TURN_DEVICE_LOCATION_ON) {
      if (Activity.RESULT_OK == resultCode) {
        LocationCallback locationCallback = new LocationCallback() {
          @Override
          public void onLocationResult(@NonNull LocationResult locationResult) {
            mMap.setMyLocationEnabled(true);
            mMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    new LatLng(
                        locationResult.getLastLocation().getLatitude(),
                        locationResult.getLastLocation().getLongitude()
                    ),
                    LocationConstants.DEFAULT_ZOOM
                )
            );
          }
        };

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(0);
        locationRequest.setFastestInterval(0);
        locationRequest.setNumUpdates(1);

        mFusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        );

      } else {
        checkDeviceLocationEnabledAndEnableLocation(false);
      }
    }
  }

  @SuppressLint("MissingPermission")
  private void moveCameraInMap() {
    Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
    locationResult.addOnCompleteListener(task -> {
      if (task.isSuccessful()) {

        Location lastKnownLocation = task.getResult();
        if (lastKnownLocation != null) {
          mMap.moveCamera(
              CameraUpdateFactory.newLatLngZoom(
                  new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()),
                  LocationConstants.DEFAULT_ZOOM
              )
          );
        }
      }
    });

  }

  @Override
  public void onRequestPermissionsResult(int requestCode,
      @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    boolean fineLocationDenied = grantResults[0] == PackageManager.PERMISSION_DENIED;

    if (fineLocationDenied) {
      mSnackbar = Snackbar
          .make(
              mBinding.pauseFab,
              R.string.permission_denied_explanation,
              Snackbar.LENGTH_INDEFINITE
          )
          .setAction(R.string.settings, v -> {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.fromParts("package", BuildConfig.APPLICATION_ID, null));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
          });
      mSnackbar.show();
    } else {
      if (mSnackbar != null) {
        mSnackbar.dismiss();
      }
      checkDeviceLocationEnabledAndEnableLocation(true);
    }
  }
}