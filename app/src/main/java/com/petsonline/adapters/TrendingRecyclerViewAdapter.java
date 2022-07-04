package com.petsonline.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.petsonline.R;
import com.petsonline.activities.AddDetail;
import com.petsonline.activities.OwnAdDetailActivity;
import com.petsonline.models.AdDetail;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TrendingRecyclerViewAdapter extends SliderViewAdapter<TrendingRecyclerViewAdapter.ViewHolder> {
    private final Context context;
    ItemClickListener mClickListener;
    List<AdDetail> AdsList;

    public TrendingRecyclerViewAdapter(Context c, ItemClickListener mClickListener, List<AdDetail> ads) {
        this.mClickListener = mClickListener;
        AdsList = ads;
        context = c;
    }

    public void renewItems(List<AdDetail> sliderItems) {
        this.AdsList = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.AdsList.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(AdDetail sliderItem) {
        this.AdsList.add(sliderItem);
        notifyDataSetChanged();
    }

    @Override
    public TrendingRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        @SuppressLint("InflateParams")
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trending_recyclerview_item,parent,false);
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final AdDetail p1 = AdsList.get(position);

        holder.Price.setText("Price : " + p1.getAd_Price());
        holder.Title.setText("Title : " + p1.getAd_Title());
        if (p1.getAd_Desc()!=null && p1.getAd_Desc().trim().equals(""))
            holder.Description.setText("Description : " + p1.getAd_Desc().trim());
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

        holder.cld.setOnClickListener(view -> context.startActivity(new Intent(context, AddDetail.class).putExtra("Ad", p1)));
    }

    @Override
    public int getCount() {
        if (AdsList == null)
            return 0;
        return AdsList.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends SliderViewAdapter.ViewHolder implements View.OnClickListener {
        ImageView Image;
        TextView Price;
        TextView Title;
        TextView Description;
        TextView Category;
        TextView Address;
        TextView Date;
        View cld;

        ViewHolder(View itemView) {
            super(itemView);
            cld = itemView.findViewById(R.id.AdDetailCard);
            Image = itemView.findViewById(R.id.image);
            Price = itemView.findViewById(R.id.price);
            Title = itemView.findViewById(R.id.title);
            Description = itemView.findViewById(R.id.description);
            Category = itemView.findViewById(R.id.category);
            Address = itemView.findViewById(R.id.address);
            Date = itemView.findViewById(R.id.date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getItemPosition(view));
        }
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

