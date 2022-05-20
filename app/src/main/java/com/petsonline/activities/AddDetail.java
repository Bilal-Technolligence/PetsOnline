package com.petsonline.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.petsonline.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.petsonline.models.AdDetail;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddDetail extends AppCompatActivity {
    TextView txtPrice, txtTitle, txtCity, txtDate, txtCategory,
            txtQuantity, txtDescription,available_quantity,subcategory;
    CircleImageView imgProfile;
    TextView SellerName;
    ImageView image;
    Button btnChat;
    AdDetail adDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_detail);
        getSupportActionBar().setTitle("Ad Detail");

        adDetail = (AdDetail) getIntent().getSerializableExtra("Ad");

        image = findViewById(R.id.image);
        txtPrice = findViewById(R.id.price);
        txtTitle = findViewById(R.id.title);
        txtCity = findViewById(R.id.city);
        txtDate = findViewById(R.id.date);
        txtCategory = findViewById(R.id.category);
        txtQuantity = findViewById(R.id.quantity);
        txtDescription = findViewById(R.id.description);
        SellerName = findViewById(R.id.txtName);
        imgProfile = findViewById(R.id.imgProfile);
        btnChat = findViewById(R.id.chat);
        available_quantity = findViewById(R.id.available_quantity);
        subcategory = findViewById(R.id.subcategory);

        if (adDetail.getAd_Img().trim().equals("")) {
            image.setImageResource(R.drawable.logo_png);
        } else {
            Picasso.get().load(adDetail.getAd_Img()).into(image);
        }

        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DatabaseReference dref = FirebaseDatabase.getInstance().getReference();
        //FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String sellerID = adDetail.getSellerID();
        txtPrice.setText("RS " + adDetail.getAd_Price());
        txtTitle.setText("Title " + adDetail.getAd_Title());
        txtCity.setText("Address : " + adDetail.getAd_Address());
        subcategory.setText(adDetail.getAd_SubCategory());
        DateFormat originalFormat = new SimpleDateFormat("ddMMyyyy HHmmss", Locale.getDefault());
        @SuppressLint("SimpleDateFormat")
        DateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        Date date;
        try {
            date = originalFormat.parse(adDetail.getDate());
            assert date != null;
            String formattedDate = targetFormat.format(date);
            txtDate.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int available = Integer.parseInt(adDetail.getAd_Quantity()) - Integer.parseInt(adDetail.getAd_Sold());

        if (available>0)
            available_quantity.setText(String.valueOf(available));
        else
            available_quantity.setText("0");

        txtCategory.setText(adDetail.getAd_Category_FID());
        txtQuantity.setText(adDetail.getAd_Quantity());
        txtDescription.setText(adDetail.getAd_Desc());

        dref.child("Employee_Profile").child(sellerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    SellerName.setText(dataSnapshot.child("name").getValue().toString());
                    if (dataSnapshot.child("imageurl").getValue().toString().trim().equals("")) {
                        imgProfile.setImageResource(R.drawable.logo_png);
                    } else {
                        Picasso.get().load(dataSnapshot.child("imageurl").getValue().toString()).into(imgProfile);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnChat.setOnClickListener(view -> {
            if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(sellerID))
            {
                Toast.makeText(AddDetail.this, "You can't chat to yourself. This Ad was posted by You.", Toast.LENGTH_LONG).show();
                return;
            }
            Intent o = new Intent(AddDetail.this, Chat.class);
            o.putExtra("chaterId", sellerID);
            o.putExtra("Ad_ID", adDetail.getAd_ID());
            startActivity(o);
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