package com.petsonline.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
import java.util.Collections;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    public FirebaseAuth mAuth;
    SearchView searchView;
    ArrayList<AdDetail> al;
    AdsListAdaptor md;
    RecyclerView rv;
    AdDetail p;
    View NoRecordFoundView;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        searchView = findViewById(R.id.searchView);
        NoRecordFoundView = findViewById(R.id.noRcdFnd);
        NoRecordFoundView.setVisibility(View.GONE);

        rv = findViewById(R.id.rec);
        RecyclerView.LayoutManager rlm = new LinearLayoutManager(this);
        rv.setLayoutManager(rlm);

        mAuth = FirebaseAuth.getInstance();

        al = new ArrayList<>();
        p = new AdDetail();

        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText !=null && !newText.equals(""))
            refresh(newText.toLowerCase());
        else
        {
            NoRecordFoundView.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        }
        return false;
    }

    private void refresh(String query) {
        FirebaseDatabase.getInstance().getReference().child("Ads").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                al.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot eachAdRecord : dataSnapshot.getChildren()) {
                        String category = Objects.requireNonNull(eachAdRecord.child("Category").getValue()).toString().trim();
                        String subcategory="";
                        String AdTitle = "";
                        String AdAddress = "" ;
                        String AdDesc = "";

                        if (eachAdRecord.child("SubCategory").exists())
                            subcategory = Objects.requireNonNull(eachAdRecord.child("SubCategory").getValue()).toString().trim();

                        if (eachAdRecord.child("Title").exists())
                            AdTitle = Objects.requireNonNull(eachAdRecord.child("Title").getValue()).toString().trim();

                        if (eachAdRecord.child("Address").exists())
                            AdAddress = Objects.requireNonNull(eachAdRecord.child("Address").getValue()).toString().trim();

                        if (eachAdRecord.child("Description").exists())
                            AdDesc = Objects.requireNonNull(eachAdRecord.child("Description").getValue()).toString().trim();

                        if (category.toLowerCase().contains(query)
                                || subcategory.toLowerCase().contains(query)
                                || AdTitle.toLowerCase().contains(query)
                                || AdAddress.toLowerCase().contains(query)
                                || AdDesc.toLowerCase().contains(query)) {

                            AdDetail p = new AdDetail();

                            p.setAd_ID(eachAdRecord.getKey());
                            p.setAd_Title(eachAdRecord.child("Title").getValue(String.class));
                            p.setAd_Img(eachAdRecord.child("Image").getValue(String.class));
                            p.setAd_Address(eachAdRecord.child("Address").getValue(String.class));
                            p.setAd_Category_FID(eachAdRecord.child("Category").getValue(String.class));
                            p.setAd_SubCategory(eachAdRecord.child("SubCategory").getValue(String.class));
                            p.setAd_Quantity(eachAdRecord.child("Quantity").getValue(String.class));
                            p.setAd_Sold(eachAdRecord.child("Sold").getValue(String.class));
                            p.setAd_Price(eachAdRecord.child("Price").getValue(String.class));
                            p.setAd_Desc(eachAdRecord.child("Description").getValue(String.class));
                            p.setSellerID(eachAdRecord.child("SellerID").getValue(String.class));
                            p.setDate(eachAdRecord.child("Date").getValue(String.class));

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
                        md = new AdsListAdaptor(SearchActivity.this, al);
                        rv.setAdapter(md);
                    } else {
                        NoRecordFoundView.setVisibility(View.VISIBLE);
                        rv.setVisibility(View.GONE);
                    }
                }
                else {
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