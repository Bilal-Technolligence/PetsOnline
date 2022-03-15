package com.petsonline;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.petsonline.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    Button btnCancel;
    Button btnSignup;
//    ProgressDialog progressDialog;
    EditText txtEmail,txtPassword,txtName,txtReenterPassword;
    TextView already;
    String eName;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_register);

       // getSupportActionBar().setTitle("Register");
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnCancel= (Button) findViewById(R.id.btncancel);
        btnSignup= (Button) findViewById(R.id.btnregister);
        txtName = (EditText)findViewById( R.id.reguname ) ;
        txtEmail =(EditText)findViewById( R.id.regmail ) ;
        txtPassword = (EditText)findViewById( R.id.regpass ) ;

        txtReenterPassword = (EditText)findViewById( R.id.regconcpass ) ;

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eName = txtName.getText().toString();
                String email=txtEmail.getText().toString().trim();
                String password=txtPassword.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

                    txtEmail.setError("Invalid Email");
                    txtEmail.setFocusable(true);

                } else if (password.length()<6){
                    txtPassword.setError("Password length must be more than 6 characters");
                    txtPassword.setFocusable(true);

                } else {

                    registeruser(email,password);
//                    Intent intent=new Intent(RegisterActivity.this,CompleteProfile.class);
//                    intent.putExtra("Email",email);
//                    intent.putExtra("Password",password);
//                    startActivity(intent);
//                    finish();
                }

            }
        });



        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });


        already = (TextView) findViewById(R.id.alreadyacc);
        already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });



    }

    private void registeruser(final String email, final String password) {
//        progressDialog.show();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            // Sign in success, update UI with the signed-in user's information
                            //progressDialog.dismiss();
                            //FirebaseUser user = mAuth.getCurrentUser();

                            String Email=email;
                            String Password=password;
//                            String Password = password;
                           // String uid=eName;
                            FirebaseDatabase database=FirebaseDatabase.getInstance();


                            DatabaseReference reference=database.getReference("Users");

                            reference.child(uid).child( "Email" ).setValue(Email);
                            reference.child(uid).child( "Password" ).setValue(password);
//                            reference.child(uid).child( "Password" ).setValue(Password);
                           reference.child(uid).child( "Id" ).setValue(uid);
                            Intent in = new Intent( RegisterActivity.this, CompleteProfile.class );
                            in.putExtra( "name", String.valueOf( eName ) );

                            Toast.makeText(RegisterActivity.this, "Registered....\n"+Email,Toast.LENGTH_SHORT).show();

//                            startActivity(new Intent(RegisterActivity.this, CompleteProfile.class));

                            startActivity( in );
                            finish();


                        } else {
                            // If sign in fails, display a message to the user.
//                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, ""+e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}

