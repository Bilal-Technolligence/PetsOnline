package com.petsonline.adapters;

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
import com.petsonline.activities.CareTakerDetailActivity;
import com.petsonline.models.CareTaker;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CareTakerListAdaptor extends RecyclerView.Adapter<CareTakerListAdaptor.MyHolder> {
    Context ct;
    ArrayList<CareTaker> al;

    public CareTakerListAdaptor(Context cont, ArrayList<CareTaker> al) {
        this.ct = cont;
        this.al = al;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Toast.makeText(ct , "Inside Adapter" , Toast.LENGTH_SHORT).show();
        LayoutInflater li = LayoutInflater.from(ct);
        View v = li.inflate(R.layout.care_taker_recyclerview_item, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(CareTakerListAdaptor.MyHolder holder, final int position) {
        final CareTaker p1 = al.get(position);

        if (p1.getNAME()!=null && !p1.getNAME().equals(""))
            holder.name.setText("Name : " + p1.getNAME());
        else
            holder.name.setVisibility(View.GONE);

        if (p1.getADDRESS()!=null && !p1.getADDRESS().equals(""))
            holder.address.setText("Address : " + p1.getADDRESS());
        else
            holder.address.setVisibility(View.GONE);

        if (p1.getFEEPERDAY()!=null && !p1.getFEEPERDAY().equals(""))
            holder.chargesperday.setText("Charges Per day : " + p1.getFEEPERDAY());
        else
            holder.chargesperday.setVisibility(View.GONE);

        if (p1.getFEEPERHOUR()!=null && !p1.getFEEPERHOUR().equals(""))
            holder.chargesperhour.setText("Charges Per hour : " + p1.getFEEPERHOUR());
        else
            holder.chargesperhour.setVisibility(View.GONE);

        if (p1.getSTARTINGTIME()!=null && !p1.getSTARTINGTIME().equals("") && p1.getENDINGTIME()!=null && !p1.getENDINGTIME().equals(""))
            holder.opening_hours.setText( p1.getSTARTINGTIME().concat(" - ").concat(p1.getENDINGTIME()));
        else
            holder.opening_hours.setVisibility(View.GONE);

        if (p1.getIMAGEURL() != null && !p1.getIMAGEURL().equals(""))
            Picasso.get().load(p1.getIMAGEURL()).into(holder.image);
        else
            holder.image.setImageResource(R.drawable.profile);

        holder.cld.setOnClickListener(view -> {
            ct.startActivity(new Intent(ct, CareTakerDetailActivity.class).putExtra("Caretaker", p1));
        });
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView opening_hours;
        TextView chargesperday;
        TextView chargesperhour;
        TextView address;
        CardView cld;

        public MyHolder(View itemView) {
            super(itemView);
            cld = itemView.findViewById(R.id.CareTakerDetailCard);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.mName);
            opening_hours = itemView.findViewById(R.id.opening_hours);
            chargesperday = itemView.findViewById(R.id.chargesperday);
            chargesperhour = itemView.findViewById(R.id.chargesperhour);
            address = itemView.findViewById(R.id.address);
        }
    }
}