package com.example.meetplan.profile;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.example.meetplan.databinding.FragmentProfileBinding;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class ProfilePicCallBack implements FindCallback<ParseUser> {

    private static final String PROFILE_PIC_KEY = "profilepic";
    private FragmentProfileBinding binding;
    private Context context;

    public ProfilePicCallBack(Context context, FragmentProfileBinding binding) {
        this.context = context;
        this.binding = binding;
    }

    @Override
    public void done(List<ParseUser> objects, ParseException e) {
        ParseFile file = objects.get(0).getParseFile(PROFILE_PIC_KEY);
        Glide.with(context).load(file.getUrl()).circleCrop().into(binding.profilePic);
    }
}
