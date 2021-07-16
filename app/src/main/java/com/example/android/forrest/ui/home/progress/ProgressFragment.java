package com.example.android.forrest.ui.home.progress;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.forrest.R;

import org.jetbrains.annotations.NotNull;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProgressFragment extends Fragment {

  private ProgressViewModel mViewModel;

  public static ProgressFragment newInstance() {
    return new ProgressFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_progress, container, false);
  }

  @Override
  public void onViewCreated(@NonNull @NotNull View view,
      @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mViewModel = new ViewModelProvider(this).get(ProgressViewModel.class);
  }

}