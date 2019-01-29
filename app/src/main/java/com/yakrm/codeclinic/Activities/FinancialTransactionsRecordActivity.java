package com.yakrm.codeclinic.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.yakrm.codeclinic.Adapter.FinancialRecordAdapter;
import com.yakrm.codeclinic.Models.TransactionsListItemModel;
import com.yakrm.codeclinic.Models.TransactionsRecordModel;
import com.yakrm.codeclinic.R;
import com.yakrm.codeclinic.Retrofit.API;
import com.yakrm.codeclinic.Retrofit.RestClass;
import com.yakrm.codeclinic.Utils.Connection_Detector;
import com.yakrm.codeclinic.Utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinancialTransactionsRecordActivity extends AppCompatActivity {
    List<TransactionsListItemModel> arrayList = new ArrayList<>();
    FinancialRecordAdapter financialRecordAdapter;
    ImageView img_back;
    RecyclerView recyclerView;
    API apiService;
    SessionManager sessionManager;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial_transactions_record);

        recyclerView = findViewById(R.id.recyclerView);
        img_back = findViewById(R.id.img_back);
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
        sessionManager = new SessionManager(this);
        progressDialog = new ProgressDialog(this);
        apiService = RestClass.getClient().create(API.class);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);


        if (Connection_Detector.isInternetAvailable(this)) {
            progressDialog.setMessage("Please Wait");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

            Call<TransactionsRecordModel> transactionsRecordModelCall = apiService.TRANSACTIONS_RECORD_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token));
            transactionsRecordModelCall.enqueue(new Callback<TransactionsRecordModel>() {
                @Override
                public void onResponse(Call<TransactionsRecordModel> call, Response<TransactionsRecordModel> response) {
                    progressDialog.dismiss();
                    String status = response.body().getStatus();
                    if (status.equals("1")) {
                        arrayList = response.body().getData();
                        financialRecordAdapter = new FinancialRecordAdapter(arrayList, FinancialTransactionsRecordActivity.this);
                        recyclerView.setAdapter(financialRecordAdapter);
                    } else {
                        Toast.makeText(FinancialTransactionsRecordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<TransactionsRecordModel> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(FinancialTransactionsRecordActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }


    }
}
