package com.example.projectandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bn = findViewById(R.id.btmnav_main);
        bn.setOnNavigationItemSelectedListener(MainActivity.this);
        bn.setSelectedItemId(R.id.btmnavmain_login);
//
        Fragment f = new LoginFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentMain,f).commit();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //digunakan untuk perpindahan antar fragment Login dan register menggunakan bottom navigation view
        if (item.getItemId() == R.id.btmnavmain_login){
            Fragment f=new LoginFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.FragmentMain,f).commit();
            return true;
        }
        if (item.getItemId() == R.id.btmnavmain_register){
            Fragment f=new RegisterFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.FragmentMain,f).commit();
            return true;
        }
//        return super.onOptionsItemSelected(item);
        return false;
    }
}