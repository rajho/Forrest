package com.example.android.forrest.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.android.forrest.R;
import com.example.android.forrest.databinding.ActivityMainBinding;
import com.example.android.forrest.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {
  private SharedPreferences mSP;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

    mSP = getSharedPreferences(LoginActivity.sharedPrefFile, MODE_PRIVATE);
    boolean isloggedIn = mSP.getBoolean(LoginActivity.LOGGED_IN_KEY, false);

    if (!isloggedIn) {
      Intent intent = new Intent(this, LoginActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(intent);
    }


  }


}