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
import android.widget.TextView;
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

public class CreateNewAdActivity extends AppCompatActivity {
    private final int SELECT_PICTURE = 100;
    private ProgressDialog pd ;
    private StorageReference storageReference;
    private DatabaseReference firebaseDatabase;
    private Uri filepath;
    private ImageView image;
    private View SelectImg,SaveAd;
    private EditText edPrice,edTitle,edAddress,edQuantity,edDescription;
    private Spinner spinnerCategory,spinnerSubCategory;
    private String Category="";
    private String[] categories;
    private String[] catsCategories;
    private String[] dogsCategories;
    private String[] hensCategories;
    private String[] rabbitsCategories;
    private String[] goatsCategories;
    private String[] parrotsCategories;

    ArrayAdapter<String> spinnerArrayAdapterSubCategory;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_ad);

        pd = new ProgressDialog(this);

        image = findViewById(R.id.image);
        SelectImg = findViewById(R.id.SelectImg);
        SaveAd = findViewById(R.id.SaveData);

        progressBar = findViewById(R.id.progressBar);

        edPrice = findViewById(R.id.edPrice);
        edTitle = findViewById(R.id.edTitle);
        edAddress = findViewById(R.id.edAddress);
        edQuantity = findViewById(R.id.edQuantity);
        edDescription = findViewById(R.id.edDescription);
        spinnerCategory = findViewById(R.id.categorySpinner);
        spinnerSubCategory = findViewById(R.id.subCategorySpinner);

        catsCategories = getResources().getStringArray(R.array.CatsSubCategory);
        dogsCategories = getResources().getStringArray(R.array.DogsSubCategory);
        parrotsCategories = getResources().getStringArray(R.array.ParrotsSubCategory);
        goatsCategories = getResources().getStringArray(R.array.GoatsSubCategory);
        hensCategories = getResources().getStringArray(R.array.HensSubCategory);
        rabbitsCategories = getResources().getStringArray(R.array.RabbitsSubCategory);

        categories = getResources().getStringArray(R.array.categories);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(spinnerArrayAdapter);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    Category = "";
                else {
                    Category = parent.getItemAtPosition(position).toString();

                    switch (position)
                    {
                        case 1:
                            spinnerArrayAdapterSubCategory = new ArrayAdapter<>(CreateNewAdActivity.this, android.R.layout.simple_spinner_dropdown_item, catsCategories);
                            spinnerSubCategory.setAdapter(spinnerArrayAdapterSubCategory);
                            break;
                        case 2:
                            spinnerArrayAdapterSubCategory = new ArrayAdapter<>(CreateNewAdActivity.this, android.R.layout.simple_spinner_dropdown_item, dogsCategories);
                            spinnerSubCategory.setAdapter(spinnerArrayAdapterSubCategory);
                            break;
                        case 3:
                            spinnerArrayAdapterSubCategory = new ArrayAdapter<>(CreateNewAdActivity.this, android.R.layout.simple_spinner_dropdown_item, hensCategories);
                            spinnerSubCategory.setAdapter(spinnerArrayAdapterSubCategory);
                            break;
                        case 4:
                            spinnerArrayAdapterSubCategory = new ArrayAdapter<>(CreateNewAdActivity.this, android.R.layout.simple_spinner_dropdown_item, rabbitsCategories);
                            spinnerSubCategory.setAdapter(spinnerArrayAdapterSubCategory);
                            break;
                        case 5:
                            spinnerArrayAdapterSubCategory = new ArrayAdapter<>(CreateNewAdActivity.this, android.R.layout.simple_spinner_dropdown_item, goatsCategories);
                            spinnerSubCategory.setAdapter(spinnerArrayAdapterSubCategory);
                            break;
                        case 6:
                            spinnerArrayAdapterSubCategory = new ArrayAdapter<>(CreateNewAdActivity.this, android.R.layout.simple_spinner_dropdown_item, parrotsCategories);
                            spinnerSubCategory.setAdapter(spinnerArrayAdapterSubCategory);
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Category = "";
            }

        });

        image.setOnClickListener(view -> ChooseImage());
        SelectImg.setOnClickListener(view -> ChooseImage());

        SaveAd.setOnClickListener(view -> {
            if (filepath==null)
            {
                Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Category.equals(""))
            {
                Toast.makeText(this, "No Category Selected, Please Select one...", Toast.LENGTH_SHORT).show();
                return;
            }
            if (spinnerSubCategory.getSelectedItemPosition() == 0)
            {
                Toast.makeText(this, "No Sub-Category Selected, Please Select one...", Toast.LENGTH_SHORT).show();
                return;
            }
            if (edPrice.getText()==null || edPrice.getText().toString().equals(""))
            {
                edPrice.setError("Enter Price");
                edPrice.requestFocus();
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

        ref = storageReference.child("Ads").child("Pictures").child(path);
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

        firebaseDatabase.child("Ads").orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
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
                map.put("Price", edPrice.getText().toString());
                map.put("Title", edTitle.getText().toString());
                map.put("Description", edDescription.getText().toString());
                map.put("Address", edAddress.getText().toString());
                map.put("Category", Category);
                map.put("Quantity", edQuantity.getText().toString());
                map.put("Image", imgUrl);
                map.put("Sold", "0");
                DateFormat df = new SimpleDateFormat("ddMMyyyy HHmmss", Locale.getDefault());
                map.put("Date",df.format(Calendar.getInstance().getTime()));
                map.put("SellerID", FirebaseAuth.getInstance().getUid());
                map.put("SubCategory",spinnerSubCategory.getSelectedItem().toString());

                firebaseDatabase.child("Ads").child(ID).setValue(map);
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