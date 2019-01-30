package com.yakrm.codeclinic.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yakrm.codeclinic.Activities.StartActivity;
import com.yakrm.codeclinic.Models.AddVoucherToCartModel;
import com.yakrm.codeclinic.Models.VoucherDetailsListItemModel;
import com.yakrm.codeclinic.R;
import com.yakrm.codeclinic.Retrofit.API;
import com.yakrm.codeclinic.Utils.ImageURL;
import com.yakrm.codeclinic.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GiftDetailListAdapter extends RecyclerView.Adapter<GiftDetailListAdapter.CustomViewHolder> {
    List<VoucherDetailsListItemModel> arrayList;
    Context context;
    API apiService;
    ArrayList<String> ar_add_cart_value;
    JSONObject jsonObject = new JSONObject();
    SessionManager sessionManager;
    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;
    String final_date;
    LayoutInflater inflater;


    public GiftDetailListAdapter(List<VoucherDetailsListItemModel> arrayList, Context context, API apiService, ArrayList<String> ar_add_cart_value, SessionManager sessionManager) {
        this.arrayList = arrayList;
        this.context = context;
        this.apiService = apiService;
        this.ar_add_cart_value = ar_add_cart_value;
        this.sessionManager = sessionManager;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public GiftDetailListAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_gift_list_items_view, null);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GiftDetailListAdapter.CustomViewHolder customViewHolder, final int i) {

        customViewHolder.tv_value.setText(arrayList.get(i).getVoucherPrice() + context.getResources().getString(R.string.SR_currency));
        int voucher_price = Integer.parseInt(arrayList.get(i).getVoucherPrice());
        customViewHolder.tv_discount.setText(arrayList.get(i).getDiscount() + "%");
        float voucher_discount = Float.parseFloat(arrayList.get(i).getDiscount()) / 100;
        float voucher_disount_price = Integer.parseInt(arrayList.get(i).getVoucherPrice()) * voucher_discount;
        if (voucher_price != 0) {
            float voucher_pay = Float.parseFloat(arrayList.get(i).getVoucherPrice()) - voucher_disount_price;
            customViewHolder.tv_pay.setText(String.valueOf(voucher_pay) + context.getResources().getString(R.string.SR_currency));
        } else {
            customViewHolder.tv_pay.setText(arrayList.get(i).getVoucherPrice() + context.getResources().getString(R.string.SR_currency));
        }

        customViewHolder.llayout_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder = new AlertDialog.Builder(context);
                final View dialogView = inflater.inflate(R.layout.custom_voucher_detail_popup, null);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setCancelable(true);

                ImageView img_brand_img = dialogView.findViewById(R.id.img_brand_img);
                TextView tv_brand_name = dialogView.findViewById(R.id.tv_brand_name);
                TextView tv_ends_on = dialogView.findViewById(R.id.tv_ends_on);
                TextView tv_ex_date = dialogView.findViewById(R.id.tv_ex_date);
                TextView tv_description = dialogView.findViewById(R.id.tv_description);
                TextView tv_gift_type = dialogView.findViewById(R.id.tv_gift_type);

                alertDialog = dialogBuilder.create();
                alertDialog.show();

                Picasso.with(context).load(ImageURL.Vendor_voucher_image + arrayList.get(i).getVoucherImage()).into(img_brand_img);
                tv_brand_name.setText(arrayList.get(i).getGiftCategoryName());
                try {
                    String date = arrayList.get(i).getExpiredAt().substring(0, arrayList.get(i).getExpiredAt().indexOf(" "));
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    final_date = date.trim();
                    Date strDate = sdf.parse(final_date);
                    if (System.currentTimeMillis() > strDate.getTime()) {
                        tv_ends_on.setText(context.getResources().getString(R.string.Ended));
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
                tv_ex_date.setText(final_date);
                tv_description.setText(arrayList.get(i).getVoucherCode());
                tv_gift_type.setText(arrayList.get(i).getVoucherType());

            }
        });

        customViewHolder.img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager.isLoggedIn()) {
                    if (ar_add_cart_value.get(i).equals("0")) {
                        ar_add_cart_value.set(i, "1");
                        customViewHolder.img_view.setImageResource(R.mipmap.ic_fillcart_icon);
                        try {
                            jsonObject.put("voucher_id", arrayList.get(i).getVoucherId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Call<AddVoucherToCartModel> addVoucherToCartModelCall = apiService.ADD_VOUCHER_TO_CART_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), jsonObject.toString());
                        addVoucherToCartModelCall.enqueue(new Callback<AddVoucherToCartModel>() {
                            @Override
                            public void onResponse(Call<AddVoucherToCartModel> call, Response<AddVoucherToCartModel> response) {
                                String status = response.body().getStatus();
                                if (status.equals("1")) {
                                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<AddVoucherToCartModel> call, Throwable t) {
                                Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(context, "You have already added this voucher in your cart.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    context.startActivity(new Intent(context, StartActivity.class));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tv_value, tv_discount, tv_pay;
        ImageView img_view;
        LinearLayout llayout_main;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_value = itemView.findViewById(R.id.tv_value);
            tv_discount = itemView.findViewById(R.id.tv_discount);
            tv_pay = itemView.findViewById(R.id.tv_pay);
            img_view = itemView.findViewById(R.id.img_view);
            llayout_main = itemView.findViewById(R.id.llayout_main);
        }
    }
}
