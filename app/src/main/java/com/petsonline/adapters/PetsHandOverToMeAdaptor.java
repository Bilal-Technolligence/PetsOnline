package com.petsonline.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.petsonline.R;
import com.petsonline.activities.OwnPetDetailActivity;
import com.petsonline.activities.PetDetailActivity;
import com.petsonline.models.MyPet;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class PetsHandOverToMeAdaptor extends RecyclerView.Adapter<PetsHandOverToMeAdaptor.MyHolder> {
    Context ct;
    ArrayList<MyPet> al;
    DatabaseReference databaseReference;
    String CareTaker;

    public PetsHandOverToMeAdaptor(Context cont, ArrayList<MyPet> al, String c) {
        this.ct = cont;
        this.al = al;
        CareTaker = c;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Pets");
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Toast.makeText(ct , "Inside Adapter" , Toast.LENGTH_SHORT).show();
        LayoutInflater li = LayoutInflater.from(ct);
        View v = li.inflate(R.layout.my_pet_handover_recyclerview_item, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(PetsHandOverToMeAdaptor.MyHolder holder, final int position) {
        final MyPet p1 = al.get(position);

        holder.Title.setText("Title : " + p1.getMyPet_Title());
        holder.Description.setText("Description : " + p1.getMyPet_Desc());
        holder.Address.setText("Address : " + p1.getAddress());

        /*holder.selection.setChecked(p1.getSelection());

        holder.selection.setOnCheckedChangeListener((compoundButton, b) -> {
            HashMap<String, Object> map = new HashMap<>();
            map.put("Selection", b);
            if (b)
                map.put("HandoverTo", CareTaker);
            else
                map.put("HandoverTo", "");

            p1.setSelection(b);
            databaseReference.child(p1.getMyPet_ID()).updateChildren(map);
        });*/

        if (p1.getMyPet_Img() != null)
            Picasso.get().load(p1.getMyPet_Img()).into(holder.Image);
        else
            holder.Image.setImageResource(R.drawable.ic_pets);

        holder.cld.setOnClickListener(view -> ct.startActivity(new Intent(ct, PetDetailActivity.class).putExtra("Pet", p1)));

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
        //CheckBox selection;
        CardView cld;

        public MyHolder(View itemView) {
            super(itemView);
            cld = itemView.findViewById(R.id.AdDetailCard);
            Image = itemView.findViewById(R.id.image);
            Title = itemView.findViewById(R.id.title);
            Description = itemView.findViewById(R.id.description);
            Address = itemView.findViewById(R.id.address);
            //selection = itemView.findViewById(R.id.selection);
        }
    }
}