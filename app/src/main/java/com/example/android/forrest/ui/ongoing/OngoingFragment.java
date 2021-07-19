package com.example.android.forrest.ui.ongoing;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;

import com.example.android.forrest.R;
import com.example.android.forrest.databinding.FragmentOngoingBinding;

import org.jetbrains.annotations.NotNull;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OngoingFragment extends Fragment {

  private FragmentOngoingBinding mBinding;
  private OngoingViewModel mViewModel;

  public static OngoingFragment newInstance() {
    return new OngoingFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    mBinding = FragmentOngoingBinding.inflate(inflater, container, false);
    mBinding.setLifecycleOwner(this);
    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull @NotNull View view,
      @Nullable  Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mViewModel = new ViewModelProvider(this).get(OngoingViewModel.class);
    mBinding.setViewmodel(mViewModel);

    setUpObservers();
    setUpListeners();
    mBinding.timeRunningText.start();
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
  }
}