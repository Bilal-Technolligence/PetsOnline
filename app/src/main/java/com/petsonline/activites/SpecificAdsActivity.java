package com.petsonline.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.petsonline.R;

public class SpecificAdsActivity extends AppCompatActivity {
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_ads);

        String Category = getIntent().getStringExtra("category");

        if (Category.equals(""))
        {
            Toast.makeText(this, "Unable to get Ad Category", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Ads").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}