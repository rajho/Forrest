package com.example.android.forrest.ui.welcome.goal;

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

import com.example.android.forrest.R;
import com.example.android.forrest.databinding.FragmentGoalBinding;
import com.example.android.forrest.databinding.FragmentPermissionsBinding;
import com.example.android.forrest.ui.welcome.permissions.PermissionsFragmentDirections;
import com.example.android.forrest.ui.welcome.permissions.PermissionsViewModel;
import com.example.android.forrest.widget.FirebaseUtils;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class GoalFragment extends Fragment {

  @Inject
  FirebaseUser mFirebaseUser;
  private FragmentGoalBinding mBinding;
  private GoalViewModel       mViewModel;

  public static GoalFragment newInstance() {
    return new GoalFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    mBinding = FragmentGoalBinding.inflate(inflater, container, false);

    String username     = FirebaseUtils.getUsername(mFirebaseUser.getDisplayName());
    String welcomeTitle = getString(R.string.welcome_title, username);
    mBinding.actionBar.toolbar.setTitle(welcomeTitle);

    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull @NotNull View view,
      @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    mViewModel = new ViewModelProvider(this).get(GoalViewModel.class);
    mBinding.setViewmodel(mViewModel);

    setUpObservers();
    setUpListeners();
  }

  private void setUpObservers() {
    mViewModel.getNavigateToHomeScreen().observe(getViewLifecycleOwner(), aBoolean -> {
      NavDirections navToHomeScreen =
          GoalFragmentDirections.actionGoalFragmentToHomeFragment();
      NavHostFragment.findNavController(this).navigate(navToHomeScreen);
    });
  }

  private void setUpListeners() {
  }

}