package com.yakrm.codeclinic.yakrm.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yakrm.codeclinic.yakrm.R;

public class NewAccountActivity extends AppCompatActivity {

    CardView main_detail_cardview, number_verify_cardview, personal_detail_cardview;
    Button btn_send, btn_sign_up_new, btn_verify;
    ImageView img_back;
    TextView tv_user_agree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);


        main_detail_cardview = findViewById(R.id.main_detail_cardview);
        number_verify_cardview = findViewById(R.id.number_verify_cardview);
        personal_detail_cardview = findViewById(R.id.personal_detail_cardview);

        img_back = findViewById(R.id.img_back);

        tv_user_agree = findViewById(R.id.tv_user_agree);

        btn_sign_up_new = findViewById(R.id.btn_sign_up_new);
        btn_verify = findViewById(R.id.btn_verify);
        btn_send = findViewById(R.id.btn_send);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_detail_cardview.setVisibility(View.GONE);
                number_verify_cardview.setVisibility(View.VISIBLE);
            }
        });

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number_verify_cardview.setVisibility(View.GONE);
                personal_detail_cardview.setVisibility(View.VISIBLE);
            }
        });

        btn_sign_up_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewAccountActivity.this, LoginActivity.class));
            }
        });

        tv_user_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewAccountActivity.this, ExcahangeInstructionsActivity.class));
            }
        });
    }
}
