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

import com.codeclinic.yakrm.Activities.UploadVoucherDataActivity;
import com.codeclinic.yakrm.Models.BrandListItemModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Utils.ImageURL;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UploadVouchersAdapter extends RecyclerView.Adapter<UploadVouchersAdapter.Holder> {

    List<BrandListItemModel> arrayList;
    Context context;

    public UploadVouchersAdapter(List<BrandListItemModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public UploadVouchersAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_up_voucher_list_item_view, null);
        return new Holder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull UploadVouchersAdapter.Holder holder, final int i) {
        holder.tv_item_name.setText(arrayList.get(i).getBrandName());
        Picasso.with(context).load(ImageURL.Vendor_brand_image + arrayList.get(i).getBrandImage()).into(holder.img_brand);
        holder.tv_discount.setText(arrayList.get(i).getTotalVoucher() + " " + context.getResources().getString(R.string.Discount_Voucher).replaceAll("1", context.getResources().getString(R.string.one))
                .replaceAll("2", context.getResources().getString(R.string.two))
                .replaceAll("3", context.getResources().getString(R.string.three))
                .replaceAll("4", context.getResources().getString(R.string.four))
                .replaceAll("5", context.getResources().getString(R.string.five))
                .replaceAll("6", context.getResources().getString(R.string.six))
                .replaceAll("7", context.getResources().getString(R.string.seven))
                .replaceAll("8", context.getResources().getString(R.string.eight))
                .replaceAll("9", context.getResources().getString(R.string.nine))
                .replaceAll("0", context.getResources().getString(R.string.zero)));

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UploadVoucherDataActivity.class);
                intent.putExtra("name", arrayList.get(i).getBrandName());
                intent.putExtra("image", arrayList.get(i).getBrandImage());
                intent.putExtra("brand_id", arrayList.get(i).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tv_item_name, tv_discount;
        ImageView img_brand;
        CardView card_view;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            tv_discount = itemView.findViewById(R.id.tv_discount);
            img_brand = itemView.findViewById(R.id.img_brand);
            card_view = itemView.findViewById(R.id.card_view);
        }
    }
}
