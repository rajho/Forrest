package com.example.android.forrest.ui.home.progress;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.forrest.R;
import com.example.android.forrest.data.model.Exercise;
import com.example.android.forrest.databinding.FragmentProgressBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProgressFragment extends Fragment {

  @Inject
  FirebaseAuth mFirebaseAuth;
  private ProgressViewModel mViewModel;
  private FragmentProgressBinding mBinding;

  public static ProgressFragment newInstance() {
    return new ProgressFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    mBinding = FragmentProgressBinding.inflate(inflater, container, false);
    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull @NotNull View view,
      @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mViewModel = new ViewModelProvider(this).get(ProgressViewModel.class);
    setUpObservers();
  }

  private void setUpObservers() {
    FirebaseUser user = mFirebaseAuth.getCurrentUser();
    if (user != null) {
      mViewModel.getExercisesByUserId(mFirebaseAuth.getCurrentUser().getUid()).observe(
          getViewLifecycleOwner(),
          exercises -> {
            ExercisesAdapter adapter = new ExercisesAdapter(exercises);
            mBinding.exercisesList.setAdapter(adapter);
          }
      );
    }
  }

}