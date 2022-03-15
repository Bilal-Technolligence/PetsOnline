package com.petsonline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.petsonline.R;

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