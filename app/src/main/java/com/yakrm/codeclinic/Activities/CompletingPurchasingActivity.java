package com.yakrm.codeclinic.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.yakrm.codeclinic.R;


public class CompletingPurchasingActivity extends AppCompatActivity {

    CardView main_pay_cardview, succesful_cardview, error_cardview;
    Button btn_cmplt_pay;
    ScrollView scrollview_pay;

    LinearLayout payment_layout;

    ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completing_purchasing);

        img_back = findViewById(R.id.img_back);
        scrollview_pay = findViewById(R.id.scrollview_pay);
        btn_cmplt_pay = findViewById(R.id.btn_cmplt_pay);
        succesful_cardview = findViewById(R.id.succesful_cardview);
        error_cardview = findViewById(R.id.error_cardview);
        main_pay_cardview = findViewById(R.id.main_pay_cardview);
        payment_layout = findViewById(R.id.payment_layout);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        main_pay_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_pay_cardview.setVisibility(View.GONE);
                succesful_cardview.setVisibility(View.VISIBLE);
            }
        });

        succesful_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                succesful_cardview.setVisibility(View.GONE);
                error_cardview.setVisibility(View.VISIBLE);
            }
        });

        error_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error_cardview.setVisibility(View.GONE);
                scrollview_pay.setVisibility(View.VISIBLE);
            }
        });
        payment_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollview_pay.setVisibility(View.GONE);
                main_pay_cardview.setVisibility(View.VISIBLE);
                startActivity(new Intent(CompletingPurchasingActivity.this, RecievedActivity.class));
            }
        });

    }
}
