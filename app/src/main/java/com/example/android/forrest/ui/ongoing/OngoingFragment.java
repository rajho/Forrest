package com.example.android.forrest.ui.ongoing;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.forrest.databinding.FragmentOngoingBinding;
import com.example.android.forrest.utils.SensorStepDetector;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OngoingFragment extends Fragment {
  @Inject
  FirebaseUser mFirebaseUser;
  private FragmentOngoingBinding mBinding;
  private OngoingViewModel       mViewModel;


  private SensorEventListener mStepDetectorListener;

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
  }
}