package com.example.meetplan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meetplan.databinding.ActivitySignupBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;

/**
 * @author Shefali Goel
 * Activity to sign up for an account in MeetPlan.
 * Users enter their name, username, email, password and directed into their account
 * if sign up is successful.
 * */
public class SignupActivity extends AppCompatActivity {

    /** Tag for this activity. */
    private static final String TAG = "SignupActivity";

    /** Key for the user profile pic in the Parse database. */
    private static final String KEY_PROFILE_PIC = "profilepic";

    /** Key for the user name in the Parse database. */
    private static final String KEY_NAME = "name";

    /** Key for the user profile pic in the Parse database. */
    private static final String IMAGE_URL = "image_file.png";

    /** Click listener that gets the user input from the text fields. */
    private SignupClickListener signupClickListener;

    /** Callback that signs up the user with an account in the Parse database. */
    private SignupCallBack signupCallBack;

    /** View binding for this activity. */
    ActivitySignupBinding binding;

    /**
     * Sets the click listener on the sign up button.
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getSupportActionBar().hide();

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
     * Signs up the user with the inputted information, then sets a default profile picture.
     * @param username username that the user inputs
     * @param email email that the user inputs
     * @param password password that the user inputs
     * @param name name that the user inputs
     * */
    private void signupUser(String username, String email, String password, String name) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        signupCallBack = new SignupCallBack();
        user.signUpInBackground(signupCallBack);
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.defaultprofilepic);
        user.put(KEY_PROFILE_PIC, convertToParseFile(imageBitmap));
        user.put(KEY_NAME, name);
        user.saveInBackground();
    }

    /**
     * Converts the image from a bitmap to a new parse file by getting the byte stream
     * @param imageBitmap bitmap of the image to covert
     * @return parseFle new parse file of the image
     * */
    private ParseFile convertToParseFile(Bitmap imageBitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        ParseFile parseFile = new ParseFile(IMAGE_URL,imageByte);
        return parseFile;
    }

    /** Creates an intent to go to the main activity and finishes login activity. */
    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    /**
     * Class for the click listener of the sign up button.
     * Calls the sign up method to proceed with the inputted user information.
     * */
    private class SignupClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String username = binding.username.getText().toString();
            String email = binding.email.getText().toString();
            String password = binding.password.getText().toString();
            String name = binding.name.getText().toString();
            signupUser(username, email, password, name);
        }
    }

    /**
     * Class for the callback of sign up.
     * Goes to the main activity if the sign up is successful.
     * */
    private class SignupCallBack implements SignUpCallback {
        @Override
        public void done(ParseException e) {
            if (e != null) {
                Log.e(TAG, "Issue with signup", e);
                return;
            }
            goMainActivity();
            Snackbar.make(binding.getRoot(), "Success", BaseTransientBottomBar.LENGTH_SHORT).show();
        }
    }
}