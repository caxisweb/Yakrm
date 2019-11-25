package com.codeclinic.yakrm.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.codeclinic.yakrm.Adapter.VoucherAboutToEndAdaper;
import com.codeclinic.yakrm.Adapter.VoucherWillEndAdapter;
import com.codeclinic.yakrm.Models.VoucherAboutToEndListItemModel;
import com.codeclinic.yakrm.Models.VoucherAboutToEndListModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.Connection_Detector;
import com.codeclinic.yakrm.Utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VoucherAboutToEndActivity extends AppCompatActivity {
    List<VoucherAboutToEndListItemModel> arrayList = new ArrayList();
    RecyclerView recyclerView;
    ImageView img_back;
    VoucherWillEndAdapter voucherWillEndAdapter;
    SessionManager sessionManager;
    ProgressDialog progressDialog;
    API apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_about_to_end);

        sessionManager = new SessionManager(this);
        progressDialog = new ProgressDialog(this);
        apiService = RestClass.getClient().create(API.class);

        img_back = findViewById(R.id.img_back);
        String language = String.valueOf(getResources().getConfiguration().locale);
        if (language.equals("ar")) {
            img_back.setImageDrawable(getResources().getDrawable(R.drawable.back_right_img));
        }
        recyclerView = findViewById(R.id.recyclerView);
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
            progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            Call<VoucherAboutToEndListModel> voucherAboutToEndListModelCall = apiService.VOUCHER_ABOUT_TO_END_LIST_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token));
            voucherAboutToEndListModelCall.enqueue(new Callback<VoucherAboutToEndListModel>() {
                @Override
                public void onResponse(Call<VoucherAboutToEndListModel> call, Response<VoucherAboutToEndListModel> response) {
                    progressDialog.dismiss();
                    String status = response.body().getStatus();
                    if (status.equals("1")) {
                        arrayList = response.body().getData();
                        VoucherAboutToEndAdaper voucherAboutToEndAdaper = new VoucherAboutToEndAdaper(arrayList, VoucherAboutToEndActivity.this);
                        recyclerView.setAdapter(voucherAboutToEndAdaper);
                    } else {
                        Toast.makeText(VoucherAboutToEndActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<VoucherAboutToEndListModel> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(VoucherAboutToEndActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(VoucherAboutToEndActivity.this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }

    }
}
