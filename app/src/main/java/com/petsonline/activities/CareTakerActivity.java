package com.petsonline.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.petsonline.R;
import com.petsonline.util.BaseUtil;

public class CareTakerActivity extends AppCompatActivity {
    CardView gotoHandOverPets,gotoCareTakerData,gotoChats,logoutCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_taker);

        gotoHandOverPets = findViewById(R.id.HandOverPetsCard);
        gotoChats = findViewById(R.id.ChatsCard);
        gotoCareTakerData = findViewById(R.id.ProfileCard);
        logoutCard = findViewById(R.id.Logout);

        gotoHandOverPets.setOnClickListener(view -> startActivity(new Intent(CareTakerActivity.this,HandoverPetsToMeActivity.class)));
        gotoChats.setOnClickListener(view -> startActivity(new Intent(CareTakerActivity.this,ChatListCareTaker.class)));
        gotoCareTakerData.setOnClickListener(view -> startActivity(new Intent(CareTakerActivity.this, CareTakerMeProfileActivity.class)));

        logoutCard.setOnClickListener(view -> {
            new BaseUtil(CareTakerActivity.this).ClearPreferences();
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(CareTakerActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            //noinspection RestrictedApi
            m.setOptionalIconsVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.logout_button)
        {
            new BaseUtil(this).ClearPreferences();
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(CareTakerActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}