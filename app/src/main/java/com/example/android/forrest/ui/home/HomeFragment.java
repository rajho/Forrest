package com.example.android.forrest.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.android.forrest.R;
import com.example.android.forrest.databinding.FragmentHomeBinding;
import com.example.android.forrest.ui.home.progress.ProgressFragment;
import com.example.android.forrest.ui.home.run.RunFragment;
import com.example.android.forrest.ui.login.LoginActivity;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

  @Inject
  FirebaseAuth mAuth;
  private FragmentHomeBinding mBinding;
  private HomeViewModel       mViewModel;

  public static HomeFragment newInstance() {
    return new HomeFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    mBinding = FragmentHomeBinding.inflate(inflater, container, false);
    mAuth    = FirebaseAuth.getInstance();

    setHasOptionsMenu(true);
    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view,
      @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    mBinding.setViewmodel(mViewModel);
    mBinding.bottomNavigation.setSelectedItemId(R.id.run_page);
    //    setUpObservers();
    setUpListeners();
  }

  private void setUpListeners() {
    mBinding.bottomNavigation.setOnItemSelectedListener(item -> {
      int itemId = item.getItemId();
      if (itemId == R.id.progress_page) {
        getChildFragmentManager().beginTransaction()
                                 .setReorderingAllowed(true)
                                 .replace(R.id.fragment_container_view, ProgressFragment.class, null)
                                 .commit();
        return true;
      }

      if (itemId == R.id.run_page) {
        getChildFragmentManager().beginTransaction()
                                 .setReorderingAllowed(true)
                                 .replace(R.id.fragment_container_view, RunFragment.class, null)
                                 .commit();
        return true;
      }

      return false;
    });
  }
}