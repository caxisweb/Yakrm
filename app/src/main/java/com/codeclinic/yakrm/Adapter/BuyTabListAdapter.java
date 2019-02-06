package com.codeclinic.yakrm.Adapter;

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

import com.codeclinic.yakrm.Activities.GiftDetailsActivity;
import com.codeclinic.yakrm.Models.AllVoucherListItemModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Utils.ImageURL;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BuyTabListAdapter extends RecyclerView.Adapter<BuyTabListAdapter.CustomViewHolder> {
    List<AllVoucherListItemModel> arrayList;
    Context context;

    public BuyTabListAdapter(List<AllVoucherListItemModel> arrayList, Context context) {
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
    public void onBindViewHolder(@NonNull BuyTabListAdapter.CustomViewHolder customViewHolder, final int i) {

        customViewHolder.tv_item_name.setText(arrayList.get(i).getBrandName());
        customViewHolder.tv_discount_percent.setText(arrayList.get(i).getDiscount() + "%");
        Picasso.with(context).load(ImageURL.Vendor_brand_image + arrayList.get(i).getBrandImage()).into(customViewHolder.brand_image);

        customViewHolder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GiftDetailsActivity.class);
                intent.putExtra("brand_id", arrayList.get(i).getBrand_id());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView brand_image;
        TextView tv_item_name, tv_discount_percent;
        CardView card_view;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            brand_image = itemView.findViewById(R.id.brand_image);
            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            tv_discount_percent = itemView.findViewById(R.id.tv_discount_percent);
            card_view = itemView.findViewById(R.id.card_view);
        }
    }
}
