package com.yakrm.codeclinic.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yakrm.codeclinic.R;
import com.yakrm.codeclinic.Utils.ImageURL;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class VoucherDetailActivity extends AppCompatActivity {

    TextView tv_header_name, tv_expiredate, tv_price, tv_barcode, tv_pincode;
    ImageView img_back, img_voucher;
    Button btn_done;
    String date, final_date, barcode, pincode, price, v_image;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_detail);

        tv_header_name = findViewById(R.id.tv_header_name);
        img_voucher = findViewById(R.id.img_voucher);
        img_back = findViewById(R.id.img_back);
        btn_done = findViewById(R.id.btn_done);
        tv_expiredate = findViewById(R.id.tv_expiredate);
        tv_price = findViewById(R.id.tv_price);
        tv_barcode = findViewById(R.id.tv_barcode);
        tv_pincode = findViewById(R.id.tv_pincode);

        date = getIntent().getStringExtra("date");
        barcode = getIntent().getStringExtra("barcode");
        pincode = getIntent().getStringExtra("pincode");
        price = getIntent().getStringExtra("price");
        v_image = getIntent().getStringExtra("v_image");

        Picasso.with(this).load(ImageURL.Vendor_voucher_image + v_image).into(img_voucher);

        SimpleDateFormat spf = null;
        Date newDate = null;
        try {
            String dates = date.substring(0, date.indexOf(" "));
            final_date = dates.trim();
            spf = new SimpleDateFormat("yyyy-MM-dd");
            newDate = spf.parse(final_date);
            spf = new SimpleDateFormat("dd-MMM-yyyy");
            final_date = spf.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tv_expiredate.setText(final_date);
        tv_price.setText(price + " " + getResources().getString(R.string.SR_currency));
        tv_pincode.setText(pincode);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String language = String.valueOf(getResources().getConfiguration().locale);
        if (language.equals("ar")) {
            img_back.setImageDrawable(getResources().getDrawable(R.drawable.back_right_img));
        }
        tv_header_name.setText(getIntent().getStringExtra("name"));

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VoucherDetailActivity.this, ExchangeVoucherActivity.class));
            }
        });


    }
}
