package com.petsonline.activities;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.petsonline.models.AdDetail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class MainActivity extends BaseActivity implements TrendingRecyclerViewAdapter.ItemClickListener {
    private RecyclerView trendingRecyclerView;
    private TrendingRecyclerViewAdapter trendingRecyclerViewAdapter;
    private DatabaseReference databaseReference;
    private BottomNavigationView bottomNavigationView;
    private View no_Trending_ads_layout;
    private FloatingActionButton add_new_ad_btn;
    private TextView viewAllCategory, gotoCareTaker, gotoDoctors;
    private ArrayList<AdDetail> currentList;

   private LinearLayout cats, dogs, hens, rabbits, goats, parrots;
   private LinearLayout.LayoutParams params;
   private LinearLayout next, prev;
   private int viewWidth;
   private GestureDetector gestureDetector = null;
   private HorizontalScrollView horizontalScrollView;
   private ArrayList<LinearLayout> layouts;
   private int mWidth;
   private int currPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Main");

        currentList = new ArrayList<>();

        initObj();
        getTrending();
        clickListeners();
        mHorizontalScrollView();
    }

    private void mHorizontalScrollView() {
        prev = (LinearLayout) findViewById(R.id.prev);
        next = (LinearLayout) findViewById(R.id.next);
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.hsv);
        gestureDetector = new GestureDetector(new MyGestureDetector());
        cats = (LinearLayout) findViewById(R.id.CatsCategory);
        dogs = (LinearLayout) findViewById(R.id.DogsCategory);
        parrots = (LinearLayout) findViewById(R.id.ParrotsCategory);
        goats = (LinearLayout) findViewById(R.id.GoatsCategory);
        rabbits = (LinearLayout) findViewById(R.id.RabbitsCategory);
        hens = (LinearLayout) findViewById(R.id.HensCategory);

        Display display = getWindowManager().getDefaultDisplay();
        mWidth = display.getWidth(); // deprecated
        viewWidth = mWidth / 3;
        layouts = new ArrayList<>();
        params = new LinearLayout.LayoutParams(viewWidth, LinearLayout.LayoutParams.WRAP_CONTENT);

        cats.setLayoutParams(params);
        dogs.setLayoutParams(params);
        parrots.setLayoutParams(params);
        goats.setLayoutParams(params);
        rabbits.setLayoutParams(params);
        hens.setLayoutParams(params);

        layouts.add(cats);
        layouts.add(dogs);
        layouts.add(parrots);
        layouts.add(goats);
        layouts.add(rabbits);
        layouts.add(hens);

        next.setOnClickListener(v -> new Handler().postDelayed(() -> horizontalScrollView.smoothScrollTo(
                (int) horizontalScrollView.getScrollX()
                        + viewWidth,
                (int) horizontalScrollView.getScrollY()), 100L));

        prev.setOnClickListener(v -> new Handler().postDelayed(() -> horizontalScrollView.smoothScrollTo(
                (int) horizontalScrollView.getScrollX()
                        - viewWidth,
                (int) horizontalScrollView.getScrollY()), 100L));

        horizontalScrollView.setOnTouchListener((v, event) -> {
            if (gestureDetector.onTouchEvent(event)) {
                return true;
            }
            return false;
        });

        cats.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, SpecificAdsActivity.class);
            i.putExtra("category","cats");
            i.putExtra("subcategory",1);
            startActivity(i);
        });

        dogs.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, SpecificAdsActivity.class);
            i.putExtra("category","dogs");
            i.putExtra("subcategory",2);
            startActivity(i);
        });

        hens.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, SpecificAdsActivity.class);
            i.putExtra("category","hens");
            i.putExtra("subcategory",3);
            startActivity(i);
        });

        rabbits.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, SpecificAdsActivity.class);
            i.putExtra("category","rabbits");
            i.putExtra("subcategory",4);
            startActivity(i);
        });

        goats.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, SpecificAdsActivity.class);
            i.putExtra("category","goats");
            i.putExtra("subcategory",5);
            startActivity(i);
        });

        parrots.setOnClickListener(view -> {
            Intent i = new Intent(MainActivity.this, SpecificAdsActivity.class);
            i.putExtra("category","parrots");
            i.putExtra("subcategory",6);
            startActivity(i);
        });
    }

    private void initObj() {
        viewAllCategory = findViewById(R.id.viewAllCategory);
        gotoCareTaker = findViewById(R.id.gotoCareTaker);
        gotoDoctors = findViewById(R.id.gotoDoctors);

        viewAllCategory.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, AllCategoriesActivity.class)));
        gotoCareTaker.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, AllCareTakersActivity.class)));
        gotoDoctors.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, AllDoctorsActivity.class)));

        no_Trending_ads_layout = findViewById(R.id.no_Trending_ads_layout);
        add_new_ad_btn = findViewById(R.id.add_new_ad_btn);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.chats:
                    startActivity(new Intent(MainActivity.this, ChatList.class));
                    return true;
                case R.id.ads:
                    startActivity(new Intent(MainActivity.this, AllMyAdsActivity.class));
                    return true;
                case R.id.search:
                    startActivity(new Intent(MainActivity.this, SearchActivity.class));
                    return true;
            }
            return true;
        });

        trendingRecyclerView = findViewById(R.id.trendingRecyclerView);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        trendingRecyclerView.setLayoutManager(horizontalLayoutManager);
    }

    private void getTrending() {
        databaseReference.child("Ads").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentList.clear();

                if (snapshot.exists()) {
                    for (DataSnapshot eachAdRecord : snapshot.getChildren()) {
                        AdDetail ad = new AdDetail();
                        if (eachAdRecord.child("Sold").exists() && !eachAdRecord.child("Sold").getValue(String.class).equals("0")) {
                            ad.setAd_Sold(eachAdRecord.child("Sold").getValue(String.class));
                            ad.setAd_ID(eachAdRecord.getKey());
                            ad.setAd_Title(eachAdRecord.child("Title").getValue(String.class));
                            ad.setAd_Img(eachAdRecord.child("Image").getValue(String.class));
                            ad.setAd_Address(eachAdRecord.child("Address").getValue(String.class));
                            ad.setAd_Category_FID(eachAdRecord.child("Category").getValue(String.class));
                            ad.setAd_SubCategory(eachAdRecord.child("SubCategory").getValue(String.class));
                            ad.setAd_Quantity(eachAdRecord.child("Quantity").getValue(String.class));
                            ad.setAd_Sold(eachAdRecord.child("Sold").getValue(String.class));
                            ad.setAd_Price(eachAdRecord.child("Price").getValue(String.class));
                            ad.setAd_Desc(eachAdRecord.child("Description").getValue(String.class));
                            ad.setSellerID(eachAdRecord.child("SellerID").getValue(String.class));
                            ad.setDate(eachAdRecord.child("Date").getValue(String.class));
                            currentList.add(ad);
                        }
                    }
                    if (currentList.size() < 10) {
                        for (DataSnapshot eachAdRecord : snapshot.getChildren()) {
                            if (currentList.size() < 10) {
                                AdDetail ad = new AdDetail();
                                if (eachAdRecord.child("Sold").exists() && eachAdRecord.child("Sold").getValue(String.class).equals("0")) {
                                    ad.setAd_Sold(eachAdRecord.child("Sold").getValue(String.class));
                                    ad.setAd_ID(eachAdRecord.getKey());
                                    ad.setAd_Title(eachAdRecord.child("Title").getValue(String.class));
                                    ad.setAd_Img(eachAdRecord.child("Image").getValue(String.class));
                                    ad.setAd_Address(eachAdRecord.child("Address").getValue(String.class));
                                    ad.setAd_Category_FID(eachAdRecord.child("Category").getValue(String.class));
                                    ad.setAd_SubCategory(eachAdRecord.child("SubCategory").getValue(String.class));
                                    ad.setAd_Quantity(eachAdRecord.child("Quantity").getValue(String.class));
                                    ad.setAd_Sold(eachAdRecord.child("Sold").getValue(String.class));
                                    ad.setAd_Price(eachAdRecord.child("Price").getValue(String.class));
                                    ad.setAd_Desc(eachAdRecord.child("Description").getValue(String.class));
                                    ad.setSellerID(eachAdRecord.child("SellerID").getValue(String.class));
                                    ad.setDate(eachAdRecord.child("Date").getValue(String.class));
                                    currentList.add(ad);
                                }
                            } else
                                break;
                        }
                    }

                    if (!currentList.isEmpty()) {
                        ShowTrending();
                        trendingRecyclerViewAdapter = new TrendingRecyclerViewAdapter(MainActivity.this, MainActivity.this, currentList);
                        trendingRecyclerViewAdapter.setClickListener(MainActivity.this);
                        trendingRecyclerView.setAdapter(trendingRecyclerViewAdapter);
                    } else {
                        HideTrending();
                    }
                } else {
                    HideTrending();
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
    }

    private void ShowTrending() {
        no_Trending_ads_layout.setVisibility(View.GONE);
        trendingRecyclerView.setVisibility(View.VISIBLE);
    }

    private void HideTrending() {
        no_Trending_ads_layout.setVisibility(View.VISIBLE);
        trendingRecyclerView.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.home);
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
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("Exit").setMessage("Are you sure to exit?")
                .setNegativeButton("No", (dialog, which) -> dialog.cancel()).setPositiveButton("Exit", (dialog, which) -> finish()).show();
    }

    @Override
    public void onItemClick(View view, int position) {
        if (position >= 0) {
            Intent o = new Intent(MainActivity.this, AddDetail.class);
            o.putExtra("Ad", currentList.get(position));
            startActivity(o);
        }
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            if (e1!=null && e2!=null && e1.getX() < e2.getX()) {
                currPosition = getVisibleViews("left");
            } else {
                currPosition = getVisibleViews("right");
            }

            horizontalScrollView.smoothScrollTo(layouts.get(currPosition)
                    .getLeft(), 0);
            return true;
        }
    }

    public int getVisibleViews(String direction) {
        Rect hitRect = new Rect();
        int position = 0;
        int rightCounter = 0;
        for (int i = 0; i < layouts.size(); i++) {
            if (layouts.get(i).getLocalVisibleRect(hitRect)) {
                if (direction.equals("left")) {
                    position = i;
                    break;
                } else if (direction.equals("right")) {
                    rightCounter++;
                    position = i;
                    if (rightCounter == 2)
                        break;
                }
            }
        }
        return position;
    }
}