package com.codeclinic.yakrm.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.codeclinic.yakrm.Models.VoucherAboutToEndListItemModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Utils.ImageURL;
import com.codeclinic.yakrm.Utils.SessionManager;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.text.TextUtils.isEmpty;

public class VoucherAboutToEndAdaper extends RecyclerView.Adapter<VoucherAboutToEndAdaper.Holder> {

    List<VoucherAboutToEndListItemModel> arrayList;
    Context context;
    SessionManager sessionManager;

    public VoucherAboutToEndAdaper(List<VoucherAboutToEndListItemModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        sessionManager = new SessionManager(context);
    }

    @NonNull
    @Override
    public VoucherAboutToEndAdaper.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_voucher_will_end_list_view, null);
        return new Holder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull VoucherAboutToEndAdaper.Holder holder, int i) {

        if (sessionManager.getLanguage("Language", "en").equals("en")) {
            holder.tv_item_name.setText(arrayList.get(i).getBrandName() + " " + "(" + arrayList.get(i).getVoucherType() + ")");
        } else {
            if (isEmpty(arrayList.get(i).getBrand_name_arab())) {
                holder.tv_item_name.setText(arrayList.get(i).getBrandName() + " " + "(" + arrayList.get(i).getVoucherType() + ")");
            } else {
                holder.tv_item_name.setText(arrayList.get(i).getBrand_name_arab() + " " + "(" + arrayList.get(i).getVoucherType() + ")");
            }
        }
        holder.tv_discount_vouchers.setText(arrayList.get(i).getDiscount());
        holder.tv_price.setText(arrayList.get(i).getVoucherPrice().replaceAll("1", context.getResources().getString(R.string.one))
                .replaceAll("2", context.getResources().getString(R.string.two))
                .replaceAll("3", context.getResources().getString(R.string.three))
                .replaceAll("4", context.getResources().getString(R.string.four))
                .replaceAll("5", context.getResources().getString(R.string.five))
                .replaceAll("6", context.getResources().getString(R.string.six))
                .replaceAll("7", context.getResources().getString(R.string.seven))
                .replaceAll("8", context.getResources().getString(R.string.eight))
                .replaceAll("9", context.getResources().getString(R.string.nine))
                .replaceAll("0", context.getResources().getString(R.string.zero)) + context.getResources().getString(R.string.SR_currency));

        Picasso.with(context).load(ImageURL.Vendor_voucher_image + arrayList.get(i).getVoucherImage()).into(holder.main_images);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tv_item_name, tv_price, tv_expiry_date, tv_discount_vouchers;
        CardView card_view;
        ImageView main_images;

        public Holder(@NonNull View itemView) {
            super(itemView);
            main_images = itemView.findViewById(R.id.main_images);
            tv_expiry_date = itemView.findViewById(R.id.tv_expiry_date);
            tv_discount_vouchers = itemView.findViewById(R.id.tv_discount_vouchers);
            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            card_view = itemView.findViewById(R.id.card_view);
        }
    }
}
