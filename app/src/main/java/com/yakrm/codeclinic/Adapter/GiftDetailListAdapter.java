package com.yakrm.codeclinic.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yakrm.codeclinic.Models.AddVoucherToCartModel;
import com.yakrm.codeclinic.Models.VoucherDetailsListItemModel;
import com.yakrm.codeclinic.R;
import com.yakrm.codeclinic.Retrofit.API;
import com.yakrm.codeclinic.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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


    public GiftDetailListAdapter(List<VoucherDetailsListItemModel> arrayList, Context context, API apiService, ArrayList<String> ar_add_cart_value, SessionManager sessionManager) {
        this.arrayList = arrayList;
        this.context = context;
        this.apiService = apiService;
        this.ar_add_cart_value = ar_add_cart_value;
        this.sessionManager = sessionManager;
    }

    @NonNull
    @Override
    public GiftDetailListAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_gift_list_items_view, null);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GiftDetailListAdapter.CustomViewHolder customViewHolder, final int i) {
        if (i == 0) {

        }

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

        customViewHolder.img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_value = itemView.findViewById(R.id.tv_value);
            tv_discount = itemView.findViewById(R.id.tv_discount);
            tv_pay = itemView.findViewById(R.id.tv_pay);
            img_view = itemView.findViewById(R.id.img_view);
        }
    }
}
