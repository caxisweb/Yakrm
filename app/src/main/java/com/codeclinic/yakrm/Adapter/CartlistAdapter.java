package com.codeclinic.yakrm.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codeclinic.yakrm.LocalNotification.NotificationHelper;
import com.codeclinic.yakrm.Models.CartListItemModel;
import com.codeclinic.yakrm.Models.RemoveCartItemModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Utils.ImageURL;
import com.codeclinic.yakrm.Utils.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;

public class CartlistAdapter extends RecyclerView.Adapter<CartlistAdapter.Holder> {
    List<CartListItemModel> arrayList;
    Context context;
    API apiService;
    SessionManager sessionManager;
    JSONObject jsonObject = new JSONObject();
    ProgressDialog progressDialog;
    String final_date;
    String[] date_array;

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

        if (sessionManager.getLanguage("Language", "en").equals("en")) {
            holder.tv_item_name.setText(arrayList.get(i).getBrandName() + " " + "(" + arrayList.get(i).getVoucherType() + ")");
        } else {
            if (isEmpty(arrayList.get(i).getBrand_name_arab())) {
                holder.tv_item_name.setText(arrayList.get(i).getBrandName() + " " + "(" + arrayList.get(i).getVoucherType() + ")");
            } else {
                holder.tv_item_name.setText(arrayList.get(i).getBrand_name_arab() + " " + "(" + arrayList.get(i).getVoucherType() + ")");
            }
        }

        try {
            date_array = arrayList.get(i).getExpiredAt().split(" ");
            String date = date_array[0];
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
        holder.tv_ex_date.setText(final_date.replaceAll("1", context.getResources().getString(R.string.one))
                .replaceAll("2", context.getResources().getString(R.string.two))
                .replaceAll("3", context.getResources().getString(R.string.three))
                .replaceAll("4", context.getResources().getString(R.string.four))
                .replaceAll("5", context.getResources().getString(R.string.five))
                .replaceAll("6", context.getResources().getString(R.string.six))
                .replaceAll("7", context.getResources().getString(R.string.seven))
                .replaceAll("8", context.getResources().getString(R.string.eight))
                .replaceAll("9", context.getResources().getString(R.string.nine))
                .replaceAll("0", context.getResources().getString(R.string.zero)) + " " + date_array[1].replaceAll("1", context.getResources().getString(R.string.one))
                .replaceAll("2", context.getResources().getString(R.string.two))
                .replaceAll("3", context.getResources().getString(R.string.three))
                .replaceAll("4", context.getResources().getString(R.string.four))
                .replaceAll("5", context.getResources().getString(R.string.five))
                .replaceAll("6", context.getResources().getString(R.string.six))
                .replaceAll("7", context.getResources().getString(R.string.seven))
                .replaceAll("8", context.getResources().getString(R.string.eight))
                .replaceAll("9", context.getResources().getString(R.string.nine))
                .replaceAll("0", context.getResources().getString(R.string.zero)));
        holder.tv_value.setText(String.valueOf(Float.parseFloat(arrayList.get(i).getVoucherPrice()) + (Float.parseFloat(arrayList.get(i).getVoucherPrice()) * Float.parseFloat(arrayList.get(i).getDiscount())) / 100).replaceAll("1", context.getResources().getString(R.string.one))
                .replaceAll("2", context.getResources().getString(R.string.two))
                .replaceAll("3", context.getResources().getString(R.string.three))
                .replaceAll("4", context.getResources().getString(R.string.four))
                .replaceAll("5", context.getResources().getString(R.string.five))
                .replaceAll("6", context.getResources().getString(R.string.six))
                .replaceAll("7", context.getResources().getString(R.string.seven))
                .replaceAll("8", context.getResources().getString(R.string.eight))
                .replaceAll("9", context.getResources().getString(R.string.nine))
                .replaceAll("0", context.getResources().getString(R.string.zero)) + context.getResources().getString(R.string.SR_currency));
        holder.tv_discount.setText(arrayList.get(i).getDiscount().replaceAll("1", context.getResources().getString(R.string.one))
                .replaceAll("2", context.getResources().getString(R.string.two))
                .replaceAll("3", context.getResources().getString(R.string.three))
                .replaceAll("4", context.getResources().getString(R.string.four))
                .replaceAll("5", context.getResources().getString(R.string.five))
                .replaceAll("6", context.getResources().getString(R.string.six))
                .replaceAll("7", context.getResources().getString(R.string.seven))
                .replaceAll("8", context.getResources().getString(R.string.eight))
                .replaceAll("9", context.getResources().getString(R.string.nine))
                .replaceAll("0", context.getResources().getString(R.string.zero)) + "%");
        holder.tv_price.setText(arrayList.get(i).getVoucherPrice().replaceAll("1", context.getResources().getString(R.string.one))
                .replaceAll("2", context.getResources().getString(R.string.two))
                .replaceAll("3", context.getResources().getString(R.string.three))
                .replaceAll("4", context.getResources().getString(R.string.four))
                .replaceAll("5", context.getResources().getString(R.string.five))
                .replaceAll("6", context.getResources().getString(R.string.six))
                .replaceAll("7", context.getResources().getString(R.string.seven))
                .replaceAll("8", context.getResources().getString(R.string.eight))
                .replaceAll("9", context.getResources().getString(R.string.nine))
                .replaceAll("0", context.getResources().getString(R.string.zero)) + context.getResources().getString(R.string.SR_currency));
        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.CustomDialogFragment);
                alert.setMessage(context.getResources().getString(R.string.are_you_sure));
                alert.setCancelable(false);
                alert.setPositiveButton(context.getResources().getString(R.string.Delete), new DialogInterface.OnClickListener() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog = new ProgressDialog(context);
                        progressDialog.setMessage(context.getResources().getString(R.string.Please_Wait));
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
                                    sessionManager.setReminderStatus(false);
                                    NotificationHelper.cancelAlarmElapsed();
                                    NotificationHelper.disableBootReceiver(context);
                                    String language = String.valueOf(context.getResources().getConfiguration().locale);
                                    if (language.equals("ar")) {
                                        if (response.body().getArab_message() != null) {
                                            Toast.makeText(context, response.body().getArab_message(), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
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
                }).setNegativeButton(context.getResources().getString(R.string.New_cancel), new DialogInterface.OnClickListener() {
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
