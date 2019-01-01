package com.yakrm.codeclinic.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yakrm.codeclinic.Models.RegistrationModel;
import com.yakrm.codeclinic.Models.RegistrationStep2Model;
import com.yakrm.codeclinic.Models.VerifyOTPModel;
import com.yakrm.codeclinic.R;
import com.yakrm.codeclinic.Retrofit.API;
import com.yakrm.codeclinic.Retrofit.RestClass;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewAccountActivity extends AppCompatActivity {

    CardView main_detail_cardview, number_verify_cardview, personal_detail_cardview;
    Button btn_send, btn_sign_up_new, btn_verify;
    ImageView img_back;
    TextView tv_user_agree, tv_login;
    EditText edt_number, edt_1, edt_2, edt_3, edt_4, edt_name, edt_email, edt_password;
    API apiService;
    ProgressDialog progressDialog;
    JSONObject jsonObject_register = new JSONObject();
    JSONObject jsonObject_verify = new JSONObject();
    JSONObject jsonObject_signup = new JSONObject();

    String str_user_id, str_number, str_edt_1, str_edt_2, str_edt_3, str_edt_4, str_name, str_email, str_password;


    public boolean isEmpty(CharSequence character) {
        return character == null || character.length() == 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);


        main_detail_cardview = findViewById(R.id.main_detail_cardview);
        number_verify_cardview = findViewById(R.id.number_verify_cardview);
        personal_detail_cardview = findViewById(R.id.personal_detail_cardview);

        progressDialog = new ProgressDialog(NewAccountActivity.this);

        img_back = findViewById(R.id.img_back);
        apiService = RestClass.getClient().create(API.class);

        tv_user_agree = findViewById(R.id.tv_user_agree);
        tv_login = findViewById(R.id.tv_login);

        edt_number = findViewById(R.id.edt_number);
        edt_1 = findViewById(R.id.edt_1);
        edt_2 = findViewById(R.id.edt_2);
        edt_3 = findViewById(R.id.edt_3);
        edt_4 = findViewById(R.id.edt_4);
        edt_name = findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);

        btn_sign_up_new = findViewById(R.id.btn_sign_up_new);
        btn_verify = findViewById(R.id.btn_verify);
        btn_send = findViewById(R.id.btn_send);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_number = edt_number.getText().toString();
                if (isEmpty(str_number)) {
                    Toast.makeText(NewAccountActivity.this, "Enter Number", Toast.LENGTH_SHORT).show();
                } else if (str_number.length() < 10) {
                    Toast.makeText(NewAccountActivity.this, "Minimum character required is 10", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.setMessage("Please Wait");
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    try {
                        jsonObject_register.put("country_id", "1");
                        jsonObject_register.put("mobile", str_number);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Call<RegistrationModel> registrationModelCall = apiService.REGISTRATION_MODEL_CALL(jsonObject_register.toString());
                    registrationModelCall.enqueue(new Callback<RegistrationModel>() {
                        @Override
                        public void onResponse(Call<RegistrationModel> call, Response<RegistrationModel> response) {
                            progressDialog.dismiss();
                            if (response.body().getStatus().equals("1")) {
                                str_user_id = String.valueOf(response.body().getUserId());
                                main_detail_cardview.setVisibility(View.GONE);
                                number_verify_cardview.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(NewAccountActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<RegistrationModel> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(NewAccountActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(NewAccountActivity.this, "Enter Code", Toast.LENGTH_SHORT).show();
                } else if (isEmpty(str_edt_2)) {
                    Toast.makeText(NewAccountActivity.this, "Enter Code", Toast.LENGTH_SHORT).show();
                } else if (isEmpty(str_edt_3)) {
                    Toast.makeText(NewAccountActivity.this, "Enter Code", Toast.LENGTH_SHORT).show();
                } else if (isEmpty(str_edt_4)) {
                    Toast.makeText(NewAccountActivity.this, "Enter Code", Toast.LENGTH_SHORT).show();
                } else {

                    progressDialog.setMessage("Please Wait");
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    try {
                        jsonObject_verify.put("user_id", str_user_id);
                        jsonObject_verify.put("otp", str_edt_1 + str_edt_2 + str_edt_3 + str_edt_4);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Call<VerifyOTPModel> verifyOTPModelCall = apiService.VERIFY_OTP_MODEL_CALL(jsonObject_verify.toString());
                    verifyOTPModelCall.enqueue(new Callback<VerifyOTPModel>() {
                        @Override
                        public void onResponse(Call<VerifyOTPModel> call, Response<VerifyOTPModel> response) {
                            progressDialog.dismiss();
                            if (response.body().getStatus().equals("1")) {
                                str_user_id = response.body().getUserId();
                                number_verify_cardview.setVisibility(View.GONE);
                                personal_detail_cardview.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(NewAccountActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<VerifyOTPModel> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(NewAccountActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        btn_sign_up_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_name = edt_name.getText().toString();
                str_email = edt_email.getText().toString();
                str_password = edt_password.getText().toString();
                if (isEmpty(str_name)) {
                    Toast.makeText(NewAccountActivity.this, "Enter Name", Toast.LENGTH_SHORT).show();
                } else if (isEmpty(str_email)) {
                    Toast.makeText(NewAccountActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                } else if (isEmpty(str_password)) {
                    Toast.makeText(NewAccountActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.setMessage("Please Wait");
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    try {
                        jsonObject_signup.put("user_id", str_user_id);
                        jsonObject_signup.put("name", str_name);
                        jsonObject_signup.put("email", str_email);
                        jsonObject_signup.put("password", str_password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Call<RegistrationStep2Model> registrationStep2ModelCall = apiService.REGISTRATION_STEP_2_MODEL_CALL(jsonObject_signup.toString());
                    registrationStep2ModelCall.enqueue(new Callback<RegistrationStep2Model>() {
                        @Override
                        public void onResponse(Call<RegistrationStep2Model> call, Response<RegistrationStep2Model> response) {
                            progressDialog.dismiss();
                            if (response.body().getStatus().equals("1")) {
                                Intent intent = new Intent(NewAccountActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(NewAccountActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<RegistrationStep2Model> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(NewAccountActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        tv_user_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewAccountActivity.this, PrivayPolicyActivity.class));
            }
        });

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewAccountActivity.this, LoginActivity.class));
            }
        });
    }
}
