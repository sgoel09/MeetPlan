package com.example.meetplan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.meetplan.databinding.ActivityLoginBinding;
import com.example.meetplan.databinding.ActivitySignupBinding;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    private static final String KEY_PROFILE_PIC = "profilepic";
    private SignupClickListener signupClickListener;
    private SignupCallBack signupCallBack;
    ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        signupClickListener = new SignupClickListener();
        binding.btnSignup.setOnClickListener(signupClickListener);
    }

    private void signupUser(String username, String email, String password) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        signupCallBack = new SignupCallBack();
        user.signUpInBackground(signupCallBack);
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.defaultprofilepic);
        user.put(KEY_PROFILE_PIC, convertToParseFile(imageBitmap));
        user.saveInBackground();
    }

    private ParseFile convertToParseFile(Bitmap imageBitmap) {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        ParseFile parseFile = new ParseFile("image_file.png",imageByte);
        return parseFile;
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private class SignupClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            String username = binding.etUsername.getText().toString();
            String email = binding.etEmail.getText().toString();
            String password = binding.etPassword.getText().toString();
            signupUser(username, email, password);
        }
    }

    private class SignupCallBack implements SignUpCallback {
        @Override
        public void done(ParseException e) {
            if (e != null) {
                Log.e(TAG, "Issue with signup", e);
                return;
            }
            goMainActivity();
            Toast.makeText(SignupActivity.this, "Success", Toast.LENGTH_SHORT).show();
        }
    }
}