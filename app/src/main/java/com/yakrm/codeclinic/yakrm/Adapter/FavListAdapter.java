package com.yakrm.codeclinic.yakrm.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yakrm.codeclinic.yakrm.R;

import java.util.ArrayList;

public class FavListAdapter extends RecyclerView.Adapter<FavListAdapter.Holder> {

    ArrayList<String> arrayList;
    Context context;

    public FavListAdapter(ArrayList<String> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public FavListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_favourite_list_item_view, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavListAdapter.Holder holder, int i) {
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
