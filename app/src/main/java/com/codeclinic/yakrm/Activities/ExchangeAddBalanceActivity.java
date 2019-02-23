package com.codeclinic.yakrm.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codeclinic.yakrm.Adapter.ReplaceVoucherListAdapter;
import com.codeclinic.yakrm.Models.AllVoucherListItemModel;
import com.codeclinic.yakrm.Models.AllVouchersListModel;
import com.codeclinic.yakrm.Models.ReplaceVoucherModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.Connection_Detector;
import com.codeclinic.yakrm.Utils.ImageURL;
import com.codeclinic.yakrm.Utils.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
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
    TextView tv_replace_voucher_name, tv_replace_price, tv_voucher_name, tv_voucher_price, tv_price;
    Button btn_complete;
    LinearLayout llayout_main;
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    List<AllVoucherListItemModel> arrayList = new ArrayList<>();
    ReplaceVoucherListAdapter replaceVoucherListAdapter;
    JSONObject jsonObject = new JSONObject();

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

        btn_complete = findViewById(R.id.btn_complete);

        recyclerView = findViewById(R.id.recyclerView);

        llayout_main = findViewById(R.id.llayout_main);

        tv_replace_voucher_name = findViewById(R.id.tv_replace_voucher_name);
        tv_replace_price = findViewById(R.id.tv_replace_price);
        tv_voucher_name = findViewById(R.id.tv_voucher_name);
        tv_voucher_price = findViewById(R.id.tv_voucher_price);
        tv_price = findViewById(R.id.tv_price);
        replace_price = Integer.parseInt(VoucherDetailActivity.price);

        try {
            tv_voucher_name.setText(VoucherDetailActivity.voucher_name);
            tv_voucher_price.setText(VoucherDetailActivity.price + " " + getResources().getString(R.string.SR_currency));
            Picasso.with(this).load(ImageURL.Vendor_voucher_image + VoucherDetailActivity.v_image).into(main_voucher_image);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

        apiService = RestClass.getClient().create(API.class);
        sessionManager = new SessionManager(this);
        progressDialog = new ProgressDialog(this);

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
                        replaceVoucherListAdapter = new ReplaceVoucherListAdapter(arrayList, ExchangeAddBalanceActivity.this, recyclerView, tv_replace_price, tv_replace_voucher_name, tv_price, voucher_image, llayout_main);
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
                progressDialog.setMessage("Please Wait");
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.show();
                try {
                    jsonObject.put("replace_active_voucher_id", VoucherDetailActivity.voucher_id);
                    jsonObject.put("voucher_payment_detail_id", VoucherDetailActivity.v_payment_id);
                    jsonObject.put("replace_voucher_id", voucher_id);
                    jsonObject.put("wallet", "0");
                    jsonObject.put("transaction_id", DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString());
                    jsonObject.put("transaction_price", String.valueOf(main_price));
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
                            main_price = 0;
                            voucher_price = 0;
                            replace_price = 0;
                            temp_price = 0;
                            startActivity(new Intent(ExchangeAddBalanceActivity.this, MainActivity.class));
                            finish();
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

            }
        });

    }
}
