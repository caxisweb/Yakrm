package com.codeclinic.yakrm.Adapter;

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

    @Override
    public void onBindViewHolder(@NonNull FinancialRecordAdapter.Holder holder, int i) {

        holder.tv_price.setText(arrayList.get(i).getVoucherPrice() + " " + context.getResources().getString(R.string.SR_currency));
        holder.tv_itemname.setText(arrayList.get(i).getBrandName());
        holder.tv_vendor_name.setText(arrayList.get(i).getVendorName());

        SimpleDateFormat spf = null;
        Date newDate = null;
        try {
            String date = arrayList.get(i).getCreatedAt().substring(0, arrayList.get(i).getCreatedAt().indexOf(" "));
            final_date = date.trim();
            spf = new SimpleDateFormat("yyyy-MM-dd");
            newDate = spf.parse(final_date);
            spf = new SimpleDateFormat("dd-MMM-yyyy");
            final_date = spf.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.tv_date.setText(final_date);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tv_itemname, tv_price, tv_vendor_name, tv_date;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tv_itemname = itemView.findViewById(R.id.tv_itemname);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_vendor_name = itemView.findViewById(R.id.tv_vendor_name);
            tv_date = itemView.findViewById(R.id.tv_date);
        }
    }
}
