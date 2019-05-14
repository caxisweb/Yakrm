package com.codeclinic.yakrm.Adapter;

import android.annotation.SuppressLint;
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
import com.codeclinic.yakrm.Models.SearchListItemModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Utils.ImageURL;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.Holder> {
    List<SearchListItemModel> arrayList;
    Context context;

    public SearchListAdapter(List<SearchListItemModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_list_item_view, null);
        return new Holder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SearchListAdapter.Holder holder, final int i) {
        holder.tv_item_name.setText(arrayList.get(i).getBrandName());
        holder.tv_discount_percent.setText(arrayList.get(i).getDiscount().replaceAll("1", context.getResources().getString(R.string.one))
                .replaceAll("2", context.getResources().getString(R.string.two))
                .replaceAll("3", context.getResources().getString(R.string.three))
                .replaceAll("4", context.getResources().getString(R.string.four))
                .replaceAll("5", context.getResources().getString(R.string.five))
                .replaceAll("6", context.getResources().getString(R.string.six))
                .replaceAll("7", context.getResources().getString(R.string.seven))
                .replaceAll("8", context.getResources().getString(R.string.eight))
                .replaceAll("9", context.getResources().getString(R.string.nine))
                .replaceAll("0", context.getResources().getString(R.string.zero)) + "%");
        Picasso.with(context).load(ImageURL.Vendor_brand_image + arrayList.get(i).getBrandImage()).into(holder.brand_image);

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GiftDetailsActivity.class);
                intent.putExtra("brand_id", arrayList.get(i).getBrandId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView brand_image;
        TextView tv_item_name, tv_discount_percent;
        CardView card_view;

        public Holder(@NonNull View itemView) {
            super(itemView);
            brand_image = itemView.findViewById(R.id.brand_image);
            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            tv_discount_percent = itemView.findViewById(R.id.tv_discount_percent);
            card_view = itemView.findViewById(R.id.card_view);
        }
    }
}
