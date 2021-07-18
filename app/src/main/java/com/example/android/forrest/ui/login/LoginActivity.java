package com.example.android.forrest.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.android.forrest.R;
import com.example.android.forrest.databinding.ActivityLoginBinding;
import com.example.android.forrest.ui.MainActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {
  private SharedPreferences mPreferences;

  private ProgressBar                  loading;
  private ExtendedFloatingActionButton loginFacebookButton;

  private FirebaseAuth    mAuth;
  private CallbackManager mCallbackManager;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

    mPreferences = getSharedPreferences(MainActivity.sharedPrefFile, MODE_PRIVATE);
    mAuth        = FirebaseAuth.getInstance();

    loading             = binding.loading;
    loginFacebookButton = binding.signInFbButton;

    mCallbackManager = CallbackManager.Factory.create();

    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.hide();
    }

    setUpViewsListeners();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    mCallbackManager.onActivityResult(requestCode, resultCode, data);
  }


  private void setUpViewsListeners() {

    // facebook button
    loginFacebookButton.setOnClickListener(v -> {
      loading.setVisibility(View.VISIBLE);
      loginFacebookButton.setEnabled(false);
      LoginManager loginManager = LoginManager.getInstance();
      loginManager.logInWithReadPermissions(
          this,
          Arrays.asList("email", "public_profile")
      );
      loginManager.registerCallback(
          mCallbackManager,
          new FacebookCallback<com.facebook.login.LoginResult>() {
            @Override
            public void onSuccess(com.facebook.login.LoginResult loginResult) {
              Timber.d("facebook:onSuccess:%s", loginResult);
              handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
              loading.setVisibility(View.GONE);
              Timber.d("facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
              loading.setVisibility(View.GONE);
              Timber.d(error, "facebook:onError");
            }
          }
      );
    });
  }

  private void handleFacebookAccessToken(AccessToken token) {
    Timber.d("handleFacebookAccessToken:%s", token);

    AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
    mAuth.signInWithCredential(credential)
         .addOnCompleteListener(this, task -> {
           if (task.isSuccessful()) {
             boolean isNewUser = Objects.requireNonNull(task.getResult().getAdditionalUserInfo()).isNewUser();
             mPreferences.edit().putBoolean(MainActivity.IS_NEW_USER_KEY, isNewUser).apply();

             // Sign in success, update UI with the signed-in user's information
             loading.setVisibility(View.GONE);
             goHome();
           } else {
             // If sign in fails, display a message to the user.
             Timber.w(task.getException(), "signInWithCredential:failure");
             Toast.makeText(LoginActivity.this, "Authentication failed.",
                 Toast.LENGTH_SHORT
             ).show();
             loading.setVisibility(View.GONE);
             loginFacebookButton.setVisibility(View.VISIBLE);
           }
         });
  }


  private void goHome() {
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
  }
}