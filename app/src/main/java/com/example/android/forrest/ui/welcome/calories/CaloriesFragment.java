package com.example.android.forrest.ui.welcome.calories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.forrest.R;
import com.example.android.forrest.databinding.FragmentCaloriesBinding;

public class CaloriesFragment extends Fragment {

  private CaloriesViewModel mViewModel;

  public static CaloriesFragment newInstance() {
    return new CaloriesFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    FragmentCaloriesBinding binding = FragmentCaloriesBinding.inflate(inflater, container, false);

    String welcomeTitle = getString(R.string.welcome_title, "Ramiro");
    binding.toolbar.setTitle(welcomeTitle);
    return binding.getRoot();
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mViewModel = new ViewModelProvider(this).get(CaloriesViewModel.class);
    // TODO: Use the ViewModel
  }

}