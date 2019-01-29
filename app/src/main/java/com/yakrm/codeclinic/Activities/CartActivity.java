package com.yakrm.codeclinic.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yakrm.codeclinic.Adapter.CartlistAdapter;
import com.yakrm.codeclinic.Models.CartListItemModel;
import com.yakrm.codeclinic.Models.CartListModel;
import com.yakrm.codeclinic.R;
import com.yakrm.codeclinic.Retrofit.API;
import com.yakrm.codeclinic.Retrofit.RestClass;
import com.yakrm.codeclinic.Utils.Connection_Detector;
import com.yakrm.codeclinic.Utils.SessionManager;

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
    Button btn_pay;

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
            progressDialog.setMessage("Please Wait");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            Call<CartListModel> cartListModelCall = apiService.CART_LIST_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token));
            cartListModelCall.enqueue(new Callback<CartListModel>() {
                @Override
                public void onResponse(Call<CartListModel> call, Response<CartListModel> response) {
                    progressDialog.dismiss();
                    String status = response.body().getStatus();
                    if (status.equals("1")) {
                        tv_header_name.setText(getResources().getString(R.string.Cart));
                        tv_total_price.setText(String.valueOf(response.body().getTotalPrice()) + getResources().getString(R.string.SR_currency));
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
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, CompletingPurchasingActivity.class);
                intent.putExtra("price", total_price);
                startActivity(intent);
                finish();
            }
        });
    }
}
