package com.codeclinic.yakrm.Activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codeclinic.yakrm.Models.FriendMobileNumberModel;
import com.codeclinic.yakrm.Models.SendVoucherToFriendModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.Connection_Detector;
import com.codeclinic.yakrm.Utils.ImageURL;
import com.codeclinic.yakrm.Utils.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SendToFriendActivity extends AppCompatActivity {

    Button btn_complete;
    ImageView img_back, img_voucher, img_search;
    TextView tv_itemname, tv_price, tv_name, tv_email, tv_sending_date;
    EditText edt_mobile, ed_description;

    API apiService;
    JSONObject jsonObject_mobile = new JSONObject();
    JSONObject jsonObject = new JSONObject();
    SessionManager sessionManager;
    ProgressDialog progressDialog;

    String mobile_number;

    public boolean isEmpty(CharSequence character) {
        return character == null || character.length() == 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to_friend);

        apiService = RestClass.getClient().create(API.class);

        btn_complete = findViewById(R.id.btn_complete);

        progressDialog = new ProgressDialog(this);

        sessionManager = new SessionManager(this);

        img_back = findViewById(R.id.img_back);
        img_voucher = findViewById(R.id.img_voucher);
        img_search = findViewById(R.id.img_search);

        tv_itemname = findViewById(R.id.tv_itemname);
        tv_price = findViewById(R.id.tv_price);
        tv_name = findViewById(R.id.tv_name);
        tv_email = findViewById(R.id.tv_email);
        tv_sending_date = findViewById(R.id.tv_sending_date);

        edt_mobile = findViewById(R.id.edt_mobile);
        ed_description = findViewById(R.id.ed_description);


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

        Picasso.with(this).load(ImageURL.Vendor_voucher_image + VoucherDetailActivity.v_image).into(img_voucher);
        tv_itemname.setText(VoucherDetailActivity.voucher_name);
        tv_price.setText(VoucherDetailActivity.price + " " + getResources().getString(R.string.SR_currency));

        tv_sending_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isEmpty(edt_mobile.getText().toString())) {
                    Toast.makeText(SendToFriendActivity.this, "Please verify your friends mobile number", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.setMessage("Please Wait");
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    try {
                        jsonObject_mobile.put("phone", edt_mobile.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Call<FriendMobileNumberModel> friendMobileNumberModelCall = apiService.FRIEND_MOBILE_NUMBER_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), jsonObject_mobile.toString());
                    friendMobileNumberModelCall.enqueue(new Callback<FriendMobileNumberModel>() {
                        @Override
                        public void onResponse(Call<FriendMobileNumberModel> call, Response<FriendMobileNumberModel> response) {
                            progressDialog.dismiss();
                            String status = response.body().getStatus();
                            if (status.equals("1")) {
                                tv_name.setText(response.body().getName());
                                tv_email.setText(response.body().getEmail());
                                mobile_number = edt_mobile.getText().toString();
                            } else {
                                Toast.makeText(SendToFriendActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<FriendMobileNumberModel> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(SendToFriendActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Connection_Detector.isInternetAvailable(SendToFriendActivity.this)) {
                    if (isEmpty(mobile_number)) {
                        Toast.makeText(SendToFriendActivity.this, "Please verify your friends mobile number", Toast.LENGTH_SHORT).show();
                    } else if (isEmpty(ed_description.getText().toString())) {
                        Toast.makeText(SendToFriendActivity.this, "Please Enter Description", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.setMessage("Please Wait");
                        progressDialog.setIndeterminate(true);
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        RequestBody voucherID = RequestBody.create(MediaType.parse("text/plain"), VoucherDetailActivity.voucher_id);
                        RequestBody mobile = RequestBody.create(MediaType.parse("text/plain"), mobile_number);
                        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), ed_description.getText().toString());
                        RequestBody voucher_payment_id = RequestBody.create(MediaType.parse("text/plain"), VoucherDetailActivity.v_payment_id);
                        Call<SendVoucherToFriendModel> sendVoucherToFriendModelCall = apiService.SEND_VOUCHER_TO_FRIEND_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), voucherID, mobile, description, voucher_payment_id);
                        sendVoucherToFriendModelCall.enqueue(new Callback<SendVoucherToFriendModel>() {
                            @Override
                            public void onResponse(Call<SendVoucherToFriendModel> call, Response<SendVoucherToFriendModel> response) {
                                progressDialog.dismiss();
                                if (response.body().equals("1")) {
                                    VoucherDetailActivity.voucher_name = "";
                                    VoucherDetailActivity.date = "";
                                    VoucherDetailActivity.barcode = "";
                                    VoucherDetailActivity.pincode = "";
                                    VoucherDetailActivity.price = "";
                                    VoucherDetailActivity.v_image = "";
                                    VoucherDetailActivity.v_payment_id = "";
                                    VoucherDetailActivity.voucher_id = "";
                                    VoucherDetailActivity.v_image = "";
                                    finish();
                                    startActivity(new Intent(SendToFriendActivity.this, MainActivity.class));
                                    Toast.makeText(SendToFriendActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SendToFriendActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<SendVoucherToFriendModel> call, Throwable t) {
                                progressDialog.dismiss();
                                Toast.makeText(SendToFriendActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } else {
                    Toast.makeText(SendToFriendActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void datePicker() {
        final Calendar c;
        c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(SendToFriendActivity.this, new DatePickerDialog.OnDateSetListener() {
            String month;

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                int m = monthOfYear + 1;

                if (m == 1) {
                    //month = getString(R.string.jaunuary);
                    month = "January";
                } else if (m == 2) {
                    //month = getString(R.string.february);
                    month = "February";
                } else if (m == 3) {
                    //month = getString(R.string.march);
                    month = "March";
                } else if (m == 4) {
                    //month = getString(R.string.april);
                    month = "April";
                } else if (m == 5) {
                    //month = getString(R.string.may);
                    month = "May";
                } else if (m == 6) {
//                            month = getString(R.string.june);
                    month = "June";
                } else if (m == 7) {
                    //month = getString(R.string.july);
                    month = "July";
                } else if (m == 8) {
                    //month = getString(R.string.august);
                    month = "August";
                } else if (m == 9) {
                    //month = getString(R.string.september);
                    month = "September";
                } else if (m == 10) {
                    //month = getString(R.string.octomer);
                    month = "October";
                } else if (m == 11) {
                    //month = getString(R.string.november);
                    month = "November";
                } else if (m == 12) {
                    //month = getString(R.string.december);
                    month = "December";
                }
                tv_sending_date.setText(year + "-" + m + "-" + dayOfMonth);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
    }

}
