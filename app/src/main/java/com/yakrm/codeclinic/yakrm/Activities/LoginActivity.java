package com.yakrm.codeclinic.yakrm.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;

import com.yakrm.codeclinic.yakrm.R;

public class LoginActivity extends AppCompatActivity {

    CardView main_login_cardview, number_verify_cardview;
    Button btn_log_in, btn_verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        main_login_cardview = findViewById(R.id.main_login_cardview);
        number_verify_cardview = findViewById(R.id.number_verify_cardview);


        btn_verify = findViewById(R.id.btn_verify);
        btn_log_in = findViewById(R.id.btn_log_in);

        btn_log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_login_cardview.setVisibility(View.GONE);
                number_verify_cardview.setVisibility(View.VISIBLE);
            }
        });

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, PersonalDataActivity.class));
            }
        });

    }
}
