package com.codeclinic.yakrm.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codeclinic.yakrm.Adapter.ReplaceVoucherListAdapter;
import com.codeclinic.yakrm.Models.AllVoucherListItemModel;
import com.codeclinic.yakrm.Models.AllVouchersListModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.Connection_Detector;
import com.codeclinic.yakrm.Utils.ImageURL;
import com.codeclinic.yakrm.Utils.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ExchangeAddBalanceActivity extends AppCompatActivity {

    public static String replace_active_voucher_id, voucher_payment_detail_id, voucher_id;
    public static double main_price, discount = 5, voucher_price, temp_price, replace_price;
    ImageView img_back, voucher_image, main_voucher_image;
    RecyclerView recyclerView;
    API apiService;
    public static Activity exchange_activity;
    Button btn_complete;
    LinearLayout llayout_main;
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    List<AllVoucherListItemModel> arrayList = new ArrayList<>();
    ReplaceVoucherListAdapter replaceVoucherListAdapter;
    JSONObject jsonObject = new JSONObject();
    TextView tv_replace_voucher_name, tv_replace_price, tv_voucher_name, tv_voucher_price, tv_price, tv_amount_be_paid;


    public boolean isEmpty(CharSequence character) {
        return character == null || character.length() == 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_add_balance);

        img_back = findViewById(R.id.img_back);
        voucher_image = findViewById(R.id.voucher_image);
        main_voucher_image = findViewById(R.id.main_voucher_image);
        exchange_activity = this;


        btn_complete = findViewById(R.id.btn_complete);

        recyclerView = findViewById(R.id.recyclerView);

        llayout_main = findViewById(R.id.llayout_main);

        tv_replace_voucher_name = findViewById(R.id.tv_replace_voucher_name);
        tv_replace_price = findViewById(R.id.tv_replace_price);
        tv_voucher_name = findViewById(R.id.tv_voucher_name);
        tv_voucher_price = findViewById(R.id.tv_voucher_price);
        tv_price = findViewById(R.id.tv_price);
        tv_amount_be_paid = findViewById(R.id.tv_amount_be_paid);
        replace_price = Double.parseDouble(VoucherDetailActivity.price);


        try {
            tv_voucher_name.setText(VoucherDetailActivity.voucher_name);
            tv_voucher_price.setText(VoucherDetailActivity.price.replaceAll("1", getResources().getString(R.string.one))
                    .replaceAll("2", getResources().getString(R.string.two))
                    .replaceAll("3", getResources().getString(R.string.three))
                    .replaceAll("4", getResources().getString(R.string.four))
                    .replaceAll("5", getResources().getString(R.string.five))
                    .replaceAll("6", getResources().getString(R.string.six))
                    .replaceAll("7", getResources().getString(R.string.seven))
                    .replaceAll("8", getResources().getString(R.string.eight))
                    .replaceAll("9", getResources().getString(R.string.nine))
                    .replaceAll("0", getResources().getString(R.string.zero)) + " " + getResources().getString(R.string.SR_currency));

            Picasso.with(this).load(ImageURL.Vendor_voucher_image + VoucherDetailActivity.v_image).into(main_voucher_image);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

        apiService = RestClass.getClient().create(API.class);
        sessionManager = new SessionManager(this);
        progressDialog = new ProgressDialog(this);


        String str_temp_paid_amount = getResources().getString(R.string.wallet_amount_is_0_and_ammount_to_be_paid_is);
        str_temp_paid_amount = str_temp_paid_amount.replace("0", sessionManager.getUserDetails().get(SessionManager.Wallet));
        str_temp_paid_amount.replaceAll("1", getResources().getString(R.string.one))
                .replaceAll("2", getResources().getString(R.string.two))
                .replaceAll("3", getResources().getString(R.string.three))
                .replaceAll("4", getResources().getString(R.string.four))
                .replaceAll("5", getResources().getString(R.string.five))
                .replaceAll("6", getResources().getString(R.string.six))
                .replaceAll("7", getResources().getString(R.string.seven))
                .replaceAll("8", getResources().getString(R.string.eight))
                .replaceAll("9", getResources().getString(R.string.nine))
                .replaceAll("0", getResources().getString(R.string.zero));

        tv_amount_be_paid.setText(str_temp_paid_amount);

        String language = String.valueOf(getResources().getConfiguration().locale);
        if (language.equals("ar")) {
            img_back.setImageDrawable(getResources().getDrawable(R.drawable.back_right_img));
        }
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        if (Connection_Detector.isInternetAvailable(this)) {
            progressDialog.setMessage("Please Wait");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            Call<AllVouchersListModel> allVouchersListModelCall = apiService.ALL_VOUCHERS_LIST_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token));
            allVouchersListModelCall.enqueue(new Callback<AllVouchersListModel>() {
                @Override
                public void onResponse(Call<AllVouchersListModel> call, Response<AllVouchersListModel> response) {
                    progressDialog.dismiss();
                    int status = response.body().getStatus();
                    if (status == 1) {
                        arrayList = response.body().getData();
                        replaceVoucherListAdapter = new ReplaceVoucherListAdapter(arrayList, ExchangeAddBalanceActivity.this, recyclerView, tv_replace_price, tv_replace_voucher_name, tv_price, voucher_image, llayout_main, sessionManager);
                        recyclerView.setAdapter(replaceVoucherListAdapter);
                    } else {
                        Toast.makeText(ExchangeAddBalanceActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<AllVouchersListModel> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(ExchangeAddBalanceActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(ExchangeAddBalanceActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExchangeAddBalanceActivity.this, CompletingPurchasingActivity.class);
                intent.putExtra("price", String.valueOf(main_price));
                intent.putExtra("flag_cart", "2");
                startActivity(intent);

           /*     if (tv_price.getText().toString().equals("0.0") || tv_price.getText().toString().equals("0")) {
                    progressDialog.setMessage("Please Wait");
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    try {
                        jsonObject.put("replace_active_voucher_id", VoucherDetailActivity.voucher_id);
                        jsonObject.put("voucher_payment_detail_id", VoucherDetailActivity.v_payment_id);
                        jsonObject.put("replace_voucher_id", voucher_id);
                        jsonObject.put("wallet", sessionManager.getUserDetails().get(SessionManager.Wallet));
                        jsonObject.put("transaction_id", DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString());
                        jsonObject.put("transaction_price", 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Call<ReplaceVoucherModel> replaceVoucherModelCall = apiService.REPLACE_VOUCHER_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), jsonObject.toString());
                    replaceVoucherModelCall.enqueue(new Callback<ReplaceVoucherModel>() {
                        @Override
                        public void onResponse(Call<ReplaceVoucherModel> call, Response<ReplaceVoucherModel> response) {
                            progressDialog.dismiss();
                            String status = response.body().getStatus();
                            if (status.equals("1")) {
                                VoucherDetailActivity.voucher_name = "";
                                VoucherDetailActivity.date = "";
                                VoucherDetailActivity.barcode = "";
                                VoucherDetailActivity.pincode = "";
                                VoucherDetailActivity.price = "";
                                VoucherDetailActivity.v_image = "";
                                VoucherDetailActivity.v_payment_id = "";
                                VoucherDetailActivity.voucher_id = "";
                                VoucherDetailActivity.v_image = "";
                                ExchangeAddBalanceActivity.main_price = 0;
                                ExchangeAddBalanceActivity.voucher_price = 0;
                                ExchangeAddBalanceActivity.replace_price = 0;
                                ExchangeAddBalanceActivity.temp_price = 0;
                                startActivity(new Intent(ExchangeAddBalanceActivity.this, MainActivity.class));
                                finish();
                                ExchangeVoucherActivity.ex_activity.finish();
                                VoucherDetailActivity.voucher_detail_activity.finish();
                                Toast.makeText(ExchangeAddBalanceActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ExchangeAddBalanceActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ReplaceVoucherModel> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(ExchangeAddBalanceActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {

                }
*/
            }
        });

    }
}
