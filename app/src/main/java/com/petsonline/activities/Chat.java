    package com.petsonline.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.petsonline.adapters.ChatAdapter;
import com.petsonline.models.MessageAttr;
import com.petsonline.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Chat extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference dref = FirebaseDatabase.getInstance().getReference();
   // String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();

    ArrayList<MessageAttr> messageAttrs=new ArrayList<>();
    EditText editText;
    ImageView imageView,imageProfile;
    TextView textName;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recyclerView=(RecyclerView) findViewById(R.id.recyclerView);
        textName=(TextView) findViewById(R.id.name);
        imageProfile=(ImageView) findViewById(R.id.imgProfile);
        Context context;
        LinearLayoutManager linearLayout=new LinearLayoutManager(this);
        linearLayout.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayout);
        final String chaterId=getIntent().getStringExtra("chaterId");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser() ;
        String userId = user.getUid();

        editText=(EditText) findViewById(R.id.message);
        imageView=(ImageView) findViewById(R.id.imgSend);
        dref.child("Employee_Profile").child(chaterId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    try {
                        String name = dataSnapshot.child("name").getValue().toString();
                        String pic = dataSnapshot.child("imageurl").getValue().toString();
                        textName.setText(name);
                        Picasso.get().load(pic).into(imageProfile);
                    }
                    catch (Exception e){}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        dref.child("ChatList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageAttrs.clear();
                try{
                    for(DataSnapshot ds: dataSnapshot.getChildren()) {
                        if (ds.exists()) {
                            if (ds.child("receiverId").getValue().equals(chaterId) && ds.child("senderId").getValue().equals(userId)) {
                                MessageAttr p = ds.getValue(MessageAttr.class);
                                messageAttrs.add(p);
                            }
                            if (ds.child("receiverId").getValue().equals(userId) && ds.child("senderId").getValue().equals(chaterId)) {
                                MessageAttr p = ds.getValue(MessageAttr.class);
                                messageAttrs.add(p);
                            }
                        }
                    }
                    chatAdapter=new ChatAdapter(messageAttrs , Chat.this , Chat.this);
                    recyclerView.setAdapter(chatAdapter);

                }catch(Exception e){

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(editText.getText() != null) {

                        // Chatid
                        //         userid1
                        //         userid2
                        //         messages
                        //                  msgid
                        //                      msgtxt
                        //                      date
                        //                      sentby
                        //                      receivedby
                        //
                        //                  msg2
                        //                      msgtxt
                        //                      date
                        //                      sentby
                        //                      receivedby

                        calendar = Calendar.getInstance();
                        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        date = dateFormat.format(calendar.getTime());
                        String push = dref.child("ChatList").push().getKey();
                        dref.child("ChatList").child(push).child("id").setValue(push);
                        dref.child("ChatList").child(push).child("receiverId").setValue(chaterId);
                        dref.child("ChatList").child(push).child("senderId").setValue(userId);
                        dref.child("ChatList").child(push).child("date").setValue(date);
                        dref.child("ChatList").child(push).child("message").setValue(editText.getText().toString());
                        final String uper =editText.getText().toString().toUpperCase();
                        editText.setText("");

                    }
                }catch(Exception e){

                }

            }
        });
    }

}