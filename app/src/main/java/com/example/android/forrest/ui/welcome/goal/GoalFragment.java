package com.example.android.forrest.ui.welcome.goal;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.example.android.forrest.R;
import com.example.android.forrest.data.model.User;
import com.example.android.forrest.databinding.FragmentGoalBinding;
import com.example.android.forrest.utils.FirebaseUtils;
import com.example.android.forrest.utils.TimeUtils;
import com.example.android.forrest.widget.customlongdialog.CustomNumberPickerDialog;
import com.example.android.forrest.widget.customlongdialog.PickerType;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class GoalFragment extends Fragment implements
                                           CustomNumberPickerDialog.NumberPickerDialogListener {

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
      @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    mViewModel = new ViewModelProvider(this).get(GoalViewModel.class);
    mBinding.setViewmodel(mViewModel);

    setUpInitialViews();
    setUpObservers();
    setUpListeners();
  }

  private void setUpInitialViews() {
    String[] frequencyOptions = getResources().getStringArray(R.array.frequency_array);
    String[] unitOptions      = getResources().getStringArray(R.array.units_array);
    mViewModel.frequencyOptions.setValue(frequencyOptions);
    mViewModel.unitOptions.setValue(unitOptions);
  }

  private void setUpObservers() {
    mViewModel.getNavigateToHomeScreen().observe(getViewLifecycleOwner(), aBoolean -> {
      NavDirections navToHomeScreen =
          GoalFragmentDirections.actionGoalFragmentToHomeFragment();
      NavHostFragment.findNavController(this).navigate(navToHomeScreen);
    });

    mViewModel.getOpenSetGoalDialog().observe(getViewLifecycleOwner(), aBoolean -> {
      String[] units = getResources().getStringArray(R.array.units_array);
      if (units[0].equals(mViewModel.unitSelected.getValue())) {
        // Open Distance dialog
        openDialog(PickerType.DISTANCE);
      } else if (units[1].equals(mViewModel.unitSelected.getValue())) {
        // Open Duration dialog
        openDialog(PickerType.DURATION);
      }
    });

    mViewModel.goal.observe(getViewLifecycleOwner(), aDouble -> {
      if (aDouble != null) {
        String[] units        = getResources().getStringArray(R.array.units_array);
        String   unitSelected = mViewModel.unitSelected.getValue();

        if (units[0].equals(unitSelected)) {
          // Distance
          mBinding.goalButton.setText(TimeUtils.getFormattedDistanceFromNumber(aDouble));
        } else if (units[1].equals(unitSelected)) {
          // Duration
          mBinding.goalButton.setText(TimeUtils.getFormattedTimeFromNumber(aDouble));

          mViewModel.setCaloriesBurnt();
        }
      } else {
        mBinding.goalButton.setText(getString(R.string.set_goal));
      }
    });

    mViewModel.getUserById(mFirebaseUser.getUid()).observe(
        getViewLifecycleOwner(),
        user -> mViewModel.user.setValue(user)
    );

    mViewModel.getCaloriesBurnt().observe(getViewLifecycleOwner(), caloriesBurnt -> {
      if (caloriesBurnt != null && caloriesBurnt > 0) {
        String caloriesMessage = getString(R.string.welcome_burning_calories, caloriesBurnt);
        mBinding.caloriesBurningText.setText(caloriesMessage);
        mBinding.caloriesBurningText.setVisibility(View.VISIBLE);
      }
    });

    mViewModel.unitSelected.observe(getViewLifecycleOwner(), s -> {
      if (getResources().getStringArray(R.array.units_array)[0].equals(s)){
        // Distance
        mViewModel.goal.setValue(null);
        disableCaloriesInfo();
      } else {
        mViewModel.goal.setValue(null);
        enableCaloriesInfo();
      }
    });
  }

  private void setUpListeners() {
    mBinding.frequencyField.setOnItemClickListener((parent, view, position, id) -> {
      String frequency = getResources().getStringArray(R.array.frequency_array)[position];
      mViewModel.frequencySelected.setValue(frequency);
    });

    mBinding.unitField.setOnItemClickListener((parent, view, position, id) -> {
      String frequency = getResources().getStringArray(R.array.units_array)[position];
      mViewModel.unitSelected.setValue(frequency);
    });
  }

  private void openDialog(@NonNull PickerType type) {
    CustomNumberPickerDialog pickerFragment = new CustomNumberPickerDialog(type, this);
    pickerFragment.show(getParentFragmentManager(), CustomNumberPickerDialog.class.getSimpleName());
  }

  public void enableCaloriesInfo() {
    mBinding.caloriesBurningText.setVisibility(View.VISIBLE);
    mBinding.mealEquivalentText.setVisibility(View.VISIBLE);
    mBinding.mealEquivalentImage.setVisibility(View.VISIBLE);
  }

  public void disableCaloriesInfo() {
    mBinding.caloriesBurningText.setVisibility(View.GONE);
    mBinding.mealEquivalentText.setVisibility(View.GONE);
    mBinding.mealEquivalentImage.setVisibility(View.GONE);
  }


  @Override
  public void onDialogPositiveClick(Double value, PickerType type) {
    mViewModel.goal.setValue(value);
  }
}