package com.example.projectandroid.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectandroid.R;
import com.example.projectandroid.databinding.FragmentUserFavoriteBinding;
import com.example.projectandroid.databinding.FragmentUserHomeBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFavoriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFavoriteFragment extends Fragment {

    private static final String ARG_PARAM_USERNAME = "username";

    private String username;
    FragmentUserFavoriteBinding binding;

    public UserFavoriteFragment() {
        // Required empty public constructor
    }

    public static UserFavoriteFragment newInstance(String username) {
        UserFavoriteFragment fragment = new UserFavoriteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(ARG_PARAM_USERNAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserFavoriteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}