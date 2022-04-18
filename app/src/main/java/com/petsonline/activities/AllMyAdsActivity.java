package com.petsonline.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.petsonline.R;
import com.petsonline.adapters.AdsListAdaptor;
import com.petsonline.adapters.OwnAdsListAdaptor;
import com.petsonline.models.AdDetail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class AllMyAdsActivity extends AppCompatActivity {
    public FirebaseAuth mAuth;
    ArrayList<AdDetail> al;
    OwnAdsListAdaptor md;
    RecyclerView rv;
    AdDetail p;
    View NoRecordFoundView;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_my_ads);

        try {
            getSupportActionBar().setTitle("My Ads");
        }catch (Exception ignored){}

        databaseReference = FirebaseDatabase.getInstance().getReference();

        NoRecordFoundView = findViewById(R.id.noRcdFnd);
        NoRecordFoundView.setVisibility(View.GONE);

        rv = findViewById(R.id.rec);
        RecyclerView.LayoutManager rlm = new LinearLayoutManager(this);
        rv.setLayoutManager(rlm);

        mAuth = FirebaseAuth.getInstance();

        al = new ArrayList<>();
        p = new AdDetail();

        loadPage();
    }

    private void loadPage(){
        al.clear();
        FirebaseDatabase.getInstance().getReference().child("Ads").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot eachAdRecord : dataSnapshot.getChildren()) {
                        String UploadedBy = Objects.requireNonNull(eachAdRecord.child("SellerID").getValue()).toString().trim();

                        if (UploadedBy.equalsIgnoreCase(mAuth.getUid())) {
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
                        Collections.reverse(al);
                        NoRecordFoundView.setVisibility(View.GONE);
                        rv.setVisibility(View.VISIBLE);
                        md = new OwnAdsListAdaptor(AllMyAdsActivity.this, al);
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