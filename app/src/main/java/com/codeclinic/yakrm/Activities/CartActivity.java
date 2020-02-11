package com.codeclinic.yakrm.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codeclinic.yakrm.Adapter.CartlistAdapter;
import com.codeclinic.yakrm.Models.CartListItemModel;
import com.codeclinic.yakrm.Models.CartListModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.Connection_Detector;
import com.codeclinic.yakrm.Utils.SessionManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView img_back;
    List<CartListItemModel> arrayList = new ArrayList<>();
    CartlistAdapter cartlistAdapter;
    API apiService;
    ProgressDialog progressDialog;
    SessionManager sessionManager;

    TextView tv_header_name;
    RelativeLayout rl_cart_filled, rl_empty_layout;
    TextView tv_total_price;
    String total_price;
    JSONObject jsonObject = new JSONObject();
    Button btn_pay, btn_start_purchasing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        img_back = findViewById(R.id.img_back);
        String language = String.valueOf(getResources().getConfiguration().locale);
        if (language.equals("ar")) {
            img_back.setImageDrawable(getResources().getDrawable(R.drawable.back_right_img));
        }
        tv_header_name = findViewById(R.id.tv_header_name);
        tv_total_price = findViewById(R.id.tv_total_price);
        recyclerView = findViewById(R.id.recyclerView);
        rl_cart_filled = findViewById(R.id.rl_cart_filled);
        rl_empty_layout = findViewById(R.id.rl_empty_layout);
        btn_pay = findViewById(R.id.btn_pay);
        btn_start_purchasing = findViewById(R.id.btn_start_purchasing);

        apiService = RestClass.getClient().create(API.class);
        progressDialog = new ProgressDialog(this);
        sessionManager = new SessionManager(this);

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
            Call<CartListModel> cartListModelCall = apiService.CART_LIST_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token));
            cartListModelCall.enqueue(new Callback<CartListModel>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<CartListModel> call, Response<CartListModel> response) {
                    progressDialog.dismiss();
                    String status = response.body().getStatus();
                    if (status.equals("1")) {
                        tv_header_name.setText(getResources().getString(R.string.Cart));
                        tv_total_price.setText(String.valueOf(response.body().getTotalPrice()).replaceAll("1", getResources().getString(R.string.one))
                                .replaceAll("2", getResources().getString(R.string.two))
                                .replaceAll("3", getResources().getString(R.string.three))
                                .replaceAll("4", getResources().getString(R.string.four))
                                .replaceAll("5", getResources().getString(R.string.five))
                                .replaceAll("6", getResources().getString(R.string.six))
                                .replaceAll("7", getResources().getString(R.string.seven))
                                .replaceAll("8", getResources().getString(R.string.eight))
                                .replaceAll("9", getResources().getString(R.string.nine))
                                .replaceAll("0", getResources().getString(R.string.zero)) + getResources().getString(R.string.SR_currency));
                        total_price = String.valueOf(response.body().getTotalPrice());
                        arrayList = response.body().getData();
                        cartlistAdapter = new CartlistAdapter(arrayList, CartActivity.this, apiService, sessionManager);
                        recyclerView.setAdapter(cartlistAdapter);
                    } else {
                        rl_cart_filled.setVisibility(View.GONE);
                        rl_empty_layout.setVisibility(View.VISIBLE);
                        Toast.makeText(CartActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CartListModel> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(CartActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }

        btn_start_purchasing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, MainActivity.class));
                finish();
            }
        });

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, CompletingPurchasingActivity.class);
                intent.putExtra("price", total_price);
                intent.putExtra("flag_cart", "1");
                startActivity(intent);
                finish();
            }
        });
    }
}
