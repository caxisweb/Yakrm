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
import com.codeclinic.yakrm.Models.AllVoucherListItemModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Utils.ImageURL;
import com.codeclinic.yakrm.Utils.SessionManager;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.text.TextUtils.isEmpty;

public class BuyTabListAdapter extends RecyclerView.Adapter<BuyTabListAdapter.CustomViewHolder> {
    List<AllVoucherListItemModel> arrayList;
    Context context;

    SessionManager sessionManager;

    public BuyTabListAdapter(List<AllVoucherListItemModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        sessionManager = new SessionManager(context);
    }

    @NonNull
    @Override
    public BuyTabListAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_list_item_view, null);
        return new CustomViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BuyTabListAdapter.CustomViewHolder customViewHolder, final int i) {

        if (sessionManager.getLanguage("Language", "en").equals("en")) {
            customViewHolder.tv_item_name.setText(arrayList.get(i).getBrandName());
        } else {
            if (isEmpty(arrayList.get(i).getBrand_name_arab())) {
                customViewHolder.tv_item_name.setText(arrayList.get(i).getBrandName());
            } else {
                customViewHolder.tv_item_name.setText(arrayList.get(i).getBrand_name_arab());
            }
        }

        customViewHolder.tv_discount_percent.setText(arrayList.get(i).getDiscount().replaceAll("1", context.getResources().getString(R.string.one))
                .replaceAll("2", context.getResources().getString(R.string.two))
                .replaceAll("3", context.getResources().getString(R.string.three))
                .replaceAll("4", context.getResources().getString(R.string.four))
                .replaceAll("5", context.getResources().getString(R.string.five))
                .replaceAll("6", context.getResources().getString(R.string.six))
                .replaceAll("7", context.getResources().getString(R.string.seven))
                .replaceAll("8", context.getResources().getString(R.string.eight))
                .replaceAll("9", context.getResources().getString(R.string.nine))
                .replaceAll("0", context.getResources().getString(R.string.zero)) + "%");
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
