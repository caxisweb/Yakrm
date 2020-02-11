package com.codeclinic.yakrm.Adapter;

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

import com.codeclinic.yakrm.Activities.GiftDetailsActivity;
import com.codeclinic.yakrm.Models.FilterListItemModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Utils.ImageURL;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FilterListAdapter extends RecyclerView.Adapter<FilterListAdapter.Holder> {
    List<FilterListItemModel> arrayList;
    Context context;

    public FilterListAdapter(List<FilterListItemModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public FilterListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_filter_list_item_view, null);
        return new Holder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FilterListAdapter.Holder holder, final int i) {
        Picasso.with(context).load(ImageURL.Vendor_voucher_image + arrayList.get(i).getVoucherImage()).into(holder.voucher_image);
        holder.tv_item_name.setText(arrayList.get(i).getBrandName());

        holder.tv_price.setText(arrayList.get(i).getVoucherPrice().replaceAll("1", context.getResources().getString(R.string.one))
                .replaceAll("2", context.getResources().getString(R.string.two))
                .replaceAll("3", context.getResources().getString(R.string.three))
                .replaceAll("4", context.getResources().getString(R.string.four))
                .replaceAll("5", context.getResources().getString(R.string.five))
                .replaceAll("6", context.getResources().getString(R.string.six))
                .replaceAll("7", context.getResources().getString(R.string.seven))
                .replaceAll("8", context.getResources().getString(R.string.eight))
                .replaceAll("9", context.getResources().getString(R.string.nine))
                .replaceAll("0", context.getResources().getString(R.string.zero)) + " " + context.getResources().getString(R.string.SR_currency));

        holder.tv_active_till.setText(arrayList.get(i).getExpiredAt().replaceAll("1", context.getResources().getString(R.string.one))
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
                Intent intent = new Intent(context, GiftDetailsActivity.class);
                intent.putExtra("brand_id", arrayList.get(i).getBrand_Id());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tv_item_name, tv_price, tv_active_till;
        CardView card_view;
        ImageView voucher_image;

        public Holder(@NonNull View itemView) {
            super(itemView);
            voucher_image = itemView.findViewById(R.id.voucher_image);
            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_active_till = itemView.findViewById(R.id.tv_active_till);
            card_view = itemView.findViewById(R.id.card_view);
        }
    }
}
