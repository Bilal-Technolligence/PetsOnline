package com.petsonline.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.petsonline.R;
import com.petsonline.util.BaseUtil;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {
    ProgressBar Loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(Color.WHITE);
        setContentView(R.layout.activity_splash_screen);

        Loading = findViewById(R.id.loading);

        new Handler().postDelayed(() -> {
            Loading.setVisibility(View.VISIBLE);

            if (FirebaseAuth.getInstance().getCurrentUser() != null)
            {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String Role = dataSnapshot.child("Role").getValue(String.class);
                        FirebaseDatabase.getInstance().getReference("Employee_Profile").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Loading.setVisibility(View.GONE);
                                if (snapshot.exists())
                                {
                                    boolean status = snapshot.child("profilecompleted").exists();
                                    boolean status2 = false;
                                    if (status)
                                        status2 = snapshot.child("profilecompleted").getValue(String.class).equals("true");

                                    if (! status|| !status2)
                                    {
                                        startActivity(new Intent(SplashScreen.this, CompleteProfile.class));
                                        finish();
                                        return;
                                    }

                                    new BaseUtil(SplashScreen.this).SetLoggedIn(true);
                                    new BaseUtil(SplashScreen.this).SetLoginRole(Role);

                                    switch (Role) {
                                        case "Buyer/Seller":
                                            startActivity(new Intent(SplashScreen.this, MainActivity.class));
                                            break;
                                        case "Care Taker":
                                            startActivity(new Intent(SplashScreen.this, CareTakerActivity.class));
                                            break;
                                        case "Doctor":
                                            startActivity(new Intent(SplashScreen.this, DoctorActivity.class));
                                            break;
                                        default:
                                            startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                                            break;
                                    }
                                    finish();
                                }else
                                {
                                    startActivity(new Intent(SplashScreen.this, CompleteProfile.class));
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                //startActivity(new Intent(LoginActivity.this,MainActivity.class));
                //finish();
                //return;
            }
            else{
                Loading.setVisibility(View.GONE);
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        }, 4000);
    }
}