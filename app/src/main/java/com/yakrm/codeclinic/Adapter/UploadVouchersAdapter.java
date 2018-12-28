package com.yakrm.codeclinic.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yakrm.codeclinic.Activities.UploadVoucherDataActivity;
import com.yakrm.codeclinic.R;

import java.util.ArrayList;

public class UploadVouchersAdapter extends RecyclerView.Adapter<UploadVouchersAdapter.Holder> {

    ArrayList<String> arrayList;
    Context context;

    public UploadVouchersAdapter(ArrayList<String> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public UploadVouchersAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_up_voucher_list_item_view, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UploadVouchersAdapter.Holder holder, final int i) {
        holder.tv_item_name.setText(arrayList.get(i));

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UploadVoucherDataActivity.class);
                intent.putExtra("name", arrayList.get(i));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tv_item_name;
        CardView card_view;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            card_view = itemView.findViewById(R.id.card_view);
        }
    }
}
