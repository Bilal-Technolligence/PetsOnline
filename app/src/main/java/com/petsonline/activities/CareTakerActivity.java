package com.petsonline.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.petsonline.R;
import com.petsonline.util.BaseUtil;

public class CareTakerActivity extends AppCompatActivity {
    TextView gotoHandOverPets,gotoCareTakerData,gotoChats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_taker);

        gotoHandOverPets = findViewById(R.id.gotoHandOverPets);
        gotoChats = findViewById(R.id.gotoChats);
        gotoCareTakerData = findViewById(R.id.gotoCareTakerData);

        gotoHandOverPets.setOnClickListener(view -> startActivity(new Intent(CareTakerActivity.this,HandoverPetsToMeActivity.class)));
        gotoChats.setOnClickListener(view -> startActivity(new Intent(CareTakerActivity.this,AllChatsCaretakerActivity.class)));
        gotoCareTakerData.setOnClickListener(view -> startActivity(new Intent(CareTakerActivity.this, CareTakerMeProfileActivity.class)));
    }

    @Override
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
    }
}