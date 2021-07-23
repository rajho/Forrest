package com.example.android.forrest.ui.welcome.goal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.android.forrest.R;
import com.example.android.forrest.data.source.remote.MealDto;
import com.example.android.forrest.databinding.FragmentGoalBinding;
import com.example.android.forrest.ui.MainActivity;
import com.example.android.forrest.utils.FirebaseUtils;
import com.example.android.forrest.data.source.remote.NetworkApi;
import com.example.android.forrest.data.source.remote.MealsJsonUtils;
import com.example.android.forrest.utils.TimeUtils;
import com.example.android.forrest.views.customnumberdialog.CustomNumberPickerDialog;
import com.example.android.forrest.views.customnumberdialog.PickerType;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

import static android.content.Context.MODE_PRIVATE;

@AndroidEntryPoint
public class GoalFragment extends Fragment implements
                                           CustomNumberPickerDialog.NumberPickerDialogListener,
                                           LoaderManager.LoaderCallbacks<List<MealDto>> {
  private static final int API_SEARCH_FOOD_LOADER = 40;


  private static final String SEARCH_FOOD_URL = "url";

  private static final int CALORIES_DEVIATION = 1;
  private static final int SEARCH_RESULTS_NUMBER = 10;

  @Inject
  FirebaseAuth mFirebaseAuth;
  private FragmentGoalBinding mBinding;
  private GoalViewModel       mViewModel;
  private FirebaseUser mFirebaseUser;

  public static GoalFragment newInstance() {
    return new GoalFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    mBinding = FragmentGoalBinding.inflate(inflater, container, false);

    mFirebaseUser = mFirebaseAuth.getCurrentUser();
    String username;
    if (mFirebaseUser != null) {
      username = FirebaseUtils.getUsername(mFirebaseUser.getDisplayName());
    } else {
      username = "";
    }

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
      // Setting the user as not new so the welcome set up screens are skipped
      SharedPreferences preferences = requireContext().getSharedPreferences(MainActivity.sharedPrefFile, MODE_PRIVATE);
      preferences.edit().putBoolean(MainActivity.IS_NEW_USER_KEY, false).apply();

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

    mViewModel.goalValue.observe(getViewLifecycleOwner(), aDouble -> {
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
        enableCaloriesInfo();

        startSearchFoodLoader(caloriesBurnt);
      }
    });

    mViewModel.unitSelected.observe(getViewLifecycleOwner(), s -> {
      if (getResources().getStringArray(R.array.units_array)[0].equals(s)) {
        // Distance
        mViewModel.goalValue.setValue(null);
        mBinding.caloriesBurningText.setText(null);
        mBinding.mealEquivalentText.setText(null);
        mBinding.mealEquivalentImage.setImageDrawable(null);
        disableCaloriesInfo();
      } else {
        mViewModel.goalValue.setValue(null);
        enableCaloriesInfo();
      }
    });

    mViewModel.showToastInt.observe(getViewLifecycleOwner(), resourceId -> {
      Toast.makeText(requireContext(), resourceId, Toast.LENGTH_LONG).show();
    });
  }

  private void startSearchFoodLoader(@NonNull Double caloriesBurnt) {
    // Getting params for API request
    String maxCalories = String.format(Locale.getDefault(), "%.2f",
        caloriesBurnt + CALORIES_DEVIATION
    );
    String minCalories = String.format(Locale.getDefault(), "%.2f",
        caloriesBurnt - CALORIES_DEVIATION
    );
    String resultsNumber = String.valueOf(SEARCH_RESULTS_NUMBER);

    URL spoonacularSearchUrl = NetworkApi.buildUrl(maxCalories, minCalories, resultsNumber);

    if (spoonacularSearchUrl != null) {
      Bundle queryBundle = new Bundle();
      queryBundle.putString(SEARCH_FOOD_URL, spoonacularSearchUrl.toString());

      LoaderManager loaderManager = LoaderManager.getInstance(this);
      loaderManager.restartLoader(API_SEARCH_FOOD_LOADER, queryBundle, this);
    }
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
    mViewModel.goalValue.setValue(value);
  }

  @NonNull
  @Override
  public Loader<List<MealDto>> onCreateLoader(int id, @Nullable Bundle args) {
    return new AsyncTaskLoader<List<MealDto>>(requireContext()) {

      @Override
      protected void onStartLoading() {
        if (args == null) {
          return;
        }

        mBinding.loadingIndicator.setVisibility(View.VISIBLE);
        forceLoad();
      }

      @Nullable
      @Override
      public List<MealDto> loadInBackground() {
        assert args != null;
        String searchFoodUrlString = args.getString(SEARCH_FOOD_URL);
        if (TextUtils.isEmpty(searchFoodUrlString)) {
          return null;
        }

        try {
          URL spoonacularUrl = new URL(searchFoodUrlString);
          String jsonSearchResult = NetworkApi.getResponseFromHttpUrl(spoonacularUrl);
          return MealsJsonUtils.getMealsListFromJson(jsonSearchResult);
        } catch (IOException e) {
          e.printStackTrace();
          return null;
        }
      }

      @Override
      public void deliverResult(@Nullable List<MealDto> data) {
        super.deliverResult(data);
      }
    };
  }

  @Override
  public void onLoadFinished(@NonNull Loader<List<MealDto>> loader, List<MealDto> data) {
    mBinding.loadingIndicator.setVisibility(View.GONE);
    if (data != null && data.size() > 0) {
      MealDto meal = data.get(0);
      String equivalentMealText = getString(R.string.meal_equivalent_text, meal.getTitle());
      mBinding.mealEquivalentText.setText(Html.fromHtml(equivalentMealText));
      Glide.with(this).load(meal.getImage()).into(mBinding.mealEquivalentImage);
    }
  }

  @Override
  public void onLoaderReset(@NonNull Loader<List<MealDto>> loader) {

  }
}