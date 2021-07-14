package com.example.android.forrest.ui.welcome.calories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.android.forrest.R;
import com.example.android.forrest.databinding.FragmentCaloriesBinding;
import com.example.android.forrest.widget.CustomLongPickerFragment;
import com.example.android.forrest.widget.FirebaseUtils;
import com.example.android.forrest.widget.PickerType;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CaloriesFragment extends Fragment implements
                                               CustomLongPickerFragment.LongPickerListener {
  private FragmentCaloriesBinding mBinding;
  private CaloriesViewModel       mViewModel;

  @Inject
  FirebaseUser mFirebaseUser;

  public static CaloriesFragment newInstance() {
    return new CaloriesFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    mBinding = FragmentCaloriesBinding.inflate(inflater, container, false);

    String username = FirebaseUtils.getUsername(mFirebaseUser.getDisplayName());
    String welcomeTitle = getString(R.string.welcome_title, username);
    mBinding.actionBar.toolbar.setTitle(welcomeTitle);

    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull @NotNull View view,
      @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    mViewModel = new ViewModelProvider(this).get(CaloriesViewModel.class);
    mBinding.setViewmodel(mViewModel);

    setUpObservers();
  }

  private void setUpObservers() {
    mViewModel.getOpenHeightDialog().observe(getViewLifecycleOwner(), o -> {
      openDialog(PickerType.HEIGHT);
    });

    mViewModel.getOpenWeightDialog().observe(getViewLifecycleOwner(), o -> {
      openDialog(PickerType.WEIGHT);
    });

    mViewModel.height.observe(getViewLifecycleOwner(), height -> {
      if (height != null && height > 0){
        String heightText = height + " cm";
        mBinding.heightChip.setText(heightText);
      }
    });

    mViewModel.weight.observe(getViewLifecycleOwner(), weight -> {
      if (weight != null && weight > 0){
        String weightText = weight + " kg";
        mBinding.weightChip.setText(weightText);
      }
    });

    mViewModel.getNavigateToPermissions().observe(getViewLifecycleOwner(), o -> {
      mViewModel.saveUser();

      NavDirections navToPermissions =
          CaloriesFragmentDirections.actionCaloriesFragmentToPermissionFragment();
      NavHostFragment.findNavController(this).navigate(navToPermissions);
    });
  }

  private void openDialog(@NonNull PickerType type) {
    CustomLongPickerFragment pickerFragment = new CustomLongPickerFragment(type, this);
    pickerFragment.show(getParentFragmentManager(), CustomLongPickerFragment.class.getSimpleName());
  }

  @Override
  public void onDialogPositiveClick(Double value, PickerType type) {
    if (PickerType.HEIGHT == type) {
      mViewModel.height.setValue(value.intValue());
    } else {
      mViewModel.weight.setValue(value.floatValue());
    }
  }
}