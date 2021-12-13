package com.example.projectandroid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.projectandroid.R;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Objects.requireNonNull(getSupportActionBar()).hide();

        // cek apakah ada sharedpref buat auto login
        SharedPreferences sharedPreferences = getSharedPreferences("autoLogin", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        Intent intent;
        if (username.isEmpty()){
            intent = new Intent(getApplicationContext(), LoginActivity.class);
        }
        else{
            intent = new Intent(getApplicationContext(), UserHomeActivity.class);
            intent.putExtra("auth", username);
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, 1500L);
    }
}