package com.codeclinic.yakrm.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.codeclinic.yakrm.Models.LoginModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.SessionManager;
import com.franmontiel.localechanger.LocaleChanger;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    CardView main_login_cardview;
    Button btn_log_in;
    ImageView img_back;
    ProgressDialog progressDialog;

    EditText edt_email, edt_password;
    TextView tv_signup, tv_forget_pass;

    API apiService;
    JSONObject jsonObject = new JSONObject();

    String str_email, str_password;
    SessionManager sessionManager;
    String str_email_regex = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    public boolean isEmpty(CharSequence character) {
        return character == null || character.length() == 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(LoginActivity.this);

        main_login_cardview = findViewById(R.id.main_login_cardview);

        sessionManager = new SessionManager(this);

        btn_log_in = findViewById(R.id.btn_log_in);

        img_back = findViewById(R.id.img_back);
        String language = String.valueOf(getResources().getConfiguration().locale);
        if (language.equals("ar")) {
            img_back.setImageDrawable(getResources().getDrawable(R.drawable.back_right_img));
        }
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);

        tv_signup = findViewById(R.id.tv_signup);
        tv_forget_pass = findViewById(R.id.tv_forget_pass);
        tv_forget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
            }
        });

        apiService = RestClass.getClient().create(API.class);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_email = edt_email.getText().toString();
                str_password = edt_password.getText().toString();
                /*if (isEmpty(str_email)) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.Please_Enter_Email), Toast.LENGTH_SHORT).show();
                }*/
                if (isEmpty(str_email)) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.Please_Enter_Mobile_Number), Toast.LENGTH_SHORT).show();
                } else if (!str_email.substring(0, 2).equals("05")) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.Mobile_number_should_start_with_05), Toast.LENGTH_LONG).show();
                } else if (str_email.length() < 10) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.Mobile_Number_should_be_minimum), Toast.LENGTH_SHORT).show();
                } else if (isEmpty(str_password)) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.Please_Enter_Password), Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    try {
                        jsonObject.put("phone", str_email);
                        jsonObject.put("password", str_password);
                        jsonObject.put("gcm_id", FirebaseInstanceId.getInstance().getToken());
                        //jsonObject.put("gcm_id","1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Call<LoginModel> loginModelCall = apiService.LOGIN_MODEL_CALL(jsonObject.toString());
                    loginModelCall.enqueue(new Callback<LoginModel>() {
                        @Override
                        public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                            progressDialog.dismiss();
                            if (response.body().getStatus().equals("1")) {
                                sessionManager.createLoginSession(response.body().getToken(), response.body().getUserId(), response.body().getName(), response.body().getEmail(), response.body().getPhone(), response.body().getCountryId(), response.body().getUser_profile(), response.body().getWallet(), response.body().getUser_type());//else salesmen
                                if (response.body().getUser_type().equals("salesmen")) {
                                    startActivity(new Intent(LoginActivity.this, UploadVouchersActivity.class));
                                } else {
                                    startActivity(new Intent(LoginActivity.this, SelectAppModeActivity.class));
                                }
                                finish();
                            } else {
                                if (sessionManager.getLanguage("Langauage", "en").equals("en")) {
                                    Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, response.body().getArab_message(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginModel> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.Server_Error), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, NewAccountActivity.class));
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleChanger.configureBaseContext(newBase);
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityRecreationHelper.onResume(this);
    }

    @Override
    protected void onDestroy() {
        ActivityRecreationHelper.onDestroy(this);
        super.onDestroy();
    }
}
