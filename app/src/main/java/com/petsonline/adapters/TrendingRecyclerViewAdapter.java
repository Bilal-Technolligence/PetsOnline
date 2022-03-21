package com.petsonline.adapters;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.petsonline.R;
import com.petsonline.models.AdDetail;
import java.util.List;

public class TrendingRecyclerViewAdapter extends RecyclerView.Adapter<TrendingRecyclerViewAdapter.ViewHolder>{
    ItemClickListener mClickListener;
    List<AdDetail> AdsList;

    public TrendingRecyclerViewAdapter(ItemClickListener mClickListener, List<AdDetail> ads) {
        this.mClickListener = mClickListener;
        AdsList = ads;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return AdsList.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View myView;

        ViewHolder(View itemView) {
            super(itemView);
            myView = itemView.findViewById(R.id.AdDetailCard);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
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

