package com.example.android.forrest.ui.home;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.forrest.R;
import com.example.android.forrest.databinding.FragmentHomeBinding;
import com.example.android.forrest.ui.login.LoginActivity;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public class HomeFragment extends Fragment {

  private HomeViewModel mViewModel;
  private FirebaseAuth mAuth;

  public static HomeFragment newInstance() {
    return new HomeFragment();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    FragmentHomeBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home,
        container, false);

    mAuth = FirebaseAuth.getInstance();

    setHasOptionsMenu(true);
    return binding.getRoot();
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    // TODO: Use the ViewModel
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.menu_home, menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    int id = item.getItemId();
    if (id == R.id.logout_item) {
      mAuth.signOut();

      if (Profile.getCurrentProfile() != null) {
        // Logging out facebook
        LoginManager.getInstance().logOut();
      }

      Intent intent = new Intent(getActivity(), LoginActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(intent);
    }

    return super.onOptionsItemSelected(item);
  }
}