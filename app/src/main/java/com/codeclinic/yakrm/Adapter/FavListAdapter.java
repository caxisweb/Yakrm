package com.codeclinic.yakrm.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codeclinic.yakrm.Activities.GiftDetailsActivity;
import com.codeclinic.yakrm.Models.AddToFavouritesModel;
import com.codeclinic.yakrm.Models.FavouritesListItemModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Utils.ImageURL;
import com.codeclinic.yakrm.Utils.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavListAdapter extends RecyclerView.Adapter<FavListAdapter.Holder> {

    List<FavouritesListItemModel> arrayList;
    Context context;
    JSONObject jsonObject = new JSONObject();
    API apiService;
    SessionManager sessionManager;

    public FavListAdapter(List<FavouritesListItemModel> arrayList, Context context, API apiService, SessionManager sessionManager) {
        this.arrayList = arrayList;
        this.context = context;
        this.sessionManager = sessionManager;
        this.apiService = apiService;
    }

    @NonNull
    @Override
    public FavListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_favourite_list_item_view, null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FavListAdapter.Holder holder, final int i) {
        holder.tv_item_name.setText(arrayList.get(i).getBrandName());
        holder.tv_discount.setText(arrayList.get(i).getDiscount() + "%");
        Picasso.with(context).load(ImageURL.Vendor_brand_image + arrayList.get(i).getBrandImage()).into(holder.brand_image);

        holder.img_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    jsonObject.put("brand_id", Integer.parseInt(arrayList.get(i).getId()));
                    jsonObject.put("is_favourite", 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                alert.setMessage("Are you sure you want to remove from Favourites?");
                alert.setCancelable(false);
                alert.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Call<AddToFavouritesModel> addToFavouritesModelCall = apiService.ADD_TO_FAVOURITES_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), jsonObject.toString());
                        addToFavouritesModelCall.enqueue(new Callback<AddToFavouritesModel>() {
                            @Override
                            public void onResponse(Call<AddToFavouritesModel> call, Response<AddToFavouritesModel> response) {
                                String status = response.body().getStatus();
                                if (status.equals("1")) {
                                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    ((Activity) context).recreate();
                                } else {
                                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<AddToFavouritesModel> call, Throwable t) {
                                Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GiftDetailsActivity.class);
                intent.putExtra("brand_id", arrayList.get(i).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tv_item_name, tv_discount;
        ImageView img_fav, brand_image;
        CardView card_view;

        public Holder(@NonNull View itemView) {
            super(itemView);

            brand_image = itemView.findViewById(R.id.brand_image);
            img_fav = itemView.findViewById(R.id.img_fav);
            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            tv_discount = itemView.findViewById(R.id.tv_discount);
            card_view = itemView.findViewById(R.id.card_view);
        }
    }
}
