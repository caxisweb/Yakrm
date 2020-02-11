package com.codeclinic.yakrm.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.codeclinic.yakrm.Models.ReturnVoucherModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.ImageURL;
import com.codeclinic.yakrm.Utils.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.codeclinic.yakrm.Activities.VoucherDetailActivity.admin_voucher_discount;
import static com.codeclinic.yakrm.Activities.VoucherDetailActivity.barcode;
import static com.codeclinic.yakrm.Activities.VoucherDetailActivity.scan_voucher_type_full;
import static com.codeclinic.yakrm.Activities.VoucherDetailActivity.v_image;
import static com.codeclinic.yakrm.Activities.VoucherDetailActivity.voucher_name;


public class ExchangeVoucherActivity extends AppCompatActivity {

    public static Activity ex_activity;
    TextView tv_item_name;
    ImageView img_back;
    LinearLayout llayout_send_friend, llayout_add_balance, llayout_display_voucher, llayout_return_voucher;
    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    ImageView imageView;

    API apiService;
    JSONObject jsonObject = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_voucher);

        ex_activity = this;
        sessionManager = new SessionManager(this);
        apiService = RestClass.getClient().create(API.class);

        img_back = findViewById(R.id.img_back);
        String language = String.valueOf(getResources().getConfiguration().locale);
        if (language.equals("ar")) {
            img_back.setImageDrawable(getResources().getDrawable(R.drawable.back_right_img));
        }
        tv_item_name = findViewById(R.id.tv_item_name);

        imageView = findViewById(R.id.img_voucher);
        llayout_send_friend = findViewById(R.id.llayout_send_friend);
        llayout_add_balance = findViewById(R.id.llayout_add_balance);
        llayout_display_voucher = findViewById(R.id.llayout_display_voucher);
        llayout_return_voucher = findViewById(R.id.llayout_return_voucher);

        tv_item_name.setText(getIntent().getStringExtra("name"));

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Picasso.with(this).load(ImageURL.Vendor_voucher_image + v_image).into(imageView);

        llayout_send_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!VoucherDetailActivity.scan_voucher_type.equals("g"))
                    startActivity(new Intent(ExchangeVoucherActivity.this, SendToFriendActivity.class));
                else
                    Toast.makeText(ExchangeVoucherActivity.this, getResources().getString(R.string.youcannotsendgifted), Toast.LENGTH_SHORT).show();
            }
        });

        llayout_add_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scan_voucher_type_full.equals("replace_voucher")) {
                    Toast.makeText(ExchangeVoucherActivity.this, getResources().getString(R.string.voucher_already_replaced_you_cant_replaced), Toast.LENGTH_LONG).show();
                } else {
                    startActivity(new Intent(ExchangeVoucherActivity.this, ExchangeAddBalanceActivity.class));
                }
            }
        });

        llayout_return_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogBuilder = new AlertDialog.Builder(ExchangeVoucherActivity.this);
                final View dialogView = getLayoutInflater().inflate(R.layout.custom_return_voucher_view, null);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setCancelable(true);

                ImageView brand_image = dialogView.findViewById(R.id.brand_image);
                TextView txt_item_name = dialogView.findViewById(R.id.tv_item_name);
                TextView txt_price = dialogView.findViewById(R.id.tv_price);
                TextView tv_price_wallet = dialogView.findViewById(R.id.tv_price_wallet);
                Button btn_return = dialogView.findViewById(R.id.btn_return);
                alertDialog = dialogBuilder.create();
                alertDialog.show();

                Picasso.with(ExchangeVoucherActivity.this).load(ImageURL.Vendor_voucher_image + v_image).into(brand_image);
                txt_item_name.setText(voucher_name);
                txt_price.setText(VoucherDetailActivity.price);
                double price_wallet = Double.parseDouble(VoucherDetailActivity.price);
                double deducted_value = price_wallet * (Double.parseDouble(admin_voucher_discount) / 100);
                price_wallet = price_wallet - deducted_value;
                tv_price_wallet.setText(String.valueOf(price_wallet));

                btn_return.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject.put("scan_voucher_type", scan_voucher_type_full);
                            jsonObject.put("scan_code", barcode.substring(0, barcode.length() - 1));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog = new ProgressDialog(ExchangeVoucherActivity.this);
                        progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
                        progressDialog.setIndeterminate(true);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        Call<ReturnVoucherModel> returnVoucherModelCall = apiService.RETURN_VOUCHER_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), jsonObject.toString());
                        returnVoucherModelCall.enqueue(new Callback<ReturnVoucherModel>() {
                            @Override
                            public void onResponse(Call<ReturnVoucherModel> call, Response<ReturnVoucherModel> response) {
                                progressDialog.dismiss();
                                if (response.body().getStatus().equals("1")) {
                                    String language = String.valueOf(getResources().getConfiguration().locale);
                                    if (language.equals("ar")) {
                                        if (response.body().getArab_message() != null) {
                                            Toast.makeText(ExchangeVoucherActivity.this, response.body().getArab_message(), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(ExchangeVoucherActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(ExchangeVoucherActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    sessionManager.createLoginSession(sessionManager.getUserDetails().get(SessionManager.User_Token), sessionManager.getUserDetails().get(SessionManager.User_ID), sessionManager.getUserDetails().get(SessionManager.User_Name), sessionManager.getUserDetails().get(SessionManager.User_Email), sessionManager.getUserDetails().get(SessionManager.USER_MOBILE), sessionManager.getUserDetails().get(SessionManager.USER_COUNTRY_ID), sessionManager.getUserDetails().get(SessionManager.USER_Profile), response.body().getWallet(), sessionManager.getUserDetails().get(SessionManager.UserType));
                                    startActivity(new Intent(ExchangeVoucherActivity.this, MainActivity.class));
                                    finishAffinity();
                                } else {
                                    Toast.makeText(ExchangeVoucherActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ReturnVoucherModel> call, Throwable t) {
                                progressDialog.dismiss();
                                Toast.makeText(ExchangeVoucherActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });
            }
        });

        llayout_display_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LayoutInflater inflater = getLayoutInflater();
                dialogBuilder = new AlertDialog.Builder(ExchangeVoucherActivity.this);
                final View dialogView = inflater.inflate(R.layout.custom_voucher_display_view, null);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setCancelable(true);

                final Button btn_accept = dialogView.findViewById(R.id.btn_accept);

                alertDialog = dialogBuilder.create();
                alertDialog.show();

                btn_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ExchangeVoucherActivity.this, MainActivity.class);
                        intent.putExtra("view_pos", "3");
                        startActivity(intent);
                    }
                });
            }
        });

    }
}
