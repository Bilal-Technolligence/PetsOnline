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
import com.petsonline.activities.AddDetail;
import com.petsonline.activities.OwnAdDetailActivity;
import com.petsonline.models.AdDetail;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class OwnAdsListAdaptor extends RecyclerView.Adapter<OwnAdsListAdaptor.MyHolder> {
    Context ct;
    ArrayList<AdDetail> al;

    public OwnAdsListAdaptor(Context cont, ArrayList<AdDetail> al) {
        this.ct = cont;
        this.al = al;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Toast.makeText(ct , "Inside Adapter" , Toast.LENGTH_SHORT).show();
        LayoutInflater li = LayoutInflater.from(ct);
        View v = li.inflate(R.layout.trending_recyclerview_item, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(OwnAdsListAdaptor.MyHolder holder, final int position) {
        final AdDetail p1 = al.get(position);

        holder.Price.setText("Price : " + p1.getAd_Price());
        holder.Title.setText("Title : " + p1.getAd_Title());
        holder.Description.setText("Description : " + p1.getAd_Desc());
        holder.Category.setText("Category : " + p1.getAd_Category_FID());
        holder.Address.setText("Address : " + p1.getAd_Address());

        DateFormat originalFormat = new SimpleDateFormat("ddMMyyyy HHmmss", Locale.getDefault());
        @SuppressLint("SimpleDateFormat")
        DateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        Date date;
        try {
            date = originalFormat.parse(p1.getDate());
            assert date != null;
            String formattedDate = targetFormat.format(date);
            holder.Date.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (p1.getAd_Img() != null)
            Picasso.get().load(p1.getAd_Img()).into(holder.Image);
        else
            holder.Image.setImageResource(R.drawable.profile);

        holder.cld.setOnClickListener(view -> ct.startActivity(new Intent(ct, OwnAdDetailActivity.class).putExtra("Ad", p1)));

    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        ImageView Image;
        TextView Price;
        TextView Title;
        TextView Description;
        TextView Category;
        TextView Address;
        TextView Date;
        CardView cld;

        public MyHolder(View itemView) {
            super(itemView);
            cld = itemView.findViewById(R.id.AdDetailCard);
            Image = itemView.findViewById(R.id.image);
            Price = itemView.findViewById(R.id.price);
            Title = itemView.findViewById(R.id.title);
            Description = itemView.findViewById(R.id.description);
            Category = itemView.findViewById(R.id.category);
            Address = itemView.findViewById(R.id.address);
            Date = itemView.findViewById(R.id.date);
        }
    }
}