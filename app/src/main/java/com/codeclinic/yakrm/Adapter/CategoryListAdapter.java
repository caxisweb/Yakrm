package com.codeclinic.yakrm.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codeclinic.yakrm.Models.GiftCategoryModel;
import com.codeclinic.yakrm.R;

import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.Holder> {
    List<GiftCategoryModel> arrayList;
    Context context;
    RecyclerView recyclerViewCategory;
    itemClickListener listener;
    int selectedposition = -1;

    public CategoryListAdapter(List<GiftCategoryModel> arrayList, Context context, RecyclerView recyclerViewCategory, itemClickListener listener) {
        this.arrayList = arrayList;
        this.context = context;
        this.recyclerViewCategory = recyclerViewCategory;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_category_list_item_view, null, false));
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull final CategoryListAdapter.Holder holder, final int position) {

        String language = String.valueOf(context.getResources().getConfiguration().locale);
        if (language.equals("ar")) {
            holder.tv_category.setText(arrayList.get(position).getArabGiftCategoryName());
        } else {
            holder.tv_category.setText(arrayList.get(position).getGiftCategoryName());
        }


        if (selectedposition != -1) {
            if (selectedposition == position) {
                holder.img_category.setBackground(context.getDrawable(R.drawable.circular_selected_bg));
            } else {
                holder.img_category.setBackground(context.getDrawable(R.drawable.circular_deselected_bg));
            }
        } else {
            holder.img_category.setBackground(context.getDrawable(R.drawable.circular_deselected_bg));
        }


        holder.img_category.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                if (selectedposition != position) {
                    selectedposition = position;
                } else {
                    selectedposition = -1;
                }
                notifyDataSetChanged();
                listener.itemClick(selectedposition);

            }
        });


    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public interface itemClickListener {
        void itemClick(int pos);
    }

    public class Holder extends RecyclerView.ViewHolder {
        LinearLayout llayout_category;
        ImageView img_category;
        TextView tv_category;

        public Holder(@NonNull View itemView) {
            super(itemView);
            llayout_category = itemView.findViewById(R.id.llayout_category);
            tv_category = itemView.findViewById(R.id.tv_category);
            img_category = itemView.findViewById(R.id.img_category);
        }
    }
}
