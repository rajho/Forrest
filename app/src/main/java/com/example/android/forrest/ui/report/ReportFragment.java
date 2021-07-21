package com.example.android.forrest.ui.report;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.example.android.forrest.data.model.Exercise;
import com.example.android.forrest.databinding.FragmentReportBinding;

public class ReportFragment extends Fragment {

  private FragmentReportBinding mBinding;
  private ReportViewModel       mViewModel;

  public static ReportFragment newInstance() {
    return new ReportFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    mBinding = FragmentReportBinding.inflate(inflater, container, false);
    mBinding.setLifecycleOwner(this);
    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
    mBinding.setViewmodel(mViewModel);

    Exercise exercise = ReportFragmentArgs.fromBundle(getArguments()).getExercise();
    mViewModel.setExercise(exercise);

    setUpObservers();
  }

  private void setUpObservers() {
    mViewModel._goHome.observe(getViewLifecycleOwner(), aBoolean -> {
      NavDirections goHomeDirection =
          ReportFragmentDirections.actionReportFragmentToHomeFragment();
      NavHostFragment.findNavController(this).navigate(goHomeDirection);
    });
  }
}