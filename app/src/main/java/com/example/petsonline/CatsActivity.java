package com.example.petsonline;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CatsActivity extends AppCompatActivity {
    CardView chat,catsDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cats);
        getSupportActionBar().setTitle("Cats");
        catsDetail= (CardView) findViewById(R.id.catsDetails);
        catsDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent o = new Intent(CatsActivity.this, AddDetail.class);
                startActivity(o);
            }
        });

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}