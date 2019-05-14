package com.codeclinic.yakrm.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.codeclinic.yakrm.Adapter.SavedCardListAdapter;
import com.codeclinic.yakrm.BuildConfig;
import com.codeclinic.yakrm.Models.GetCardListItemModel;
import com.codeclinic.yakrm.Models.GetCardListModel;
import com.codeclinic.yakrm.Models.GetCheckoutIDModel;
import com.codeclinic.yakrm.Models.PaymentTransactionModel;
import com.codeclinic.yakrm.Models.ReplaceVoucherModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.PaymentRestClass;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.CheckoutBroadcastReceiver;
import com.codeclinic.yakrm.Utils.SessionManager;
import com.oppwa.mobile.connect.checkout.dialog.CheckoutActivity;
import com.oppwa.mobile.connect.checkout.meta.CheckoutSettings;
import com.oppwa.mobile.connect.exception.PaymentError;
import com.oppwa.mobile.connect.exception.PaymentException;
import com.oppwa.mobile.connect.exception.PaymentProviderNotInitializedException;
import com.oppwa.mobile.connect.provider.Connect;
import com.oppwa.mobile.connect.provider.Transaction;
import com.oppwa.mobile.connect.provider.TransactionType;
import com.oppwa.mobile.connect.service.ConnectService;
import com.oppwa.mobile.connect.service.IProviderBinder;
import com.paytabs.paytabs_sdk.utils.PaymentParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class CompletingPurchasingActivity extends AppCompatActivity {

    private IProviderBinder binder;

    private ServiceConnection serviceConnection;

    CardView main_pay_cardview, succesful_cardview, error_cardview;
    Button btn_cmplt_pay;
    ScrollView scrollview_pay;
    public static String card_ID;
    LinearLayout payment_layout;
    LinearLayout rl_pay_pal, rl_mastercard, rl_visa;
    TextView tv_total_price, tv_sc_total_price, tv_wallet_amount;
    RecyclerView recyclerView;
    JSONObject jsonObject = new JSONObject();
    String total_price, checkoutId;

    ImageView img_back;
    API apiService;
    API apiInterface;
    SessionManager sessionManager;
    ProgressDialog progressDialog;
    double price;

    List<GetCardListItemModel> arrayList = new ArrayList<>();
    ArrayList<String> card_type_arrayList = new ArrayList<>();
    ArrayList<String> card_id_arrayList = new ArrayList<>();
    ArrayList<String> card_no_arrayList = new ArrayList<>();
    ArrayList<String> card_name_arrayList = new ArrayList<>();
    ArrayList<String> card_expiry_arrayList = new ArrayList<>();
    ArrayList<String> card_cvv_arrayList = new ArrayList<>();
    String card_type_select = "0", card_holder_name, card_expiry_date, card_cvv, card_no, card_ex_date, card_ex_year, flag_cart = "1", main_price;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEnv();
        setContentView(R.layout.activity_completing_purchasing);

        img_back = findViewById(R.id.img_back);
        String language = String.valueOf(getResources().getConfiguration().locale);
        if (language.equals("ar")) {
            img_back.setImageDrawable(getResources().getDrawable(R.drawable.back_right_img));
        }
        scrollview_pay = findViewById(R.id.scrollview_pay);
        btn_cmplt_pay = findViewById(R.id.btn_cmplt_pay);


       /* rl_visa = findViewById(R.id.rl_visa);
        rl_mastercard = findViewById(R.id.rl_mastercard);*/
        rl_pay_pal = findViewById(R.id.rl_pay_pal);
        recyclerView = findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        apiService = RestClass.getClient().create(API.class);
        sessionManager = new SessionManager(this);
        succesful_cardview = findViewById(R.id.succesful_cardview);
        error_cardview = findViewById(R.id.error_cardview);
        main_pay_cardview = findViewById(R.id.main_pay_cardview);
        payment_layout = findViewById(R.id.payment_layout);

        tv_wallet_amount = findViewById(R.id.tv_wallet_amount);
        tv_wallet_amount.setText(sessionManager.getUserDetails().get(SessionManager.Wallet).replaceAll("1", getResources().getString(R.string.one))
                .replaceAll("2", getResources().getString(R.string.two))
                .replaceAll("3", getResources().getString(R.string.three))
                .replaceAll("4", getResources().getString(R.string.four))
                .replaceAll("5", getResources().getString(R.string.five))
                .replaceAll("6", getResources().getString(R.string.six))
                .replaceAll("7", getResources().getString(R.string.seven))
                .replaceAll("8", getResources().getString(R.string.eight))
                .replaceAll("9", getResources().getString(R.string.nine))
                .replaceAll("0", getResources().getString(R.string.zero)) + " " + getResources().getString(R.string.SR_currency));

        tv_total_price = findViewById(R.id.tv_total_price);
        tv_sc_total_price = findViewById(R.id.tv_sc_total_price);
        flag_cart = getIntent().getStringExtra("flag_cart");
        main_price = getIntent().getStringExtra("price");

        total_price = getIntent().getStringExtra("price");
        price = Double.parseDouble(total_price);

        if (price > Double.parseDouble(sessionManager.getUserDetails().get(SessionManager.Wallet))) {
            double t_price = price - Double.parseDouble(sessionManager.getUserDetails().get(SessionManager.Wallet));
            tv_total_price.setText(String.valueOf(t_price).replaceAll("1", getResources().getString(R.string.one))
                    .replaceAll("2", getResources().getString(R.string.two))
                    .replaceAll("3", getResources().getString(R.string.three))
                    .replaceAll("4", getResources().getString(R.string.four))
                    .replaceAll("5", getResources().getString(R.string.five))
                    .replaceAll("6", getResources().getString(R.string.six))
                    .replaceAll("7", getResources().getString(R.string.seven))
                    .replaceAll("8", getResources().getString(R.string.eight))
                    .replaceAll("9", getResources().getString(R.string.nine))
                    .replaceAll("0", getResources().getString(R.string.zero)) + " " + getResources().getString(R.string.SR_currency));

            tv_sc_total_price.setText(String.valueOf(t_price).replaceAll("1", getResources().getString(R.string.one))
                    .replaceAll("2", getResources().getString(R.string.two))
                    .replaceAll("3", getResources().getString(R.string.three))
                    .replaceAll("4", getResources().getString(R.string.four))
                    .replaceAll("5", getResources().getString(R.string.five))
                    .replaceAll("6", getResources().getString(R.string.six))
                    .replaceAll("7", getResources().getString(R.string.seven))
                    .replaceAll("8", getResources().getString(R.string.eight))
                    .replaceAll("9", getResources().getString(R.string.nine))
                    .replaceAll("0", getResources().getString(R.string.zero)) + " " + getResources().getString(R.string.SR_currency));
        } else {
            String t_price = "0";
            tv_total_price.setText(t_price.replaceAll("0", getResources().getString(R.string.zero)) + " " + getResources().getString(R.string.SR_currency));
            tv_sc_total_price.setText(t_price.replaceAll("0", getResources().getString(R.string.zero)) + " " + getResources().getString(R.string.SR_currency));
        }

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
                if (Double.parseDouble(sessionManager.getUserDetails().get(SessionManager.Wallet)) > price) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(CompletingPurchasingActivity.this, R.style.CustomDialogFragment);
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
                                jsonObject.put("amount_from_wallet", sessionManager.getUserDetails().get(SessionManager.Wallet));
                                jsonObject.put("amount_from_bank", "0");
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
                                        sessionManager.createLoginSession(sessionManager.getUserDetails().get(SessionManager.User_Token), sessionManager.getUserDetails().get(SessionManager.User_ID), sessionManager.getUserDetails().get(SessionManager.User_Name), sessionManager.getUserDetails().get(SessionManager.User_Email), sessionManager.getUserDetails().get(SessionManager.USER_MOBILE), sessionManager.getUserDetails().get(SessionManager.USER_COUNTRY_ID), sessionManager.getUserDetails().get(SessionManager.USER_Profile), response.body().getWallet(), sessionManager.getUserDetails().get(SessionManager.UserType));
                                        succesful_cardview.setVisibility(View.VISIBLE);
                                        scrollview_pay.setVisibility(View.GONE);
                                        Toast.makeText(CompletingPurchasingActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(new Intent(CompletingPurchasingActivity.this, MainActivity.class));
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

                } else if (Double.parseDouble(sessionManager.getUserDetails().get(SessionManager.Wallet)) == price) {
                    try {
                        jsonObject.put("replace_active_voucher_id", VoucherDetailActivity.voucher_id);
                        jsonObject.put("voucher_payment_detail_id", VoucherDetailActivity.v_payment_id);
                        jsonObject.put("replace_voucher_id", ExchangeAddBalanceActivity.voucher_id);
                        jsonObject.put("card_id", arrayList.get(0).getId());
                        jsonObject.put("transaction_id", DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString());
                        if (Double.parseDouble(main_price) > Double.parseDouble(sessionManager.getUserDetails().get(SessionManager.Wallet))) {
                            jsonObject.put("transaction_price", Double.parseDouble(main_price));
                            jsonObject.put("wallet", "0");
                        } else {
                            jsonObject.put("transaction_price", "0");
                            jsonObject.put("wallet", sessionManager.getUserDetails().get(SessionManager.Wallet));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Call<ReplaceVoucherModel> replaceVoucherModelCall = apiService.REPLACE_VOUCHER_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), jsonObject.toString());
                    replaceVoucherModelCall.enqueue(new Callback<ReplaceVoucherModel>() {
                        @Override
                        public void onResponse(Call<ReplaceVoucherModel> call, Response<ReplaceVoucherModel> response) {
                            Log.i("response_main", response.body().getStatus());
                            progressDialog.dismiss();
                            String status = response.body().getStatus();
                            if (status.equals("1")) {
                                sessionManager.createLoginSession(sessionManager.getUserDetails().get(SessionManager.User_Token), sessionManager.getUserDetails().get(SessionManager.User_ID), sessionManager.getUserDetails().get(SessionManager.User_Name), sessionManager.getUserDetails().get(SessionManager.User_Email), sessionManager.getUserDetails().get(SessionManager.USER_MOBILE), sessionManager.getUserDetails().get(SessionManager.USER_COUNTRY_ID), sessionManager.getUserDetails().get(SessionManager.USER_Profile), response.body().getWallet(), sessionManager.getUserDetails().get(SessionManager.UserType));
                                VoucherDetailActivity.voucher_name = "";
                                VoucherDetailActivity.date = "";
                                VoucherDetailActivity.barcode = "";
                                VoucherDetailActivity.pincode = "";
                                VoucherDetailActivity.price = "";
                                VoucherDetailActivity.v_image = "";
                                VoucherDetailActivity.v_payment_id = "";
                                VoucherDetailActivity.voucher_id = "";
                                VoucherDetailActivity.v_image = "";
                                ExchangeAddBalanceActivity.main_price = 0;
                                ExchangeAddBalanceActivity.voucher_price = 0;
                                ExchangeAddBalanceActivity.replace_price = 0;
                                ExchangeAddBalanceActivity.temp_price = 0;
                                startActivity(new Intent(CompletingPurchasingActivity.this, MainActivity.class));
                                finish();
                                ExchangeAddBalanceActivity.exchange_activity.finish();
                                ExchangeVoucherActivity.ex_activity.finish();
                                VoucherDetailActivity.voucher_detail_activity.finish();
                                Toast.makeText(CompletingPurchasingActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CompletingPurchasingActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ReplaceVoucherModel> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(CompletingPurchasingActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    main_pay_cardview.setVisibility(View.GONE);
                    scrollview_pay.setVisibility(View.VISIBLE);
                }
            }
        });

   /*     btn_cmplt_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject jobj = new JSONObject();
                try {
                    jobj.put("price", 50.00);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Call<GetCheckoutIDModel> getCheckoutIDModelCall = apiService.GET_CHECKOUT_ID_MODEL_CALL(jobj.toString());
                getCheckoutIDModelCall.enqueue(new Callback<GetCheckoutIDModel>() {
                    @Override
                    public void onResponse(Call<GetCheckoutIDModel> call, Response<GetCheckoutIDModel> response) {
                        if (response.body().getResult().getCode().equals("000.200.100")) {
                            configCheckout(response.body().getId());
                        } else {
                            Toast.makeText(CompletingPurchasingActivity.this, response.body().getResult().getDescription(), Toast.LENGTH_SHORT).show();
                        }


                        configCheckout(response.body().getId());
                        //   Toast.makeText(CompletingPurchasingActivity.this, response.body().getResult().getDescription(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<GetCheckoutIDModel> call, Throwable t) {
                        Toast.makeText(CompletingPurchasingActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    }
                });

              *//*  RetrieveFeedTask retrieveFeedTask = new RetrieveFeedTask();
                retrieveFeedTask.execute();*//*

            }
        });*/

        Retrofit retrofit = PaymentRestClass.getClient();
        apiInterface = retrofit.create(API.class);

        getAllcardList();
    }

    class RetrieveFeedTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {

            URL url;
            String urlString;
            HttpURLConnection connection = null;
            String checkoutId = null;

            urlString = "http://test.yakrm.com/api/checkout" + "?amount=49.99&currency=SAR&paymentType=DB";

            try {
                url = new URL(urlString);
                connection = (HttpURLConnection) url.openConnection();

                @SuppressLint({"NewApi", "LocalSuppress"}) JsonReader reader = new JsonReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));

                reader.beginObject();

                while (reader.hasNext()) {
                    if (reader.nextName().equals("id")) {
                        checkoutId = reader.nextString();

                        break;
                    }
                }

                reader.endObject();
                reader.close();
            } catch (Exception e) {
                /* error occurred */
                Log.i("error_transaction", e.toString());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            configCheckout(checkoutId);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, ConnectService.class);
        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(serviceConnection);
        stopService(new Intent(this, ConnectService.class));
    }

    private void configCheckout(String checkoutId) {

        this.checkoutId = checkoutId;

        Set<String> paymentBrands = new LinkedHashSet<>();

        paymentBrands.add("VISA");
        paymentBrands.add("MASTER");
        paymentBrands.add("DIRECTDEBIT_SEPA");

        CheckoutSettings checkoutSettings = null;

        checkoutSettings = new CheckoutSettings(checkoutId, paymentBrands);
        checkoutSettings.getStorePaymentDetailsMode();
        checkoutSettings.getAndroidPaySettings();
        checkoutSettings.setWebViewEnabledFor3DSecure(true);

        ComponentName componentName = new ComponentName(BuildConfig.APPLICATION_ID, CheckoutBroadcastReceiver.class.getCanonicalName());

        Intent intent = new Intent(CompletingPurchasingActivity.this, CheckoutActivity.class);
        intent.putExtra(CheckoutActivity.CHECKOUT_SETTINGS, checkoutSettings);
        intent.putExtra(CheckoutActivity.CHECKOUT_RECEIVER, componentName);
        startActivityForResult(intent, CheckoutActivity.CHECKOUT_ACTIVITY);

       /* Intent intent = new Intent(CompletingPurchasingActivity.this, CheckoutActivity.class);
        startActivityForResult(intent, CheckoutActivity.CHECKOUT_ACTIVITY);*/
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getScheme().equals("codeclinic")) {
            checkoutId = intent.getData().getQueryParameter("id");
        }
    }

    private void initEnv() {
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                binder = (IProviderBinder) service;

                try {
                    binder.initializeProvider(Connect.ProviderMode.TEST);
                  /*  if (TextUtils.equals("test", "test")) {
                        binder.initializeProvider(Connect.ProviderMode.TEST);
                    } else if (TextUtils.equals("test", "test")) {
                        binder.initializeProvider(Connect.ProviderMode.LIVE);
                    } else {
                        //  fireBroadcast(Config.FAILED, "Invalid Environment");
                    }*/
                } catch (PaymentException ee) {
                    //fireBroadcast(Config.FAILED, ee.getMessage());
                }

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                binder = null;
            }
        };
    }

  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case CheckoutActivity.RESULT_OK:
                *//* transaction completed *//*
                Transaction transaction = data.getParcelableExtra(CheckoutActivity.CHECKOUT_RESULT_TRANSACTION);

                *//* resource path if needed *//*
                String resourcePath = data.getStringExtra(CheckoutActivity.CHECKOUT_RESULT_RESOURCE_PATH);

                if (transaction.getTransactionType() == TransactionType.SYNC) {
                    *//* check the result of synchronous transaction *//*
                    // fireBroadcast(Config.SUCCESS, "checkoutId=" + checkoutId);
                } else {
                    *//* wait for the asynchronous transaction callback in the onNewIntent() *//*
                }

                break;
            case CheckoutActivity.RESULT_CANCELED:
                //fireBroadcast(Config.FAILED, "Shoper cancelled transaction");
                break;
            case CheckoutActivity.RESULT_ERROR:
                PaymentError error = data.getParcelableExtra(CheckoutActivity.CHECKOUT_RESULT_ERROR);
                //fireBroadcast(Config.FAILED, error.getErrorMessage());
                break;
            default:
                break;
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    /*    if (resultCode == RESULT_OK && requestCode == 1001) {
            Log.e("Tag", data.getStringExtra(PaymentParams.RESPONSE_CODE));
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


            progressDialog.setMessage("Please Wait");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

            if (flag_cart.equals("1")) {
                try {
                    jsonObject.put("transaction_id", data.getStringExtra("transaction_id"));
                    jsonObject.put("card_id", card_ID);
                    jsonObject.put("amount_from_wallet", sessionManager.getUserDetails().get(SessionManager.Wallet));
                    if (price > Double.parseDouble(sessionManager.getUserDetails().get(SessionManager.Wallet))) {
                        jsonObject.put("amount_from_bank", price - Double.parseDouble(sessionManager.getUserDetails().get(SessionManager.Wallet)));
                    } else {
                        jsonObject.put("amount_from_bank", Double.parseDouble(sessionManager.getUserDetails().get(SessionManager.Wallet)) - price);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Call<PaymentTransactionModel> paymentTransactionModelCall = apiService.PAYMENT_TRANSACTION_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), jsonObject.toString());
                paymentTransactionModelCall.enqueue(new Callback<PaymentTransactionModel>() {
                    @Override
                    public void onResponse(Call<PaymentTransactionModel> call, Response<PaymentTransactionModel> response) {
                        Log.i("main_message", response.body().getMessage());
                        String status = response.body().getStatus();
                        progressDialog.dismiss();
                        if (status.equals("1")) {
                            sessionManager.createLoginSession(sessionManager.getUserDetails().get(SessionManager.User_Token), sessionManager.getUserDetails().get(SessionManager.User_ID), sessionManager.getUserDetails().get(SessionManager.User_Name), sessionManager.getUserDetails().get(SessionManager.User_Email), sessionManager.getUserDetails().get(SessionManager.USER_MOBILE), sessionManager.getUserDetails().get(SessionManager.USER_COUNTRY_ID), sessionManager.getUserDetails().get(SessionManager.USER_Profile), response.body().getWallet(), sessionManager.getUserDetails().get(SessionManager.UserType));
                            succesful_cardview.setVisibility(View.VISIBLE);
                            scrollview_pay.setVisibility(View.GONE);
                            Toast.makeText(CompletingPurchasingActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(CompletingPurchasingActivity.this, MainActivity.class));

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
            } else {
                try {
                    jsonObject.put("replace_active_voucher_id", VoucherDetailActivity.voucher_id);
                    jsonObject.put("voucher_payment_detail_id", VoucherDetailActivity.v_payment_id);
                    jsonObject.put("replace_voucher_id", ExchangeAddBalanceActivity.voucher_id);
                    jsonObject.put("card_id", card_ID);
                    jsonObject.put("transaction_id", data.getStringExtra("transaction_id"));
                    if (Double.parseDouble(main_price) > Double.parseDouble(sessionManager.getUserDetails().get(SessionManager.Wallet))) {
                        jsonObject.put("transaction_price", Double.parseDouble(main_price));
                        jsonObject.put("wallet", "0");
                    } else {
                        jsonObject.put("transaction_price", "0");
                        jsonObject.put("wallet", sessionManager.getUserDetails().get(SessionManager.Wallet));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Call<ReplaceVoucherModel> replaceVoucherModelCall = apiService.REPLACE_VOUCHER_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), jsonObject.toString());
                replaceVoucherModelCall.enqueue(new Callback<ReplaceVoucherModel>() {
                    @Override
                    public void onResponse(Call<ReplaceVoucherModel> call, Response<ReplaceVoucherModel> response) {
                        Log.i("response_main", response.body().getStatus());
                        progressDialog.dismiss();
                        String status = response.body().getStatus();
                        if (status.equals("1")) {
                            sessionManager.createLoginSession(sessionManager.getUserDetails().get(SessionManager.User_Token), sessionManager.getUserDetails().get(SessionManager.User_ID), sessionManager.getUserDetails().get(SessionManager.User_Name), sessionManager.getUserDetails().get(SessionManager.User_Email), sessionManager.getUserDetails().get(SessionManager.USER_MOBILE), sessionManager.getUserDetails().get(SessionManager.USER_COUNTRY_ID), sessionManager.getUserDetails().get(SessionManager.USER_Profile), response.body().getWallet(), sessionManager.getUserDetails().get(SessionManager.UserType));
                            VoucherDetailActivity.voucher_name = "";
                            VoucherDetailActivity.date = "";
                            VoucherDetailActivity.barcode = "";
                            VoucherDetailActivity.pincode = "";
                            VoucherDetailActivity.price = "";
                            VoucherDetailActivity.v_image = "";
                            VoucherDetailActivity.v_payment_id = "";
                            VoucherDetailActivity.voucher_id = "";
                            VoucherDetailActivity.v_image = "";
                            ExchangeAddBalanceActivity.main_price = 0;
                            ExchangeAddBalanceActivity.voucher_price = 0;
                            ExchangeAddBalanceActivity.replace_price = 0;
                            ExchangeAddBalanceActivity.temp_price = 0;
                            startActivity(new Intent(CompletingPurchasingActivity.this, MainActivity.class));
                            finish();
                            ExchangeAddBalanceActivity.exchange_activity.finish();
                            ExchangeVoucherActivity.ex_activity.finish();
                            VoucherDetailActivity.voucher_detail_activity.finish();
                            Toast.makeText(CompletingPurchasingActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CompletingPurchasingActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ReplaceVoucherModel> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(CompletingPurchasingActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }


    public void getAllcardList() {
        Log.i("token_session", sessionManager.getUserDetails().get(SessionManager.User_Token));
        Call<GetCardListModel> getCardListModelCall = apiService.GET_CARD_LIST_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token));
        getCardListModelCall.enqueue(new Callback<GetCardListModel>() {
            @Override
            public void onResponse(Call<GetCardListModel> call, Response<GetCardListModel> response) {
                String status = response.body().getStatus();
                if (status.equals("1")) {
                    arrayList = response.body().getData();
                    SavedCardListAdapter savedCardListAdapter = new SavedCardListAdapter(arrayList, CompletingPurchasingActivity.this, apiInterface, progressDialog, sessionManager, price);
                    recyclerView.setAdapter(savedCardListAdapter);
                } else {
                    arrayList = null;
                    startActivity(new Intent(CompletingPurchasingActivity.this, EnterCardDetailsActivity.class));
                }
            }

            @Override
            public void onFailure(Call<GetCardListModel> call, Throwable t) {
                Toast.makeText(CompletingPurchasingActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllcardList();
    }
}
