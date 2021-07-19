package com.example.android.forrest.ui.welcome.calories;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.example.android.forrest.R;
import com.example.android.forrest.databinding.FragmentCaloriesBinding;
import com.example.android.forrest.ui.MainActivity;
import com.example.android.forrest.utils.FirebaseUtils;
import com.example.android.forrest.views.customlongdialog.CustomNumberPickerDialog;
import com.example.android.forrest.views.customlongdialog.PickerType;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

import static android.content.Context.MODE_PRIVATE;

@AndroidEntryPoint
public class CaloriesFragment extends Fragment implements
                                               CustomNumberPickerDialog.NumberPickerDialogListener {
  @Inject
  FirebaseUser mFirebaseUser;
  private SharedPreferences       mPreferences;
  private FragmentCaloriesBinding mBinding;
  private CaloriesViewModel       mViewModel;

  public static CaloriesFragment newInstance() {
    return new CaloriesFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    // If is new user no welcome screen is displayed
    mPreferences = requireContext().getSharedPreferences(MainActivity.sharedPrefFile, MODE_PRIVATE);
    //    if (false) {
    if (!mPreferences.getBoolean(MainActivity.IS_NEW_USER_KEY, false)) {
      NavDirections goHomeDirections =
          CaloriesFragmentDirections.actionCaloriesFragmentToHomeFragment();
      NavHostFragment.findNavController(this).navigate(goHomeDirections);
    }

    mBinding = FragmentCaloriesBinding.inflate(inflater, container, false);

    String username     = FirebaseUtils.getUsername(mFirebaseUser.getDisplayName());
    String welcomeTitle = getString(R.string.welcome_title, username);
    mBinding.actionBar.toolbar.setTitle(welcomeTitle);

    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull @NotNull View view,
      @Nullable Bundle savedInstanceState) {
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
      if (height != null && height > 0) {
        String heightText = height + " cm";
        mBinding.heightChip.setText(heightText);
      }
    });

    mViewModel.weight.observe(getViewLifecycleOwner(), weight -> {
      if (weight != null && weight > 0) {
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

    mViewModel.showToastInt.observe(getViewLifecycleOwner(), resourceId -> {
      Toast.makeText(requireContext(), resourceId, Toast.LENGTH_LONG).show();
    });
  }

  private void openDialog(@NonNull PickerType type) {
    CustomNumberPickerDialog pickerFragment = new CustomNumberPickerDialog(type, this);
    pickerFragment.show(getParentFragmentManager(), CustomNumberPickerDialog.class.getSimpleName());
  }

  @Override
  public void onDialogPositiveClick(Double value, PickerType type) {
    if (PickerType.HEIGHT == type) {
      mViewModel.height.setValue(value.intValue());
    } else {
      mViewModel.weight.setValue(value);
    }
  }
}