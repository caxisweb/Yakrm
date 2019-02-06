package com.codeclinic.yakrm.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codeclinic.yakrm.R;

import java.util.ArrayList;

public class FavVouchersAdapter extends RecyclerView.Adapter<FavVouchersAdapter.Holder> {
    ArrayList<String> arrayList;
    Context context;

    public FavVouchersAdapter(ArrayList<String> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;

    }

    @NonNull
    @Override
    public FavVouchersAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_fav_vouchers_list_items, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavVouchersAdapter.Holder holder, int i) {
        holder.tv_item_name.setText(arrayList.get(i));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tv_item_name;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tv_item_name = itemView.findViewById(R.id.tv_item_name);

        }
    }
}
