package com.yakrm.codeclinic.yakrm.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yakrm.codeclinic.yakrm.R;

public class VoucherDetailActivity extends AppCompatActivity {

    TextView tv_header_name;
    ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_detail);

        tv_header_name = findViewById(R.id.tv_header_name);
        img_back = findViewById(R.id.img_back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_header_name.setText(getIntent().getStringExtra("name"));


    }
}
