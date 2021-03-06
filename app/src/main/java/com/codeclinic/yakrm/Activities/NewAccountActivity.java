package com.codeclinic.yakrm.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustEvent;
import com.codeclinic.yakrm.Models.CountryListItemModel;
import com.codeclinic.yakrm.Models.CountryListModel;
import com.codeclinic.yakrm.Models.RegistrationModel;
import com.codeclinic.yakrm.Models.RegistrationStep2Model;
import com.codeclinic.yakrm.Models.VerifyOTPModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.Connection_Detector;
import com.codeclinic.yakrm.Utils.SessionManager;
import com.franmontiel.localechanger.LocaleChanger;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewAccountActivity extends AppCompatActivity {

    CardView main_detail_cardview, number_verify_cardview, personal_detail_cardview;
    Button btn_send, btn_sign_up_new, btn_verify, btn_resend;
    ImageView img_back;
    TextView tv_user_agree, tv_change_number, tv_min;
    EditText edt_number, edt_1, edt_2, edt_3, edt_4, edt_name, edt_email, edt_password;
    Spinner sp_country;


    API apiService;
    ProgressDialog progressDialog;
    JSONObject jsonObject_register = new JSONObject();
    JSONObject jsonObject_verify = new JSONObject();
    JSONObject jsonObject_signup = new JSONObject();
    JSONObject jsonObject_country = new JSONObject();

    String str_email_regex = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    String str_country_code, str_otp, str_user_token, str_number, str_edt_1, str_edt_2, str_edt_3, str_edt_4, str_name, str_email, str_password;

    SessionManager sessionManager;
    String language;
    CountDownTimer countDownTimer;
    List<CountryListItemModel> arrayList_country = new ArrayList<>();
    ArrayList<String> arrayList_country_name = new ArrayList<>();
    ArrayList<String> arrayList_country_id = new ArrayList<>();


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
        tv_min = findViewById(R.id.tv_min);
        sp_country = findViewById(R.id.sp_country);
        progressDialog = new ProgressDialog(NewAccountActivity.this);
        sessionManager = new SessionManager(this);

        img_back = findViewById(R.id.img_back);
        language = String.valueOf(getResources().getConfiguration().locale);
        if (language.equals("ar")) {
            img_back.setImageDrawable(getResources().getDrawable(R.drawable.back_right_img));
        }
        apiService = RestClass.getClient().create(API.class);

        tv_user_agree = findViewById(R.id.tv_user_agree);
        tv_change_number = findViewById(R.id.tv_change_number);
        String textToHighlight = getResources().getString(R.string.Usage_Agreememnt);
        String replacedWith = "<font color='red'>" + textToHighlight + "</font>";
        String originalString = tv_user_agree.getText().toString();
        String modifiedString = originalString.replaceAll(textToHighlight, replacedWith);
        tv_user_agree.setText(Html.fromHtml(modifiedString));
        String textToHighlight2 = getResources().getString(R.string.privacy_policy);
        String replacedWith2 = "<font color='red'>" + textToHighlight2 + "</font>";
        String modifiedString2 = modifiedString.replaceAll(textToHighlight2, replacedWith2);
        tv_user_agree.setText(Html.fromHtml(modifiedString2));

        String textHighlight = getResources().getString(R.string.change_the_number);
        String replace = "<font color='red'>" + textHighlight + "</font>";
        String main_string = tv_change_number.getText().toString();
        String modifyString = main_string.replaceAll(textHighlight, replace);
        tv_change_number.setText(Html.fromHtml(modifyString));

        tv_change_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_detail_cardview.setVisibility(View.VISIBLE);
                number_verify_cardview.setVisibility(View.GONE);
            }
        });

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
        btn_resend = findViewById(R.id.btn_resend);

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
                       /* .replaceAll("2", getResources().getString(R.string.two))
                        .replaceAll("3", getResources().getString(R.string.three))
                        .replaceAll("4", getResources().getString(R.string.four))
                        .replaceAll("5", getResources().getString(R.string.five))
                        .replaceAll("6", getResources().getString(R.string.six))
                        .replaceAll("7", getResources().getString(R.string.seven))
                        .replaceAll("8", getResources().getString(R.string.eight))
                        .replaceAll("9", getResources().getString(R.string.nine))
                        .replaceAll("0", getResources().getString(R.string.zero));*/
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
                    Toast.makeText(NewAccountActivity.this, getResources().getString(R.string.Please_Enter_Mobile_Number), Toast.LENGTH_SHORT).show();
                } else if (!str_number.substring(0, 2).equals("05")) {
                    Toast.makeText(NewAccountActivity.this, getResources().getString(R.string.Mobile_number_should_start_with_05), Toast.LENGTH_LONG).show();
                } else if (str_number.length() < 10) {
                    Toast.makeText(NewAccountActivity.this, getResources().getString(R.string.Mobile_Number_should_be_minimum), Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    try {
                        jsonObject_register.put("country_id", str_country_code);
                        jsonObject_register.put("phone", str_number);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.i("request_response", jsonObject_register.toString());
                    Call<RegistrationModel> registrationModelCall = apiService.REGISTRATION_MODEL_CALL(jsonObject_register.toString());
                    registrationModelCall.enqueue(new Callback<RegistrationModel>() {
                        @Override
                        public void onResponse(Call<RegistrationModel> call, Response<RegistrationModel> response) {
                            progressDialog.dismiss();
                            if (response.body().getStatus().equals("1")) {
                                str_otp = String.valueOf(response.body().getOtp());
                                str_user_token = String.valueOf(response.body().getToken());
                                main_detail_cardview.setVisibility(View.GONE);
                                number_verify_cardview.setVisibility(View.VISIBLE);
                                if (countDownTimer != null) {
                                    countDownTimer.cancel();
                                }
                                callCountDownTimer();
                            } else {
                                Toast.makeText(NewAccountActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<RegistrationModel> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(NewAccountActivity.this, getResources().getString(R.string.Server_Error), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        btn_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_number = edt_number.getText().toString();
                if (isEmpty(str_number)) {
                    Toast.makeText(NewAccountActivity.this, getResources().getString(R.string.Please_Enter_Mobile_Number), Toast.LENGTH_SHORT).show();
                } else if (!str_number.substring(0, 2).equals("05")) {
                    Toast.makeText(NewAccountActivity.this, getResources().getString(R.string.Mobile_number_should_start_with_05), Toast.LENGTH_LONG).show();
                } else if (str_number.length() < 10) {
                    Toast.makeText(NewAccountActivity.this, getResources().getString(R.string.Mobile_Number_should_be_minimum), Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    try {
                        jsonObject_register.put("country_id", str_country_code);
                        jsonObject_register.put("phone", str_number);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Call<RegistrationModel> registrationModelCall = apiService.REGISTRATION_MODEL_CALL(jsonObject_register.toString());
                    registrationModelCall.enqueue(new Callback<RegistrationModel>() {
                        @Override
                        public void onResponse(Call<RegistrationModel> call, Response<RegistrationModel> response) {
                            progressDialog.dismiss();
                            if (response.body().getStatus().equals("1")) {
                                Toast.makeText(NewAccountActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                str_otp = String.valueOf(response.body().getOtp());
                                str_user_token = String.valueOf(response.body().getToken());
                                main_detail_cardview.setVisibility(View.GONE);
                                number_verify_cardview.setVisibility(View.VISIBLE);
                                if (countDownTimer != null) {
                                    countDownTimer.cancel();
                                }
                                callCountDownTimer();
                            } else {
                                if (sessionManager.getLanguage("Langauage", "en").equals("en")) {
                                    Toast.makeText(NewAccountActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(NewAccountActivity.this, "?????? ?????????? ?????????? ????????????", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<RegistrationModel> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(NewAccountActivity.this, getResources().getString(R.string.Server_Error), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(NewAccountActivity.this, getResources().getString(R.string.Please_Enter_OTP), Toast.LENGTH_SHORT).show();
                } else if (isEmpty(str_edt_2)) {
                    Toast.makeText(NewAccountActivity.this, getResources().getString(R.string.Please_Enter_OTP), Toast.LENGTH_SHORT).show();
                } else if (isEmpty(str_edt_3)) {
                    Toast.makeText(NewAccountActivity.this, getResources().getString(R.string.Please_Enter_OTP), Toast.LENGTH_SHORT).show();
                } else if (isEmpty(str_edt_4)) {
                    Toast.makeText(NewAccountActivity.this, getResources().getString(R.string.Please_Enter_OTP), Toast.LENGTH_SHORT).show();
                } else {

                    String temp_otp = null;
                    if (language.equals("ar")) {
                        temp_otp = str_edt_4 + str_edt_3 + str_edt_2 + str_edt_1;
                    } else {
                        temp_otp = str_edt_1 + str_edt_2 + str_edt_3 + str_edt_4;
                    }

                    progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    try {
                        jsonObject_verify.put("otp", temp_otp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Call<VerifyOTPModel> verifyOTPModelCall = apiService.VERIFY_OTP_MODEL_CALL(str_user_token, jsonObject_verify.toString());
                    verifyOTPModelCall.enqueue(new Callback<VerifyOTPModel>() {
                        @Override
                        public void onResponse(Call<VerifyOTPModel> call, Response<VerifyOTPModel> response) {
                            progressDialog.dismiss();
                            if (response.body().getStatus().equals("2")) {
                                if (countDownTimer != null) {
                                    countDownTimer.cancel();
                                }
                                str_user_token = response.body().getToken();
                                number_verify_cardview.setVisibility(View.GONE);
                                personal_detail_cardview.setVisibility(View.VISIBLE);
                                Toast.makeText(NewAccountActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                if (sessionManager.getLanguage("Langauage", "en").equals("en")) {
                                    Toast.makeText(NewAccountActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(NewAccountActivity.this, "???????? ?????????? ?????? ????????", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<VerifyOTPModel> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(NewAccountActivity.this, getResources().getString(R.string.Server_Error), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(NewAccountActivity.this, getResources().getString(R.string.Please_Enter_Name), Toast.LENGTH_SHORT).show();
                } else if (str_name.length() < 3) {
                    Toast.makeText(NewAccountActivity.this, getResources().getString(R.string.Name_should_be_minimum_of_three_characters), Toast.LENGTH_SHORT).show();
                } else if (isEmpty(str_email)) {
                    Toast.makeText(NewAccountActivity.this, getResources().getString(R.string.Please_Enter_Email), Toast.LENGTH_SHORT).show();
                } else if (!str_email.matches(str_email_regex)) {
                    Toast.makeText(NewAccountActivity.this, getResources().getString(R.string.Please_Enter_Valid_Email), Toast.LENGTH_SHORT).show();
                } else if (isEmpty(str_password)) {
                    Toast.makeText(NewAccountActivity.this, getResources().getString(R.string.Please_Enter_Password), Toast.LENGTH_SHORT).show();
                } else if (str_password.length() < 6) {
                    Toast.makeText(NewAccountActivity.this, getResources().getString(R.string.Password_should_be_minimum_of_six_characters), Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    try {
                        jsonObject_signup.put("name", str_name);
                        jsonObject_signup.put("email", str_email);
                        jsonObject_signup.put("password", str_password);
                        jsonObject_signup.put("gcm_id", FirebaseInstanceId.getInstance().getToken());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Call<RegistrationStep2Model> registrationStep2ModelCall = apiService.REGISTRATION_STEP_2_MODEL_CALL(str_user_token, jsonObject_signup.toString());
                    registrationStep2ModelCall.enqueue(new Callback<RegistrationStep2Model>() {
                        @Override
                        public void onResponse(Call<RegistrationStep2Model> call, Response<RegistrationStep2Model> response) {
                            progressDialog.dismiss();
                            if (response.body().getStatus().equals("1")) {
                                sessionManager.createLoginSession(response.body().getToken(), response.body().getUserId(), response.body().getName(), response.body().getEmail(), response.body().getPhone(), response.body().getCountryId(), response.body().getUserProfile(), "0", "users");//else salesmen
                                startActivity(new Intent(NewAccountActivity.this, SelectAppModeActivity.class));
                                finish();

                                AdjustEvent adjustEvent = new AdjustEvent("e47c27");
                                Adjust.trackEvent(adjustEvent);

                                Toast.makeText(NewAccountActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(NewAccountActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<RegistrationStep2Model> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(NewAccountActivity.this, getResources().getString(R.string.Server_Error), Toast.LENGTH_SHORT).show();
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

        if (Connection_Detector.isInternetAvailable(this)) {
            progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            Call<CountryListModel> countryListModelCall = apiService.COUNTRY_LIST_MODEL_CALL();
            countryListModelCall.enqueue(new Callback<CountryListModel>() {
                @Override
                public void onResponse(Call<CountryListModel> call, Response<CountryListModel> response) {
                    progressDialog.dismiss();
                    arrayList_country = response.body().getData();
                    for (int i = 0; i < arrayList_country.size(); i++) {
                        arrayList_country_name.add(arrayList_country.get(i).getCountryName());
                        arrayList_country_id.add(arrayList_country.get(i).getId());
                    }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(NewAccountActivity.this, android.R.layout.simple_spinner_dropdown_item, arrayList_country_name);
                    sp_country.setAdapter(dataAdapter);
                    sp_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            str_country_code = arrayList_country_id.get(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                @Override
                public void onFailure(Call<CountryListModel> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(NewAccountActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            //Toast.makeText(this, getResources().getString(R.string.), Toast.LENGTH_SHORT).show();
        }

    }


    public void callCountDownTimer() {
        btn_resend.setEnabled(false);
        countDownTimer = new CountDownTimer(60000, 1000) {
            @SuppressLint("DefaultLocale")
            public void onTick(long millisUntilFinished) {
                tv_min.setText(String.format("%02d:%02d", TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)), TimeUnit.SECONDS.toSeconds(TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished))));
            }

            public void onFinish() {
                btn_resend.setEnabled(true);
                tv_min.setText("00:00");
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityRecreationHelper.onDestroy(this);
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
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

}
