package com.example.meetplan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meetplan.databinding.ActivityLoginBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private LoginClickListener loginClickListener;
    private LoginCallBack loginCallBack;
    private SignupClickListener signupClickListener;
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }

        loginClickListener = new LoginClickListener();
        binding.loginButton.setOnClickListener(loginClickListener);

        signupClickListener = new SignupClickListener();
        binding.signupButton.setOnClickListener(signupClickListener);
    }

    private void loginUser(String username, String password) {
        loginCallBack = new LoginCallBack();
        ParseUser.logInInBackground(username, password, loginCallBack);
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void goSignupActivity() {
        Intent i = new Intent(this, SignupActivity.class);
        startActivity(i);
    }

    private class LoginClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String username = binding.username.getText().toString();
            String password = binding.password.getText().toString();
            loginUser(username, password);
        }
    }

    private class LoginCallBack implements LogInCallback {
        @Override
        public void done(ParseUser user, ParseException e) {
            if (e != null) {
                Snackbar.make(binding.getRoot(), R.string.login_failed, BaseTransientBottomBar.LENGTH_SHORT).show();
                return;
            }
            goMainActivity();
            Snackbar.make(binding.getRoot(), R.string.success, BaseTransientBottomBar.LENGTH_SHORT).show();
        }
    }

    private class SignupClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            goSignupActivity();
        }
    }
}