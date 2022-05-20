package com.petsonline.activities;

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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.petsonline.R;
import com.petsonline.models.AdDetail;
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

public class OwnAdDetailActivity extends AppCompatActivity {
    private TextView txtDate,available_quantity,total_quantity;
    private EditText txtPrice,txtTitle,txtCity,txtQuantity,txtDescription;
    private ImageView image;
    private AdDetail adDetail;
    private Button updateBtn,deleteBtn;
    private View SelectImg;
    private final int SELECT_PICTURE = 100;
    private Uri filepath;
    private Spinner spinnerCategory,spinnerSubCategory;
    private List<String> categories;
    private List<String> catsCategories;
    private List<String> dogsCategories;
    private List<String> hensCategories;
    private List<String> rabbitsCategories;
    private List<String> goatsCategories;
    private List<String> parrotsCategories;
    private String Category="";
    private ArrayAdapter<String> spinnerArrayAdapterSubCategory;
    private StorageReference storageReference;
    private DatabaseReference firebaseDatabase;
    private ProgressDialog pd ;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_ad_detail);

        try {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Ad Detail");
        }catch (Exception ignored){}

        SelectImg = findViewById(R.id.SelectImg);
        pd = new ProgressDialog(this);
        adDetail = (AdDetail) getIntent().getSerializableExtra("Ad");

        progressBar = findViewById(R.id.progressBar);

        updateBtn = findViewById(R.id.updateBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        spinnerCategory = findViewById(R.id.categorySpinner);
        spinnerSubCategory = findViewById(R.id.subCategorySpinner);

        image = findViewById(R.id.image);
        txtPrice = findViewById(R.id.edPrice);
        txtTitle = findViewById(R.id.edTitle);
        txtCity = findViewById(R.id.edAddress);
        txtDate = findViewById(R.id.date);
        txtQuantity = findViewById(R.id.edQuantity);
        txtDescription = findViewById(R.id.edDescription);
        available_quantity = findViewById(R.id.txt_available_quantity);
        total_quantity = findViewById(R.id.txt_total_quantity);

        if (adDetail.getAd_Img().trim().equals("")) {
            image.setImageResource(R.drawable.no_image_icon);
        } else {
            Picasso.get().load(adDetail.getAd_Img()).into(image);
        }

        txtPrice.setText(adDetail.getAd_Price());
        txtTitle.setText(adDetail.getAd_Title());
        txtCity.setText(adDetail.getAd_Address());

        DateFormat originalFormat = new SimpleDateFormat("ddMMyyyy HHmmss", Locale.getDefault());
        @SuppressLint("SimpleDateFormat")
        DateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        Date date;
        try {
            date = originalFormat.parse(adDetail.getDate());
            assert date != null;
            String formattedDate = targetFormat.format(date);
            txtDate.setText("Ad was uploaded on " + formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        total_quantity.setText(adDetail.getAd_Quantity());
        int available = Integer.parseInt(adDetail.getAd_Quantity()) - Integer.parseInt(adDetail.getAd_Sold());

        if (available>0)
            available_quantity.setText(String.valueOf(available));
        else
            available_quantity.setText("0");

        txtQuantity.setText(adDetail.getAd_Quantity());
        txtDescription.setText(adDetail.getAd_Desc());

        updateBtn.setOnClickListener(view -> UpdateAd());
        deleteBtn.setOnClickListener(view -> DeleteAd());

        Category = adDetail.getAd_Category_FID();

        image.setOnClickListener(view -> ChooseImage());
        SelectImg.setOnClickListener(view -> ChooseImage());

        catsCategories = Arrays.asList(getResources().getStringArray(R.array.CatsSubCategory));
        dogsCategories = Arrays.asList(getResources().getStringArray(R.array.DogsSubCategory));
        parrotsCategories = Arrays.asList(getResources().getStringArray(R.array.ParrotsSubCategory));
        goatsCategories = Arrays.asList(getResources().getStringArray(R.array.GoatsSubCategory));
        hensCategories = Arrays.asList(getResources().getStringArray(R.array.HensSubCategory));
        rabbitsCategories = Arrays.asList(getResources().getStringArray(R.array.RabbitsSubCategory));

        categories = Arrays.asList(getResources().getStringArray(R.array.categories)) ;
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
                            spinnerArrayAdapterSubCategory = new ArrayAdapter<>(OwnAdDetailActivity.this, android.R.layout.simple_spinner_dropdown_item, catsCategories);
                            spinnerSubCategory.setAdapter(spinnerArrayAdapterSubCategory);
                            int index =catsCategories.indexOf(adDetail.getAd_SubCategory()) ;
                            spinnerSubCategory.setSelection(index);
                            break;
                        case 2:
                            spinnerArrayAdapterSubCategory = new ArrayAdapter<>(OwnAdDetailActivity.this, android.R.layout.simple_spinner_dropdown_item, dogsCategories);
                            spinnerSubCategory.setAdapter(spinnerArrayAdapterSubCategory);
                            int index1 = dogsCategories.indexOf(adDetail.getAd_SubCategory()) ;
                            spinnerSubCategory.setSelection(index1);
                            break;
                        case 3:
                            spinnerArrayAdapterSubCategory = new ArrayAdapter<>(OwnAdDetailActivity.this, android.R.layout.simple_spinner_dropdown_item, hensCategories);
                            spinnerSubCategory.setAdapter(spinnerArrayAdapterSubCategory);
                            spinnerSubCategory.setSelection(hensCategories.indexOf(adDetail.getAd_SubCategory()));
                            break;
                        case 4:
                            spinnerArrayAdapterSubCategory = new ArrayAdapter<>(OwnAdDetailActivity.this, android.R.layout.simple_spinner_dropdown_item, rabbitsCategories);
                            spinnerSubCategory.setAdapter(spinnerArrayAdapterSubCategory);
                            spinnerSubCategory.setSelection(rabbitsCategories.indexOf(adDetail.getAd_SubCategory()));
                            break;
                        case 5:
                            spinnerArrayAdapterSubCategory = new ArrayAdapter<>(OwnAdDetailActivity.this, android.R.layout.simple_spinner_dropdown_item, goatsCategories);
                            spinnerSubCategory.setAdapter(spinnerArrayAdapterSubCategory);
                            spinnerSubCategory.setSelection(goatsCategories.indexOf(adDetail.getAd_SubCategory()));
                            break;
                        case 6:
                            spinnerArrayAdapterSubCategory = new ArrayAdapter<>(OwnAdDetailActivity.this, android.R.layout.simple_spinner_dropdown_item, parrotsCategories);
                            spinnerSubCategory.setAdapter(spinnerArrayAdapterSubCategory);
                            spinnerSubCategory.setSelection(parrotsCategories.indexOf(adDetail.getAd_SubCategory()));
                            break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Category = "";
            }

        });

        spinnerCategory.setSelection(categories.indexOf(adDetail.getAd_Category_FID()));

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
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

        firebaseDatabase.child("Ads").orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("Title", txtTitle.getText().toString());
                map.put("Description", txtDescription.getText().toString());
                DateFormat df = new SimpleDateFormat("ddMMyyyy HHmmss", Locale.getDefault());
                map.put("UpdatedOn",df.format(Calendar.getInstance().getTime()));
                map.put("Price", txtPrice.getText().toString());
                map.put("Address", txtCity.getText().toString());
                map.put("Category", Category);
                map.put("Quantity", txtQuantity.getText().toString());
                if (!Objects.equals(imgUrl, ""))
                    map.put("Image", imgUrl);

                if (spinnerSubCategory.getSelectedItemPosition() == 0)
                {
                    map.put("SubCategory","All");
                }
                else
                    map.put("SubCategory",spinnerSubCategory.getSelectedItem().toString());

                firebaseDatabase.child("Ads").child(adDetail.getAd_ID()).updateChildren(map).addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(OwnAdDetailActivity.this, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(OwnAdDetailActivity.this, "Unable to Update Data", Toast.LENGTH_SHORT).show();
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
        if (Category.equals(""))
        {
            Toast.makeText(this, "No Category Selected, Please Select one...", Toast.LENGTH_SHORT).show();
            return;
        }
        if (txtPrice.getText()==null || txtPrice.getText().toString().equals(""))
        {
            txtPrice.setError("Enter Price");
            txtPrice.requestFocus();
            return;
        }
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

        firebaseDatabase.child("Ads").child(adDetail.getAd_ID()).updateChildren(map).addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                Toast.makeText(OwnAdDetailActivity.this, "Data Deleted Successfully", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(OwnAdDetailActivity.this, "Unable to Delete Data", Toast.LENGTH_SHORT).show();
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