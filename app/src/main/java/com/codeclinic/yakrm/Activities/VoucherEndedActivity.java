package com.codeclinic.yakrm.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codeclinic.yakrm.Adapter.VoucherWillEndAdapter;
import com.codeclinic.yakrm.Models.EndedVoucherListItemModel;
import com.codeclinic.yakrm.Models.EndedVoucherListModel;
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

public class VoucherEndedActivity extends AppCompatActivity {
    List<EndedVoucherListItemModel> arrayList = new ArrayList();
    RecyclerView recyclerView;
    ImageView img_back;
    VoucherWillEndAdapter voucherWillEndAdapter;
    SessionManager sessionManager;
    ProgressDialog progressDialog;
    API apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_ended);

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
            Call<EndedVoucherListModel> endedVoucherListModelCall = apiService.ENDED_VOUCHER_LIST_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token));
            endedVoucherListModelCall.enqueue(new Callback<EndedVoucherListModel>() {
                @Override
                public void onResponse(Call<EndedVoucherListModel> call, Response<EndedVoucherListModel> response) {
                    progressDialog.dismiss();
                    String status = response.body().getStatus();
                    if (status.equals("1")) {
                        arrayList = response.body().getData();
                        voucherWillEndAdapter = new VoucherWillEndAdapter(arrayList, VoucherEndedActivity.this);
                        recyclerView.setAdapter(voucherWillEndAdapter);

                    } else {
                        Toast.makeText(VoucherEndedActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<EndedVoucherListModel> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(VoucherEndedActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(VoucherEndedActivity.this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }

    }
}
