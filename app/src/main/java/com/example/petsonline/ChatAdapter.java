package com.example.petsonline;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<com.example.petsonline.ChatAdapter.ViewHolder> {
    ArrayList<MessageAttr> messageAttrs=new ArrayList<>();
    Context context;
    Activity activity;
    String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
    public ChatAdapter(ArrayList<MessageAttr> messageAttrs, Context context, Activity activity) {
        this.activity=activity;
        this.context=context;
        this.messageAttrs=messageAttrs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_items, parent, false);
        return new com.example.petsonline.ChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try{
            String newDate=messageAttrs.get(position).getDate();
            if(position!=0) {
                if (!newDate.equals(messageAttrs.get(position-1).getDate())){
                    holder.date.setText(newDate);
                }
                else{
                    holder.date.setText("");
                }
            }else{
                holder.date.setText(newDate);
            }
            if (!messageAttrs.get(position).getMessage().equals(""))
                if(userId.equals(messageAttrs.get(position).getSenderId())) {
                    holder.senderMessage.setVisibility(View.VISIBLE);
                    holder.receiverMessage.setVisibility(View.GONE);
                    holder.senderMessage.setBackground(ResourcesCompat.getDrawable(context.getResources(),R.drawable.background_right,context.getTheme()));
                    holder.senderMessage.setText(messageAttrs.get(position).getMessage());
                }else {
                    holder.senderMessage.setVisibility(View.GONE);
                    holder.receiverMessage.setVisibility(View.VISIBLE);
                    holder.receiverMessage.setBackground(ResourcesCompat.getDrawable(context.getResources(),R.drawable.button_round,context.getTheme()));
                    holder.receiverMessage.setText(messageAttrs.get(position).getMessage());
                }
        }catch(Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return messageAttrs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date,receiverMessage,senderMessage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date=(TextView) itemView.findViewById(R.id.date);
            senderMessage=(TextView) itemView.findViewById(R.id.senderMessage);
            receiverMessage=(TextView) itemView.findViewById(R.id.receiverMessage);
        }
    }
}
