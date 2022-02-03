package com.example.petsonline;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends BaseActivity {
    CardView chat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Main");
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        String uid = "DWBonAfwdTViiAYTxZgHMrx46j43";
        chat= (CardView) findViewById(R.id.cardA);

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent o = new Intent(MainActivity.this, Chat.class);
                o.putExtra("chaterId", uid);
                startActivity(o);
            }
        });

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( MainActivity.this );
        alertDialogBuilder.setTitle( "Logout" ).setMessage( "Are you sure to exit?" )
                .setNegativeButton( "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                } ).setPositiveButton( "Exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        } ).show();
    }
}