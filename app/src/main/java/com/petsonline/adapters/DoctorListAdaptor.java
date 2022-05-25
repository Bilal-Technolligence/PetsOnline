package com.petsonline.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.petsonline.R;
import com.petsonline.models.CareTaker;
import com.petsonline.models.Doctor;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DoctorListAdaptor extends RecyclerView.Adapter<DoctorListAdaptor.MyHolder> {
    Context ct;
    ArrayList<Doctor> al;

    public DoctorListAdaptor(Context cont, ArrayList<Doctor> al) {
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
    public void onBindViewHolder(DoctorListAdaptor.MyHolder holder, final int position) {
        final Doctor p1 = al.get(position);

        if (p1.getNAME()!=null && !p1.getNAME().equals(""))
            holder.name.setText("Name : " + p1.getNAME());
        else
            holder.name.setVisibility(View.GONE);

        if (p1.getADDRESS()!=null && !p1.getADDRESS().equals(""))
            holder.address.setText("Address : " + p1.getADDRESS());
        else
            holder.address.setVisibility(View.GONE);

        if (p1.getIMAGEURL() != null && !p1.getIMAGEURL().equals(""))
            Picasso.get().load(p1.getIMAGEURL()).into(holder.image);
        else
            holder.image.setImageResource(R.drawable.profile);

        //holder.cld.setOnClickListener(view -> ct.startActivity(new Intent(ct, AddDetail.class).putExtra("Ad", p1)));
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