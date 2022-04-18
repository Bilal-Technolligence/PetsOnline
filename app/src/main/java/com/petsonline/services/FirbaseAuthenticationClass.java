package com.petsonline.services;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.petsonline.activities.CareTakerActivity;
import com.petsonline.activities.CompleteProfile;
import com.petsonline.activities.DoctorActivity;
import com.petsonline.activities.LoginActivity;
import com.petsonline.activities.MainActivity;
import com.petsonline.util.BaseUtil;

public class FirbaseAuthenticationClass extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    final DatabaseReference reference = database.getReference("Users");

    public void LoginUser(String EMAIL, String PASSWORD, final Activity activity, final ProgressDialog progressDialog) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(EMAIL, PASSWORD)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uid = mAuth.getCurrentUser().getUid();

                            reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String Role = dataSnapshot.child("Role").getValue(String.class);
                                    database.getReference("Employee_Profile").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists())
                                            {
                                                if (!snapshot.child("profilecompleted").exists() || !snapshot.child("profilecompleted").getValue(String.class).equals("true"))
                                                {
                                                    activity.startActivity(new Intent(activity, CompleteProfile.class));
                                                    activity.finish();
                                                    progressDialog.dismiss();
                                                    return;
                                                }

                                                new BaseUtil(activity).SetLoggedIn(true);
                                                new BaseUtil(activity).SetLoginRole(Role);

                                                switch (Role) {
                                                    case "Buyer/Seller":
                                                        activity.startActivity(new Intent(activity, MainActivity.class));
                                                        break;
                                                    case "Care Taker":
                                                        activity.startActivity(new Intent(activity, CareTakerActivity.class));
                                                        break;
                                                    case "Doctor":
                                                        activity.startActivity(new Intent(activity, DoctorActivity.class));
                                                        break;
                                                    default:
                                                        activity.startActivity(new Intent(activity, LoginActivity.class));
                                                        break;
                                                }
                                                activity.finish();
                                                progressDialog.dismiss();
                                            }else
                                            {
                                                activity.startActivity(new Intent(activity, CompleteProfile.class));
                                                activity.finish();
                                                progressDialog.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}

//                    RegisterUser(userGmail, userPassword, Contact, Name,  imagePath, lati, loni, addressString, CompleteProfileActivity.this, progressDialog);


//    public void RegisterUser(final String userGmail, String userPassword, final String contact, final String name, final String imagePath, final String address, final CompleteProfileActivity completeProActivity, final ProgressDialog progressDialog) {
//        FirebaseAuth.getInstance().createUserWithEmailAndPassword(userGmail, userPassword)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//                            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/" + FirebaseDatabase.getInstance().getReference().child("Users").push().getKey());
//                            storageReference.putFile(Uri.parse(imagePath)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                @Override
//                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//                                    while (!uriTask.isSuccessful()) ;
//                                    Uri downloadUri = uriTask.getResult();
//
////                                  UserAttr userAttr = new UserAttr();
////                                  userAttr.setEmail(userGmail);
////                                  userAttr.setContact(contact);
////                                  userAttr.setName(name);
////                                  userAttr.setAddress(address);
////                                  userAttr.setId(uid);
////                                  userAttr.setImageUrl(downloadUri.toString());
////                                  reference.child(uid).setValue(userAttr);
////                                  Toast.makeText(completeProfileActivity, "Account Created", Toast.LENGTH_SHORT).show();
////                                  completeProfileActivity.startActivity(new Intent(completeProfileActivity, MainActivity.class));
////                                  progressDialog.dismiss();
//
//                                }
//                            });
//
//
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(completeProActivity, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
//            }
//        });
//    }
//}