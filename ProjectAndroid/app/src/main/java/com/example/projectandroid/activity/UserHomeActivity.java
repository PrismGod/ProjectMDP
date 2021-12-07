package com.example.projectandroid.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.projectandroid.R;
import com.example.projectandroid.databinding.ActivityLoginBinding;
import com.example.projectandroid.databinding.ActivityUserHomeBinding;
import com.example.projectandroid.fragment.UserFavoriteFragment;
import com.example.projectandroid.fragment.UserHomeFragment;
import com.example.projectandroid.fragment.UserRatingFragment;
import com.example.projectandroid.fragment.UserWatchListFragment;
import com.google.android.material.navigation.NavigationBarView;

public class UserHomeActivity extends AppCompatActivity {

    ActivityUserHomeBinding binding;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        username = getIntent().getStringExtra("auth");
        getSupportActionBar().setTitle("Welcome, "+username);

        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()){
                    case R.id.navWatchlist:
                        fragment = UserWatchListFragment.newInstance(username);
                        break;
                    case R.id.navFavorite:
                        fragment = UserFavoriteFragment.newInstance(username);
                        break;
                    case R.id.navRating:
                        fragment = UserRatingFragment.newInstance(username);
                        break;
                    default:
                        fragment = UserHomeFragment.newInstance(username);
                        break;
                }
                try {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentPlaceholder, fragment)
                            .commit();
                }catch (Exception e){
                    Log.e("MainActivity", e.getMessage());
                }
                return true;
            }
        });

        if (savedInstanceState == null){
            binding.bottomNavigation.setSelectedItemId(R.id.navHome);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menulogout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.optionLogout){
            Intent intent = new Intent(UserHomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}