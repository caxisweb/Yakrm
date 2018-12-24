package com.yakrm.codeclinic.yakrm.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.yakrm.codeclinic.yakrm.Adapter.VoucherWillEndAdapter;
import com.yakrm.codeclinic.yakrm.R;

import java.util.ArrayList;

public class VoucherEndedActivity extends AppCompatActivity {
    ArrayList arrayList = new ArrayList();
    RecyclerView recyclerView;
    ImageView img_back;
    VoucherWillEndAdapter voucherWillEndAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_ended);


        img_back = findViewById(R.id.img_back);
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

        arrayList.add("STABUCKSCARD");
        arrayList.add("Coca Cola ");
        arrayList.add("Adidas Inc. ");
        arrayList.add("XBOX");
        arrayList.add("STABUCKSCARD");

        voucherWillEndAdapter = new VoucherWillEndAdapter(arrayList, this);
        recyclerView.setAdapter(voucherWillEndAdapter);
    }
}
