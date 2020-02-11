package com.codeclinic.yakrm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codeclinic.yakrm.R;

import java.util.ArrayList;

public class MyVoucherAuctionAdapter extends RecyclerView.Adapter<MyVoucherAuctionAdapter.Holder> {
    ArrayList<String> arrayList;
    Context context;

    public MyVoucherAuctionAdapter(ArrayList<String> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyVoucherAuctionAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_my_voucher_ended_view, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVoucherAuctionAdapter.Holder holder, int i) {

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        public Holder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
