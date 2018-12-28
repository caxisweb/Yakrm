package com.yakrm.codeclinic.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yakrm.codeclinic.Models.GiftDetailListItemModel;
import com.yakrm.codeclinic.R;

import java.util.ArrayList;

public class GiftDetailListAdapter extends RecyclerView.Adapter<GiftDetailListAdapter.CustomViewHolder> {
    ArrayList<GiftDetailListItemModel> arrayList;
    Context context;

    public GiftDetailListAdapter(ArrayList<GiftDetailListItemModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public GiftDetailListAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_gift_list_items_view, null);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GiftDetailListAdapter.CustomViewHolder customViewHolder, int i) {
        if (i == 0) {
            customViewHolder.img_view.setImageResource(R.mipmap.ic_fillcart_icon);
        }
        customViewHolder.tv_value.setText(arrayList.get(i).getValu());
        customViewHolder.tv_discount.setText(arrayList.get(i).getDiscount());
        customViewHolder.tv_pay.setText(arrayList.get(i).getPay());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tv_value, tv_discount, tv_pay;
        ImageView img_view;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_value = itemView.findViewById(R.id.tv_value);
            tv_discount = itemView.findViewById(R.id.tv_discount);
            tv_pay = itemView.findViewById(R.id.tv_pay);
            img_view = itemView.findViewById(R.id.img_view);
        }
    }
}
