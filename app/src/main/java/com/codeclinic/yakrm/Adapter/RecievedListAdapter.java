package com.codeclinic.yakrm.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codeclinic.yakrm.Activities.MainActivity;
import com.codeclinic.yakrm.Models.RecievedGftListItemModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Utils.ImageURL;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecievedListAdapter extends RecyclerView.Adapter<RecievedListAdapter.Holder> {
    List<RecievedGftListItemModel> arrayList;
    Context context;
    String final_date;
    String[] date_array;

    public RecievedListAdapter(List<RecievedGftListItemModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecievedListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_recieved_item_view, null);
        return new Holder(view);
    }

    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
    @Override
    public void onBindViewHolder(@NonNull RecievedListAdapter.Holder holder, int i) {
        holder.tv_description.setText(arrayList.get(i).getDescription());
        holder.tv_friend_name.setText(arrayList.get(i).getName());
        holder.tv_item_name.setText(arrayList.get(i).getBrandName() + " " + "(" + arrayList.get(i).getVoucherType() + ")");
        Picasso.with(context).load(ImageURL.Vendor_voucher_image + arrayList.get(i).getVoucherImage()).into(holder.voucher_image);
        try {
            date_array = arrayList.get(i).getExpiredAt().split(" ");
            String date = date_array[0];
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            final_date = date.trim();
            Date strDate = sdf.parse(final_date);
            if (System.currentTimeMillis() > strDate.getTime()) {
                holder.tv_ends_on.setText(" " + context.getResources().getString(R.string.Ended) + " ");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat spf = null;
        Date newDate = null;
        try {
            spf = new SimpleDateFormat("yyyy-MM-dd");
            newDate = spf.parse(final_date);
            spf = new SimpleDateFormat("dd-MM-yyyy");
            final_date = spf.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tv_expiry_date.setText(final_date.replaceAll("1", context.getResources().getString(R.string.one))
                .replaceAll("2", context.getResources().getString(R.string.two))
                .replaceAll("3", context.getResources().getString(R.string.three))
                .replaceAll("4", context.getResources().getString(R.string.four))
                .replaceAll("5", context.getResources().getString(R.string.five))
                .replaceAll("6", context.getResources().getString(R.string.six))
                .replaceAll("7", context.getResources().getString(R.string.seven))
                .replaceAll("8", context.getResources().getString(R.string.eight))
                .replaceAll("9", context.getResources().getString(R.string.nine))
                .replaceAll("0", context.getResources().getString(R.string.zero)) + " " + date_array[1].replaceAll("1", context.getResources().getString(R.string.one))
                .replaceAll("2", context.getResources().getString(R.string.two))
                .replaceAll("3", context.getResources().getString(R.string.three))
                .replaceAll("4", context.getResources().getString(R.string.four))
                .replaceAll("5", context.getResources().getString(R.string.five))
                .replaceAll("6", context.getResources().getString(R.string.six))
                .replaceAll("7", context.getResources().getString(R.string.seven))
                .replaceAll("8", context.getResources().getString(R.string.eight))
                .replaceAll("9", context.getResources().getString(R.string.nine))
                .replaceAll("0", context.getResources().getString(R.string.zero)));

        holder.tv_price.setText(String.valueOf(Float.parseFloat(arrayList.get(i).getVoucherPrice()) + (Float.parseFloat(arrayList.get(i).getVoucherPrice()) * Float.parseFloat(arrayList.get(i).getDiscount())) / 100).replaceAll("1", context.getResources().getString(R.string.one))
                .replaceAll("2", context.getResources().getString(R.string.two))
                .replaceAll("3", context.getResources().getString(R.string.three))
                .replaceAll("4", context.getResources().getString(R.string.four))
                .replaceAll("5", context.getResources().getString(R.string.five))
                .replaceAll("6", context.getResources().getString(R.string.six))
                .replaceAll("7", context.getResources().getString(R.string.seven))
                .replaceAll("8", context.getResources().getString(R.string.eight))
                .replaceAll("9", context.getResources().getString(R.string.nine))
                .replaceAll("0", context.getResources().getString(R.string.zero)) + context.getResources().getString(R.string.SR_currency));
        holder.tv_discount.setText(arrayList.get(i).getDiscount().replaceAll("1", context.getResources().getString(R.string.one))
                .replaceAll("2", context.getResources().getString(R.string.two))
                .replaceAll("3", context.getResources().getString(R.string.three))
                .replaceAll("4", context.getResources().getString(R.string.four))
                .replaceAll("5", context.getResources().getString(R.string.five))
                .replaceAll("6", context.getResources().getString(R.string.six))
                .replaceAll("7", context.getResources().getString(R.string.seven))
                .replaceAll("8", context.getResources().getString(R.string.eight))
                .replaceAll("9", context.getResources().getString(R.string.nine))
                .replaceAll("0", context.getResources().getString(R.string.zero)) + "%");
        holder.tv_discount_price.setText(String.valueOf(Float.parseFloat(arrayList.get(i).getVoucherPrice())).replaceAll("1", context.getResources().getString(R.string.one))
                .replaceAll("2", context.getResources().getString(R.string.two))
                .replaceAll("3", context.getResources().getString(R.string.three))
                .replaceAll("4", context.getResources().getString(R.string.four))
                .replaceAll("5", context.getResources().getString(R.string.five))
                .replaceAll("6", context.getResources().getString(R.string.six))
                .replaceAll("7", context.getResources().getString(R.string.seven))
                .replaceAll("8", context.getResources().getString(R.string.eight))
                .replaceAll("9", context.getResources().getString(R.string.nine))
                .replaceAll("0", context.getResources().getString(R.string.zero)) + context.getResources().getString(R.string.SR_currency));

        holder.img_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.viewPager.setCurrentItem(4);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView img_wallet, voucher_image;
        TextView tv_item_name, tv_expiry_date, tv_price, tv_discount, tv_discount_price, tv_friend_name, tv_description, tv_ends_on;

        public Holder(@NonNull View itemView) {
            super(itemView);
            voucher_image = itemView.findViewById(R.id.voucher_image);
            img_wallet = itemView.findViewById(R.id.img_wallet);
            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            tv_expiry_date = itemView.findViewById(R.id.tv_expiry_date);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_discount = itemView.findViewById(R.id.tv_discount);
            tv_discount_price = itemView.findViewById(R.id.tv_discount_price);
            tv_friend_name = itemView.findViewById(R.id.tv_friend_name);
            tv_description = itemView.findViewById(R.id.tv_description);
            tv_ends_on = itemView.findViewById(R.id.tv_ends_on);
        }
    }
}
