package com.codeclinic.yakrm.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.codeclinic.yakrm.DeliveryService.DeliveryMain;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Utils.SessionManager;

import java.util.Locale;

public class SelectAppModeActivity extends AppCompatActivity {

    SessionManager sessionManager;

    Button btn_voucher,btn_delivery;
    TextView tv_english,tv_arbic;
    ImageView img_back;

    @SuppressLint({"ResourceAsColor", "NewApi"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appmode_selection);

        sessionManager=new SessionManager(this);

        img_back=findViewById(R.id.img_back);

        String language = String.valueOf(getResources().getConfiguration().locale);
        if (language.equals("ar")) {
            img_back.setImageDrawable(getResources().getDrawable(R.drawable.back_right_img));
        }

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_delivery=findViewById(R.id.btn_delivery);
        btn_voucher=findViewById(R.id.btn_voucher);

        tv_english=findViewById(R.id.tv_english);
        tv_arbic=findViewById(R.id.tv_arbic);

        btn_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(SelectAppModeActivity.this, DeliveryMain.class));
            }
        });

        btn_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(SelectAppModeActivity.this, MainActivity.class));
            }
        });

        tv_english.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"ResourceAsColor", "NewApi"})
            @Override
            public void onClick(View view) {

                tv_english.setBackgroundColor(getApplicationContext().getColor(R.color.colorPrimary));
                tv_english.setTextColor(getApplicationContext().getColor(R.color.white));

                tv_arbic.setBackgroundColor(getApplicationContext().getColor(R.color.white));
                tv_arbic.setTextColor(getApplicationContext().getColor(R.color.colorPrimary));

                sessionManager.putLanguage("Language", "en");
                Locale locale = new Locale("en");
                Resources resources = getResources();
                Configuration configuration = resources.getConfiguration();
                DisplayMetrics displayMetrics = resources.getDisplayMetrics();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
                    configuration.setLocale(locale);
                } else{
                    configuration.locale=locale;
                }
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N){
                    getApplicationContext().createConfigurationContext(configuration);
                } else {
                    resources.updateConfiguration(configuration,displayMetrics);
                }

                finish();
                startActivity(new Intent(getApplicationContext(), SelectAppModeActivity.class));
            }
        });

        tv_arbic.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"ResourceAsColor", "NewApi"})
            @Override
            public void onClick(View view) {

                tv_arbic.setBackgroundColor(getApplicationContext().getColor(R.color.colorPrimary));
                tv_arbic.setTextColor(getApplicationContext().getColor(R.color.white));

                tv_english.setBackgroundColor(getApplicationContext().getColor(R.color.white));
                tv_english.setTextColor(getApplicationContext().getColor(R.color.colorPrimary));

                sessionManager.putLanguage("Language", "ar");
                Locale locale = new Locale("ar");
                Resources resources = getResources();
                Configuration configuration = resources.getConfiguration();
                DisplayMetrics displayMetrics = resources.getDisplayMetrics();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
                    configuration.setLocale(locale);
                } else{
                    configuration.locale=locale;
                }
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N){
                    getApplicationContext().createConfigurationContext(configuration);
                } else {
                    resources.updateConfiguration(configuration,displayMetrics);
                }
                /*Locale locale = new Locale("ar");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());*/
                finish();
                startActivity(new Intent(getApplicationContext(), SelectAppModeActivity.class));
            }
        });

        //String language = String.valueOf(getResources().getConfiguration().locale);
        if (language.equals("ar")) {

            tv_arbic.setBackgroundColor(getApplicationContext().getColor(R.color.colorPrimary));
            tv_arbic.setTextColor(getApplicationContext().getColor(R.color.white));

            tv_english.setBackgroundColor(getApplicationContext().getColor(R.color.white));
            tv_english.setTextColor(getApplicationContext().getColor(R.color.colorPrimary));

        }else{

            tv_arbic.setBackgroundColor(getApplicationContext().getColor(R.color.white));
            tv_arbic.setTextColor(getApplicationContext().getColor(R.color.colorPrimary));

            tv_english.setBackgroundColor(getApplicationContext().getColor(R.color.colorPrimary));
            tv_english.setTextColor(getApplicationContext().getColor(R.color.white));
        }
    }
}
