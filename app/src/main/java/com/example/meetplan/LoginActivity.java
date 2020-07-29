package com.example.meetplan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meetplan.databinding.ActivityLoginBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Activity to log into a MeetPlan account.
 * Users enter their email and password and directed into their account
 * if login is successful.
 * */
public class LoginActivity extends AppCompatActivity {

    /** Click listener that gets the user input from the text fields. */
    private LoginClickListener loginClickListener;

    /** Callback to log in the user. */
    private LoginCallBack loginCallBack;

    /** Click listener that directs the user to the signup activity. */
    private SignupClickListener signupClickListener;

    /** View binding for this activity. */
    ActivityLoginBinding binding;

    /**
     * Checks if a user is still logged in, and directs to the main activity if so.
     * Otherwise, sets the click listeners on the login activity.
     * */
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

    /** Hides the keyboard on the event of a touch outside of the keyboard. */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * Logs the user into Parse with a callback.
     * @param username username that the user inputs
     * @param password password that the user inputs
     * */
    private void loginUser(String username, String password) {
        loginCallBack = new LoginCallBack();
        ParseUser.logInInBackground(username, password, loginCallBack);
    }

    /** Creates an intent to go to the main activity and finishes login activity. */
    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    /** Creates an intent to go to the signup activity. */
    private void goSignupActivity() {
        Intent i = new Intent(this, SignupActivity.class);
        startActivity(i);
    }

    /**
     * Class for the click listener of the log in button.
     * Calls the login method to proceed with the inputted user information.
     * */
    private class LoginClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String username = binding.username.getText().toString();
            String password = binding.password.getText().toString();
            loginUser(username, password);
        }
    }

    /**
     * Class for the callback of log in.
     * Goes to the main activity if the log in is successful.
     * */
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

    /**
     * Class for the click listener of the sign up button.
     * Calls the sign up method to proceed to sign up activity.
     * */
    private class SignupClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            goSignupActivity();
        }
    }
}