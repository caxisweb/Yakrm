package com.codeclinic.yakrm.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.codeclinic.yakrm.Activities.MainActivity;
import com.codeclinic.yakrm.Activities.VoucherDetailActivity;
import com.codeclinic.yakrm.Models.ReturnVoucherModel;
import com.codeclinic.yakrm.Models.WalletActiveListItemModel;
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

public class ReturnVoucherListAdapter extends RecyclerView.Adapter<ReturnVoucherListAdapter.Holder> {
    List<WalletActiveListItemModel> arrayList;
    Context context;
    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;
    LayoutInflater inflater;
    String admin_discount;
    API apiService;
    JSONObject jsonObject = new JSONObject();
    ProgressDialog progressDialog;
    SessionManager sessionManager;

    public ReturnVoucherListAdapter(List<WalletActiveListItemModel> arrayList, String admin_discount, Context context, API apiService, ProgressDialog progressDialog, SessionManager sessionManager) {
        this.arrayList = arrayList;
        this.context = context;
        this.admin_discount = admin_discount;
        inflater = LayoutInflater.from(context);
        this.progressDialog = progressDialog;
        this.apiService = apiService;
        this.sessionManager = sessionManager;
    }

    @NonNull
    @Override
    public ReturnVoucherListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_mywallet_liste_item_view, null);
        return new Holder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ReturnVoucherListAdapter.Holder holder, final int i) {
        Picasso.with(context).load(ImageURL.Vendor_voucher_image + arrayList.get(i).getVoucherImage()).into(holder.voucher_image);

        holder.tv_item_name.setText(arrayList.get(i).getBrandName());

        holder.tv_price.setText(arrayList.get(i).getVoucherPrice().replaceAll("1", context.getResources().getString(R.string.one))
                .replaceAll("2", context.getResources().getString(R.string.two))
                .replaceAll("3", context.getResources().getString(R.string.three))
                .replaceAll("4", context.getResources().getString(R.string.four))
                .replaceAll("5", context.getResources().getString(R.string.five))
                .replaceAll("6", context.getResources().getString(R.string.six))
                .replaceAll("7", context.getResources().getString(R.string.seven))
                .replaceAll("8", context.getResources().getString(R.string.eight))
                .replaceAll("9", context.getResources().getString(R.string.nine))
                .replaceAll("0", context.getResources().getString(R.string.zero)) + " " + context.getResources().getString(R.string.SR_currency));

        holder.tv_active_till.setText(arrayList.get(i).getCreatedAt().replaceAll("1", context.getResources().getString(R.string.one))
                .replaceAll("2", context.getResources().getString(R.string.two))
                .replaceAll("3", context.getResources().getString(R.string.three))
                .replaceAll("4", context.getResources().getString(R.string.four))
                .replaceAll("5", context.getResources().getString(R.string.five))
                .replaceAll("6", context.getResources().getString(R.string.six))
                .replaceAll("7", context.getResources().getString(R.string.seven))
                .replaceAll("8", context.getResources().getString(R.string.eight))
                .replaceAll("9", context.getResources().getString(R.string.nine))
                .replaceAll("0", context.getResources().getString(R.string.zero)));

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder = new AlertDialog.Builder(context);
                final View dialogView = inflater.inflate(R.layout.custom_return_voucher_view, null);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setCancelable(true);

                ImageView brand_image = dialogView.findViewById(R.id.brand_image);
                TextView txt_item_name = dialogView.findViewById(R.id.tv_item_name);
                TextView txt_price = dialogView.findViewById(R.id.tv_price);
                TextView tv_price_wallet = dialogView.findViewById(R.id.tv_price_wallet);
                Button btn_return = dialogView.findViewById(R.id.btn_return);
                alertDialog = dialogBuilder.create();
                alertDialog.show();

                Picasso.with(context).load(ImageURL.Vendor_voucher_image + arrayList.get(i).getVoucherImage()).into(brand_image);
                txt_item_name.setText(arrayList.get(i).getBrandName());
                txt_price.setText(arrayList.get(i).getVoucherPrice());
                double price_wallet = Double.parseDouble(arrayList.get(i).getVoucherPrice());
                double deducted_value = price_wallet * (Double.parseDouble(admin_discount) / 100);
                price_wallet = price_wallet - deducted_value;
                tv_price_wallet.setText(String.valueOf(price_wallet));

                btn_return.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject.put("scan_voucher_type", arrayList.get(i).getScanVoucherType());
                            jsonObject.put("scan_code", arrayList.get(i).getScanCode());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.setMessage(context.getResources().getString(R.string.Please_Wait));
                        progressDialog.setIndeterminate(true);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        Call<ReturnVoucherModel> returnVoucherModelCall = apiService.RETURN_VOUCHER_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), jsonObject.toString());
                        returnVoucherModelCall.enqueue(new Callback<ReturnVoucherModel>() {
                            @Override
                            public void onResponse(Call<ReturnVoucherModel> call, Response<ReturnVoucherModel> response) {
                                progressDialog.dismiss();
                                if (response.body().getStatus().equals("1")) {
                                    sessionManager.createLoginSession(sessionManager.getUserDetails().get(SessionManager.User_Token), sessionManager.getUserDetails().get(SessionManager.User_ID), sessionManager.getUserDetails().get(SessionManager.User_Name), sessionManager.getUserDetails().get(SessionManager.User_Email), sessionManager.getUserDetails().get(SessionManager.USER_MOBILE), sessionManager.getUserDetails().get(SessionManager.USER_COUNTRY_ID), sessionManager.getUserDetails().get(SessionManager.USER_Profile), response.body().getWallet(), sessionManager.getUserDetails().get(SessionManager.UserType));
                                    context.startActivity(new Intent(context, MainActivity.class));
                                    VoucherDetailActivity.voucher_detail_activity.finish();
                                    ((Activity) context).finish();
                                } else {
                                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ReturnVoucherModel> call, Throwable t) {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });
            }
        });
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tv_item_name, tv_price, tv_active_till;
        CardView card_view;
        ImageView voucher_image;

        public Holder(@NonNull View itemView) {
            super(itemView);
            voucher_image = itemView.findViewById(R.id.brand_image);
            tv_item_name = itemView.findViewById(R.id.tv_item_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_active_till = itemView.findViewById(R.id.tv_active_till);
            card_view = itemView.findViewById(R.id.card_view);
        }
    }
}
