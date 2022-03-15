package com.petsonline;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.petsonline.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
    ArrayList<UserAttr> userAttrs = new ArrayList<>();
    Context context;
    Activity activity;
    DatabaseReference dref = FirebaseDatabase.getInstance().getReference();
    String da=" ";
    String ms = " ";

    public ChatListAdapter(ArrayList<UserAttr> userAttrs, Context context, Activity activity) {
        this.context = context;
        this.userAttrs = userAttrs;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_items, parent, false);
        return new ChatListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(userAttrs.get(position).getName());
        Picasso.get().load(userAttrs.get(position).getImageUrl()).into(holder.profile);
        final String id = userAttrs.get(position).getId();
        dref.child("ChatList").orderByChild("receiverId").equalTo(id).limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        if (ds.exists()) {
                            da = ds.child("date").getValue().toString();
                            ms = ds.child("message").getValue().toString();
                        }
                        holder.date.setText(da);
                        holder.msg.setText(ms);
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dref.child("ChatList").orderByChild("senderId").equalTo(id).limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        if (ds.exists()) {
                            da = ds.child("date").getValue().toString();
                            ms = ds.child("message").getValue().toString();
                        }
                    }
                    holder.date.setText(da);
                    holder.msg.setText(ms);
                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, Chat.class);
                i.putExtra("chaterId", id);
                activity.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return userAttrs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView name, date, msg;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);
            profile = (ImageView) itemView.findViewById(R.id.imgProfile);
            name = (TextView) itemView.findViewById(R.id.name);
            date = (TextView) itemView.findViewById(R.id.date);
            msg = (TextView) itemView.findViewById(R.id.msg);
        }
    }
}
