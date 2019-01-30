package com.yakrm.codeclinic.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yakrm.codeclinic.Models.CartListItemModel;
import com.yakrm.codeclinic.Models.RemoveCartItemModel;
import com.yakrm.codeclinic.R;
import com.yakrm.codeclinic.Retrofit.API;
import com.yakrm.codeclinic.Utils.ImageURL;
import com.yakrm.codeclinic.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartlistAdapter extends RecyclerView.Adapter<CartlistAdapter.Holder> {
    List<CartListItemModel> arrayList;
    Context context;
    API apiService;
    SessionManager sessionManager;
    JSONObject jsonObject = new JSONObject();
    ProgressDialog progressDialog;
    String final_date;

    public CartlistAdapter(List<CartListItemModel> arrayList, Context context, API apiService, SessionManager sessionManager) {
        this.arrayList = arrayList;
        this.context = context;
        this.apiService = apiService;
        this.sessionManager = sessionManager;
    }

    @NonNull
    @Override
    public CartlistAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_cart_list_items_view, null);
        return new Holder(view);
    }

    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
    @Override
    public void onBindViewHolder(@NonNull CartlistAdapter.Holder holder, @SuppressLint("RecyclerView") final int i) {
        Picasso.with(context).load(ImageURL.Vendor_brand_image + arrayList.get(i).getBrandImage()).into(holder.brand_images);
        holder.tv_item_name.setText(arrayList.get(i).getBrandName() + "(" + context.getResources().getString(R.string.Electronic_and_paper_gifts) + ")");
        try {
            String date = arrayList.get(i).getExpiredAt().substring(0, arrayList.get(i).getExpiredAt().indexOf(" "));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            final_date = date.trim();
            Date strDate = sdf.parse(final_date);
            if (System.currentTimeMillis() > strDate.getTime()) {
                holder.tv_ends_on.setText(context.getResources().getString(R.string.Ended));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat spf = null;
        Date newDate = null;
        try {
            spf = new SimpleDateFormat("yyyy-MM-dd");
            newDate = spf.parse(final_date);
            spf = new SimpleDateFormat("dd-MM-yyyy");
            final_date = spf.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.tv_ex_date.setText(final_date);
        holder.tv_value.setText(arrayList.get(i).getVoucherPrice() + context.getResources().getString(R.string.SR_currency));
        holder.tv_discount.setText(arrayList.get(i).getDiscount() + "%");
        holder.tv_price.setText(String.valueOf(Float.parseFloat(arrayList.get(i).getVoucherPrice()) - (Float.parseFloat(arrayList.get(i).getVoucherPrice()) * Float.parseFloat(arrayList.get(i).getDiscount())) / 100) + context.getResources().getString(R.string.SR_currency));
        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                alert.setMessage("Are you Sure?");
                alert.setCancelable(false);
                alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog = new ProgressDialog(context);
                        progressDialog.setMessage("Please Wait");
                        progressDialog.setIndeterminate(true);
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        try {
                            jsonObject.put("cart_id", arrayList.get(i).getCartId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Call<RemoveCartItemModel> removeCartItemModelCall = apiService.REMOVE_CART_ITEM_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), jsonObject.toString());
                        removeCartItemModelCall.enqueue(new Callback<RemoveCartItemModel>() {
                            @Override
                            public void onResponse(Call<RemoveCartItemModel> call, Response<RemoveCartItemModel> response) {
                                progressDialog.dismiss();
                                String status = response.body().getStatus();
                                if (status.equals("1")) {
                                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    ((Activity) context).recreate();
                                } else {
                                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<RemoveCartItemModel> call, Throwable t) {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tv_item_name, tv_ex_date, tv_ends_on, tv_value, tv_discount, tv_price;
        ImageView brand_images, img_delete;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            tv_ex_date = itemView.findViewById(R.id.tv_ex_date);
            tv_ends_on = itemView.findViewById(R.id.tv_ends_on);
            tv_value = itemView.findViewById(R.id.tv_value);
            tv_discount = itemView.findViewById(R.id.tv_discount);
            tv_price = itemView.findViewById(R.id.tv_price);
            brand_images = itemView.findViewById(R.id.brand_images);
            img_delete = itemView.findViewById(R.id.img_delete);
        }
    }
}
