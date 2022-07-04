package com.petsonline.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.petsonline.models.AdDetail;
import com.petsonline.models.MyPet;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class OwnPetDetailActivity extends AppCompatActivity {
    private EditText txtTitle,txtCity,txtQuantity,txtDescription,edAge;
    private ImageView image;
    private MyPet petDetail;
    private Button updateBtn,deleteBtn;
    private View SelectImg;
    private final int SELECT_PICTURE = 100;
    private Uri filepath;
    private StorageReference storageReference;
    private DatabaseReference firebaseDatabase;
    private ProgressDialog pd ;
    private ProgressBar progressBar;
    private View CareTakerDetailLayout;
    private TextView eName;
    private ImageView CareTakerImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_pet_detail);
        try {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Pet Detail");
        }catch (Exception ignored){}

        SelectImg = findViewById(R.id.SelectImg);
        pd = new ProgressDialog(this);
        petDetail = (MyPet) getIntent().getSerializableExtra("Pet");

        progressBar = findViewById(R.id.progressBar);

        eName = findViewById(R.id.HandoverName);
        CareTakerImage = findViewById(R.id.CareTakerImage);

        updateBtn = findViewById(R.id.updateBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        CareTakerDetailLayout = findViewById(R.id.CareTakerDetailLayout);
        image = findViewById(R.id.image);
        edAge = findViewById(R.id.edAge);
        txtTitle = findViewById(R.id.edTitle);
        txtCity = findViewById(R.id.edAddress);
        txtQuantity = findViewById(R.id.edQuantity);
        txtDescription = findViewById(R.id.edDescription);

        if (petDetail.getMyPet_Img().trim().equals("")) {
            image.setImageResource(R.drawable.no_image_icon);
        } else {
            Picasso.get().load(petDetail.getMyPet_Img()).into(image);
        }

        edAge.setText(petDetail.getMyPet_Age());
        txtTitle.setText(petDetail.getMyPet_Title());
        txtCity.setText(petDetail.getAddress());

        txtQuantity.setText(petDetail.getMyPet_Quantity());
        txtDescription.setText(petDetail.getMyPet_Desc());

        updateBtn.setOnClickListener(view -> UpdateAd());
        deleteBtn.setOnClickListener(view -> DeleteAd());

        if(petDetail.getSelection())
        {
            CareTakerDetailLayout.setVisibility(View.VISIBLE);

            FirebaseDatabase.getInstance().getReference().child("Employee_Profile").child(petDetail.getHandoverTo()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        eName.setText(Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString());
                        if (Objects.requireNonNull(dataSnapshot.child("imageurl").getValue()).toString().equals("")) {
//                        Picasso.get().load( dataSnapshot.child( "imageurl" ).getValue().toString() ).into( imageView );
                        } else {
                            Glide.with(OwnPetDetailActivity.this).load(Objects.requireNonNull(dataSnapshot.child("imageurl").getValue()).toString()).into(CareTakerImage);
                            //Picasso.get().load().into();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        else {
            CareTakerDetailLayout.setVisibility(View.GONE);
        }

        image.setOnClickListener(view -> ChooseImage());
        SelectImg.setOnClickListener(view -> ChooseImage());

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void UploadImage() {
        StorageReference ref;

        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssZ", Locale.getDefault());
        String path = "IMG-" + df.format(Calendar.getInstance().getTime()) + "AP" + filepath.getLastPathSegment();

        ref = storageReference.child("Pets").child("Pictures").child(path);
        if (filepath != null) {
            pd.setTitle("Uploading Pet Image....");
            pd.show();

            ref.putFile(filepath).addOnSuccessListener(taskSnapshot -> {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(pd::dismiss, 500);
                Task<Uri> result = Objects.requireNonNull(Objects.requireNonNull(taskSnapshot.getMetadata()).getReference()).getDownloadUrl();
                result.addOnSuccessListener(uri -> {
                    String urlToImage = uri.toString();
                    saveDatabase(urlToImage);
                    Picasso.get().load(urlToImage).transform(new CropCircleTransformation()).into(image);
                    Toast.makeText(getApplicationContext(), "Pet Image Uploaded", Toast.LENGTH_SHORT).show();
                });
            }).addOnFailureListener(e -> {
                pd.dismiss();
                Toast.makeText(getApplication(), "Pet Image Uploading failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }).addOnProgressListener(taskSnapshot -> {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                pd.setMessage("Uploaded" + (int) progress + "%");
            });
        } else {
            Toast.makeText(getApplication(), "No Pet Image file selected", Toast.LENGTH_SHORT).show();
            saveDatabase("");
        }
    }

    private void saveDatabase(String imgUrl) {
        progressBar.setVisibility(View.VISIBLE);

        firebaseDatabase.child("Pets").orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("Title", txtTitle.getText().toString());
                map.put("Description", txtDescription.getText().toString());
                DateFormat df = new SimpleDateFormat("ddMMyyyy HHmmss", Locale.getDefault());
                map.put("UpdatedOn",df.format(Calendar.getInstance().getTime()));
                if (edAge.getText() !=null && !edAge.getText().toString().trim().equals(""))
                    map.put("Age", edAge.getText().toString());
                map.put("Address", txtCity.getText().toString());
                map.put("Quantity", txtQuantity.getText().toString());
                if (!Objects.equals(imgUrl, ""))
                    map.put("Image", imgUrl);

                firebaseDatabase.child("Pets").child(petDetail.getMyPet_ID()).updateChildren(map).addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(OwnPetDetailActivity.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(OwnPetDetailActivity.this, "Unable to Update Data", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                    finish();
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void ChooseImage() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    private void UpdateAd() {
        if (txtTitle.getText()==null || txtTitle.getText().toString().equals(""))
        {
            txtTitle.setError("Enter Title");
            txtTitle.requestFocus();
            return;
        }
        if (txtCity.getText()==null || txtCity.getText().toString().equals(""))
        {
            txtCity.setError("Enter Address");
            txtCity.requestFocus();
            return;
        }
        if (txtQuantity.getText()==null || txtQuantity.getText().toString().equals(""))
        {
            txtQuantity.setError("Enter Quantity");
            txtQuantity.requestFocus();
            return;
        }
        if (txtDescription.getText()==null || txtDescription.getText().toString().equals(""))
        {
            txtDescription.setError("Enter Description");
            txtDescription.requestFocus();
            return;
        }
        if (filepath!=null)
        {
            UploadImage();
        }
        else
        {
            saveDatabase("");
        }
    }

    private void DeleteAd() {
        progressBar.setVisibility(View.VISIBLE);
        HashMap<String, Object> map = new HashMap<>();
        map.put("Deleted", "true");

        firebaseDatabase.child("Ads").child(petDetail.getMyPet_ID()).updateChildren(map).addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                Toast.makeText(OwnPetDetailActivity.this, "Data Deleted Successfully", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(OwnPetDetailActivity.this, "Unable to Delete Data", Toast.LENGTH_SHORT).show();
            }
        });
        progressBar.setVisibility(View.GONE);

        finish();
    }

    public void onActivityResult(int requestcode, int resultcode, Intent data) {
        super.onActivityResult(requestcode, resultcode, data);
        if (resultcode == RESULT_OK) {
            if (requestcode == SELECT_PICTURE) {
                filepath = data.getData();

                if (filepath != null) {
                    Picasso.get().load(filepath).transform(new CropCircleTransformation()).into(image);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}