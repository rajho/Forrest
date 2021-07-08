package com.example.android.forrest.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

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

import timber.log.Timber;


public class LoginActivity extends AppCompatActivity {
  private SharedPreferences mPreferences;

  private EditText                     usernameEditText;
  private EditText                     passwordEditText;
  private Button                       loginButton;
  private ProgressBar                  loadingProgressBar;
  private ExtendedFloatingActionButton loginFacebookButton;

  private LoginViewModel  loginViewModel;
  private FirebaseAuth    mAuth;
  private CallbackManager mCallbackManager;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

    mPreferences   = getSharedPreferences(MainActivity.sharedPrefFile, MODE_PRIVATE);
    mAuth          = FirebaseAuth.getInstance();
    loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
        .get(LoginViewModel.class);

//    usernameEditText    = binding.username;
//    passwordEditText    = binding.password;
//    loginButton         = binding.login;
//    loadingProgressBar  = binding.loading;
    loginFacebookButton = binding.signInFbButton;

    mCallbackManager = CallbackManager.Factory.create();

    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.hide();
    }


    setUpObservers();
    setUpViewsListeners();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    mCallbackManager.onActivityResult(requestCode, resultCode, data);
  }

  private void setUpObservers() {
    loginViewModel.getLoginFormState().observe(this, loginFormState -> {
      if (loginFormState == null) {
        return;
      }
      loginButton.setEnabled(loginFormState.isDataValid());
      if (loginFormState.getUsernameError() != null) {
        usernameEditText.setError(getString(loginFormState.getUsernameError()));
      }
      if (loginFormState.getPasswordError() != null) {
        passwordEditText.setError(getString(loginFormState.getPasswordError()));
      }
    });

    loginViewModel.getLoginResult().observe(this, loginResult -> {
      if (loginResult == null) {
        return;
      }
      loadingProgressBar.setVisibility(View.GONE);
      if (loginResult.getError() != null) {
        showLoginFailed(loginResult.getError());
      }
      if (loginResult.getSuccess() != null) {
        goHome();
      }
      setResult(Activity.RESULT_OK);

      //Complete and destroy login activity once successful
      finish();
    });
  }

  private void setUpViewsListeners() {
    // defining behavior on text changed in any of the fields in the login
//    TextWatcher afterTextChangedListener = new TextWatcher() {
//      @Override
//      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//        // ignore
//      }
//
//      @Override
//      public void onTextChanged(CharSequence s, int start, int before, int count) {
//        // ignore
//      }
//
//      @Override
//      public void afterTextChanged(Editable s) {
//        loginViewModel.loginDataChanged(
//            usernameEditText.getText().toString(),
//            passwordEditText.getText().toString()
//        );
//      }
//    };

//    usernameEditText.addTextChangedListener(afterTextChangedListener);
//    passwordEditText.addTextChangedListener(afterTextChangedListener);
//    passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
//      if (actionId == EditorInfo.IME_ACTION_DONE) {
//        loginViewModel.login(
//            usernameEditText.getText().toString(),
//            passwordEditText.getText().toString()
//        );
//      }
//      return false;
//    });
//
//    loginButton.setOnClickListener(v -> {
//      loadingProgressBar.setVisibility(View.VISIBLE);
//      loginViewModel.login(
//          usernameEditText.getText().toString(),
//          passwordEditText.getText().toString()
//      );
//    });

    // facebook button
    loginFacebookButton.setOnClickListener(v -> {
      loadingProgressBar.setVisibility(View.VISIBLE);
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
              loadingProgressBar.setVisibility(View.GONE);
              Timber.d("facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
              loadingProgressBar.setVisibility(View.GONE);
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
             boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();
             mPreferences.edit().putBoolean(MainActivity.IS_NEW_USER_KEY, isNewUser).apply();

             // Sign in success, update UI with the signed-in user's information
             Timber.d("signInWithCredential:success");
             loadingProgressBar.setVisibility(View.GONE);
             goHome();
           } else {
             // If sign in fails, display a message to the user.
             Timber.w(task.getException(), "signInWithCredential:failure");
             Toast.makeText(LoginActivity.this, "Authentication failed.",
                 Toast.LENGTH_SHORT
             ).show();
             loadingProgressBar.setVisibility(View.GONE);
             loginFacebookButton.setVisibility(View.VISIBLE);
             // updateUI(null);
           }
         });
  }


  private void goHome() {
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
  }

  private void showLoginFailed(@StringRes Integer errorString) {
    Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
  }
}