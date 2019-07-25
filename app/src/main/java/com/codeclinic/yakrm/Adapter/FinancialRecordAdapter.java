package com.codeclinic.yakrm.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codeclinic.yakrm.Models.TransactionsListItemModel;
import com.codeclinic.yakrm.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FinancialRecordAdapter extends RecyclerView.Adapter<FinancialRecordAdapter.Holder> {
    List<TransactionsListItemModel> arrayList;
    Context context;
    String final_date;

    public FinancialRecordAdapter(List<TransactionsListItemModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public FinancialRecordAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_financial_record_list_view, null);
        return new Holder(view);
    }

    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
    @Override
    public void onBindViewHolder(@NonNull FinancialRecordAdapter.Holder holder, int i) {

        holder.tv_price.setText(arrayList.get(i).getTotalPrice() + " " + context.getResources().getString(R.string.SR_currency));
        holder.tv_by_visa.setText(arrayList.get(i).getAmountFromBank() + " " + context.getResources().getString(R.string.SR_currency));
        holder.tv_by_wallet.setText(arrayList.get(i).getAmountFromWallet() + " " + context.getResources().getString(R.string.SR_currency));

        if (arrayList.get(i).getScanVoucherType().equals("purchase_voucher")) {
            holder.tv_itemname.setText(context.getResources().getString(R.string.Debited));
            holder.view.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            //holder.tv_itemname.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.tv_price.setTextColor(context.getResources().getColor(R.color.colorPrimary));

        } else {
            holder.tv_itemname.setText(context.getResources().getString(R.string.Credited));
            //holder.tv_itemname.setTextColor(context.getResources().getColor(R.color.digit_color));
            holder.view.setBackgroundColor(context.getResources().getColor(R.color.digit_color));
            holder.tv_price.setTextColor(context.getResources().getColor(R.color.digit_color));
        }

        SimpleDateFormat spf = null;
        Date newDate = null;
        try {
            String date = arrayList.get(i).getUpdatedAt().substring(0, arrayList.get(i).getUpdatedAt().indexOf(" "));
            final_date = date.trim();
            spf = new SimpleDateFormat("yyyy-MM-dd");
            newDate = spf.parse(final_date);
            spf = new SimpleDateFormat("dd-MMM-yyyy");
            final_date = spf.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.tv_date.setText(final_date);

        if (arrayList.get(i).getScanVoucherType().equals("return_voucher")) {
            holder.tv_transaction_type.setText(context.getResources().getString(R.string.ReturnedVoucher));
        } else if (arrayList.get(i).getScanVoucherType().equals("purchase_voucher")) {
            holder.tv_transaction_type.setText(context.getResources().getString(R.string.PurchasedVoucher));
        } else if (arrayList.get(i).getScanVoucherType().equals("replace_voucher")) {
            holder.tv_transaction_type.setText(context.getResources().getString(R.string.ReplacedVoucher));
        } else {
            holder.tv_transaction_type.setText(context.getResources().getString(R.string.GiftedVoucher));
        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tv_itemname, tv_price, tv_by_visa, tv_by_wallet, tv_date, tv_transaction_type;
        View view;

        public Holder(@NonNull View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.view);
            tv_itemname = itemView.findViewById(R.id.tv_itemname);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_by_visa = itemView.findViewById(R.id.tv_by_visa);
            tv_by_wallet = itemView.findViewById(R.id.tv_by_wallet);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_transaction_type = itemView.findViewById(R.id.tv_transaction_type);
        }
    }
}
