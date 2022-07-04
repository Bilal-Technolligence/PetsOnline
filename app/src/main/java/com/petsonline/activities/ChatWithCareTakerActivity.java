package com.petsonline.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.petsonline.R;
import com.petsonline.adapters.ChatAdapter;
import com.petsonline.models.MessageAttr;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class ChatWithCareTakerActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private final DatabaseReference dref = FirebaseDatabase.getInstance().getReference();
    private final ArrayList<MessageAttr> messageAttrs = new ArrayList<>();
    private EditText editText;
    ImageView SendButton, imageProfile;
    private TextView textName;
    Calendar calendar;
    SimpleDateFormat dateFormat;
    private String date;
    Button handOverYourPetsButton;
    private ChatAdapter chatAdapter;
    boolean ChatExist = false;
    private String SellerID;
    private String MineID;
    private String ChatID = "";
    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    ImageView imgAttachBtn;
    ImageView imgClearSelectedImage;
    private ImageView imageViewAttachPicture;
    private Uri SelectedImg;
    private View AttachedImgView;

    private ValueEventListener valueEventListener1,valueEventListener2,valueEventListener3,valueEventListener4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_with_care_taker);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        handOverYourPetsButton = findViewById(R.id.handOverYourPetsButton);
        recyclerView = findViewById(R.id.recyclerView);
        textName = findViewById(R.id.name);
        imageProfile = findViewById(R.id.imgProfile);

        progressDialog = new ProgressDialog(this);

        storageReference = FirebaseStorage.getInstance().getReference();
        imgClearSelectedImage = findViewById(R.id.clearAttachedImg);
        AttachedImgView = findViewById(R.id.AttachedImgView);
        AttachedImgView.setVisibility(View.GONE);

        imageViewAttachPicture = findViewById(R.id.imageViewAttachedPicture);
        imgAttachBtn = findViewById(R.id.imgAttachBtn);

        imgClearSelectedImage.setOnClickListener(view -> {
            AttachedImgView.setVisibility(View.GONE);
            imageViewAttachPicture.setImageDrawable(null);
            SelectedImg = null;
        });
        imgAttachBtn.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 2);
        });

        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        linearLayout.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayout);

        SellerID = getIntent().getStringExtra("chaterId");
        if (user != null)
            MineID = user.getUid();

        editText = findViewById(R.id.message);
        SendButton = findViewById(R.id.imgSend);

        SendButton.setOnClickListener(v -> SendMsg());

        LoadSellerProfile();
        CheckChat();
        UpdateReadReceipt();

        handOverYourPetsButton.setOnClickListener(view -> startActivity(new Intent(ChatWithCareTakerActivity.this,MyPetsHandOverActivity.class).putExtra("CareTaker",SellerID)));
    }

    private void UpdateReadReceipt() {
        dref.child("ChatListCareTaker").child(ChatID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    if (snapshot.child("LastMsg").exists())
                    {
                        String LastMsgBy = Objects.requireNonNull(snapshot.child("LastMsg").getValue()).toString();
                        if (!LastMsgBy.equals(MineID))
                        {
                            dref.child("ChatListCareTaker").child(ChatID).child("Read").setValue("true");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void LoadSellerProfile() {
        dref.child("Employee_Profile").child(SellerID).addValueEventListener(valueEventListener1 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    try {
                        String name = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                        String pic = Objects.requireNonNull(dataSnapshot.child("imageurl").getValue()).toString();
                        textName.setText(name);
                        if (!pic.equals(""))
                            Picasso.get().load(pic).into(imageProfile);
                    } catch (Exception e) {
                        Log.e("Error",e.getMessage());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void CheckChat() {
        dref.child("ChatListCareTaker").addValueEventListener(valueEventListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.exists()) {
                            if (Objects.equals(ds.child("ReceiverId").getValue(), SellerID) && Objects.equals(ds.child("SenderId").getValue(), MineID)) {
                                ChatExist = true;
                                ChatID = ds.getKey();
                                break;
                            }
                            if (Objects.equals(ds.child("ReceiverId").getValue(), MineID) && Objects.equals(ds.child("SenderId").getValue(), SellerID)) {
                                ChatExist = true;
                                ChatID = ds.getKey();
                                break;
                            }
                        }
                    }
                    if (!ChatExist)
                        CreateNewChat();
                    else
                        LoadChat(ChatID);
                } catch (Exception ignored) {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void LoadChat(String chat_id) {
        dref.child("MessagesCareTaker").child(chat_id).addValueEventListener(valueEventListener3 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageAttrs.clear();
                try {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            if (Objects.equals(ds.child("receiverId").getValue(), SellerID) && Objects.equals(ds.child("senderId").getValue(), MineID)) {
                                MessageAttr p = ds.getValue(MessageAttr.class);
                                messageAttrs.add(p);
                            }
                            if (Objects.equals(ds.child("receiverId").getValue(), MineID) && Objects.equals(ds.child("senderId").getValue(), SellerID)) {
                                MessageAttr p = ds.getValue(MessageAttr.class);
                                messageAttrs.add(p);
                            }
                        }
                    }
                    chatAdapter = new ChatAdapter(messageAttrs, ChatWithCareTakerActivity.this, ChatWithCareTakerActivity.this);
                    recyclerView.setAdapter(chatAdapter);
                } catch (Exception e) {
                    Log.e("Error ", e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void CreateNewChat() {
        ChatID = dref.child("ChatListCareTaker").push().getKey();

        dref.child("ChatListCareTaker").child(ChatID).child("ReceiverId").setValue(SellerID);
        dref.child("ChatListCareTaker").child(ChatID).child("SenderId").setValue(MineID);
        //dref.child("ChatListCareTaker").child(ChatID).child("AdID").setValue(AdID);
    }

    private void CreateChat() {
        ChatID = dref.child("ChatListCareTaker").push().getKey();

        dref.child("ChatListCareTaker").child(ChatID).child("ReceiverId").setValue(SellerID);
        dref.child("ChatListCareTaker").child(ChatID).child("SenderId").setValue(MineID);
        //dref.child("ChatListCareTaker").child(ChatID).child("AdID").setValue(AdID);

        SendMsg();
    }

    private void SendMsg() {
        try {
            if (editText.getText() != null && !editText.getText().toString().trim().equals("") || SelectedImg!=null) {
                dref.child("ChatListCareTaker").child(ChatID).child("Read").setValue("false");
                dref.child("ChatListCareTaker").child(ChatID).child("LastMsg").setValue(MineID);
                calendar = Calendar.getInstance();
                dateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.getDefault());
                date = dateFormat.format(calendar.getTime());

                if (ChatID == null || ChatID.equals("")) {
                    CreateChat();
                    return;
                }

                DatabaseReference MsgRef = dref.child("MessagesCareTaker").child(ChatID);

                MsgRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(valueEventListener4 = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String MsgID = "1";
                        if (snapshot.exists()) {
                            for (DataSnapshot s : snapshot.getChildren()) {
                                String oldMsgID = s.getKey();
                                int MsgIDNeedToBEIncrement = Integer.parseInt(Objects.requireNonNull(oldMsgID));
                                MsgID = String.valueOf(MsgIDNeedToBEIncrement + 1);
                            }
                        }
                        SendMsgWithMsgID(MsgID);
                    }

                    private void SendMsgWithMsgID(String msgID) {
                        if (SelectedImg != null) {
                            progressDialog.setMessage("Uploading Image....");
                            progressDialog.show();

                            final String push = FirebaseDatabase.getInstance().getReference().child("Ads").child("Payments").push().getKey();
                            StorageReference fileReference = storageReference.child("Ads/Payments/" + push);
                            fileReference.putFile(SelectedImg)
                                    .addOnSuccessListener(taskSnapshot ->
                                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    MessageAttr msg = new MessageAttr();
                                                    msg.setReceiverId(SellerID);
                                                    msg.setSenderId(MineID);
                                                    msg.setMessage(editText.getText().toString());
                                                    msg.setDate(date);
                                                    msg.setImgURL(task.getResult().toString());
                                                    editText.setText("");

                                                    MsgRef.child(msgID).setValue(msg);

                                                    AttachedImgView.setVisibility(View.GONE);
                                                    imageViewAttachPicture.setImageDrawable(null);
                                                    SelectedImg = null;
                                                } else {
                                                    Toast.makeText(ChatWithCareTakerActivity.this, "Unable to Send Image, try again", Toast.LENGTH_SHORT).show();
                                                }
                                                progressDialog.dismiss();
                                            }));
                        } else {
                            MessageAttr msg = new MessageAttr();
                            msg.setReceiverId(SellerID);
                            msg.setSenderId(MineID);
                            if (editText.getText()!=null)
                                msg.setMessage(editText.getText().toString().trim());
                            msg.setDate(date);
                            editText.setText("");
                            MsgRef.child(msgID).setValue(msg);
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        } catch (Exception ignored) {
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            AttachedImgView.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                SelectedImg = data.getData();
                imageViewAttachPicture.setImageURI(SelectedImg);
            } else {
                if (data.getData() != null) {
                    SelectedImg = data.getData();
                    Glide.with(this).load(SelectedImg).into(imageViewAttachPicture);
                }
            }
        }
    }

    @Override
    protected void onStop() {
        if (valueEventListener1 !=null)
        {
            dref.removeEventListener(valueEventListener1);
        }
        if (valueEventListener2 !=null)
        {
            dref.removeEventListener(valueEventListener2);
        }
        if (valueEventListener3 !=null)
        {
            dref.removeEventListener(valueEventListener3);
        }
        if (valueEventListener4 !=null)
        {
            dref.removeEventListener(valueEventListener4);
        }
        super.onStop();
    }

    @Override
    protected void onPause() {
        if (valueEventListener1 !=null)
        {
            dref.removeEventListener(valueEventListener1);
        }
        if (valueEventListener2 !=null)
        {
            dref.removeEventListener(valueEventListener2);
        }
        if (valueEventListener3 !=null)
        {
            dref.removeEventListener(valueEventListener3);
        }
        if (valueEventListener4 !=null)
        {
            dref.removeEventListener(valueEventListener4);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (valueEventListener1 !=null)
        {
            dref.removeEventListener(valueEventListener1);
        }
        if (valueEventListener2 !=null)
        {
            dref.removeEventListener(valueEventListener2);
        }
        if (valueEventListener3 !=null)
        {
            dref.removeEventListener(valueEventListener3);
        }
        if (valueEventListener4 !=null)
        {
            dref.removeEventListener(valueEventListener4);
        }
        super.onDestroy();
    }
}