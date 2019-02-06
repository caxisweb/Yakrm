package com.codeclinic.yakrm.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.codeclinic.yakrm.Activities.StartActivity;

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


    // Sharedpref file name
    private static final String PREF_NAME = "Yakrm";
    // All Shared Preferences Keys-
    private static final String IS_LOGIN = "IsLoggedIn";
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    private Editor editor;
    // Context
    private Context _context;
    // Shared pref mode
    private int PRIVATE_MODE = 0;


    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
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

    public void createLoginSession(String token, String id, String name, String email, String number, String user_country_id, String user_profile) {
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
        Intent i = new Intent(_context, StartActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    public void clearsession() {
        editor.clear();
        editor.commit();
    }


}
