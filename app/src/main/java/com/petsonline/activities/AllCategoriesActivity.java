package com.petsonline.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.petsonline.R;
import com.petsonline.adapters.TrendingRecyclerViewAdapter;

public class AllCategoriesActivity extends AppCompatActivity implements TrendingRecyclerViewAdapter.ItemClickListener {
    private CardView cats,dogs,hens,rabbits,goats,parrots;
    DatabaseReference databaseReference;
    //private View no_Trending_ads_layout;
    //private RecyclerView trendingRecyclerView;
    //private TrendingRecyclerViewAdapter trendingRecyclerViewAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_categories);
        
        cats= findViewById(R.id.cardA);
        dogs= findViewById(R.id.cardB);
        hens= findViewById(R.id.cardC);
        rabbits= findViewById(R.id.cardD);
        goats= findViewById(R.id.cardE);
        parrots= findViewById(R.id.cardF);

        //no_Trending_ads_layout = findViewById(R.id.no_Trending_ads_layout);
        //trendingRecyclerView = findViewById(R.id.trendingRecyclerView);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        cats.setOnClickListener(view -> {
            Intent i = new Intent(AllCategoriesActivity.this, SpecificAdsActivity.class);
            i.putExtra("category","cats");
            i.putExtra("subcategory",1);
            startActivity(i);
        });

        dogs.setOnClickListener(view -> {
            Intent i = new Intent(AllCategoriesActivity.this, SpecificAdsActivity.class);
            i.putExtra("category","dogs");
            i.putExtra("subcategory",2);
            startActivity(i);
        });

        hens.setOnClickListener(view -> {
            Intent i = new Intent(AllCategoriesActivity.this, SpecificAdsActivity.class);
            i.putExtra("category","hens");
            i.putExtra("subcategory",3);
            startActivity(i);
        });

        rabbits.setOnClickListener(view -> {
            Intent i = new Intent(AllCategoriesActivity.this, SpecificAdsActivity.class);
            i.putExtra("category","rabbits");
            i.putExtra("subcategory",4);
            startActivity(i);
        });

        goats.setOnClickListener(view -> {
            Intent i = new Intent(AllCategoriesActivity.this, SpecificAdsActivity.class);
            i.putExtra("category","goats");
            i.putExtra("subcategory",5);
            startActivity(i);
        });

        parrots.setOnClickListener(view -> {
            Intent i = new Intent(AllCategoriesActivity.this, SpecificAdsActivity.class);
            i.putExtra("category","parrots");
            i.putExtra("subcategory",6);
            startActivity(i);
        });

        //getTrending();
    }
/*

    private void getTrending() {
        databaseReference.child("Ads").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    trendingRecyclerViewAdapter = new TrendingRecyclerViewAdapter(AllCategoriesActivity.this, null);
                    trendingRecyclerViewAdapter.setClickListener(AllCategoriesActivity.this);
                    trendingRecyclerView.setAdapter(trendingRecyclerViewAdapter);
                    //ShowTrending();
                    HideTrending(); // remove this Line to Show Original Trending
                }
                else
                {
                    HideTrending();
                    //Show No Trending Exists
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ShowTrending(){
        no_Trending_ads_layout.setVisibility(View.GONE);
        trendingRecyclerView.setVisibility(View.VISIBLE);
    }

    private void HideTrending(){
        no_Trending_ads_layout.setVisibility(View.VISIBLE);
        trendingRecyclerView.setVisibility(View.GONE);
    }
*/

    @Override
    public void onItemClick(View view, int position) {
        Intent o = new Intent(AllCategoriesActivity.this, AddDetail.class);
        o.putExtras(new Bundle()); //Here Pass Extra Ad Details
        startActivity(o);
    }
}