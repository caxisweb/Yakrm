package com.yakrm.codeclinic.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yakrm.codeclinic.Adapter.MyWalletAdapter;
import com.yakrm.codeclinic.R;

import java.util.ArrayList;

public class MyWalletActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView img_back;
    MyWalletAdapter myWalletAdapter;
    ArrayList<String> arrayList = new ArrayList<>();
    LinearLayout llayout_voucher_ended;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);

        llayout_voucher_ended = findViewById(R.id.llayout_voucher_ended);
        recyclerView = findViewById(R.id.recyclerView);
        img_back = findViewById(R.id.img_back);

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

        arrayList.add("STARBUCKSCARD");
        arrayList.add("Coca Cola");
        arrayList.add("Adidas Inc.");
        arrayList.add("Xbox");

        myWalletAdapter = new MyWalletAdapter(arrayList, this);
        recyclerView.setAdapter(myWalletAdapter);

        llayout_voucher_ended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyWalletActivity.this, VoucherEndedActivity.class));
            }
        });

    }
}
