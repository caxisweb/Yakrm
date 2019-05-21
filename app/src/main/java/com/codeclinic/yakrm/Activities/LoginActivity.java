package com.codeclinic.yakrm.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codeclinic.yakrm.Models.LoginModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.SessionManager;
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
                if (isEmpty(str_email)) {
                    Toast.makeText(LoginActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                } else if (isEmpty(str_password)) {
                    Toast.makeText(LoginActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.setMessage("Please Wait");
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    try {
                        jsonObject.put("email", str_email);
                        jsonObject.put("password", str_password);
                        jsonObject.put("gcm_id", FirebaseInstanceId.getInstance().getToken());
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
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                }

                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginModel> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
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
}
