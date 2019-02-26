package com.codeclinic.yakrm.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codeclinic.yakrm.Activities.ExchangeAddBalanceActivity;
import com.codeclinic.yakrm.Activities.VoucherDetailActivity;
import com.codeclinic.yakrm.Models.AllVoucherListItemModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Utils.ImageURL;
import com.codeclinic.yakrm.Utils.SessionManager;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReplaceVoucherListAdapter extends RecyclerView.Adapter<ReplaceVoucherListAdapter.Holder> {

    List<AllVoucherListItemModel> arrayList;
    Context context;
    RecyclerView recyclerView;
    ImageView voucher_image;
    TextView tv_replace_price, tv_replace_voucher_name, tv_price;
    LinearLayout llayout_main;
    SessionManager sessionManager;
    double wallet_amt = 0.00;

    public ReplaceVoucherListAdapter(List<AllVoucherListItemModel> arrayList, Context context, RecyclerView recyclerView, TextView tv_replace_price, TextView tv_replace_voucher_name, TextView tv_price, ImageView voucher_image, LinearLayout llayout_main, SessionManager sessionManager) {
        this.arrayList = arrayList;
        this.context = context;
        this.recyclerView = recyclerView;
        this.tv_replace_price = tv_replace_price;
        this.tv_replace_voucher_name = tv_replace_voucher_name;
        this.voucher_image = voucher_image;
        this.llayout_main = llayout_main;
        this.tv_price = tv_price;
        this.sessionManager = sessionManager;
    }

    @NonNull
    @Override
    public ReplaceVoucherListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_mywallet_liste_item_view, null);
        return new Holder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ReplaceVoucherListAdapter.Holder holder, final int i) {
        Picasso.with(context).load(ImageURL.Vendor_brand_image + arrayList.get(i).getBrandImage()).into(holder.voucher_image);

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
                ExchangeAddBalanceActivity.voucher_id = arrayList.get(i).getVoucherId();
                ExchangeAddBalanceActivity.voucher_price = Integer.parseInt(arrayList.get(i).getVoucherPrice());

                recyclerView.setVisibility(View.GONE);
                llayout_main.setVisibility(View.VISIBLE);

                tv_replace_voucher_name.setText(arrayList.get(i).getBrandName());
                tv_replace_price.setText(arrayList.get(i).getVoucherPrice().replaceAll("1", context.getResources().getString(R.string.one))
                        .replaceAll("2", context.getResources().getString(R.string.two))
                        .replaceAll("3", context.getResources().getString(R.string.three))
                        .replaceAll("4", context.getResources().getString(R.string.four))
                        .replaceAll("5", context.getResources().getString(R.string.five))
                        .replaceAll("6", context.getResources().getString(R.string.six))
                        .replaceAll("7", context.getResources().getString(R.string.seven))
                        .replaceAll("8", context.getResources().getString(R.string.eight))
                        .replaceAll("9", context.getResources().getString(R.string.nine))
                        .replaceAll("0", context.getResources().getString(R.string.zero)) + " " + context.getResources().getString(R.string.SR_currency));

                Picasso.with(context).load(ImageURL.Vendor_brand_image + arrayList.get(i).getBrandImage()).into(voucher_image);

                double percent = Integer.parseInt(VoucherDetailActivity.admin_voucher_discount);
                double temp_percent = percent / 100;

                ExchangeAddBalanceActivity.temp_price = ExchangeAddBalanceActivity.replace_price * temp_percent;
                ExchangeAddBalanceActivity.replace_price = ExchangeAddBalanceActivity.replace_price - ExchangeAddBalanceActivity.temp_price;
                wallet_amt = Double.parseDouble(sessionManager.getUserDetails().get(SessionManager.Wallet));

                if (ExchangeAddBalanceActivity.voucher_price > ExchangeAddBalanceActivity.replace_price) {
                    ExchangeAddBalanceActivity.main_price = ExchangeAddBalanceActivity.voucher_price - ExchangeAddBalanceActivity.replace_price;
                    if (wallet_amt > ExchangeAddBalanceActivity.main_price) {
                       /* ExchangeAddBalanceActivity.main_price = wallet_amt - ExchangeAddBalanceActivity.main_price;
                        tv_price.setText(String.valueOf(ExchangeAddBalanceActivity.main_price) + " " + context.getResources().getString(R.string.SR_currency));*/
                        ExchangeAddBalanceActivity.main_price = 0;
                        tv_price.setText(String.valueOf(ExchangeAddBalanceActivity.main_price) + " " + context.getResources().getString(R.string.SR_currency));

                    } else {
                        ExchangeAddBalanceActivity.main_price = ExchangeAddBalanceActivity.main_price - wallet_amt;
                        tv_price.setText(String.valueOf(ExchangeAddBalanceActivity.main_price) + " " + context.getResources().getString(R.string.SR_currency));
                    }
                } else {
                    //ExchangeAddBalanceActivity.main_price = ExchangeAddBalanceActivity.replace_price - ExchangeAddBalanceActivity.voucher_price;
                    ExchangeAddBalanceActivity.main_price = 0;
                    tv_price.setText(String.valueOf(0 + " " + context.getResources().getString(R.string.SR_currency)));
                  /*  if (wallet_amt > ExchangeAddBalanceActivity.main_price) {
                        ExchangeAddBalanceActivity.main_price = wallet_amt - ExchangeAddBalanceActivity.main_price;
                        tv_price.setText(String.valueOf(wallet_amt - ExchangeAddBalanceActivity.main_price) + " " + context.getResources().getString(R.string.SR_currency));
                    } else {
                        ExchangeAddBalanceActivity.main_price = ExchangeAddBalanceActivity.main_price - wallet_amt;
                        tv_price.setText(String.valueOf(ExchangeAddBalanceActivity.main_price - wallet_amt) + " " + context.getResources().getString(R.string.SR_currency));
                    }*/
                }
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
