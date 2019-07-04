package com.codeclinic.yakrm.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Utils.SessionManager;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class SplashActivity extends AppCompatActivity {

    SessionManager sessionManager;
    public static String language_name = "ar";

    public void forceCrash() {
        throw new RuntimeException("This is a crash");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);
        sessionManager = new SessionManager(this);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sessionManager.isLoggedIn()) {
                    if (sessionManager.getUserDetails().get(SessionManager.UserType).equals("users")) {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashActivity.this, UploadVouchersActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    if (sessionManager.isFirstTimeLaunch()) {
                        Intent intent = new Intent(SplashActivity.this, StartActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        }, 2000);
    }
}
