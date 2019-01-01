package com.yakrm.codeclinic.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.yakrm.codeclinic.R;
import com.yakrm.codeclinic.Utils.SessionManager;

public class SplashActivity extends AppCompatActivity {

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sessionManager = new SessionManager(this);

   /*     Locale locale = new Locale("ar");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sessionManager.isLoggedIn()) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    finish();
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashActivity.this, StartActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        }, 2000);
    }
}
