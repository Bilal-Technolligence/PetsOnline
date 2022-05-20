package com.petsonline.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class SpecificAdsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    public FirebaseAuth mAuth;
    TextView category;
    ArrayList<AdDetail> al;
    AdsListAdaptor md;
    RecyclerView rv;
    AdDetail p;
    View NoRecordFoundView;
    DatabaseReference databaseReference;

    private String[] catsCategories;
    private String[] dogsCategories;
    private String[] hensCategories;
    private String[] rabbitsCategories;
    private String[] goatsCategories;
    private String[] parrotsCategories;
    
    ArrayAdapter<String> spinnerArrayAdapterSubCategory;
    
    Spinner spinnerSubCategory;
    String Category;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_ads);

        Category = getIntent().getStringExtra("category");

        catsCategories = getResources().getStringArray(R.array.CatsSubCategory);
        dogsCategories = getResources().getStringArray(R.array.DogsSubCategory);
        parrotsCategories = getResources().getStringArray(R.array.ParrotsSubCategory);
        goatsCategories = getResources().getStringArray(R.array.GoatsSubCategory);
        hensCategories = getResources().getStringArray(R.array.HensSubCategory);
        rabbitsCategories = getResources().getStringArray(R.array.RabbitsSubCategory);
        
        int position = getIntent().getIntExtra("subcategory",1);

        spinnerSubCategory = findViewById(R.id.subCategorySpinner);
        
        switch (position)
        {
            case 1:
                spinnerArrayAdapterSubCategory = new ArrayAdapter<>(SpecificAdsActivity.this, android.R.layout.simple_spinner_dropdown_item, catsCategories);
                spinnerSubCategory.setAdapter(spinnerArrayAdapterSubCategory);
                spinnerSubCategory.setOnItemSelectedListener(this);
                break;
            case 2:
                spinnerArrayAdapterSubCategory = new ArrayAdapter<>(SpecificAdsActivity.this, android.R.layout.simple_spinner_dropdown_item, dogsCategories);
                spinnerSubCategory.setAdapter(spinnerArrayAdapterSubCategory);
                spinnerSubCategory.setOnItemSelectedListener(this);
                break;
            case 3:
                spinnerArrayAdapterSubCategory = new ArrayAdapter<>(SpecificAdsActivity.this, android.R.layout.simple_spinner_dropdown_item, hensCategories);
                spinnerSubCategory.setAdapter(spinnerArrayAdapterSubCategory);
                spinnerSubCategory.setOnItemSelectedListener(this);
                break;
            case 4:
                spinnerArrayAdapterSubCategory = new ArrayAdapter<>(SpecificAdsActivity.this, android.R.layout.simple_spinner_dropdown_item, rabbitsCategories);
                spinnerSubCategory.setAdapter(spinnerArrayAdapterSubCategory);
                spinnerSubCategory.setOnItemSelectedListener(this);
                break;
            case 5:
                spinnerArrayAdapterSubCategory = new ArrayAdapter<>(SpecificAdsActivity.this, android.R.layout.simple_spinner_dropdown_item, goatsCategories);
                spinnerSubCategory.setAdapter(spinnerArrayAdapterSubCategory);
                spinnerSubCategory.setOnItemSelectedListener(this);
                break;
            case 6:
                spinnerArrayAdapterSubCategory = new ArrayAdapter<>(SpecificAdsActivity.this, android.R.layout.simple_spinner_dropdown_item, parrotsCategories);
                spinnerSubCategory.setAdapter(spinnerArrayAdapterSubCategory);
                spinnerSubCategory.setOnItemSelectedListener(this);
                break;
        }
        
        if (Category.equals(""))
        {
            Toast.makeText(this, "Unable to get Ad Category", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();

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

        //loadPage();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (spinnerSubCategory.getSelectedItem().toString().equals("Select"))
            loadPage();
        else
            refresh(spinnerSubCategory.getSelectedItem().toString());
    }

    private void loadPage(){
        al.clear();
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

    private void refresh(String query) {
        al.clear();
        FirebaseDatabase.getInstance().getReference().child("Ads").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot eachAdRecord : dataSnapshot.getChildren()) {
                        String category = Objects.requireNonNull(eachAdRecord.child("Category").getValue()).toString().trim();
                        String subcategory="";
                        if (eachAdRecord.child("SubCategory").exists())
                            subcategory = Objects.requireNonNull(eachAdRecord.child("SubCategory").getValue()).toString().trim();

                        if (category.equalsIgnoreCase(Category)
                                && subcategory.equals(query)) {

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

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}