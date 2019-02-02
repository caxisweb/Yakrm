package com.yakrm.codeclinic.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.paytabs.paytabs_sdk.payment.ui.activities.PayTabActivity;
import com.paytabs.paytabs_sdk.utils.PaymentParams;
import com.yakrm.codeclinic.Models.PaymentTransactionModel;
import com.yakrm.codeclinic.R;
import com.yakrm.codeclinic.Retrofit.API;
import com.yakrm.codeclinic.Retrofit.RestClass;
import com.yakrm.codeclinic.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CompletingPurchasingActivity extends AppCompatActivity {

    CardView main_pay_cardview, succesful_cardview, error_cardview;
    Button btn_cmplt_pay;
    ScrollView scrollview_pay;
    TextView tv_total_price, tv_sc_total_price;
    LinearLayout payment_layout;
    RelativeLayout rl_pay_pal, rl_mastercard, rl_visa;

    JSONObject jsonObject = new JSONObject();
    String total_price;

    ImageView img_back;
    API apiService;
    SessionManager sessionManager;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completing_purchasing);

        img_back = findViewById(R.id.img_back);
        String language = String.valueOf(getResources().getConfiguration().locale);
        if (language.equals("ar")) {
            img_back.setImageDrawable(getResources().getDrawable(R.drawable.back_right_img));
        }
        scrollview_pay = findViewById(R.id.scrollview_pay);
        btn_cmplt_pay = findViewById(R.id.btn_cmplt_pay);
        rl_visa = findViewById(R.id.rl_visa);
        rl_mastercard = findViewById(R.id.rl_mastercard);
        rl_pay_pal = findViewById(R.id.rl_pay_pal);
        apiService = RestClass.getClient().create(API.class);
        sessionManager = new SessionManager(this);
        succesful_cardview = findViewById(R.id.succesful_cardview);
        error_cardview = findViewById(R.id.error_cardview);
        main_pay_cardview = findViewById(R.id.main_pay_cardview);
        payment_layout = findViewById(R.id.payment_layout);
        tv_total_price = findViewById(R.id.tv_total_price);
        tv_sc_total_price = findViewById(R.id.tv_sc_total_price);
        tv_total_price.setText(getIntent().getStringExtra("price"));
        total_price = getIntent().getStringExtra("price");
        tv_sc_total_price.setText(total_price);
        progressDialog = new ProgressDialog(this);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btn_cmplt_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_pay_cardview.setVisibility(View.GONE);
                scrollview_pay.setVisibility(View.VISIBLE);
            }
        });

        rl_pay_pal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(CompletingPurchasingActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                alert.setMessage("Are you Sure?");
                alert.setCancelable(false);
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog.setMessage("Please Wait");
                        progressDialog.setIndeterminate(true);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        try {
                            jsonObject.put("transaction_id", DateFormat.format("yyyy-MM-dd", new Date()).toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Call<PaymentTransactionModel> paymentTransactionModelCall = apiService.PAYMENT_TRANSACTION_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), jsonObject.toString());
                        paymentTransactionModelCall.enqueue(new Callback<PaymentTransactionModel>() {
                            @Override
                            public void onResponse(Call<PaymentTransactionModel> call, Response<PaymentTransactionModel> response) {
                                progressDialog.dismiss();
                                String status = response.body().getStatus();
                                if (status.equals("1")) {
                           /*         succesful_cardview.setVisibility(View.VISIBLE);
                                    scrollview_pay.setVisibility(View.GONE);
                                    Toast.makeText(CompletingPurchasingActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();*/
                                    Intent in = new Intent(getApplicationContext(), PayTabActivity.class);
                                    in.putExtra(PaymentParams.MERCHANT_EMAIL, "ahmed@yakrm.com"); //this a demo account for testing the sdk
                                    in.putExtra(PaymentParams.SECRET_KEY, "ex2SHCqdgtJlrF2gp5fGCis3tUGh5EkjcmcTZD7g6RCxwEOWJ3Cml4qOY664KroXOBQNeY3lPFTlkHh4KUq6YQVXW22HtrFh2w4g");//Add your Secret Key Here
                                    in.putExtra(PaymentParams.LANGUAGE, PaymentParams.ENGLISH);
                                    in.putExtra(PaymentParams.TRANSACTION_TITLE, "Paytabs");
                                    in.putExtra(PaymentParams.AMOUNT, 5.0);

                                    in.putExtra(PaymentParams.CURRENCY_CODE, "SAR");
                                    in.putExtra(PaymentParams.CUSTOMER_PHONE_NUMBER, "009733");
                                    in.putExtra(PaymentParams.CUSTOMER_EMAIL, "jaypokarjdp@gmail.com");
                                    in.putExtra(PaymentParams.ORDER_ID, "123456");
                                    in.putExtra(PaymentParams.PRODUCT_NAME, "Product 1, Product 2");
//Billing Address
                                    in.putExtra(PaymentParams.ADDRESS_BILLING, "Flat 1,Building 123, Road 2345");
                                    in.putExtra(PaymentParams.CITY_BILLING, "Manama");
                                    in.putExtra(PaymentParams.STATE_BILLING, "Manama");
                                    in.putExtra(PaymentParams.COUNTRY_BILLING, "BHR");
                                    in.putExtra(PaymentParams.POSTAL_CODE_BILLING, "00973"); //Put Country Phone code if Postal code not available '00973'
//Shipping Address
                                    in.putExtra(PaymentParams.ADDRESS_SHIPPING, "Flat 1,Building 123, Road 2345");
                                    in.putExtra(PaymentParams.CITY_SHIPPING, "Manama");
                                    in.putExtra(PaymentParams.STATE_SHIPPING, "Manama");
                                    in.putExtra(PaymentParams.COUNTRY_SHIPPING, "BHR");
                                    in.putExtra(PaymentParams.POSTAL_CODE_SHIPPING, "00973"); //Put Country Phone code if Postal code not available '00973'
//Payment Page Style
                                    in.putExtra(PaymentParams.PAY_BUTTON_COLOR, "#2474bc");
                                    in.putExtra(PaymentParams.THEME, PaymentParams.THEME_LIGHT);

                                    in.putExtra(PaymentParams.IS_TOKENIZATION, true);
                                    startActivityForResult(in, PaymentParams.PAYMENT_REQUEST_CODE);
                                } else {
                                    scrollview_pay.setVisibility(View.GONE);
                                    error_cardview.setVisibility(View.VISIBLE);
                                    Toast.makeText(CompletingPurchasingActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<PaymentTransactionModel> call, Throwable t) {
                                progressDialog.dismiss();
                                scrollview_pay.setVisibility(View.GONE);
                                error_cardview.setVisibility(View.VISIBLE);
                                Toast.makeText(CompletingPurchasingActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });

        rl_mastercard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(CompletingPurchasingActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                alert.setMessage("Are you Sure?");
                alert.setCancelable(false);
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog.setMessage("Please Wait");
                        progressDialog.setIndeterminate(true);
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        try {
                            jsonObject.put("transaction_id", DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Call<PaymentTransactionModel> paymentTransactionModelCall = apiService.PAYMENT_TRANSACTION_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), jsonObject.toString());
                        paymentTransactionModelCall.enqueue(new Callback<PaymentTransactionModel>() {
                            @Override
                            public void onResponse(Call<PaymentTransactionModel> call, Response<PaymentTransactionModel> response) {
                                String status = response.body().getStatus();
                                progressDialog.dismiss();
                                if (status.equals("1")) {
                                    //succesful_cardview.setVisibility(View.VISIBLE);
                                    //scrollview_pay.setVisibility(View.GONE);
                                    //Toast.makeText(CompletingPurchasingActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    Intent in = new Intent(getApplicationContext(), PayTabActivity.class);
                                    in.putExtra(PaymentParams.MERCHANT_EMAIL, "ahmed@yakrm.com"); //this a demo account for testing the sdk
                                    in.putExtra(PaymentParams.SECRET_KEY, "ex2SHCqdgtJlrF2gp5fGCis3tUGh5EkjcmcTZD7g6RCxwEOWJ3Cml4qOY664KroXOBQNeY3lPFTlkHh4KUq6YQVXW22HtrFh2w4g");//Add your Secret Key Here
                                    in.putExtra(PaymentParams.LANGUAGE, PaymentParams.ENGLISH);
                                    in.putExtra(PaymentParams.TRANSACTION_TITLE, "Paytabs");
                                    in.putExtra(PaymentParams.AMOUNT, 5.0);

                                    in.putExtra(PaymentParams.CURRENCY_CODE, "SAR");
                                    in.putExtra(PaymentParams.CUSTOMER_PHONE_NUMBER, "009733");
                                    in.putExtra(PaymentParams.CUSTOMER_EMAIL, "jaypokarjdp@gmail.com");
                                    in.putExtra(PaymentParams.ORDER_ID, "123456");
                                    in.putExtra(PaymentParams.PRODUCT_NAME, "Product 1, Product 2");

//Billing Address
                                    in.putExtra(PaymentParams.ADDRESS_BILLING, "Flat 1,Building 123, Road 2345");
                                    in.putExtra(PaymentParams.CITY_BILLING, "Manama");
                                    in.putExtra(PaymentParams.STATE_BILLING, "Manama");
                                    in.putExtra(PaymentParams.COUNTRY_BILLING, "BHR");
                                    in.putExtra(PaymentParams.POSTAL_CODE_BILLING, "00973"); //Put Country Phone code if Postal code not available '00973'

//Shipping Address
                                    in.putExtra(PaymentParams.ADDRESS_SHIPPING, "Flat 1,Building 123, Road 2345");
                                    in.putExtra(PaymentParams.CITY_SHIPPING, "Manama");
                                    in.putExtra(PaymentParams.STATE_SHIPPING, "Manama");
                                    in.putExtra(PaymentParams.COUNTRY_SHIPPING, "BHR");
                                    in.putExtra(PaymentParams.POSTAL_CODE_SHIPPING, "00973"); //Put Country Phone code if Postal code not available '00973'

//Payment Page Style
                                    in.putExtra(PaymentParams.PAY_BUTTON_COLOR, "#2474bc");
                                    in.putExtra(PaymentParams.THEME, PaymentParams.THEME_LIGHT);

                                    in.putExtra(PaymentParams.IS_TOKENIZATION, true);
                                    startActivityForResult(in, PaymentParams.PAYMENT_REQUEST_CODE);
                                } else {
                                    scrollview_pay.setVisibility(View.GONE);
                                    error_cardview.setVisibility(View.VISIBLE);
                                    Toast.makeText(CompletingPurchasingActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<PaymentTransactionModel> call, Throwable t) {
                                progressDialog.dismiss();
                                scrollview_pay.setVisibility(View.GONE);
                                error_cardview.setVisibility(View.VISIBLE);
                                Toast.makeText(CompletingPurchasingActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });

        rl_visa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(CompletingPurchasingActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                alert.setMessage("Are you Sure?");
                alert.setCancelable(false);
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog.setMessage("Please Wait");
                        progressDialog.setIndeterminate(true);
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        try {
                            jsonObject.put("transaction_id", DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Call<PaymentTransactionModel> paymentTransactionModelCall = apiService.PAYMENT_TRANSACTION_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), jsonObject.toString());
                        paymentTransactionModelCall.enqueue(new Callback<PaymentTransactionModel>() {
                            @Override
                            public void onResponse(Call<PaymentTransactionModel> call, Response<PaymentTransactionModel> response) {
                                progressDialog.dismiss();
                                String status = response.body().getStatus();
                                if (status.equals("1")) {
                                /*    succesful_cardview.setVisibility(View.VISIBLE);
                                    scrollview_pay.setVisibility(View.GONE);
                                    Toast.makeText(CompletingPurchasingActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();*/
                                    Intent in = new Intent(getApplicationContext(), PayTabActivity.class);
                                    in.putExtra(PaymentParams.MERCHANT_EMAIL, "ahmed@yakrm.com"); //this a demo account for testing the sdk
                                    in.putExtra(PaymentParams.SECRET_KEY, "ex2SHCqdgtJlrF2gp5fGCis3tUGh5EkjcmcTZD7g6RCxwEOWJ3Cml4qOY664KroXOBQNeY3lPFTlkHh4KUq6YQVXW22HtrFh2w4g");//Add your Secret Key Here
                                    in.putExtra(PaymentParams.LANGUAGE, PaymentParams.ENGLISH);
                                    in.putExtra(PaymentParams.TRANSACTION_TITLE, "Paytabs");
                                    in.putExtra(PaymentParams.AMOUNT, 5.0);

                                    in.putExtra(PaymentParams.CURRENCY_CODE, "SAR");
                                    in.putExtra(PaymentParams.CUSTOMER_PHONE_NUMBER, "009733");
                                    in.putExtra(PaymentParams.CUSTOMER_EMAIL, "jaypokarjdp@gmail.com");
                                    in.putExtra(PaymentParams.ORDER_ID, "123456");
                                    in.putExtra(PaymentParams.PRODUCT_NAME, "Product 1, Product 2");

//Billing Address
                                    in.putExtra(PaymentParams.ADDRESS_BILLING, "Flat 1,Building 123, Road 2345");
                                    in.putExtra(PaymentParams.CITY_BILLING, "Manama");
                                    in.putExtra(PaymentParams.STATE_BILLING, "Manama");
                                    in.putExtra(PaymentParams.COUNTRY_BILLING, "BHR");
                                    in.putExtra(PaymentParams.POSTAL_CODE_BILLING, "00973"); //Put Country Phone code if Postal code not available '00973'

//Shipping Address
                                    in.putExtra(PaymentParams.ADDRESS_SHIPPING, "Flat 1,Building 123, Road 2345");
                                    in.putExtra(PaymentParams.CITY_SHIPPING, "Manama");
                                    in.putExtra(PaymentParams.STATE_SHIPPING, "Manama");
                                    in.putExtra(PaymentParams.COUNTRY_SHIPPING, "BHR");
                                    in.putExtra(PaymentParams.POSTAL_CODE_SHIPPING, "00973"); //Put Country Phone code if Postal code not available '00973'

//Payment Page Style
                                    in.putExtra(PaymentParams.PAY_BUTTON_COLOR, "#2474bc");
                                    in.putExtra(PaymentParams.THEME, PaymentParams.THEME_LIGHT);

                                    in.putExtra(PaymentParams.IS_TOKENIZATION, true);
                                    startActivityForResult(in, PaymentParams.PAYMENT_REQUEST_CODE);
                                } else {
                                    scrollview_pay.setVisibility(View.GONE);
                                    error_cardview.setVisibility(View.VISIBLE);
                                    Toast.makeText(CompletingPurchasingActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<PaymentTransactionModel> call, Throwable t) {
                                progressDialog.dismiss();
                                scrollview_pay.setVisibility(View.GONE);
                                error_cardview.setVisibility(View.VISIBLE);
                                Toast.makeText(CompletingPurchasingActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PaymentParams.PAYMENT_REQUEST_CODE) {
           /* Log.e("Tag", data.getStringExtra(PaymentParams.RESPONSE_CODE));
            Log.e("Tag", data.getStringExtra(PaymentParams.TRANSACTION_ID));
            Toast.makeText(CompletingPurchasingActivity.this, data.getStringExtra(PaymentParams.RESPONSE_CODE), Toast.LENGTH_LONG).show();
            Toast.makeText(CompletingPurchasingActivity.this, data.getStringExtra(PaymentParams.TRANSACTION_ID), Toast.LENGTH_LONG).show();
            if (data.hasExtra(PaymentParams.TOKEN) && !data.getStringExtra(PaymentParams.TOKEN).isEmpty()) {
                Log.e("Tag", data.getStringExtra(PaymentParams.TOKEN));
                Log.e("Tag", data.getStringExtra(PaymentParams.CUSTOMER_EMAIL));
                Log.e("Tag", data.getStringExtra(PaymentParams.CUSTOMER_PASSWORD));
                Toast.makeText(CompletingPurchasingActivity.this, data.getStringExtra(PaymentParams.TOKEN), Toast.LENGTH_LONG).show();
                Toast.makeText(CompletingPurchasingActivity.this, data.getStringExtra(PaymentParams.CUSTOMER_EMAIL), Toast.LENGTH_LONG).show();
                Toast.makeText(CompletingPurchasingActivity.this, data.getStringExtra(PaymentParams.CUSTOMER_PASSWORD), Toast.LENGTH_LONG).show();
            }*/
            finish();
        }
    }
}
