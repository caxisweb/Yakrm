package com.codeclinic.yakrm.Utils;

import android.app.Application;
import android.content.res.Configuration;

import com.franmontiel.localechanger.LocaleChanger;

import static com.codeclinic.yakrm.Utils.CommonMethods.SUPPORTED_LOCALES;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LocaleChanger.initialize(getApplicationContext(), SUPPORTED_LOCALES);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleChanger.onConfigurationChanged();
    }

}
