package com.yakrm.codeclinic.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
                                    succesful_cardview.setVisibility(View.VISIBLE);
                                    scrollview_pay.setVisibility(View.GONE);
                                    Toast.makeText(CompletingPurchasingActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
                                    succesful_cardview.setVisibility(View.VISIBLE);
                                    scrollview_pay.setVisibility(View.GONE);
                                    Toast.makeText(CompletingPurchasingActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
                                    succesful_cardview.setVisibility(View.VISIBLE);
                                    scrollview_pay.setVisibility(View.GONE);
                                    Toast.makeText(CompletingPurchasingActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
}
