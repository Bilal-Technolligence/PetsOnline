package com.petsonline.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.petsonline.R;
import com.petsonline.models.MessageAttr;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    ArrayList<MessageAttr> messageAttrs;
    Context context;
    Activity activity;
    String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    private final SimpleDateFormat FormatInHourAndDate = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.getDefault());
    private final SimpleDateFormat FormatInDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    public ChatAdapter(ArrayList<MessageAttr> messageAttrs, Context context, Activity activity) {
        this.activity = activity;
        this.context = context;
        this.messageAttrs = messageAttrs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            String newDate = "";
            String DateOldFromMsg;
            Date nd2;
            String oldDate = "";

            //New Date
            String DateCurrentFromMsg = messageAttrs.get(position).getDate();
            Date nd1 = FormatInHourAndDate.parse(DateCurrentFromMsg);
            if (nd1 != null)
                newDate = FormatInDate.format(nd1);

            if (position != 0) {
                DateOldFromMsg = messageAttrs.get(position - 1).getDate();
                nd2 = FormatInHourAndDate.parse(DateOldFromMsg);
                if (nd2 != null)
                    oldDate = FormatInDate.format(nd2);

                if (!newDate.equals(oldDate)) {
                    holder.date.setText(newDate);
                } else {
                    holder.date.setText("");
                    holder.date.setVisibility(View.GONE);
                }
            } else {
                holder.date.setText(newDate);
            }
            if (!messageAttrs.get(position).getMessage().equals(""))
                if (userId.equals(messageAttrs.get(position).getSenderId())) {
                    if (messageAttrs.get(position).getImgURL() != null && !Objects.equals(messageAttrs.get(position).getImgURL(), "")) {
                        holder.chatImgSender.setVisibility(View.VISIBLE);
                        Picasso.get().load(messageAttrs.get(position).getImgURL()).into(holder.chatImgSender);
                    }
                    holder.chatImgReceiver.setVisibility(View.GONE);
                    holder.senderMessage.setVisibility(View.VISIBLE);
                    holder.receiverMessage.setVisibility(View.GONE);
                    holder.senderMessage.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.background_right, context.getTheme()));
                    holder.senderMessage.setText(messageAttrs.get(position).getMessage());
                } else {
                    if (messageAttrs.get(position).getImgURL() != null && !Objects.equals(messageAttrs.get(position).getImgURL(), "")) {
                        holder.chatImgReceiver.setVisibility(View.VISIBLE);
                        Picasso.get().load(messageAttrs.get(position).getImgURL()).into(holder.chatImgReceiver);
                    }
                    holder.chatImgSender.setVisibility(View.GONE);
                    holder.senderMessage.setVisibility(View.GONE);
                    holder.receiverMessage.setVisibility(View.VISIBLE);
                    holder.receiverMessage.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.button_round, context.getTheme()));
                    holder.receiverMessage.setText(messageAttrs.get(position).getMessage());
                }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageAttrs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, receiverMessage, senderMessage;
        ImageView chatImgReceiver, chatImgSender;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
            senderMessage = (TextView) itemView.findViewById(R.id.senderMessage);
            receiverMessage = (TextView) itemView.findViewById(R.id.receiverMessage);
            chatImgReceiver = itemView.findViewById(R.id.chatImgReceiver);
            chatImgSender = itemView.findViewById(R.id.chatImgSender);
        }
    }
}
