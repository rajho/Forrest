package com.example.android.forrest.ui.home.run;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.forrest.R;
import com.example.android.forrest.databinding.FragmentCaloriesBinding;
import com.example.android.forrest.databinding.FragmentRunBinding;

import org.jetbrains.annotations.NotNull;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RunFragment extends Fragment {

  private FragmentRunBinding mBinding;
  private RunViewModel       mViewModel;

  public static RunFragment newInstance() {
    return new RunFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    mBinding = FragmentRunBinding.inflate(inflater, container, false);
    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    mViewModel = new ViewModelProvider(this).get(RunViewModel.class);
    mBinding.setViewmodel(mViewModel);
  }


}