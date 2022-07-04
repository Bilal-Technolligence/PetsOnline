package com.petsonline.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class AddNewPetActivity extends AppCompatActivity {
    private final int SELECT_PICTURE = 100;
    private ProgressDialog pd ;
    private StorageReference storageReference;
    private DatabaseReference firebaseDatabase;
    private Uri filepath;
    private ImageView image;
    private View SelectImg,SavePet;
    private EditText edTitle,edAddress,edQuantity,edDescription,edAge;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_pet);
        pd = new ProgressDialog(this);

        image = findViewById(R.id.image);
        SelectImg = findViewById(R.id.SelectImg);
        SavePet = findViewById(R.id.SaveData);

        progressBar = findViewById(R.id.progressBar);

        edAge = findViewById(R.id.edAge);
        edTitle = findViewById(R.id.edTitle);
        edAddress = findViewById(R.id.edAddress);
        edQuantity = findViewById(R.id.edQuantity);
        edDescription = findViewById(R.id.edDescription);

        image.setOnClickListener(view -> ChooseImage());
        SelectImg.setOnClickListener(view -> ChooseImage());

        SavePet.setOnClickListener(view -> {
            if (filepath==null)
            {
                Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
                return;
            }
            if (edTitle.getText()==null || edTitle.getText().toString().equals(""))
            {
                edTitle.setError("Enter Title");
                edTitle.requestFocus();
                return;
            }
            if (edAddress.getText()==null || edAddress.getText().toString().equals(""))
            {
                edAddress.setError("Enter Address");
                edAddress.requestFocus();
                return;
            }
            if (edQuantity.getText()==null || edQuantity.getText().toString().equals(""))
            {
                edQuantity.setError("Enter Quantity");
                edQuantity.requestFocus();
                return;
            }
            if (edDescription.getText()==null || edDescription.getText().toString().equals(""))
            {
                edDescription.setError("Enter Description");
                edDescription.requestFocus();
                return;
            }
            UploadImage();
        });

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void ChooseImage() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
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
                });
                Toast.makeText(getApplicationContext(), "Pet Image Uploaded", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                pd.dismiss();
                Toast.makeText(getApplication(), "Pet Image Uploading failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }).addOnProgressListener(taskSnapshot -> {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                pd.setMessage("Uploaded" + (int) progress + "%");
            });
        } else {
            Toast.makeText(getApplication(), "No Pet file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveDatabase(String imgUrl) {
        progressBar.setVisibility(View.VISIBLE);

        firebaseDatabase.child("Pets").orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String ID = "1";
                if (snapshot.exists())
                {
                    String LastID = "";
                    for (DataSnapshot s:snapshot.getChildren() ) {
                        LastID = s.getKey();
                    }
                    assert LastID != null;
                    int LastIntID = Integer.parseInt(LastID);
                    LastIntID++;
                    ID = String.valueOf(LastIntID);
                }
                HashMap<String, Object> map = new HashMap<>();
                if (edAge.getText() !=null && !edAge.getText().toString().trim().equals(""))
                    map.put("Age", edAge.getText().toString());
                map.put("Title", edTitle.getText().toString());
                map.put("Description", edDescription.getText().toString());
                map.put("Address", edAddress.getText().toString());
                map.put("Quantity", edQuantity.getText().toString());
                map.put("Image", imgUrl);
                map.put("SellerID", FirebaseAuth.getInstance().getUid());

                firebaseDatabase.child("Pets").child(ID).setValue(map);
                progressBar.setVisibility(View.GONE);

                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
}