package com.petsonline.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.petsonline.R;
import com.petsonline.adapters.AdsListAdaptor;
import com.petsonline.adapters.CareTakerListAdaptor;
import com.petsonline.models.AdDetail;
import com.petsonline.models.CareTaker;

import java.util.ArrayList;
import java.util.Objects;

public class AllCareTakersActivity extends AppCompatActivity {
    public FirebaseAuth mAuth;
    ArrayList<CareTaker> al;
    CareTakerListAdaptor md;
    RecyclerView rv;
    View NoRecordFoundView;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_care_takers);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        NoRecordFoundView = findViewById(R.id.noRcdFnd);
        NoRecordFoundView.setVisibility(View.GONE);

        rv = findViewById(R.id.rec);
        RecyclerView.LayoutManager rlm = new LinearLayoutManager(this);
        rv.setLayoutManager(rlm);

        mAuth = FirebaseAuth.getInstance();

        al = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("Employee_Profile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot eachAdRecord : dataSnapshot.getChildren()) {
                        if (eachAdRecord.child("role").exists())
                        {
                            String Role = eachAdRecord.child("role").getValue(String.class);
                            if (!Objects.equals(Role, "Care Taker"))
                                continue;
                            if (eachAdRecord.child("availablestatus").exists()
                                    && Objects.equals(eachAdRecord.child("availablestatus").getValue(String.class), "true"))
                            {
                                CareTaker p = new CareTaker();
                                p.setID(eachAdRecord.getKey());
                                p.setNAME(eachAdRecord.child("name").getValue(String.class));
                                p.setIMAGEURL(eachAdRecord.child("imageurl").getValue(String.class));
                                p.setFEEPERDAY(eachAdRecord.child("feeperday").getValue(String.class));
                                p.setFEEPERHOUR(eachAdRecord.child("feeperhour").getValue(String.class));
                                p.setADDRESS(eachAdRecord.child("address").getValue(String.class));
                                p.setAVAILABLESTATUS(eachAdRecord.child("availablestatus").getValue(String.class));
                                p.setSTARTINGTIME(eachAdRecord.child("startingtime").getValue(String.class));
                                p.setENDINGTIME(eachAdRecord.child("endingtime").getValue(String.class));
                                p.setMOBILE(eachAdRecord.child("mobile").getValue(String.class));
                                p.setEMAIL(eachAdRecord.child("email").getValue(String.class));

                                al.add(p);
                            }
                        }
                    }
                    if (!al.isEmpty()) {
                        NoRecordFoundView.setVisibility(View.GONE);
                        rv.setVisibility(View.VISIBLE);
                        md = new CareTakerListAdaptor(AllCareTakersActivity.this, al);
                        rv.setAdapter(md);
                    } else {
                        NoRecordFoundView.setVisibility(View.VISIBLE);
                        rv.setVisibility(View.GONE);
                    }
                } else {
                    NoRecordFoundView.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}