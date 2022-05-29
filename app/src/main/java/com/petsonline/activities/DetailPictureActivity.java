package com.petsonline.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.petsonline.R;
import com.squareup.picasso.Picasso;

public class DetailPictureActivity extends AppCompatActivity {
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_picture);

        imageView = findViewById(R.id.imageView);

        String ImgUrl = getIntent().getStringExtra("ImageURL");

        if (ImgUrl!=null && !ImgUrl.equals(""))
        {
            Picasso.get().load(ImgUrl).into(imageView);
        }
    }
}