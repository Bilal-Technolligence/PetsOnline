package com.petsonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.petsonline.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    ImageView imageView;
    TextView name,gmail;
    String eName,eImage,userName;
   public String abc;
    ActionBarDrawerToggle drawerToggle;
    DatabaseReference dref= FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser() ;
    String uid = user.getUid();
    public int logout=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());


        drawerLayout = (DrawerLayout) findViewById(R.id.drawarLayout);

        Intent in =getIntent();
        userName = in.getStringExtra( "name" );
        //adding drawar button
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        //navbar item click



        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
           // getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //header click navbar
        View headerview = navigationView.getHeaderView(0);
        imageView = (ImageView) headerview.findViewById(R.id.profile_image);
        name = (TextView)headerview.findViewById( R.id.ename );



            dref.child( "Employee_Profile" ).child( uid ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    eName = dataSnapshot.child( "name" ).getValue().toString();
                    name.setText(String.valueOf( eName ));
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



    }

    protected abstract int getContentViewId();


    //drawer open close click
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.home: {

                Intent in = new Intent(this,MainActivity.class);
                startActivity(in);
                finish();
                break;
            }

            case R.id.profile: {
                Intent in = new Intent(this,MainActivity.class);
                startActivity(in);
                finish();

                break;
            }
            case R.id.history: {
                Intent in = new Intent(this,ChatList.class);
                startActivity(in);
                finish();
                break;

            }

            case R.id.logout: {

                if (uid != null) {
                    startActivity(new Intent(this, LoginActivity.class));
                    finish();
                    break;

                }
            }
        }
        return false;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in = new Intent( BaseActivity.this, MainActivity.class );
        startActivity(in);
        finish();
    }
}
