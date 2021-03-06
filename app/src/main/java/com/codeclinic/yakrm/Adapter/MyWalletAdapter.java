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

import com.codeclinic.yakrm.Activities.VoucherDetailActivity;
import com.codeclinic.yakrm.Models.WalletActiveListItemModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Utils.ImageURL;
import com.codeclinic.yakrm.Utils.SessionManager;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyWalletAdapter extends RecyclerView.Adapter<MyWalletAdapter.Holder> {
    List<WalletActiveListItemModel> arrayList;
    Context context;
    String admin_discount;
    SessionManager sessionManager;

    public MyWalletAdapter(List<WalletActiveListItemModel> arrayList, Context context, String admin_discount) {
        this.arrayList = arrayList;
        this.context = context;
        this.admin_discount = admin_discount;
        sessionManager = new SessionManager(context);
    }

    public boolean isEmpty(CharSequence character) {
        return character == null || character.length() == 0;
    }

    @NonNull
    @Override
    public MyWalletAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_mywallet_liste_item_view, null);
        return new Holder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyWalletAdapter.Holder holder, @SuppressLint("RecyclerView") final int i) {
        Picasso.with(context).load(ImageURL.Vendor_voucher_image + arrayList.get(i).getVoucherImage()).into(holder.voucher_image);


        if (sessionManager.getLanguage("Language", "en").equals("en")) {
            if (arrayList.get(i).getScanVoucherType().equals("gift_voucher")) {
                holder.tv_item_name.setText(arrayList.get(i).getBrandName() + " (Gifted)");
            } else {
                holder.tv_item_name.setText(arrayList.get(i).getBrandName());
            }

        } else {

            if (isEmpty(arrayList.get(i).getBrand_name_arab())) {
                if (arrayList.get(i).getScanVoucherType().equals("gift_voucher")) {
                    holder.tv_item_name.setText(arrayList.get(i).getBrandName() + " (Gifted)");
                } else {
                    holder.tv_item_name.setText(arrayList.get(i).getBrandName());
                }
            } else {
                if (arrayList.get(i).getScanVoucherType().equals("gift_voucher")) {
                    holder.tv_item_name.setText(arrayList.get(i).getBrand_name_arab() + " (Gifted)");
                } else {
                    holder.tv_item_name.setText(arrayList.get(i).getBrand_name_arab());
                }
            }
        }

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


        holder.tv_active_till.setText(arrayList.get(i).getExpired_at().substring(0, arrayList.get(i).getExpired_at().indexOf(" ")));

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VoucherDetailActivity.class);
                if (sessionManager.getLanguage("Language", "en").equals("en")) {
                    intent.putExtra("name", arrayList.get(i).getBrandName());
                } else {

                    if (isEmpty(arrayList.get(i).getBrand_name_arab())) {
                        intent.putExtra("name", arrayList.get(i).getBrandName());
                    } else {
                        intent.putExtra("name", arrayList.get(i).getBrand_name_arab());
                    }
                }
                //intent.putExtra("name", arrayList.get(i).getBrandName());
                intent.putExtra("date", arrayList.get(i).getExpired_at().substring(0, arrayList.get(i).getExpired_at().indexOf(" ")));
                intent.putExtra("barcode", arrayList.get(i).getScanCode());
                intent.putExtra("pincode", arrayList.get(i).getPinCode());
                intent.putExtra("price", arrayList.get(i).getVoucherPrice());
                intent.putExtra("v_image", arrayList.get(i).getVoucherImage());
                intent.putExtra("v_desc", arrayList.get(i).getVoucher_description());
                if (!isEmpty(arrayList.get(i).getVoucherPaymentDetailId())) {
                    intent.putExtra("v_payment_id", arrayList.get(i).getVoucherPaymentDetailId());
                    intent.putExtra("v_payment_type", "voucher_payment");
                } else {
                    intent.putExtra("v_payment_id", arrayList.get(i).getReplaceVoucherPaymentId());
                    intent.putExtra("v_payment_type", "replace_payment");
                }
                if (!isEmpty(arrayList.get(i).getVoucherId())) {
                    intent.putExtra("voucher_id", arrayList.get(i).getVoucherId());
                } else {
                    intent.putExtra("voucher_id", arrayList.get(i).getNewVoucherId());
                }
                intent.putExtra("admin_voucher_discount", admin_discount);
                intent.putExtra("scan_voucher_type", arrayList.get(i).getScanVoucherType());
                intent.putExtra("voucher_gift_send_id", arrayList.get(i).getVouchergiftsendid());
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
            voucher_image = itemView.findViewById(R.id.brand_image);
            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_active_till = itemView.findViewById(R.id.tv_active_till);
            card_view = itemView.findViewById(R.id.card_view);
        }
    }
}
