package com.codeclinic.yakrm.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.codeclinic.yakrm.Models.ChangePasswordModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.Connection_Detector;
import com.codeclinic.yakrm.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText edt_old_pass, edt_new_pass, edt_confm_new_pass;
    Button btn_change_pass;
    API apiService;
    SessionManager sessionManager;
    ProgressDialog progressDialog;
    JSONObject jsonObject = new JSONObject();

    ImageView img_back;

    public boolean isEmpty(CharSequence character) {
        return character == null || character.length() == 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        edt_old_pass = findViewById(R.id.edt_old_pass);
        edt_confm_new_pass = findViewById(R.id.edt_confm_new_pass);
        edt_new_pass = findViewById(R.id.edt_new_pass);
        btn_change_pass = findViewById(R.id.btn_change_pass);

        progressDialog = new ProgressDialog(this);
        sessionManager = new SessionManager(this);

        img_back = findViewById(R.id.img_back);
        String language = String.valueOf(getResources().getConfiguration().locale);
        if (language.equals("ar")) {
            img_back.setImageDrawable(getResources().getDrawable(R.drawable.back_right_img));
        }
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        apiService = RestClass.getClient().create(API.class);


        btn_change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty(edt_old_pass.getText().toString())) {
                    Toast.makeText(ChangePasswordActivity.this, "Please Enter Old Password", Toast.LENGTH_SHORT).show();
                } else if (isEmpty(edt_new_pass.getText().toString())) {
                    Toast.makeText(ChangePasswordActivity.this, "Please Enter New Password", Toast.LENGTH_SHORT).show();
                } else if (isEmpty(edt_confm_new_pass.getText().toString())) {
                    Toast.makeText(ChangePasswordActivity.this, "Please Confirm New Password", Toast.LENGTH_SHORT).show();
                } else if (!edt_confm_new_pass.getText().toString().matches(edt_new_pass.getText().toString())) {
                    Toast.makeText(ChangePasswordActivity.this, "Confirm Password not matched", Toast.LENGTH_SHORT).show();
                } else {

                    if (Connection_Detector.isInternetAvailable(ChangePasswordActivity.this)) {
                        progressDialog.setMessage("Please Wait");
                        progressDialog.setIndeterminate(true);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        try {
                            jsonObject.put("old_password", edt_old_pass.getText().toString());
                            jsonObject.put("new_password", edt_new_pass.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Call<ChangePasswordModel> changePasswordModelCall = apiService.CHANGE_PASSWORD_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), jsonObject.toString());
                        changePasswordModelCall.enqueue(new Callback<ChangePasswordModel>() {
                            @Override
                            public void onResponse(Call<ChangePasswordModel> call, Response<ChangePasswordModel> response) {
                                progressDialog.dismiss();
                                String status = response.body().getStatus();
                                if (status.equals("1")) {
                                    Toast.makeText(ChangePasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(ChangePasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ChangePasswordModel> call, Throwable t) {
                                progressDialog.dismiss();
                                Toast.makeText(ChangePasswordActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "No internet available", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}
