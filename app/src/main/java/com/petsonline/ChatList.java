package com.petsonline;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.petsonline.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ChatList extends BaseActivity {
    RecyclerView recyclerView;
    DatabaseReference dref = FirebaseDatabase.getInstance().getReference();

    ArrayList<String> chaterId=new ArrayList<>();
    ArrayList<UserAttr> userAttrs=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_chat_list);
        getSupportActionBar().setTitle("Chat List");
        recyclerView=(RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Toast.makeText(getApplicationContext() , "Msg" , Toast.LENGTH_LONG).show();
        try {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            dref.child("ChatList").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try{
                        chaterId.clear();
                        userAttrs.clear();
                        for(DataSnapshot ds: dataSnapshot.getChildren()) {
                            if (ds.exists()) {
                                if (ds.child("receiverId").getValue().equals(userId))
                                {
                                    chaterId.add(ds.child("senderId").getValue().toString());
                                }
                                if(ds.child("senderId").getValue().equals(userId)) {
                                    chaterId.add(ds.child("receiverId").getValue().toString());
                                }
                            }
                        }
                        showChatList();
                    }catch (Exception e){

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        catch (Exception e){
        }
    }


    @Override
    protected int getContentViewId() {
        return R.layout.activity_chat_list;
    }

    private void showChatList() {
        Set<String> set = new HashSet<>(chaterId);
        chaterId.clear();
        chaterId.addAll(set);

        for(int i=0;i<chaterId.size();i++) {
            dref.child("Employee_Profile").child(chaterId.get(i)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()){
                        String Id = "";
                        String Name= "";
                        String Email= "";
                        String Contact= "";
                        String Imageurl= "";
                        String City= "";
                        String Language= "";

                        if (dataSnapshot.child("id").exists())
                            Id = Objects.requireNonNull(dataSnapshot.child("id").getValue()).toString();
                        if (dataSnapshot.child("name").exists())
                            Name = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                        if (dataSnapshot.child("email").exists())
                            Email = Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString();
                        if (dataSnapshot.child("contact").exists())
                            Contact = Objects.requireNonNull(dataSnapshot.child("contact").getValue()).toString();
                        if (dataSnapshot.child("imageurl").exists())
                            Imageurl = Objects.requireNonNull(dataSnapshot.child("imageurl").getValue()).toString();
                        if (dataSnapshot.child("city").exists())
                            City = Objects.requireNonNull(dataSnapshot.child("city").getValue()).toString();
                        if (dataSnapshot.child("language").exists())
                            Language = dataSnapshot.child("language") .toString();

                        UserAttr p = new UserAttr(Id,Name,Email,Contact,Imageurl,City,Language);
                        userAttrs.add(p);
                    }
                    recyclerView.setAdapter(new ChatListAdapter(userAttrs , getApplicationContext() , ChatList.this));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
    }

}