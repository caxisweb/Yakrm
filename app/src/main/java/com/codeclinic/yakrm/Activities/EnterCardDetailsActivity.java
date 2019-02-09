package com.codeclinic.yakrm.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.codeclinic.yakrm.R;

public class EnterCardDetailsActivity extends AppCompatActivity {
    ImageView img_back, img_mada_select, img_paypal_select, img_visa_select;
    EditText edt_ex_date;
    String card_select = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_card_details);

        edt_ex_date = findViewById(R.id.edt_ex_date);
        img_back = findViewById(R.id.img_back);

        img_mada_select = findViewById(R.id.img_mada_select);
        img_paypal_select = findViewById(R.id.img_paypal_select);
        img_visa_select = findViewById(R.id.img_visa_select);

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

        img_visa_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_visa_select.setBackground(getResources().getDrawable(R.drawable.card_selected_background));
                img_paypal_select.setBackground(getResources().getDrawable(R.drawable.card_details_item_background));
                img_mada_select.setBackground(getResources().getDrawable(R.drawable.card_details_item_background));
            }
        });

        img_paypal_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_visa_select.setBackground(getResources().getDrawable(R.drawable.card_details_item_background));
                img_paypal_select.setBackground(getResources().getDrawable(R.drawable.card_selected_background));
                img_mada_select.setBackground(getResources().getDrawable(R.drawable.card_details_item_background));
            }
        });

        img_mada_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_visa_select.setBackground(getResources().getDrawable(R.drawable.card_details_item_background));
                img_paypal_select.setBackground(getResources().getDrawable(R.drawable.card_details_item_background));
                img_mada_select.setBackground(getResources().getDrawable(R.drawable.card_selected_background));
            }
        });

    }
}
