package com.petsonline.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.petsonline.R;
import com.petsonline.util.BaseUtil;

public class RegisterActivity extends AppCompatActivity {
    Button btnCancel;
    Button btnSignup;
    EditText txtEmail, txtPassword, txtName, txtReenterPassword;
    TextView already;
    String eName;
    Spinner spinnerLoginAs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_register);

        spinnerLoginAs = findViewById(R.id.spinner_loginAs);
        ArrayAdapter<String> spinnerCountShoesArrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.LoginAs));
        spinnerLoginAs.setAdapter(spinnerCountShoesArrayAdapter);

        btnCancel = findViewById(R.id.btncancel);
        btnSignup = findViewById(R.id.btnregister);
        txtName = findViewById(R.id.reguname);
        txtEmail = findViewById(R.id.regmail);
        txtPassword = findViewById(R.id.regpass);

        txtReenterPassword = findViewById(R.id.regconcpass);

        btnSignup.setOnClickListener(view -> {
            eName = txtName.getText().toString();
            String email = txtEmail.getText().toString().trim();
            String password = txtPassword.getText().toString().trim();
            String RePassword = txtReenterPassword.getText().toString().trim();
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                txtEmail.setError("Invalid Email");
                txtEmail.setFocusable(true);
            } else if (password.length() < 6) {
                txtPassword.setError("Password length must be more than 6 characters");
                txtPassword.setFocusable(true);
            } else if (RePassword.length() < 6) {
                txtReenterPassword.setError("Confirmation Password length must be more than 6 characters");
                txtReenterPassword.setFocusable(true);
            } else if (!password.equals(RePassword)) {
                Toast.makeText(RegisterActivity.this, "Passwords didn't matched!", Toast.LENGTH_SHORT).show();
            } else if (spinnerLoginAs.getSelectedItemPosition() == 0) {
                Toast.makeText(RegisterActivity.this, "Please select your SignUp Role", Toast.LENGTH_SHORT).show();
            } else {
                registeruser(email, password);
            }
        });

        btnCancel.setOnClickListener(view -> {
            Intent i = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });

        already = findViewById(R.id.alreadyacc);
        already.setOnClickListener(view -> {
            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        });
    }

    private void registeruser(final String email, final String password) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference reference = database.getReference("Users");

                        reference.child(uid).child("Email").setValue(email);
                        reference.child(uid).child("Password").setValue(password);
                        reference.child(uid).child("Role").setValue(spinnerLoginAs.getSelectedItem().toString());
                        new BaseUtil(RegisterActivity.this).SetLoginRole(spinnerLoginAs.getSelectedItem().toString());
                        reference.child(uid).child("Id").setValue(uid);
                        Intent in = new Intent(RegisterActivity.this, CompleteProfile.class);
                        in.putExtra("name", String.valueOf(eName));
                        Toast.makeText(RegisterActivity.this, "Registered....\n" + email, Toast.LENGTH_SHORT).show();
                        startActivity(in);
                        finish();


                    } else {
                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }

                }).addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "" + e.getMessage(),
                        Toast.LENGTH_SHORT).show());
    }
}

