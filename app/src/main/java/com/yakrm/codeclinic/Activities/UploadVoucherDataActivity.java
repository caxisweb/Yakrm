package com.yakrm.codeclinic.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yakrm.codeclinic.R;


public class UploadVoucherDataActivity extends AppCompatActivity {

    LinearLayout layout_scan_img, llayout_scan, llayout_scan_detail, llayout_manual_voucher_details;
    Button btn_add_note1, btn_add_note2;
    TextView tv_item_name, tv_enter_manualy;
    ImageView img_back;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_voucher_data);

        tv_item_name = findViewById(R.id.tv_item_name);
        tv_enter_manualy = findViewById(R.id.tv_enter_manualy);

        llayout_manual_voucher_details = findViewById(R.id.llayout_manual_voucher_details);
        llayout_scan_detail = findViewById(R.id.llayout_scan_detail);
        llayout_scan = findViewById(R.id.llayout_scan);
        layout_scan_img = findViewById(R.id.layout_scan_img);

        img_back = findViewById(R.id.img_back);
        String language = String.valueOf(getResources().getConfiguration().locale);
        if (language.equals("ar")) {
            img_back.setImageDrawable(getResources().getDrawable(R.drawable.back_right_img));
        }
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_item_name.setText(getIntent().getStringExtra("name"));
        name = getIntent().getStringExtra("name");

        btn_add_note1 = findViewById(R.id.btn_add_note1);
        btn_add_note2 = findViewById(R.id.btn_add_note2);

        layout_scan_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llayout_scan.setVisibility(View.GONE);
                llayout_scan_detail.setVisibility(View.VISIBLE);
            }
        });

        btn_add_note1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llayout_scan_detail.setVisibility(View.GONE);
                llayout_manual_voucher_details.setVisibility(View.VISIBLE);
                Intent intent = new Intent(UploadVoucherDataActivity.this, MainActivity.class);
                intent.putExtra("view_pos", "4");
                startActivity(intent);
            }
        });

        btn_add_note2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llayout_manual_voucher_details.setVisibility(View.GONE);
                llayout_scan.setVisibility(View.VISIBLE);
                Intent intent = new Intent(UploadVoucherDataActivity.this, MainActivity.class);
                intent.putExtra("view_pos", "4");
                startActivity(intent);
            }
        });

        tv_enter_manualy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llayout_scan.setVisibility(View.GONE);
                llayout_manual_voucher_details.setVisibility(View.VISIBLE);
            }
        });
    }
}
