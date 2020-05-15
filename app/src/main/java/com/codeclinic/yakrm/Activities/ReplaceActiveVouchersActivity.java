package com.codeclinic.yakrm.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

public class ReplaceActiveVouchersActivity extends AppCompatActivity {

    ImageView img_back;
    RecyclerView recyclerView;
    MyWalletAdapter myWalletAdapter;
    List<WalletActiveListItemModel> arrayList = new ArrayList<>();
    API apiService;
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    String admin_discount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replace_active_vouchers);

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

        recyclerView = findViewById(R.id.recyclerView);
        apiService = RestClass.getClient().create(API.class);
        sessionManager = new SessionManager(this);
        progressDialog = new ProgressDialog(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

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
                        myWalletAdapter = new MyWalletAdapter(arrayList, ReplaceActiveVouchersActivity.this, admin_discount);
                        recyclerView.setAdapter(myWalletAdapter);
                    } else {
                        String language = String.valueOf(getResources().getConfiguration().locale);
                        if (language.equals("ar")) {
                            if (response.body().getArab_message() != null) {
                                Toast.makeText(ReplaceActiveVouchersActivity.this, response.body().getArab_message(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ReplaceActiveVouchersActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ReplaceActiveVouchersActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<WalletActiveListModel> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(ReplaceActiveVouchersActivity.this, getResources().getString(R.string.Server_Error), Toast.LENGTH_SHORT).show();
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
