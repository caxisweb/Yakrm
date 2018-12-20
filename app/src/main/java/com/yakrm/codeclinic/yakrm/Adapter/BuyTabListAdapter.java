package com.yakrm.codeclinic.yakrm.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yakrm.codeclinic.yakrm.Activities.GiftDetailsActivity;
import com.yakrm.codeclinic.yakrm.R;

import java.util.ArrayList;

public class BuyTabListAdapter extends RecyclerView.Adapter<BuyTabListAdapter.CustomViewHolder> {
    ArrayList<String> arrayList;
    Context context;

    public BuyTabListAdapter(ArrayList<String> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public BuyTabListAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_list_item_view, null);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyTabListAdapter.CustomViewHolder customViewHolder, int i) {
        customViewHolder.tv_item_name.setText(arrayList.get(i));

        if (i == 1) {
            customViewHolder.main_images.setImageResource(R.drawable.demo_img_2);
        } else if (i == 2) {
            customViewHolder.main_images.setImageResource(R.drawable.demo_img_1);
        } else if (i == 3) {
            customViewHolder.main_images.setImageResource(R.drawable.demo_img_2);
        } else if (i == 4) {
            customViewHolder.main_images.setImageResource(R.drawable.demo_img_1);
        } else if (i == 5) {
            customViewHolder.main_images.setImageResource(R.drawable.demo_img_2);
        }

        customViewHolder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, GiftDetailsActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView main_images;
        TextView tv_item_name;
        CardView card_view;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            main_images = itemView.findViewById(R.id.main_images);
            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            card_view = itemView.findViewById(R.id.card_view);
        }
    }
}
