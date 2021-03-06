package com.example.petsonline;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    Button login;
    TextView signup,mRecoverPasswordtv;
    EditText emailValidate, password;
    ProgressDialog pd;
    String name,UserEmail,UserPassword,userId;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    DatabaseReference dref= FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_login);

//        getSupportActionBar().setTitle("LogIn");
      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pd=new ProgressDialog(this);
        pd.setMessage("Logging In..... ");
        login= (Button) findViewById(R.id.btnlogin);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Logging In..... ");
        final FirbaseAuthenticationClass firbaseAuthenticationClass=new FirbaseAuthenticationClass();
        Intent in =getIntent();
        name = in.getStringExtra( "name" );




        emailValidate = (EditText)findViewById(R.id.loginuname);

        password = (EditText) findViewById(R.id.loginpass);



       // mRecoverPasswordtv = (TextView) findViewById(R.id.recoverpasswordtv);
        //recover password TextviewCLICK
//        mRecoverPasswordtv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showRecoverPasswordDialog();
//            }
//        });




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String EMAIL = emailValidate.getText().toString().trim();
                String PASSWORD = password.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(EMAIL).matches()){
                    emailValidate.setError("Invalid email");
                    emailValidate.setFocusable(true);
                }else {
                    progressDialog.show();
                    firbaseAuthenticationClass.LoginUser(EMAIL,PASSWORD, LoginActivity.this, progressDialog);

                }
            }
        });



        signup= (TextView) findViewById(R.id.notreg);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void loginuser(final String EMAIL, final String PASSWORD) {
        pd.setMessage("Logging In....");
        pd.show();
        dref.child( "Users" ).addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds_user:dataSnapshot.getChildren())
                    if(ds_user.exists()) {
                        UserEmail = ds_user.child( "Email" ).getValue().toString();
                        userId = ds_user.getKey();
                        UserPassword = ds_user.child( "Password" ).getValue().toString();
                        if (UserEmail.equals( EMAIL )&& UserPassword.equals( PASSWORD )) {
                            if(name==null)
                                name= userId;
                            pd.dismiss();
                            // Sign in success, update UI with the signed-in user's information
//                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent in = new Intent( LoginActivity.this, MainActivity.class );
                            in.putExtra( "name", String.valueOf( name ) );

                            startActivity( in );
                            finish();
                        }

                        else {
                            pd.dismiss();


                        }

                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        } );
    }

    private void showRecoverPasswordDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        LinearLayout linearLayout=new LinearLayout(this);
        final EditText emailET=new EditText(this);
        emailET.setHint("Email");
        emailET.setInputType( InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailET.setMinEms(16);



        linearLayout.addView(emailET);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);
        //button recover
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email=emailET.getText().toString().trim();
                beginRecovery(email);
            }
        });
//button cancel
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void beginRecovery(String email) {

        pd.setMessage("Sending Email....");
        pd.show();
        mAuth = FirebaseAuth.getInstance(  );
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Email Sent", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
