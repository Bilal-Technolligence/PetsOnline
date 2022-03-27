package com.petsonline.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.petsonline.R;
import com.petsonline.adapters.TrendingRecyclerViewAdapter;

import java.util.Objects;

public class MainActivity extends BaseActivity implements TrendingRecyclerViewAdapter.ItemClickListener {
    private CardView cats,dogs,hens,rabbits,goats,parrots;
    private RecyclerView trendingRecyclerView;
    private TrendingRecyclerViewAdapter trendingRecyclerViewAdapter;
    DatabaseReference databaseReference;
    BottomNavigationView bottomNavigationView;
    private View no_Trending_ads_layout;
    private FloatingActionButton add_new_ad_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Main");

        initObj();
        getTrending();
        clickListeners();
    }

    private void initObj() {
        cats= findViewById(R.id.cardA);
        dogs= findViewById(R.id.cardB);
        hens= findViewById(R.id.cardC);
        rabbits= findViewById(R.id.cardD);
        goats= findViewById(R.id.cardE);
        parrots= findViewById(R.id.cardF);

        no_Trending_ads_layout = findViewById(R.id.no_Trending_ads_layout);
        add_new_ad_btn = findViewById(R.id.add_new_ad_btn);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);

        trendingRecyclerView = findViewById(R.id.trendingRecyclerView);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        trendingRecyclerView.setLayoutManager(horizontalLayoutManager);
    }

    private void getTrending() {
        databaseReference.child("Ads").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    trendingRecyclerViewAdapter = new TrendingRecyclerViewAdapter(MainActivity.this, null);
                    trendingRecyclerViewAdapter.setClickListener(MainActivity.this);
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

    private void clickListeners() {
        add_new_ad_btn.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, CreateNewAdActivity.class);
            startActivity(i);
        });

        cats.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, SpecificAdsActivity.class);
            i.putExtra("category","cats");
            startActivity(i);
        });

        dogs.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, SpecificAdsActivity.class);
            i.putExtra("category","dogs");
            startActivity(i);
        });

        hens.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, SpecificAdsActivity.class);
            i.putExtra("category","hens");
            startActivity(i);
        });

        rabbits.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, SpecificAdsActivity.class);
            i.putExtra("category","rabbits");
            startActivity(i);
        });

        goats.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, SpecificAdsActivity.class);
            i.putExtra("category","goats");
            startActivity(i);
        });

        parrots.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, SpecificAdsActivity.class);
            i.putExtra("category","parrots");
            startActivity(i);
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

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( MainActivity.this );
        alertDialogBuilder.setTitle( "Exit" ).setMessage( "Are you sure to exit?" )
                .setNegativeButton( "No", (dialog, which) -> dialog.cancel()).setPositiveButton( "Exit", (dialog, which) -> finish()).show();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent o = new Intent(MainActivity.this, AddDetail.class);
        o.putExtras(new Bundle()); //Here Pass Extra Ad Details
        startActivity(o);
    }
}