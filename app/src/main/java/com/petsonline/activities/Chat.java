package com.petsonline.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.petsonline.models.profiledata;
import com.petsonline.util.BaseUtil;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Chat extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference dref = FirebaseDatabase.getInstance().getReference();
    private ArrayList<MessageAttr> messageAttrs = new ArrayList<>();
    private EditText editText;
    private ImageView SendButton, imageProfile;
    private TextView textName;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    private Button btnBuyProduct;
    private ChatAdapter chatAdapter;
    boolean ChatExist = false;
    private String SellerID;
    private String MineID;
    private String AdID;
    private String ChatID = "";

    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    private ImageView imgAttachBtn;
    private ImageView imgClearSelectedImage;
    private ImageView imageViewAttachPicture;
    private Uri SelectedImg;
    private View AttachedImgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        btnBuyProduct = findViewById(R.id.buyProduct);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        textName = (TextView) findViewById(R.id.name);
        imageProfile = (ImageView) findViewById(R.id.imgProfile);

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

        AdID = getIntent().getStringExtra("Ad_ID");

        editText = (EditText) findViewById(R.id.message);
        SendButton = (ImageView) findViewById(R.id.imgSend);

        SendButton.setOnClickListener(v -> SendMsg());

        LoadSellerProfile();
        CheckChat();

        btnBuyProduct.setOnClickListener(view -> FirebaseDatabase.getInstance().getReference().child("Ads").child(AdID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String TotalQuantity = "0";
                String SoldQuantity = "0";

                if (snapshot.exists()) {
                    if (snapshot.child("Quantity").exists()) {
                        TotalQuantity = snapshot.child("Quantity").getValue(String.class);
                    }
                    if (snapshot.child("Sold").exists()) {
                        SoldQuantity = snapshot.child("Sold").getValue(String.class);
                    }

                    int total = 0;
                    int sold = 0;
                    if (TotalQuantity != null)
                        total = Integer.parseInt(TotalQuantity);
                    if (SoldQuantity != null)
                        sold = Integer.parseInt(SoldQuantity);

                    int remaining = total - sold;

                    if (remaining <= 0) {
                        Toast.makeText(Chat.this, "Sorry, no item available for sale", Toast.LENGTH_SHORT).show();
                    } else {
                        FirebaseDatabase.getInstance().getReference().child("Ads").child(AdID).child("Sold").setValue((sold + 1) + "").addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                OrderHasBeenPlaced();
                                Toast.makeText(Chat.this, "Congrats, your order has been received. Contact with Seller for more Details", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Chat.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(Chat.this, "Unable to get Ad Detail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }));
    }

    private void LoadSellerProfile() {
        dref.child("Employee_Profile").child(SellerID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    try {
                        String name = dataSnapshot.child("name").getValue().toString();
                        String pic = dataSnapshot.child("imageurl").getValue().toString();
                        textName.setText(name);
                        Picasso.get().load(pic).into(imageProfile);
                    } catch (Exception ignored) {
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void CheckChat() {
        dref.child("ChatList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.exists()) {
                            if (ds.child("ReceiverId").getValue().equals(SellerID) && ds.child("SenderId").getValue().equals(MineID) && ds.child("AdID").getValue().equals(AdID)) {
                                ChatExist = true;
                                ChatID = ds.getKey();
                                break;
                            }
                            if (ds.child("ReceiverId").getValue().equals(MineID) && ds.child("SenderId").getValue().equals(SellerID) && ds.child("AdID").getValue().equals(AdID)) {
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
        dref.child("Messages").child(chat_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageAttrs.clear();
                try {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            if (ds.child("receiverId").getValue().equals(SellerID) && ds.child("senderId").getValue().equals(MineID)) {
                                MessageAttr p = ds.getValue(MessageAttr.class);
                                messageAttrs.add(p);
                            }
                            if (ds.child("receiverId").getValue().equals(MineID) && ds.child("senderId").getValue().equals(SellerID)) {
                                MessageAttr p = ds.getValue(MessageAttr.class);
                                messageAttrs.add(p);
                            }
                        }
                    }
                    chatAdapter = new ChatAdapter(messageAttrs, Chat.this, Chat.this);
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
        ChatID = dref.child("ChatList").push().getKey();

        dref.child("ChatList").child(ChatID).child("ReceiverId").setValue(SellerID);
        dref.child("ChatList").child(ChatID).child("SenderId").setValue(MineID);
        dref.child("ChatList").child(ChatID).child("SenderId").setValue(MineID);
        dref.child("ChatList").child(ChatID).child("AdID").setValue(AdID);
    }

    private void CreateChat() {
        ChatID = dref.child("ChatList").push().getKey();

        dref.child("ChatList").child(ChatID).child("ReceiverId").setValue(SellerID);
        dref.child("ChatList").child(ChatID).child("SenderId").setValue(MineID);
        dref.child("ChatList").child(ChatID).child("SenderId").setValue(MineID);
        dref.child("ChatList").child(ChatID).child("AdID").setValue(AdID);

        SendMsg();
    }

    private void SendMsg() {
        try {
            if (editText.getText() != null && !editText.getText().toString().trim().equals("")) {
                calendar = Calendar.getInstance();
                dateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.getDefault());
                date = dateFormat.format(calendar.getTime());

                if (ChatID == null || ChatID.equals("")) {
                    CreateChat();
                    return;
                }

                DatabaseReference MsgRef = dref.child("Messages").child(ChatID);

                MsgRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String MsgID = "1";
                        if (snapshot.exists()) {
                            for (DataSnapshot s : snapshot.getChildren()) {
                                String oldMsgID = s.getKey();
                                int MsgIDNeedToBEIncrement = Integer.parseInt(oldMsgID);
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
                                                    Toast.makeText(Chat.this, "Unable to Send Image, try again", Toast.LENGTH_SHORT).show();
                                                }
                                                progressDialog.dismiss();
                                            }));
                        } else {
                            MessageAttr msg = new MessageAttr();
                            msg.setReceiverId(SellerID);
                            msg.setSenderId(MineID);
                            msg.setMessage(editText.getText().toString());
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

    private void CreateWithOrderChat() {
        ChatID = dref.child("ChatList").push().getKey();

        dref.child("ChatList").child(ChatID).child("ReceiverId").setValue(SellerID);
        dref.child("ChatList").child(ChatID).child("SenderId").setValue(MineID);
        dref.child("ChatList").child(ChatID).child("SenderId").setValue(MineID);
        dref.child("ChatList").child(ChatID).child("AdID").setValue(AdID);

        OrderHasBeenPlaced();
    }

    private void OrderHasBeenPlaced() {
        try {
            calendar = Calendar.getInstance();
            dateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.getDefault());
            date = dateFormat.format(calendar.getTime());

            if (ChatID == null || ChatID.equals("")) {
                CreateWithOrderChat();
                return;
            }

            DatabaseReference MsgRef = dref.child("Messages").child(ChatID);

            MsgRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String MsgID = "1";
                    if (snapshot.exists()) {
                        for (DataSnapshot s : snapshot.getChildren()) {
                            String oldMsgID = s.getKey();
                            int MsgIDNeedToBEIncrement = Integer.parseInt(oldMsgID);
                            MsgID = String.valueOf(MsgIDNeedToBEIncrement + 1);
                        }
                    }
                    SendMsgWithMsgID(MsgID);
                }

                private void SendMsgWithMsgID(String msgID) {
                    MessageAttr msg = new MessageAttr();
                    msg.setReceiverId(SellerID);
                    msg.setSenderId(MineID);
                    msg.setMessage("Order has been Placed On ".concat(date));
                    msg.setDate(date);
                    MsgRef.child(msgID).setValue(msg);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
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
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), SelectedImg);
                        imageViewAttachPicture.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}