package com.codeclinic.yakrm.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codeclinic.yakrm.R;

import java.util.ArrayList;

public class ActiveAuctionAdapter extends RecyclerView.Adapter<ActiveAuctionAdapter.Holder> {

    ArrayList<String> arrayList;
    Context context;

    public ActiveAuctionAdapter(ArrayList<String> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ActiveAuctionAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_active_now_auction_list_view, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveAuctionAdapter.Holder holder, int i) {

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
