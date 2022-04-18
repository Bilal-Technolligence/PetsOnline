package com.petsonline.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.petsonline.R;
import com.petsonline.models.CareTaker;

import java.util.HashMap;
import java.util.Objects;

public class CareTakerMeProfileActivity extends AppCompatActivity {
    TextInputEditText Name,FeePerDay,FeeParHour,JobStartingTime,JobEndingTime,Address,Mobile,Email;
    SwitchMaterial AvailabilityStatus;
    Button profCONFIRM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caker_taker_me_profile);

        Name = findViewById(R.id.profnames);
        FeePerDay = findViewById(R.id.feeperday);
        FeeParHour= findViewById(R.id.feeperhour);
        JobStartingTime = findViewById(R.id.startingjobtime);
        JobEndingTime = findViewById(R.id.endingjobtime);
        Address = findViewById(R.id.profaddress);
        Mobile = findViewById(R.id.mobile);
        Email = findViewById(R.id.email);

        AvailabilityStatus = findViewById(R.id.availabilitystatus);

        profCONFIRM = findViewById(R.id.profCONFIRM);

        profCONFIRM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> data = new HashMap<>();

                data.put("id", FirebaseAuth.getInstance().getUid());
                if (Name.getText()!=null)
                    data.put("name",Name.getText().toString().trim());
                if (FeePerDay.getText()!=null)
                    data.put("feeperday",FeePerDay.getText().toString().trim());
                if (FeeParHour.getText()!=null)
                    data.put("feeperhour",FeeParHour.getText().toString().trim());
                if (JobStartingTime.getText()!=null)
                    data.put("startingtime",JobStartingTime.getText().toString().trim());
                if (JobEndingTime.getText()!=null)
                    data.put("endingtime",JobEndingTime.getText().toString().trim());
                if (Address.getText()!=null)
                    data.put("address",Address.getText().toString().trim());
                if (Mobile.getText()!=null)
                    data.put("mobile",Mobile.getText().toString().trim());
                if (Email.getText()!=null)
                    data.put("email",Email.getText().toString().trim());
                if (AvailabilityStatus.isChecked())
                    data.put("availablestatus","true");
                else
                    data.put("availablestatus","false");

                FirebaseDatabase.getInstance().getReference().child("Employee_Profile")
                        .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(CareTakerMeProfileActivity.this, "Data Updated Successful", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(CareTakerMeProfileActivity.this, "Unable to Update Data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        FirebaseDatabase.getInstance().getReference().child("Employee_Profile").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    if (snapshot.child("name").exists())
                    {
                        Name.setText(snapshot.child("name").getValue(String.class));
                    }
                    if (snapshot.child("feeperday").exists())
                    {
                        FeePerDay.setText(snapshot.child("feeperday").getValue(String.class));
                    }
                    if (snapshot.child("feeperhour").exists())
                    {
                        FeeParHour.setText(snapshot.child("feeperhour").getValue(String.class));
                    }
                    if (snapshot.child("address").exists())
                    {
                        Address.setText(snapshot.child("address").getValue(String.class));
                    }
                    if (snapshot.child("availablestatus").exists())
                    {
                        String status = snapshot.child("availablestatus").getValue(String.class);

                        AvailabilityStatus.setChecked(status.equals("true"));
                    }
                    if (snapshot.child("startingtime").exists())
                    {
                        JobStartingTime.setText(snapshot.child("startingtime").getValue(String.class));
                    }
                    if (snapshot.child("endingtime").exists())
                    {
                        JobEndingTime.setText(snapshot.child("endingtime").getValue(String.class));
                    }
                    if (snapshot.child("mobile").exists())
                    {
                        Mobile.setText(snapshot.child("mobile").getValue(String.class));
                    }
                    if (snapshot.child("email").exists())
                    {
                        Email.setText(snapshot.child("email").getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}