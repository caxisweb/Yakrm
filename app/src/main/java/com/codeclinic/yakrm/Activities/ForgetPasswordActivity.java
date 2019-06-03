package com.codeclinic.yakrm.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.codeclinic.yakrm.Models.ForgetPasswordNumberModel;
import com.codeclinic.yakrm.Models.ForgotPasswordOTPModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity {
    CardView main_detail_cardview, number_verify_cardview, main_change_pass_cardview;
    Button btn_send, btn_verify, btn_change_pass;
    ImageView img_back;
    EditText edt_number, edt_1, edt_2, edt_3, edt_4, edt_new_pass, edt_confm_new_pass;
    API apiService;
    ProgressDialog progressDialog;
    JSONObject jsonObject_register = new JSONObject();
    JSONObject jsonObject_verify = new JSONObject();

    String str_otp, str_user_token, str_number, str_edt_1, str_edt_2, str_edt_3, str_edt_4;

    SessionManager sessionManager;


    public boolean isEmpty(CharSequence character) {
        return character == null || character.length() == 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        main_detail_cardview = findViewById(R.id.main_detail_cardview);
        number_verify_cardview = findViewById(R.id.number_verify_cardview);
        main_change_pass_cardview = findViewById(R.id.main_change_pass_cardview);

        progressDialog = new ProgressDialog(ForgetPasswordActivity.this);
        sessionManager = new SessionManager(this);

        img_back = findViewById(R.id.img_back);
        String language = String.valueOf(getResources().getConfiguration().locale);
        if (language.equals("ar")) {
            img_back.setImageDrawable(getResources().getDrawable(R.drawable.back_right_img));
        }
        apiService = RestClass.getClient().create(API.class);


        edt_number = findViewById(R.id.edt_number);
        edt_1 = findViewById(R.id.edt_1);
        edt_2 = findViewById(R.id.edt_2);
        edt_3 = findViewById(R.id.edt_3);
        edt_4 = findViewById(R.id.edt_4);
        edt_new_pass = findViewById(R.id.edt_new_pass);
        edt_confm_new_pass = findViewById(R.id.edt_confm_new_pass);

        btn_verify = findViewById(R.id.btn_verify);
        btn_send = findViewById(R.id.btn_send);
        btn_change_pass = findViewById(R.id.btn_change_pass);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edt_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edt_number.getText().toString()
                        .replaceAll("1", getResources().getString(R.string.one));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_number = edt_number.getText().toString();
                if (isEmpty(str_number)) {
                    Toast.makeText(ForgetPasswordActivity.this, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                } else if (str_number.length() < 10) {
                    Toast.makeText(ForgetPasswordActivity.this, "Mobile Number should be minimum of 10 characters ", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.setMessage("Please Wait");
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    try {
                        jsonObject_register.put("phone", str_number);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Call<ForgetPasswordNumberModel> forgetPasswordNumberModelCall = apiService.FORGET_PASSWORD_NUMBER_MODEL_CALL(jsonObject_register.toString());
                    forgetPasswordNumberModelCall.enqueue(new Callback<ForgetPasswordNumberModel>() {
                        @Override
                        public void onResponse(Call<ForgetPasswordNumberModel> call, Response<ForgetPasswordNumberModel> response) {
                            progressDialog.dismiss();
                            if (response.body().getStatus().equals("3")) {
                                str_otp = String.valueOf(response.body().getOtp());
                                str_user_token = String.valueOf(response.body().getToken());
                                main_detail_cardview.setVisibility(View.GONE);
                                number_verify_cardview.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(ForgetPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ForgetPasswordNumberModel> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(ForgetPasswordActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        edt_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_1.getText().toString().length() == 1) {
                    edt_2.requestFocus();
                    //editText2.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edt_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_2.getText().toString().length() == 1) {
                    edt_3.requestFocus();
                    //editText3.setText("");
                } else {
                    edt_1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edt_3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_3.getText().toString().length() == 1) {
                    edt_4.requestFocus();
                    //editText4.setText("");
                } else {
                    edt_2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edt_4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_4.getText().toString().length() == 1) {
                    //editText1.requestFocus();
                } else {
                    edt_3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_edt_1 = edt_1.getText().toString();
                str_edt_2 = edt_2.getText().toString();
                str_edt_3 = edt_3.getText().toString();
                str_edt_4 = edt_4.getText().toString();

                if (isEmpty(str_edt_1)) {
                    Toast.makeText(ForgetPasswordActivity.this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
                } else if (isEmpty(str_edt_2)) {
                    Toast.makeText(ForgetPasswordActivity.this, "Enter Code", Toast.LENGTH_SHORT).show();
                } else if (isEmpty(str_edt_3)) {
                    Toast.makeText(ForgetPasswordActivity.this, "Enter Code", Toast.LENGTH_SHORT).show();
                } else if (isEmpty(str_edt_4)) {
                    Toast.makeText(ForgetPasswordActivity.this, "Enter Code", Toast.LENGTH_SHORT).show();
                } else {
                    String temp_otp = str_edt_1 + str_edt_2 + str_edt_3 + str_edt_4;
                    if (temp_otp.equals(str_otp)) {
                        number_verify_cardview.setVisibility(View.GONE);
                        main_change_pass_cardview.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(ForgetPasswordActivity.this, "OTP do not match", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        btn_change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty(edt_new_pass.getText().toString())) {
                    Toast.makeText(ForgetPasswordActivity.this, "Please Enter New Password", Toast.LENGTH_SHORT).show();
                } else if (isEmpty(edt_confm_new_pass.getText().toString())) {
                    Toast.makeText(ForgetPasswordActivity.this, "Please Enter Confirm Password", Toast.LENGTH_SHORT).show();
                } else if (!edt_confm_new_pass.getText().toString().matches(edt_confm_new_pass.getText().toString())) {
                    Toast.makeText(ForgetPasswordActivity.this, "Please Enter correct Password", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.setMessage("Please Wait");
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("password", edt_new_pass.getText().toString());
                        jsonObject.put("otp", str_otp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Call<ForgotPasswordOTPModel> forgotPasswordOTPModelCall = apiService.FORGOT_PASSWORD_OTP_MODEL_CALL(str_user_token, jsonObject.toString());
                    forgotPasswordOTPModelCall.enqueue(new Callback<ForgotPasswordOTPModel>() {
                        @Override
                        public void onResponse(Call<ForgotPasswordOTPModel> call, Response<ForgotPasswordOTPModel> response) {
                            progressDialog.dismiss();
                            if (response.body().getStatus().equals("1")) {
                                finish();
                            } else {
                                Toast.makeText(ForgetPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ForgotPasswordOTPModel> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(ForgetPasswordActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        });
    }
}