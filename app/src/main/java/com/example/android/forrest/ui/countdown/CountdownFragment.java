package com.example.android.forrest.ui.countdown;

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

import com.example.android.forrest.R;
import com.example.android.forrest.databinding.FragmentCaloriesBinding;
import com.example.android.forrest.databinding.FragmentCountdownBinding;
import com.example.android.forrest.ui.welcome.goal.GoalViewModel;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CountdownFragment extends Fragment {

  private FragmentCountdownBinding mBinding;
  private CountdownViewModel mViewModel;

  public static CountdownFragment newInstance() {
    return new CountdownFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    mBinding = FragmentCountdownBinding.inflate(inflater, container, false);
    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    mViewModel = new ViewModelProvider(this).get(CountdownViewModel.class);
    mBinding.setViewmodel(mViewModel);

    setUpObservers();
  }

  private void setUpObservers() {
    mViewModel.getCountDownFinish().observe(getViewLifecycleOwner(), aBoolean -> {
      NavDirections ongoingScreen =
          CountdownFragmentDirections.actionCountdownFragmentToOngoingFragment();
      NavHostFragment.findNavController(this).navigate(ongoingScreen);
    });

    mViewModel.currentTimeString.observe(getViewLifecycleOwner(),
        s -> mBinding.countdownNumber.setText(s)
    );
  }
}