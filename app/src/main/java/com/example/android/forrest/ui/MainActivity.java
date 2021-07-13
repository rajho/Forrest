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

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
  public static String sharedPrefFile = "com.example.android.forrestsharedprefs";
  public static String IS_NEW_USER_KEY = "isNewUser";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    DataBindingUtil.setContentView(this, R.layout.activity_main);

    FirebaseAuth auth = FirebaseAuth.getInstance();

    if (auth.getCurrentUser() == null) {
      Intent intent = new Intent(this, LoginActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(intent);
    }
  }
}