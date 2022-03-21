package com.petsonline.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.petsonline.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AddDetail extends AppCompatActivity {
    TextView chat,name,gmail,cats;
    ImageView imageView;
    String eName,eImage,userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_detail);
        getSupportActionBar().setTitle("Ad Detail");

        imageView = (ImageView)findViewById(R.id.imgProfile);
        name = (TextView)findViewById( R.id.txtName );
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DatabaseReference dref= FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser() ;
        String currentUser = user.getUid();

        String uid = "DWBonAfwdTViiAYTxZgHMrx46j43";
        cats= (TextView) findViewById(R.id.chat);

        dref.child( "Employee_Profile" ).child( uid ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    eName = dataSnapshot.child( "name" ).getValue().toString();
                    name.setText(eName);
                    if(dataSnapshot.child( "imageurl" ).getValue().toString().equals( " " )) {
//                        Picasso.get().load( dataSnapshot.child( "imageurl" ).getValue().toString() ).into( imageView );
                    }
                    else {
                        Picasso.get().load( dataSnapshot.child( "imageurl" ).getValue().toString() ).into( imageView );

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

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