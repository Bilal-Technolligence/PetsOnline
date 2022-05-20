package com.petsonline.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.petsonline.R;
import com.petsonline.adapters.ChatAdapter;
import com.petsonline.models.MessageAttr;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ChatWithoutAdActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference dref = FirebaseDatabase.getInstance().getReference();
    ArrayList<MessageAttr> messageAttrs = new ArrayList<>();
    EditText editText;
    ImageView SendButton, imageProfile;
    TextView textName;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    ChatAdapter chatAdapter;

    boolean ChatExist = false;
    String SellerID;
    String MineID;
    String ChatID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_without_ad);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        textName = (TextView) findViewById(R.id.name);
        imageProfile = (ImageView) findViewById(R.id.imgProfile);

        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        linearLayout.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayout);

        SellerID = getIntent().getStringExtra("chaterId");
        MineID = user.getUid();

        editText = (EditText) findViewById(R.id.message);
        SendButton = (ImageView) findViewById(R.id.imgSend);

        SendButton.setOnClickListener(v -> SendMsg());

        LoadSellerProfile();
        CheckChat();
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
                            if (ds.child("ReceiverId").getValue().equals(SellerID) && ds.child("SenderId").getValue().equals(MineID)) {
                                ChatExist = true;
                                ChatID = ds.getKey();
                                break;
                            }
                            if (ds.child("ReceiverId").getValue().equals(MineID) && ds.child("SenderId").getValue().equals(SellerID)) {
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
                    chatAdapter = new ChatAdapter(messageAttrs, ChatWithoutAdActivity.this, ChatWithoutAdActivity.this);
                    recyclerView.setAdapter(chatAdapter);
                } catch (Exception e) {
                    Log.e("Error ",e.getMessage());
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
    }

    private void CreateChat() {
        ChatID = dref.child("ChatList").push().getKey();

        dref.child("ChatList").child(ChatID).child("ReceiverId").setValue(SellerID);
        dref.child("ChatList").child(ChatID).child("SenderId").setValue(MineID);
        dref.child("ChatList").child(ChatID).child("SenderId").setValue(MineID);

        SendMsg();
    }

    private void SendMsg() {
        try {
            if (editText.getText() != null && !editText.getText().toString().trim().equals("")) {
                calendar = Calendar.getInstance();
                dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
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
                        MessageAttr msg = new MessageAttr();
                        msg.setReceiverId(SellerID);
                        msg.setSenderId(MineID);
                        msg.setMessage(editText.getText().toString());
                        msg.setDate(date);
                        editText.setText("");
                        MsgRef.child(msgID).setValue(msg);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        } catch (Exception ignored) {
        }
    }
}