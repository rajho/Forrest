package com.example.android.forrest.ui.welcome.permissions;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
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
import com.example.android.forrest.R;
import com.example.android.forrest.databinding.FragmentPermissionsBinding;
import com.example.android.forrest.utils.FirebaseUtils;
import com.example.android.forrest.utils.Permissions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PermissionsFragment extends Fragment {

  @Inject
  FirebaseAuth mFirebaseAuth;
  private FragmentPermissionsBinding mBinding;
  private PermissionsViewModel       mViewModel;

  private Snackbar mSnackbar;

  public static PermissionsFragment newInstance() {
    return new PermissionsFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    mBinding = FragmentPermissionsBinding.inflate(inflater, container, false);

    FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
    String username;
    if (currentUser != null) {
      username = FirebaseUtils.getUsername(mFirebaseAuth.getCurrentUser().getDisplayName());
    } else {
      username = "";
    }
    String welcomeTitle = getString(R.string.welcome_title, username);
    mBinding.actionBar.toolbar.setTitle(welcomeTitle);

    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull @NotNull View view,
      @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    mViewModel = new ViewModelProvider(this).get(PermissionsViewModel.class);
    mBinding.setViewmodel(mViewModel);

    setUpObservers();
    setUpListeners();
  }

  @Override
  public void onStart() {
    super.onStart();
    checkLocationPermission();
  }

  @Override
  public void onRequestPermissionsResult(int requestCode,
      @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    boolean fineLocationDenied = grantResults[0] == PackageManager.PERMISSION_DENIED;

    if (fineLocationDenied) {
      mBinding.locationSwitch.setChecked(false);
      mSnackbar = Snackbar
          .make(
              mBinding.nextButton,
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
      mBinding.locationSwitch.setChecked(true);
    }
  }

  private void setUpObservers() {
    mViewModel.getNavigateToGoalScreen().observe(getViewLifecycleOwner(), aBoolean -> {
      NavDirections navToGoalScreen =
          PermissionsFragmentDirections.actionPermissionFragmentToGoalFragment();
      NavHostFragment.findNavController(this).navigate(navToGoalScreen);
    });
  }

  private void setUpListeners() {
    mBinding.locationSwitch.setOnClickListener(v -> Permissions.requestLocationPermission(this));

    mBinding.locationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
      if (isChecked) {
        enablePermissionCheck();
      } else {
        disablePermissionCheck();
      }
    });
  }

  private void checkLocationPermission() {
    mBinding.locationSwitch.setChecked(Permissions.isLocationPermissionGranted(requireContext()));
  }

  private void enablePermissionCheck() {
    mBinding.locationSwitch.setVisibility(View.INVISIBLE);
    mBinding.checkImage.setVisibility(View.VISIBLE);
  }

  private void disablePermissionCheck() {
    mBinding.checkImage.setVisibility(View.GONE);
    mBinding.locationSwitch.setVisibility(View.VISIBLE);
  }
}