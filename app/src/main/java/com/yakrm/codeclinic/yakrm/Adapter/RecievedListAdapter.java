package com.yakrm.codeclinic.yakrm.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yakrm.codeclinic.yakrm.Activities.MainActivity;
import com.yakrm.codeclinic.yakrm.R;

import java.util.ArrayList;

public class RecievedListAdapter extends RecyclerView.Adapter<RecievedListAdapter.Holder> {
    ArrayList<String> arrayList;
    Context context;

    public RecievedListAdapter(ArrayList<String> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecievedListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_recieved_item_view, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecievedListAdapter.Holder holder, int i) {

        holder.img_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.viewPager.setCurrentItem(4);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView img_wallet;

        public Holder(@NonNull View itemView) {
            super(itemView);
            img_wallet = itemView.findViewById(R.id.img_wallet);
        }
    }
}
