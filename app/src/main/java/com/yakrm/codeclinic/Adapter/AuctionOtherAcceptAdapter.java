package com.yakrm.codeclinic.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yakrm.codeclinic.R;

import java.util.ArrayList;

public class AuctionOtherAcceptAdapter extends RecyclerView.Adapter<AuctionOtherAcceptAdapter.Holder> {
    ArrayList<String> arrayList;
    Context context;

    public AuctionOtherAcceptAdapter(ArrayList<String> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public AuctionOtherAcceptAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_other_auction_accept_list_view, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AuctionOtherAcceptAdapter.Holder holder, int i) {

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
