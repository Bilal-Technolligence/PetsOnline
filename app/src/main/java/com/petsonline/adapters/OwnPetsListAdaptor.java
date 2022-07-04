package com.petsonline.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.petsonline.R;
import com.petsonline.activities.OwnAdDetailActivity;
import com.petsonline.activities.OwnPetDetailActivity;
import com.petsonline.models.AdDetail;
import com.petsonline.models.MyPet;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OwnPetsListAdaptor extends RecyclerView.Adapter<OwnPetsListAdaptor.MyHolder> {
    Context ct;
    ArrayList<MyPet> al;

    public OwnPetsListAdaptor(Context cont, ArrayList<MyPet> al) {
        this.ct = cont;
        this.al = al;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Toast.makeText(ct , "Inside Adapter" , Toast.LENGTH_SHORT).show();
        LayoutInflater li = LayoutInflater.from(ct);
        View v = li.inflate(R.layout.my_pet_recyclerview_item, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(OwnPetsListAdaptor.MyHolder holder, final int position) {
        final MyPet p1 = al.get(position);

        holder.Title.setText("Title : " + p1.getMyPet_Title());
        holder.Description.setText("Description : " + p1.getMyPet_Desc());
        holder.Address.setText("Address : " + p1.getAddress());


        if (p1.getMyPet_Img() != null)
            Picasso.get().load(p1.getMyPet_Img()).into(holder.Image);
        else
            holder.Image.setImageResource(R.drawable.ic_pets);

        holder.cld.setOnClickListener(view -> ct.startActivity(new Intent(ct, OwnPetDetailActivity.class).putExtra("Pet", p1)));

    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        ImageView Image;
        TextView Title;
        TextView Description;
        TextView Address;
        CardView cld;

        public MyHolder(View itemView) {
            super(itemView);
            cld = itemView.findViewById(R.id.AdDetailCard);
            Image = itemView.findViewById(R.id.image);
            Title = itemView.findViewById(R.id.title);
            Description = itemView.findViewById(R.id.description);
            Address = itemView.findViewById(R.id.address);
        }
    }
}