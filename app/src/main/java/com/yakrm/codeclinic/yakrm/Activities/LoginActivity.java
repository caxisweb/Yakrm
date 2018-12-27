package com.yakrm.codeclinic.yakrm.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.yakrm.codeclinic.yakrm.R;

public class LoginActivity extends AppCompatActivity {

    CardView main_login_cardview;
    Button btn_log_in;
    ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        main_login_cardview = findViewById(R.id.main_login_cardview);
        btn_log_in = findViewById(R.id.btn_log_in);
        img_back = findViewById(R.id.img_back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, PersonalDataActivity.class));
            }
        });

    }
}
