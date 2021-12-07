package com.example.projectandroid.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectandroid.R;
import com.example.projectandroid.databinding.FragmentUserHomeBinding;
import com.example.projectandroid.databinding.FragmentUserWatchListBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserWatchListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserWatchListFragment extends Fragment {

    private static final String ARG_PARAM_USERNAME = "username";

    private String username;
    FragmentUserWatchListBinding binding;

    public UserWatchListFragment() {
        // Required empty public constructor
    }

    public static UserWatchListFragment newInstance(String username) {
        UserWatchListFragment fragment = new UserWatchListFragment();
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
        binding = FragmentUserWatchListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}