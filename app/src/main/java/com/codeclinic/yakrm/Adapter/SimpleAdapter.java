package com.codeclinic.yakrm.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.codeclinic.yakrm.Models.NotificationListItemModel;
import com.codeclinic.yakrm.R;

import java.util.List;

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder> {
    final Context mContext;
    List<NotificationListItemModel> mData;

    public SimpleAdapter(Context context, List<NotificationListItemModel> data) {
        mContext = context;
        this.mData = data;
    }

    public void add(NotificationListItemModel s, int position) {
        position = position == -1 ? getItemCount() : position;
        mData.add(position, s);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        if (position < getItemCount()) {
            mData.remove(position);
            notifyItemRemoved(position);
        }
    }

    public SimpleAdapter.SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.custom_notification_list_items, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
        holder.tv_notification_Title.setText(mData.get(position).getSubject());
        holder.tv_notification.setText(mData.get(position).getDescription());
        holder.tv_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Position =" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        TextView tv_notification, tv_notification_Title;

        public SimpleViewHolder(View view) {
            super(view);
            tv_notification = view.findViewById(R.id.tv_notification);
            tv_notification_Title = view.findViewById(R.id.tv_notification_Title);
        }
    }
}
