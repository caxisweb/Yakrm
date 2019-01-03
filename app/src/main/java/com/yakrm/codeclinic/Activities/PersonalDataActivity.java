package com.yakrm.codeclinic.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yakrm.codeclinic.R;
import com.yakrm.codeclinic.Utils.SessionManager;

public class PersonalDataActivity extends AppCompatActivity {
    ImageView img_back;
    Button btn_modify_data;
    TextView tv_mobile, tv_email, tv_username;
    String str_mobile, str_email;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);

        sessionManager = new SessionManager(this);

        img_back = findViewById(R.id.img_back);

        btn_modify_data = findViewById(R.id.btn_modify_data);

        tv_mobile = findViewById(R.id.tv_mobile);
        tv_email = findViewById(R.id.tv_email);
        tv_username = findViewById(R.id.tv_username);

        tv_username.setText(sessionManager.getUserDetails().get(SessionManager.User_Name));
        tv_mobile.setText(sessionManager.getUserDetails().get(SessionManager.USER_MOBILE));
        tv_email.setText(sessionManager.getUserDetails().get(SessionManager.User_Email));

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_modify_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(PersonalDataActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}
