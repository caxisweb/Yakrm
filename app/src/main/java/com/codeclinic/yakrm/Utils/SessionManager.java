package com.codeclinic.yakrm.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.codeclinic.yakrm.Activities.LoginActivity;

import java.util.HashMap;

/**
 * Created by Inforaam on 6/29/2017.
 */

public class SessionManager {

    public static final String User_ID = "user_id";
    public static final String User_Token = "user_token";
    public static final String User_Name = "user_name";
    public static final String User_Email = "user_email";
    public static final String USER_MOBILE = "user_mobile";
    public static final String USER_COUNTRY_ID = "user_country_id";
    public static final String USER_Profile = "user_profile";
    public static final String Wallet = "wallet";
    public static final String UserType = "userType";


    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    // Sharedpref file name
    private static final String PREF_NAME = "Yakrm";
    private static final String PREF_NAME_FIRST = "Yakrmfirst";
    // All Shared Preferences Keys-
    private static final String IS_LOGIN = "IsLoggedIn";
    // Shared Preferences
    SharedPreferences pref;
    SharedPreferences pref1;
    // Editor for Shared preferences
    private Editor editor;
    private Editor editor1;
    // Context
    private Context _context;
    // Shared pref mode
    private int PRIVATE_MODE = 0;

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        pref1 = _context.getSharedPreferences(PREF_NAME_FIRST, PRIVATE_MODE);
        editor = pref.edit();
        editor1 = pref1.edit();
    }

    public Editor getEditor() {
        return this.pref.edit();
    }

    public void putLanguage(String key, String value) {
        getEditor().putString(key, value).commit();
    }

    public String getLanguage(String key, String defValue) {
        return this.pref.getString(key, defValue);
    }

    public boolean isFirstTimeLaunch() {
        return pref1.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor1.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor1.commit();
    }


    public void createLoginSession(String token, String id, String name, String email, String number, String user_country_id, String user_profile, String str_wallet, String str_userType) {
        // Storing login value as TRUE
        try {
            editor.putBoolean(IS_LOGIN, true);
            // Storing name in pref
            editor.putString(User_ID, id);
            editor.putString(User_Name, name);
            editor.putString(User_Token, token);
            editor.putString(User_Email, email);
            editor.putString(USER_MOBILE, number);
            editor.putString(USER_COUNTRY_ID, user_country_id);
            editor.putString(USER_Profile, user_profile);
            editor.putString(Wallet, str_wallet);
            editor.putString(UserType, str_userType);

            // commit changes
            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public HashMap<String, String> getUserDetails() {

        HashMap<String, String> user = new HashMap<>();

        user.put(User_ID, pref.getString(User_ID, null));
        user.put(User_Token, pref.getString(User_Token, null));
        user.put(User_Name, pref.getString(User_Name, null));
        user.put(User_Email, pref.getString(User_Email, null));
        user.put(USER_MOBILE, pref.getString(USER_MOBILE, null));
        user.put(USER_COUNTRY_ID, pref.getString(USER_COUNTRY_ID, null));
        user.put(USER_Profile, pref.getString(USER_Profile, null));
        user.put(Wallet, pref.getString(Wallet, null));
        user.put(UserType, pref.getString(UserType, null));

        return user;
    }


    public int checkLogin() {
        // Check login status

        int flag;

        if (!this.isLoggedIn()) {
            flag = 0;
        } else {
            flag = 1;
        }

        return flag;
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }


    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
        ((Activity) _context).finish();
    }

    public void clearsession() {
        editor.clear();
        editor.commit();
    }


}
