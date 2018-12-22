package com.yakrm.codeclinic.yakrm.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yakrm.codeclinic.yakrm.R;

public class ExchangeVoucherActivity extends AppCompatActivity {

    TextView tv_item_name;
    ImageView img_back;

    LinearLayout llayout_send_friend, llayout_add_balance, llayout_display_voucher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_voucher);

        img_back = findViewById(R.id.img_back);
        tv_item_name = findViewById(R.id.tv_item_name);

        llayout_send_friend = findViewById(R.id.llayout_send_friend);
        llayout_add_balance = findViewById(R.id.llayout_add_balance);
        llayout_display_voucher = findViewById(R.id.llayout_display_voucher);

        tv_item_name.setText(getIntent().getStringExtra("name"));

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        llayout_send_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExchangeVoucherActivity.this, SendToFriendActivity.class));
            }
        });

        llayout_add_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExchangeVoucherActivity.this, ExchangeAddBalanceActivity.class));
            }
        });

    }
}
