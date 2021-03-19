package com.example.android.forrest.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.android.forrest.R;
import com.example.android.forrest.databinding.ActivityMainBinding;
import com.example.android.forrest.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
  public static String sharedPrefFile = "com.example.android.forrestsharedprefs";
  public static String IS_NEW_USER_KEY = "isNewUser";

  private FirebaseAuth mAuth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

    mAuth = FirebaseAuth.getInstance();

    if (mAuth.getCurrentUser() == null) {
      Intent intent = new Intent(this, LoginActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(intent);
    }

    NavHostFragment navHostFragment =
        (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
    NavController navController = navHostFragment.getNavController();
    NavigationUI.setupActionBarWithNavController(this, navController);
  }

  @Override
  public boolean onSupportNavigateUp() {
    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    return navController.navigateUp();
  }
}