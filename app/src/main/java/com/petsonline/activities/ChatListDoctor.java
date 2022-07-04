package com.petsonline.activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.petsonline.R;
import com.petsonline.adapters.ChatListDoctorAdapter;
import com.petsonline.models.Doctor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ChatListDoctor extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference dref = FirebaseDatabase.getInstance().getReference();
    ArrayList<String> chaterId=new ArrayList<>();
    ArrayList<Doctor> userAttrs=new ArrayList<>();
    String userId;
    View NoRecordFoundView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list_doctor);

        try {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Chat List");
        }catch (Exception ignored){}

        NoRecordFoundView = findViewById(R.id.noRcdFnd);
        NoRecordFoundView.setVisibility(View.GONE);
        recyclerView= findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        try {
            userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            dref.child("ChatListDoctor").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try{
                        chaterId.clear();
                        userAttrs.clear();
                        for(DataSnapshot ds: dataSnapshot.getChildren()) {
                            if (ds.exists()) {
                                if (Objects.equals(ds.child("ReceiverId").getValue(), userId))
                                {
                                    chaterId.add(Objects.requireNonNull(ds.child("SenderId").getValue()).toString());
                                }
                                if(Objects.equals(ds.child("SenderId").getValue(), userId)) {
                                    chaterId.add(Objects.requireNonNull(ds.child("ReceiverId").getValue()).toString());
                                }
                            }
                        }
                        if(chaterId.size() == 0)
                        {
                            NoRecordFoundView.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                        else
                            showChatList();
                    }catch (Exception ignored){

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        catch (Exception ignored){
        }
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
                        String DOB = "";
                        String Age = "";
                        String Education = "";
                        String Specialization = "";

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
                        if (dataSnapshot.child("DOB").exists())
                            DOB = dataSnapshot.child("DOB") .toString();
                        if (dataSnapshot.child("age").exists())
                            Age = dataSnapshot.child("age") .toString();
                        if (dataSnapshot.child("education").exists())
                            Education = dataSnapshot.child("education") .toString();
                        if (dataSnapshot.child("specialization").exists())
                            Specialization = dataSnapshot.child("specialization") .toString();

                        Doctor d = new Doctor(Id,Name,Contact,City,DOB,Age,Imageurl,Language,"" ,Email,Education,Specialization);
                        userAttrs.add(d);
                    }
                    recyclerView.setAdapter(new ChatListDoctorAdapter(userAttrs , getApplicationContext() , ChatListDoctor.this,userId));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}