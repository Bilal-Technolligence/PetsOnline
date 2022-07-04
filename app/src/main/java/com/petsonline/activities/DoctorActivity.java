package com.petsonline.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.petsonline.R;
import com.petsonline.util.BaseUtil;

public class DoctorActivity extends AppCompatActivity {
    CardView gotoChats, ProfileCard,logoutCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        gotoChats = findViewById(R.id.ChatsCard);
        logoutCard = findViewById(R.id.Logout);
        ProfileCard = findViewById(R.id.ProfileCard);

        gotoChats.setOnClickListener(view -> startActivity(new Intent(DoctorActivity.this,ChatListDoctor.class)));
        ProfileCard.setOnClickListener(view -> startActivity(new Intent(DoctorActivity.this,DoctorMeProfileActivity.class)));

        logoutCard.setOnClickListener(view -> {
            new BaseUtil(DoctorActivity.this).ClearPreferences();
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(DoctorActivity.this, LoginActivity.class);
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