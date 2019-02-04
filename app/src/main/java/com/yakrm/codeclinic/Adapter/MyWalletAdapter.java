package com.yakrm.codeclinic.Adapter;

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

import com.squareup.picasso.Picasso;
import com.yakrm.codeclinic.Activities.VoucherDetailActivity;
import com.yakrm.codeclinic.Models.ActiveVoucherListItemModel;
import com.yakrm.codeclinic.R;
import com.yakrm.codeclinic.Utils.ImageURL;

import java.util.List;

public class MyWalletAdapter extends RecyclerView.Adapter<MyWalletAdapter.Holder> {
    List<ActiveVoucherListItemModel> arrayList;
    Context context;

    public MyWalletAdapter(List<ActiveVoucherListItemModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyWalletAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_mywallet_liste_item_view, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyWalletAdapter.Holder holder, final int i) {

        Picasso.with(context).load(ImageURL.Vendor_voucher_image + arrayList.get(i).getVoucherImage()).into(holder.brand_image);

        holder.tv_item_name.setText(arrayList.get(i).getBrandName());
        holder.tv_price.setText(arrayList.get(i).getVoucherPrice() + " " + context.getResources().getString(R.string.SR_currency));
        holder.tv_active_till.setText(arrayList.get(i).getCreatedAt());

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VoucherDetailActivity.class);
                intent.putExtra("name", arrayList.get(i).getBrandName());
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
        ImageView brand_image;

        public Holder(@NonNull View itemView) {
            super(itemView);
            brand_image = itemView.findViewById(R.id.brand_image);
            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_active_till = itemView.findViewById(R.id.tv_active_till);
            card_view = itemView.findViewById(R.id.card_view);
        }
    }
}
