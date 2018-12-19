package com.yakrm.codeclinic.yakrm.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.yakrm.codeclinic.yakrm.R;

public class PersonalDataActivity extends AppCompatActivity {
    ImageView img_back;
    Button btn_modify_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);

        img_back = findViewById(R.id.img_back);
        btn_modify_data = findViewById(R.id.btn_modify_data);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_modify_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PersonalDataActivity.this, MainActivity.class));
            }
        });
    }
}
