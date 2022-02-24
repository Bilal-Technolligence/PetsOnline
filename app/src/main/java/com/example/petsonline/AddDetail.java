package com.example.petsonline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddDetail extends AppCompatActivity {
    TextView chat,cats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_detail);
        getSupportActionBar().setTitle("Ad Detail");
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        String uid = "DWBonAfwdTViiAYTxZgHMrx46j43";
        cats= (TextView) findViewById(R.id.chat);

        cats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent o = new Intent(AddDetail.this, Chat.class);
                o.putExtra("chaterId", uid);
                startActivity(o);
            }
        });

    }

//    @Override
//    protected int getContentViewId() {
//        return R.layout.activity_main;
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}