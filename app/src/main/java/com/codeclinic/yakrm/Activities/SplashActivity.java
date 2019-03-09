package com.codeclinic.yakrm.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Utils.SessionManager;

public class SplashActivity extends AppCompatActivity {

    SessionManager sessionManager;
    public static String language_name = "ar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sessionManager = new SessionManager(this);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sessionManager.isLoggedIn()) {
                    if (sessionManager.getUserDetails().get(SessionManager.UserType).equals("users")) {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        finish();
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SplashActivity.this, UploadVouchersActivity.class);
                        finish();
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(SplashActivity.this, StartActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        }, 2000);
    }
}
