package com.petsonline.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.petsonline.R;
import com.petsonline.adapters.AdsListAdaptor;
import com.petsonline.models.AdDetail;

import java.util.ArrayList;
import java.util.Objects;

public class SpecificAdsActivity extends AppCompatActivity {
    public FirebaseAuth mAuth;
    TextView category;
    ArrayList<AdDetail> al;
    AdsListAdaptor md;
    RecyclerView rv;
    AdDetail p;

    View NoRecordFoundView;

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

        category = findViewById(R.id.ava_category);
        NoRecordFoundView = findViewById(R.id.noRcdFnd);
        NoRecordFoundView.setVisibility(View.GONE);

        category.setText(Category);

        rv = findViewById(R.id.rec);
        RecyclerView.LayoutManager rlm = new LinearLayoutManager(this);
        rv.setLayoutManager(rlm);

        mAuth = FirebaseAuth.getInstance();

        al = new ArrayList<>();
        p = new AdDetail();

        FirebaseDatabase.getInstance().getReference().child("Ads").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot eachAdRecord : dataSnapshot.getChildren()) {
                        String category = Objects.requireNonNull(eachAdRecord.child("Category").getValue()).toString().trim();

                        if (category.equalsIgnoreCase(Category)) {

                            AdDetail p = new AdDetail();

                            p.setAd_ID(eachAdRecord.getKey());
                            p.setAd_Title(eachAdRecord.child("Title").getValue(String.class));
                            p.setAd_Img(eachAdRecord.child("Image").getValue(String.class));
                            p.setAd_Address(eachAdRecord.child("Address").getValue(String.class));
                            p.setAd_Category_FID(eachAdRecord.child("Category").getValue(String.class));
                            p.setAd_Quantity(eachAdRecord.child("Quantity").getValue(String.class));
                            p.setAd_Sold(eachAdRecord.child("Sold").getValue(String.class));
                            p.setAd_Price(eachAdRecord.child("Price").getValue(String.class));
                            p.setAd_Desc(eachAdRecord.child("Description").getValue(String.class));
                            p.setSellerID(eachAdRecord.child("SellerID").getValue(String.class));
                            p.setDate(eachAdRecord.child("Date").getValue(String.class));

                            al.add(p);
                        }

                    }
                    if (!al.isEmpty()) {
                        NoRecordFoundView.setVisibility(View.GONE);
                        rv.setVisibility(View.VISIBLE);
                        md = new AdsListAdaptor(SpecificAdsActivity.this, al);
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