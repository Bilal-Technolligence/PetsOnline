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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.petsonline.R;
import com.petsonline.models.CareTaker;

import java.util.Objects;

public class CareTakerDetailActivity extends AppCompatActivity {
    TextView Name,FeePerDay,FeeParHour,JobStartingTime,JobEndingTime,Address,Mobile,Email;
    SwitchMaterial AvailabilityStatus;
    Button ChatButton;
    CareTaker careTaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_taker_detail);

        Name = findViewById(R.id.profnames);
        FeePerDay = findViewById(R.id.feeperday);
        FeeParHour= findViewById(R.id.feeperhour);
        JobStartingTime = findViewById(R.id.startingjobtime);
        JobEndingTime = findViewById(R.id.endingjobtime);
        Address = findViewById(R.id.profaddress);
        Mobile = findViewById(R.id.mobile);
        Email = findViewById(R.id.email);

        AvailabilityStatus = findViewById(R.id.availabilitystatus);

        ChatButton = findViewById(R.id.ChatButton);
        ChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(careTaker.getID()))
                {
                    Toast.makeText(CareTakerDetailActivity.this, "You can't chat to yourself. This Profile belongs to you.", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent o = new Intent(CareTakerDetailActivity.this, ChatWithCareTakerActivity.class);
                o.putExtra("chaterId", careTaker.getID());
                startActivity(o);
            }
        });
        careTaker = (CareTaker) getIntent().getSerializableExtra("Caretaker");

        FirebaseDatabase.getInstance().getReference().child("Employee_Profile").child(careTaker.getID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    if (snapshot.child("name").exists())
                    {
                        Name.setText("Name : " + snapshot.child("name").getValue(String.class));
                    }
                    if (snapshot.child("feeperday").exists())
                    {
                        FeePerDay.setText("Fee Per Day : " +snapshot.child("feeperday").getValue(String.class));
                    }
                    if (snapshot.child("feeperhour").exists())
                    {
                        FeeParHour.setText("Fee Per Hour : " +snapshot.child("feeperhour").getValue(String.class));
                    }
                    if (snapshot.child("address").exists())
                    {
                        Address.setText("Address : " +snapshot.child("address").getValue(String.class));
                    }
                    if (snapshot.child("availablestatus").exists())
                    {
                        String status = snapshot.child("availablestatus").getValue(String.class);

                        AvailabilityStatus.setChecked(status.equals("true"));
                    }
                    if (snapshot.child("startingtime").exists())
                    {
                        JobStartingTime.setText("Job Starting time : " + snapshot.child("startingtime").getValue(String.class));
                    }
                    if (snapshot.child("endingtime").exists())
                    {
                        JobEndingTime.setText("Job Ending Time : " + snapshot.child("endingtime").getValue(String.class));
                    }
                    if (snapshot.child("mobile").exists())
                    {
                        Mobile.setText("Mobile : " + snapshot.child("mobile").getValue(String.class));
                    }
                    if (snapshot.child("email").exists())
                    {
                        Email.setText("Email : " + snapshot.child("email").getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}