package com.petsonline.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.petsonline.R;
import com.petsonline.activities.ChatWithDoctorActivity;
import com.petsonline.models.Doctor;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Objects;

public class ChatListDoctorAdapter extends RecyclerView.Adapter<ChatListDoctorAdapter.ViewHolder> {
    ArrayList<Doctor> userAttrs;
    Context context;
    Activity activity;
    DatabaseReference dref = FirebaseDatabase.getInstance().getReference();
    String da = "";
    String ms = "";
    String MyID;

    public ChatListDoctorAdapter(ArrayList<Doctor> userAttrs, Context context, Activity activity, String ownID) {
        this.context = context;
        this.userAttrs = userAttrs;
        this.activity = activity;
        MyID = ownID ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_items, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(userAttrs.get(position).getNAME());
        if (userAttrs.get(position).getIMAGEURL() != null && !userAttrs.get(position).getIMAGEURL().equals(""))
            Picasso.get().load(userAttrs.get(position).getIMAGEURL()).into(holder.profile);
        else
            holder.profile.setImageResource(R.drawable.profile);

        final String id = userAttrs.get(position).getID();

        dref.child("ChatListDoctor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    if (snapshot.exists()) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            if (ds.exists()) {
                                String ReceiverId = Objects.requireNonNull(ds.child("ReceiverId").getValue()).toString();
                                String SenderId = Objects.requireNonNull(ds.child("SenderId").getValue()).toString();

                                if (ReceiverId.equals(MyID) && SenderId.equals(id)
                                    ||
                                        ReceiverId.equals(id) && SenderId.equals(MyID) )
                                {
                                    dref.child("MessagesDoctor").child(Objects.requireNonNull(ds.getKey())).orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            try {
                                                if (dataSnapshot.exists()) {

                                                    for (DataSnapshot s:dataSnapshot.getChildren()) {
                                                        da = Objects.requireNonNull(s.child("date").getValue()).toString();
                                                        ms = Objects.requireNonNull(s.child("message").getValue()).toString();
                                                        holder.date.setText(da);
                                                        holder.msg.setText(ms);
                                                    }
                                                }
                                            } catch (Exception e) {
                                                Log.e("Error ", e.getMessage());
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    break;
                                }
                            }
                        }
                    }
                } catch (Exception ignored) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.layout.setOnClickListener(v -> {

            Intent i = new Intent(context, ChatWithDoctorActivity.class);
            i.putExtra("chaterId", id);
            activity.startActivity(i);

        });

    }

    @Override
    public int getItemCount() {
        return userAttrs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView name, date, msg;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            profile = itemView.findViewById(R.id.imgProfile);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            msg = itemView.findViewById(R.id.msg);
        }
    }
}
