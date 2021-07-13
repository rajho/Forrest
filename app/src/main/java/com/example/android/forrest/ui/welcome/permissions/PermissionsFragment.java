package com.example.android.forrest.ui.welcome.permissions;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.example.android.forrest.R;
import com.example.android.forrest.databinding.FragmentCaloriesBinding;
import com.example.android.forrest.databinding.FragmentPermissionsBinding;
import com.example.android.forrest.ui.welcome.calories.CaloriesFragmentDirections;
import com.example.android.forrest.ui.welcome.calories.CaloriesViewModel;
import com.example.android.forrest.widget.FirebaseUtils;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PermissionsFragment extends Fragment {

  private FragmentPermissionsBinding mBinding;
  private PermissionsViewModel    mViewModel;

  @Inject
  FirebaseUser mFirebaseUser;

  public static PermissionsFragment newInstance() {
    return new PermissionsFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    mBinding = FragmentPermissionsBinding.inflate(inflater, container, false);

    String username = FirebaseUtils.getUsername(mFirebaseUser.getDisplayName());
    String welcomeTitle = getString(R.string.welcome_title, username);
    mBinding.toolbar.setTitle(welcomeTitle);

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

  private void setUpObservers() {
    mViewModel.getNavigateToGoalScreen().observe(getViewLifecycleOwner(), aBoolean -> {
      NavDirections navToGoalScreen = PermissionsFragmentDirections.actionPermissionFragmentToGoalFragment();
      NavHostFragment.findNavController(this).navigate(navToGoalScreen);
    });
  }

  private void setUpListeners() {
    mBinding.locationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
      if (isChecked) {


        buttonView.setVisibility(View.INVISIBLE);
        mBinding.checkImage.setVisibility(View.VISIBLE);
      } else {
        mBinding.checkImage.setVisibility(View.GONE);
        buttonView.setVisibility(View.VISIBLE);
      }
    });
  }
}