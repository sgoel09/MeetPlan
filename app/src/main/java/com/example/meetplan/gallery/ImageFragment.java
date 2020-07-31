package com.example.meetplan.gallery;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.meetplan.R;
import com.example.meetplan.databinding.FragmentGalleryBinding;
import com.example.meetplan.databinding.FragmentImageBinding;
import com.parse.ParseFile;

public class ImageFragment extends Fragment {

    private static final String KEY_IMAGE_RES = "image";
    private FragmentImageBinding binding;

    public ImageFragment() {
        // Required empty public constructor
    }

    public static ImageFragment newInstance(ParseFile parseFile) {
        ImageFragment fragment = new ImageFragment();
        Bundle argument = new Bundle();
        argument.putParcelable(KEY_IMAGE_RES, parseFile);
        fragment.setArguments(argument);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentImageBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        ParseFile image = arguments.getParcelable(KEY_IMAGE_RES);
        Glide.with(this).load(image.getUrl()).into(binding.image);
    }
}