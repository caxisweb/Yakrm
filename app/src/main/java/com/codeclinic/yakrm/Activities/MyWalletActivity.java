package com.codeclinic.yakrm.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codeclinic.yakrm.Adapter.MyWalletAdapter;
import com.codeclinic.yakrm.Models.WalletActiveListItemModel;
import com.codeclinic.yakrm.Models.WalletActiveListModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.Connection_Detector;
import com.codeclinic.yakrm.Utils.SessionManager;
import com.franmontiel.localechanger.LocaleChanger;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyWalletActivity extends AppCompatActivity {
    ImageView img_back;
    RecyclerView recyclerView;
    MyWalletAdapter myWalletAdapter;
    List<WalletActiveListItemModel> arrayList = new ArrayList<>();
    TextView tv_upload_voucher, tv_wallet_amount, tv_fav_voucher, tv_active_voucher, tv_voucher_about_end, tv_voucher_ended;
    LinearLayout llayout_voucher_ended_valid, llayout_fav_vouchers, llayout_active_vouchers, llayout_voucher_ended_done;
    API apiService;
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    String admin_discount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);

        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String language = String.valueOf(getResources().getConfiguration().locale);
        if (language.equals("ar")) {
            img_back.setImageDrawable(getResources().getDrawable(R.drawable.back_right_img));
        }

        llayout_fav_vouchers = findViewById(R.id.llayout_fav_vouchers);
        llayout_voucher_ended_done = findViewById(R.id.llayout_voucher_ended_done);
        llayout_active_vouchers = findViewById(R.id.llayout_active_vouchers);
        llayout_voucher_ended_valid = findViewById(R.id.llayout_voucher_ended_valid);
        recyclerView = findViewById(R.id.recyclerView);
        //tv_upload_voucher = findViewById(R.id.tv_upload_voucher);
        tv_wallet_amount = findViewById(R.id.tv_wallet_amount);

        tv_fav_voucher = findViewById(R.id.tv_fav_voucher);
        tv_active_voucher = findViewById(R.id.tv_active_voucher);
        tv_voucher_about_end = findViewById(R.id.tv_voucher_about_end);
        tv_voucher_ended = findViewById(R.id.tv_voucher_ended);

        sessionManager = new SessionManager(this);
        apiService = RestClass.getClient().create(API.class);
        progressDialog = new ProgressDialog(this);

        try {
            tv_wallet_amount.setText(sessionManager.getUserDetails().get(SessionManager.Wallet).replaceAll("1", getResources().getString(R.string.one))
                    .replaceAll("2", getResources().getString(R.string.two))
                    .replaceAll("3", getResources().getString(R.string.three))
                    .replaceAll("4", getResources().getString(R.string.four))
                    .replaceAll("5", getResources().getString(R.string.five))
                    .replaceAll("6", getResources().getString(R.string.six))
                    .replaceAll("7", getResources().getString(R.string.seven))
                    .replaceAll("8", getResources().getString(R.string.eight))
                    .replaceAll("9", getResources().getString(R.string.nine))
                    .replaceAll("0", getResources().getString(R.string.zero)) + " " + getResources().getString(R.string.SR_currency));
        } catch (Exception e) {
            e.printStackTrace();
        }


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);


     /*   tv_upload_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyWalletActivity.this, UploadVouchersActivity.class));
            }
        });*/

        llayout_voucher_ended_valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyWalletActivity.this, VoucherAboutToEndActivity.class));
            }
        });

        llayout_fav_vouchers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyWalletActivity.this, FavouritesActivity.class));
            }
        });

        llayout_active_vouchers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyWalletActivity.this, AllActiveVouchersActivity.class));
            }
        });

        llayout_voucher_ended_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyWalletActivity.this, VoucherEndedActivity.class));
            }
        });

        if (Connection_Detector.isInternetAvailable(this)) {
            progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            Call<WalletActiveListModel> walletActiveListModelCall = apiService.WALLET_ACTIVE_LIST_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token));
            walletActiveListModelCall.enqueue(new Callback<WalletActiveListModel>() {
                @Override
                public void onResponse(Call<WalletActiveListModel> call, Response<WalletActiveListModel> response) {
                    progressDialog.dismiss();
                    String status = response.body().getStatus();
                    if (status.equals("1")) {
                        arrayList = response.body().getData();
                        admin_discount = response.body().getAdminProfitDis();
                        myWalletAdapter = new MyWalletAdapter(arrayList, MyWalletActivity.this, admin_discount);
                        recyclerView.setAdapter(myWalletAdapter);
                    } else {
                        //Toast.makeText(this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    tv_fav_voucher.setText(response.body().getTotal_favourites());
                    tv_active_voucher.setText(response.body().getTotal_active_voucher());
                    tv_voucher_about_end.setText(response.body().getVoucher_end_soon());
                    tv_voucher_ended.setText(response.body().getVoucher_ended());
                }

                @Override
                public void onFailure(Call<WalletActiveListModel> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(MyWalletActivity.this, getResources().getString(R.string.Server_Error), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, getResources().getString(R.string.No_Internet_Connection), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleChanger.configureBaseContext(newBase);
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityRecreationHelper.onResume(this);
    }

    @Override
    protected void onDestroy() {
        ActivityRecreationHelper.onDestroy(this);
        super.onDestroy();
    }
}
