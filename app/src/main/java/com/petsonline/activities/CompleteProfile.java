package com.petsonline.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.petsonline.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.petsonline.models.profiledata;
import com.squareup.picasso.Picasso;

import java.io.IOException;

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
        pimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent();
//                intent.setType("images/*");
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
            }
        });


        database=FirebaseDatabase.getInstance();
        ref = database.getReference("Employee_Profile");
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        mAuth = FirebaseAuth.getInstance(  );

        final String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        eConfirm.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Profile Data Saving....");
                progressDialog.show();
                if (!eName.getText().toString().isEmpty() && !eMobile.getText().toString().isEmpty()&& !eAddress.getText().toString().isEmpty()&& !eAge.getText().toString().isEmpty() && !filepath.getPath().isEmpty() )
                {

                    final String push = FirebaseDatabase.getInstance().getReference().child("Packages").push().getKey();
                    StorageReference fileReference  = storageReference.child("images/"+ push);
                    fileReference.putFile(filepath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    if(filepath!=null) {
                                        profiledata profiledata = new profiledata();
                                        profiledata.setID(id);
                                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                        while (!uriTask.isSuccessful());
                                        Uri downloadUri = uriTask.getResult();
                                        profiledata.setIMAGEURL(downloadUri.toString());
                                        profiledata.setNAME(eName.getText().toString());
                                        profiledata.setMOBILE(eMobile.getText().toString());
                                        profiledata.setADDRESS(eAddress.getText().toString());
                                        profiledata.setAge(eAge.getText().toString());

                                        ref.child(id)
                                                .setValue(profiledata);
                                        progressDialog.dismiss();
                                        Toast.makeText(CompleteProfile.this,"profile successfully saved..",Toast.LENGTH_LONG).show();
                                        Intent in = new Intent( CompleteProfile.this, LoginActivity.class );
                                        in.putExtra( "name", String.valueOf( name ) );
                                        startActivity(in);
                                        finish();

                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please enter all Information", Toast.LENGTH_SHORT).show();
                }

            }
        } );

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
                        if(profiledata.getIMAGEURL().equals( " " )) {
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

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M){
          //  final Uri data = intent.getData();
           // final File file = new File(data.getPath());
            // now you can upload your image file
            filepath=data.getData();
        } else {
            if (requestCode==requestCode&&resultCode==resultCode
                    &&data!=null && data.getData()!=null){

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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent in = new Intent( CompleteProfile.this, MainActivity.class );
        in.putExtra( "name", String.valueOf( name ) );
        startActivity(in);
        finish();
    }

}
