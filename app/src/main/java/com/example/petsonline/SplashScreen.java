package com.example.petsonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.WHITE);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(() -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }, 4000);
    }
}