package com.example.android.forrest.ui.home.run;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.example.android.forrest.BuildConfig;
import com.example.android.forrest.R;
import com.example.android.forrest.databinding.FragmentRunBinding;
import com.example.android.forrest.ui.home.HomeFragmentDirections;
import com.example.android.forrest.ui.login.LoginActivity;
import com.example.android.forrest.utils.Permissions;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class RunFragment extends Fragment implements OnMapReadyCallback {
  private final int DEFAULT_ZOOM                    = 16;
  private final int REQUEST_TURN_DEVICE_LOCATION_ON = 11;

  private final LatLng defaultLocation = new LatLng(-12.067986, -77.041844);

  @Inject
  FirebaseAuth mFirebaseAuth;

  private FragmentRunBinding          mBinding;
  private RunViewModel                mViewModel;
  private GoogleMap                   mMap;
  private Snackbar                    mSnackbar;
  private FusedLocationProviderClient mFusedLocationProviderClient;

  public static RunFragment newInstance() {
    return new RunFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    mBinding = FragmentRunBinding.inflate(inflater, container, false);

    // if user is logged in request location permission and load map
    if (mFirebaseAuth.getCurrentUser() != null) {
      ((AppCompatActivity) requireActivity()).setSupportActionBar(mBinding.toolbar);
      setHasOptionsMenu(true);

      mFusedLocationProviderClient = LocationServices
          .getFusedLocationProviderClient(requireContext());
      SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
          .findFragmentById(R.id.map);
      Objects.requireNonNull(mapFragment).getMapAsync(this);
    }

    return mBinding.getRoot();

  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    mViewModel = new ViewModelProvider(this).get(RunViewModel.class);
    mBinding.setViewmodel(mViewModel);

    setUpObservers();
  }

  private void setUpObservers() {
    mViewModel.verifyLocationEnabled().observe(getViewLifecycleOwner(), aBoolean -> {
      checkDeviceLocationEnabledAndEnableLocation(true, true);
    });
    mViewModel.getNavigateToCountDown().observe(getViewLifecycleOwner(), aBoolean -> {
      NavDirections navToCountDownScreen =
          HomeFragmentDirections.actionHomeFragmentToCountdownFragment();
      NavHostFragment.findNavController(this).navigate(navToCountDownScreen);
    });
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.menu_home, menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.logout_item) {
      mFirebaseAuth.signOut();

      if (Profile.getCurrentProfile() != null) {
        // Logging out facebook
        LoginManager.getInstance().logOut();
      }

      Intent intent = new Intent(getActivity(), LoginActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(intent);
      return true;
    }

    return super.onOptionsItemSelected(item);
  }


  @Override
  public void onMapReady(@NonNull GoogleMap googleMap) {
    mMap = googleMap;
    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
    mMap.getUiSettings().setScrollGesturesEnabled(false);

    checkLocationPermissionsAndEnableMyLocation();
  }

  @SuppressLint("MissingPermission")
  private void checkLocationPermissionsAndEnableMyLocation() {
    if (Permissions.isLocationPermissionGranted(requireContext())) {
      checkDeviceLocationEnabledAndEnableLocation(false, true);
    } else {
      Permissions.requestLocationPermission(this);
    }
  }

  @SuppressLint("MissingPermission")
  private void checkDeviceLocationEnabledAndEnableLocation(boolean navigateToCountDown,
      boolean resolve) {
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
        if (navigateToCountDown) {
          mViewModel.navigateToCountDown();
        }
      }
    });

    locationSettingsResponseTask.addOnFailureListener(exception -> {
      if (exception instanceof ResolvableApiException && resolve) {
        try {
          startIntentSenderForResult(
              ((ResolvableApiException) exception).getResolution().getIntentSender(),
              REQUEST_TURN_DEVICE_LOCATION_ON,
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
                mBinding.runButton,
                R.string.location_required_error,
                Snackbar.LENGTH_INDEFINITE
            )
            .setAction(android.R.string.ok, v -> {
              checkDeviceLocationEnabledAndEnableLocation(false, true);
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

    if (requestCode == REQUEST_TURN_DEVICE_LOCATION_ON) {
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
                    DEFAULT_ZOOM
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
        checkDeviceLocationEnabledAndEnableLocation(false, false);
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
                  DEFAULT_ZOOM
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
              mBinding.runButton,
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
      checkDeviceLocationEnabledAndEnableLocation(false, true);
    }
  }
}