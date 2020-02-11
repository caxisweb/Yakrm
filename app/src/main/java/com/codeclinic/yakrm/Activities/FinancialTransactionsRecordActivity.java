package com.codeclinic.yakrm.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codeclinic.yakrm.Adapter.FinancialRecordAdapter;
import com.codeclinic.yakrm.Models.TransactionsListItemModel;
import com.codeclinic.yakrm.Models.TransactionsRecordModel;
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

public class FinancialTransactionsRecordActivity extends AppCompatActivity {
    List<TransactionsListItemModel> arrayList = new ArrayList<>();
    FinancialRecordAdapter financialRecordAdapter;
    ImageView img_back;
    RecyclerView recyclerView;
    API apiService;
    TextView tv_wallet_amount;
    SessionManager sessionManager;
    ProgressDialog progressDialog;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial_transactions_record);

        recyclerView = findViewById(R.id.recyclerView);
        img_back = findViewById(R.id.img_back);
        tv_wallet_amount = findViewById(R.id.tv_wallet_amount);
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
            progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
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

                        tv_wallet_amount.setText(response.body().getWallet().replaceAll("1", getResources().getString(R.string.one))
                                .replaceAll("2", getResources().getString(R.string.two))
                                .replaceAll("3", getResources().getString(R.string.three))
                                .replaceAll("4", getResources().getString(R.string.four))
                                .replaceAll("5", getResources().getString(R.string.five))
                                .replaceAll("6", getResources().getString(R.string.six))
                                .replaceAll("7", getResources().getString(R.string.seven))
                                .replaceAll("8", getResources().getString(R.string.eight))
                                .replaceAll("9", getResources().getString(R.string.nine))
                                .replaceAll("0", getResources().getString(R.string.zero)) + " " + getResources().getString(R.string.SR_currency));

                        arrayList = response.body().getData();
                   /*     for (int i = 0; i < 5; i++) {
                            TransactionsListItemModel transactionsListItemModel = new TransactionsListItemModel();
                            transactionsListItemModel.setAmountFromBank(arrayList.get(0).getAmountFromBank());
                            transactionsListItemModel.setAmountFromWallet(arrayList.get(0).getAmountFromWallet());
                            transactionsListItemModel.setId(arrayList.get(0).getId());
                            transactionsListItemModel.setScanVoucherType(arrayList.get(0).getScanVoucherType());
                            transactionsListItemModel.setTotalPrice(arrayList.get(0).getTotalPrice());
                            transactionsListItemModel.setTransactionId(arrayList.get(0).getTransactionId());
                            transactionsListItemModel.setUpdatedAt(arrayList.get(0).getUpdatedAt());
                            arrayList.add(transactionsListItemModel);
                        }
*/
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
            Toast.makeText(this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }


    }
}
