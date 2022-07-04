package com.petsonline.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.petsonline.R;
import com.petsonline.models.CareTaker;
import com.petsonline.models.Doctor;

public class DoctorDetail extends AppCompatActivity {
    TextView Name,Address,Mobile,Email,Education,Specialization;
    Button ChatButton;
    Doctor doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_detail);
        Name = findViewById(R.id.profnames);
        Address = findViewById(R.id.profaddress);
        Mobile = findViewById(R.id.mobile);
        Email = findViewById(R.id.email);
        Education = findViewById(R.id.education);
        Specialization = findViewById(R.id.specialization);

        ChatButton = findViewById(R.id.ChatButton);
        ChatButton.setOnClickListener(view -> {
            if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(doctor.getID()))
            {
                Toast.makeText(DoctorDetail.this, "You can't chat to yourself. This Profile belongs to you.", Toast.LENGTH_LONG).show();
                return;
            }
            Intent o = new Intent(DoctorDetail.this, ChatWithDoctorActivity.class);
            o.putExtra("chaterId", doctor.getID());
            startActivity(o);
        });
        doctor = (Doctor) getIntent().getSerializableExtra("doctor");

        FirebaseDatabase.getInstance().getReference().child("Employee_Profile").child(doctor.getID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    if (snapshot.child("name").exists())
                    {
                        Name.setText("Name : " + snapshot.child("name").getValue(String.class));
                    }
                    if (snapshot.child("address").exists())
                    {
                        Address.setText("Address : " +snapshot.child("address").getValue(String.class));
                    }
                    if (snapshot.child("mobile").exists())
                    {
                        Mobile.setText("Mobile : " + snapshot.child("mobile").getValue(String.class));
                    }
                    if (snapshot.child("email").exists())
                    {
                        Email.setText("Email : " + snapshot.child("email").getValue(String.class));
                    }
                    if (snapshot.child("education").exists())
                    {
                        Education.setVisibility(View.VISIBLE);
                        Education.setText("Education : " + snapshot.child("education").getValue(String.class));
                    }
                    if (snapshot.child("specialization").exists())
                    {
                        Specialization.setVisibility(View.VISIBLE);
                        Specialization.setText("Specialization : " + snapshot.child("specialization").getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}