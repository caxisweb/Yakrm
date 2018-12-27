package com.yakrm.codeclinic.yakrm.Activities;

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

import com.yakrm.codeclinic.yakrm.Adapter.CartlistAdapter;
import com.yakrm.codeclinic.yakrm.R;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView img_back;
    ArrayList<String> arrayList = new ArrayList<>();
    CartlistAdapter cartlistAdapter;

    TextView tv_header_name;
    RelativeLayout rl_cart_filled, rl_empty_layout;

    Button btn_pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        img_back = findViewById(R.id.img_back);
        tv_header_name = findViewById(R.id.tv_header_name);
        recyclerView = findViewById(R.id.recyclerView);
        rl_cart_filled = findViewById(R.id.rl_cart_filled);
        rl_empty_layout = findViewById(R.id.rl_empty_layout);
        btn_pay = findViewById(R.id.btn_pay);
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


        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");

        tv_header_name.setText("(Five Elements) the basket");

        if (arrayList.size() == 0) {
            rl_cart_filled.setVisibility(View.GONE);
            rl_empty_layout.setVisibility(View.VISIBLE);
        } else {
            rl_cart_filled.setVisibility(View.VISIBLE);
            rl_empty_layout.setVisibility(View.GONE);
        }

        cartlistAdapter = new CartlistAdapter(arrayList, this);
        recyclerView.setAdapter(cartlistAdapter);

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, CompletingPurchasingActivity.class));
            }
        });
    }
}
