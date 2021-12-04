package com.example.projectandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user2);

        bn = findViewById(R.id.btmnav_user);
        bn.setOnNavigationItemSelectedListener(UserActivity.this);
        bn.setSelectedItemId(R.id.btmnavuser_movie);

        Fragment f = new UserFavourite();
        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentUser,f).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.btmnavuser_fav){
            Fragment f=new UserFavourite();
            getSupportFragmentManager().beginTransaction().replace(R.id.FragmentUser,f).commit();
            return true;
        }
        if (item.getItemId() == R.id.btmnavuser_movie){
            Fragment f=new UserMovieFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.FragmentUser,f).commit();
            return true;
        }
        if (item.getItemId() == R.id.btmnavuser_rating){
            Fragment f=new UserRatingFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.FragmentUser,f).commit();
            return true;
        }
        if (item.getItemId() == R.id.btmnavuser_watchlist){
            Fragment f=new UserWatchListFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.FragmentUser,f).commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menulogout,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menulogout_logout)
        {
            finish();
        }
        return true;
    }
}