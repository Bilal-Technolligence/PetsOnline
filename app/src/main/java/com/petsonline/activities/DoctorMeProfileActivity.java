package com.petsonline.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.petsonline.R;
import com.petsonline.adapters.ChatAdapter;
import com.petsonline.models.MessageAttr;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class DoctorMeProfileActivity extends AppCompatActivity {
    TextInputEditText Name,Address,Mobile,Email,Education,Specialization;
    Button profCONFIRM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_me_profile);

        Name = findViewById(R.id.profnames);
        Education = findViewById(R.id.education);
        Specialization= findViewById(R.id.specialization);
        Address = findViewById(R.id.profaddress);
        Mobile = findViewById(R.id.mobile);
        Email = findViewById(R.id.email);

        profCONFIRM = findViewById(R.id.profCONFIRM);

        profCONFIRM.setOnClickListener(view -> {
            HashMap<String, Object> data = new HashMap<>();

            data.put("id", FirebaseAuth.getInstance().getUid());
            if (Name.getText()!=null)
                data.put("name",Name.getText().toString().trim());
            if (Education.getText()!=null)
                data.put("education",Education.getText().toString().trim());
            if (Specialization.getText()!=null)
                data.put("specialization",Specialization.getText().toString().trim());
            if (Address.getText()!=null)
                data.put("address",Address.getText().toString().trim());
            if (Mobile.getText()!=null)
                data.put("mobile",Mobile.getText().toString().trim());
            if (Email.getText()!=null)
                data.put("email",Email.getText().toString().trim());

            FirebaseDatabase.getInstance().getReference().child("Employee_Profile")
                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).updateChildren(data).addOnCompleteListener(task -> {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(DoctorMeProfileActivity.this, "Data Updated Successful", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(DoctorMeProfileActivity.this, "Unable to Update Data", Toast.LENGTH_SHORT).show();
                        }
                    });

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
                    if (snapshot.child("education").exists())
                    {
                        Education.setText(snapshot.child("education").getValue(String.class));
                    }
                    if (snapshot.child("specialization").exists())
                    {
                        Specialization.setText(snapshot.child("specialization").getValue(String.class));
                    }
                    if (snapshot.child("address").exists())
                    {
                        Address.setText(snapshot.child("address").getValue(String.class));
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