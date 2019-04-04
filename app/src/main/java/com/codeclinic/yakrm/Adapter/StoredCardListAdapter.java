package com.codeclinic.yakrm.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codeclinic.yakrm.Models.GetCardListItemModel;
import com.codeclinic.yakrm.R;

import java.util.List;

public class StoredCardListAdapter extends RecyclerView.Adapter<StoredCardListAdapter.Holder> {
    List<GetCardListItemModel> arrayList;
    Context context;

    public StoredCardListAdapter(List<GetCardListItemModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public StoredCardListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custoom_stored_list_view, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoredCardListAdapter.Holder holder, int i) {
        holder.tv_card_no.setText("**** **** **** " + arrayList.get(i).getCardNumber().substring(arrayList.get(i).getCardNumber().length() - 4));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tv_card_no;
        ImageView img_edit;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tv_card_no = itemView.findViewById(R.id.tv_card_no);
            img_edit = itemView.findViewById(R.id.img_edit);
        }
    }
}
