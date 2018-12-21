package com.yakrm.codeclinic.yakrm.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

    Button btn_pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        img_back = findViewById(R.id.img_back);
        tv_header_name = findViewById(R.id.tv_header_name);
        recyclerView = findViewById(R.id.recyclerView);
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
