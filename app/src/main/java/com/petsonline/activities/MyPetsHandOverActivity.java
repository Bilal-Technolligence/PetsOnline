package com.petsonline.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.petsonline.R;
import com.petsonline.adapters.OwnPetsListAdaptor;
import com.petsonline.adapters.PetsHandOverSelectionListAdaptor;
import com.petsonline.models.MyPet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class MyPetsHandOverActivity extends AppCompatActivity {
    public FirebaseAuth mAuth;
    ArrayList<MyPet> al;
    PetsHandOverSelectionListAdaptor md;
    RecyclerView rv;
    MyPet p;
    View NoRecordFoundView;
    DatabaseReference databaseReference;
    String CareTaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pets_hand_over);
        try {
            getSupportActionBar().setTitle("My Pets");
        }catch (Exception ignored){}

        CareTaker = getIntent().getStringExtra("CareTaker");

        databaseReference = FirebaseDatabase.getInstance().getReference();

        NoRecordFoundView = findViewById(R.id.noRcdFnd);
        NoRecordFoundView.setVisibility(View.GONE);

        rv = findViewById(R.id.rec);
        RecyclerView.LayoutManager rlm = new LinearLayoutManager(this);
        rv.setLayoutManager(rlm);

        mAuth = FirebaseAuth.getInstance();

        al = new ArrayList<>();
        p = new MyPet();

        //loadPage();
    }

    private void loadPage(){
        FirebaseDatabase.getInstance().getReference().child("Pets").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    al.clear();
                    for (DataSnapshot eachAdRecord : dataSnapshot.getChildren()) {
                        String UploadedBy = Objects.requireNonNull(eachAdRecord.child("SellerID").getValue()).toString().trim();

                        if (UploadedBy.equalsIgnoreCase(mAuth.getUid())) {
                            MyPet p = new MyPet();

                            p.setMyPet_ID(eachAdRecord.getKey());
                            p.setMyPet_Title(eachAdRecord.child("Title").getValue(String.class));
                            p.setMyPet_Img(eachAdRecord.child("Image").getValue(String.class));
                            p.setAddress(eachAdRecord.child("Address").getValue(String.class));
                            p.setMyPet_Quantity(eachAdRecord.child("Quantity").getValue(String.class));
                            p.setMyPet_Age(eachAdRecord.child("Age").getValue(String.class));
                            p.setMyPet_Desc(eachAdRecord.child("Description").getValue(String.class));
                            p.setSellerID(eachAdRecord.child("SellerID").getValue(String.class));
                            p.setSelection(eachAdRecord.child("Selection").getValue(Boolean.class));

                            String Deleted = "";
                            if (eachAdRecord.child("Deleted").exists())
                                Deleted  = eachAdRecord.child("Deleted").getValue(String.class);

                            if (Deleted!=null && !Deleted.equalsIgnoreCase("true"))
                                al.add(p);
                        }

                    }
                    if (!al.isEmpty()) {
                        Collections.reverse(al);
                        NoRecordFoundView.setVisibility(View.GONE);
                        rv.setVisibility(View.VISIBLE);
                        md = new PetsHandOverSelectionListAdaptor(MyPetsHandOverActivity.this, al,CareTaker);
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

    @Override
    protected void onResume() {
        super.onResume();
        loadPage();
    }
}