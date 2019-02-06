package com.codeclinic.yakrm.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codeclinic.yakrm.R;


public class ExchangeVoucherActivity extends AppCompatActivity {

    TextView tv_item_name;
    ImageView img_back;

    LinearLayout llayout_send_friend, llayout_add_balance, llayout_display_voucher;
    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_voucher);

        img_back = findViewById(R.id.img_back);
        String language = String.valueOf(getResources().getConfiguration().locale);
        if (language.equals("ar")) {
            img_back.setImageDrawable(getResources().getDrawable(R.drawable.back_right_img));
        }
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

        llayout_display_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LayoutInflater inflater = getLayoutInflater();
                dialogBuilder = new AlertDialog.Builder(ExchangeVoucherActivity.this);
                final View dialogView = inflater.inflate(R.layout.custom_voucher_display_view, null);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setCancelable(true);

                final Button btn_accept = dialogView.findViewById(R.id.btn_accept);

                alertDialog = dialogBuilder.create();
                alertDialog.show();

                btn_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ExchangeVoucherActivity.this, MainActivity.class);
                        intent.putExtra("view_pos", "3");
                        startActivity(intent);
                    }
                });
            }
        });

    }
}