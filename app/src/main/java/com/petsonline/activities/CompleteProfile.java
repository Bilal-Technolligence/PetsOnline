package com.petsonline.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.petsonline.R;
import com.petsonline.models.profiledata;
import com.petsonline.util.BaseUtil;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Objects;

public class CompleteProfile extends AppCompatActivity {
    EditText eName,eMobile,eEmail,eAddress,eAge;
    private static final int pick_image = 2;
    Button eConfirm , pback;
//    Spinner spinner;
    ImageView pimage;
    private Uri filepath;
    FirebaseDatabase database;
    String name;
    String check = "update";
    DatabaseReference ref;
    FirebaseStorage storage;
    ProgressDialog progressDialog;
    StorageReference storageReference ;
    private FirebaseAuth mAuth;
    private String Role = "";
    String id;
    profiledata mProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_complete_profile );

        eName=(EditText)findViewById(R.id.profnames);
        eMobile=(EditText)findViewById(R.id.profmobile);
        eAddress=(EditText)findViewById(R.id.profaddress);
        eAge=(EditText)findViewById(R.id.profage);
        eConfirm=(Button) findViewById(R.id.profCONFIRM);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Logging In..... ");

        Intent in = getIntent();
        final String update = in.getStringExtra( "update" );
        name = in.getStringExtra( "name" );

        pimage=(ImageView)findViewById(R.id.pimage);
        pimage.setOnClickListener(v -> {
//                Intent intent=new Intent();
//                intent.setType("images/*");
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);
        });

        database=FirebaseDatabase.getInstance();
        ref = database.getReference("Employee_Profile");
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        mAuth = FirebaseAuth.getInstance();

        id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        database.getReference("Users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    if (snapshot.child("Role").exists())
                    {
                        Role = snapshot.child("Role").getValue(String.class);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        eConfirm.setOnClickListener(v -> {
            if (!eName.getText().toString().isEmpty() && !eMobile.getText().toString().isEmpty()&& !eAddress.getText().toString().isEmpty()&& !eAge.getText().toString().isEmpty() )
            {
                progressDialog.setMessage("Profile Data Saving....");
                progressDialog.show();

                if (filepath!=null)
                {
                    final String push = FirebaseDatabase.getInstance().getReference().child("images").push().getKey();
                    StorageReference fileReference  = storageReference.child("images/"+ push);
                    fileReference.putFile(filepath)
                            .addOnSuccessListener(taskSnapshot -> {
                                mProfile = new profiledata();
                                mProfile.setID(id);
                                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!uriTask.isSuccessful());
                                Uri downloadUri = uriTask.getResult();
                                mProfile.setIMAGEURL(downloadUri.toString());
                                mProfile.setNAME(eName.getText().toString());
                                mProfile.setMOBILE(eMobile.getText().toString());
                                mProfile.setADDRESS(eAddress.getText().toString());
                                mProfile.setAge(eAge.getText().toString());
                                mProfile.setPROFILECOMPLETED("true");
                                mProfile.setROLE(Role);

                                new BaseUtil(CompleteProfile.this).SetLoggedIn(true);

                                SaveProfile(mProfile);
                            })
                            .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show());
                }
                else
                {
                    mProfile = new profiledata();
                    mProfile.setID(id);
                    mProfile.setIMAGEURL("");
                    mProfile.setNAME(eName.getText().toString());
                    mProfile.setMOBILE(eMobile.getText().toString());
                    mProfile.setADDRESS(eAddress.getText().toString());
                    mProfile.setAge(eAge.getText().toString());
                    mProfile.setPROFILECOMPLETED("true");
                    mProfile.setROLE(Role);

                    new BaseUtil(CompleteProfile.this).SetLoggedIn(true);

                    SaveProfile(mProfile);
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Please enter all Information", Toast.LENGTH_SHORT).show();
            }

        });

        ref.child(id).addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    profiledata profiledata = dataSnapshot.getValue(profiledata.class);
                    if(profiledata != null) {
                        eName.setText(profiledata.getNAME());
                        eMobile.setText(profiledata.getMOBILE());
                        eAddress.setText(profiledata.getADDRESS());
                        eAge.setText(profiledata.getAge());
                        if(profiledata.getIMAGEURL()==null || profiledata.getIMAGEURL().trim().equals( "" )) {
                            pimage.setImageResource(R.drawable.logo_png);
                        }
                        else {
                            Picasso.get().load( profiledata.getIMAGEURL() ).into( pimage );
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data!=null)
        {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
                filepath=data.getData();
                pimage.setImageURI(filepath);
            } else {
                if (data.getData() != null){
                    filepath=data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                        pimage.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*Intent in = new Intent( CompleteProfile.this, MainActivity.class );
        in.putExtra( "name", String.valueOf( name ) );
        startActivity(in);
        finish();*/
    }

    void SaveProfile(profiledata profiledata){
        ref.child(id).setValue(profiledata);
        progressDialog.dismiss();
        Toast.makeText(CompleteProfile.this,"profile successfully saved..",Toast.LENGTH_LONG).show();

        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String Role = dataSnapshot.child("Role").getValue(String.class);
                        switch (Objects.requireNonNull(Role)) {
                            case "Buyer/Seller":
                                startActivity(new Intent(CompleteProfile.this, MainActivity.class));
                                break;
                            case "Care Taker":
                                startActivity(new Intent(CompleteProfile.this, CareTakerActivity.class));
                                break;
                            case "Doctor":
                                startActivity(new Intent(CompleteProfile.this, DoctorActivity.class));
                                break;
                            default:
                                startActivity(new Intent(CompleteProfile.this, LoginActivity.class));
                                break;
                        }
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }

}
