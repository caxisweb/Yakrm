package com.codeclinic.yakrm.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codeclinic.yakrm.Adapter.SavedCardListAdapter;
import com.codeclinic.yakrm.Hyperpay_checkout.BasePaymentActivity;
import com.codeclinic.yakrm.Hyperpay_checkout.Constants;
import com.codeclinic.yakrm.LocalNotification.NotificationHelper;
import com.codeclinic.yakrm.Models.GetCardListItemModel;
import com.codeclinic.yakrm.Models.GetCardListModel;
import com.codeclinic.yakrm.Models.GetCheckoutIDModel;
import com.codeclinic.yakrm.Models.PaymentStatusModel;
import com.codeclinic.yakrm.Models.PaymentTransactionModel;
import com.codeclinic.yakrm.Models.ReplaceGiftVoucherModel;
import com.codeclinic.yakrm.Models.ReplaceVoucherModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.SessionManager;
import com.oppwa.mobile.connect.checkout.dialog.CheckoutActivity;
import com.oppwa.mobile.connect.exception.PaymentError;
import com.oppwa.mobile.connect.exception.PaymentException;
import com.oppwa.mobile.connect.payment.BrandsValidation;
import com.oppwa.mobile.connect.payment.CheckoutInfo;
import com.oppwa.mobile.connect.payment.ImagesRequest;
import com.oppwa.mobile.connect.payment.PaymentParams;
import com.oppwa.mobile.connect.payment.card.CardPaymentParams;
import com.oppwa.mobile.connect.provider.Connect;
import com.oppwa.mobile.connect.provider.ITransactionListener;
import com.oppwa.mobile.connect.provider.Transaction;
import com.oppwa.mobile.connect.provider.TransactionType;
import com.oppwa.mobile.connect.service.ConnectService;
import com.oppwa.mobile.connect.service.IProviderBinder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.codeclinic.yakrm.Activities.VoucherDetailActivity.voucher_gift_send_id;
import static com.codeclinic.yakrm.Activities.VoucherDetailActivity.voucher_id;


public class CompletingPurchasingActivity extends BasePaymentActivity implements ITransactionListener {

    public static String card_ID;
    CardView main_pay_cardview, succesful_cardview, error_cardview;
    Button btn_cmplt_pay;
    LinearLayout scrollview_pay;
    LinearLayout payment_layout;
    TextView tv_total_price, tv_sc_total_price, tv_wallet_amount;
    RecyclerView recyclerView;
    JSONObject jsonObject = new JSONObject();
    JSONObject replace_gift_voucher = new JSONObject();
    String total_price, checkoutId;
    String resourcePath = "0";
    String checkcredit;
    ImageView img_back;
    API apiService;
    API apiInterface;
    SessionManager sessionManager;
    ProgressDialog progressDialog;
    double price;
    List<GetCardListItemModel> arrayList = new ArrayList<>();
    String card_holder_name, card_expiry_date, card_cvv, card_no, flag_cart = "1", main_price;
    String[] card_date;
    double transaction_main_price;
    String ptVisa = "^4[0-9]{6,}$";
    String ptMasterCard = "^5[1-5][0-9]{5,}$";
    private IProviderBinder binder;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (IProviderBinder) service;
            binder.addTransactionListener(CompletingPurchasingActivity.this);
            /* we have a connection to the service */
            try {
                binder.initializeProvider(Connect.ProviderMode.LIVE);
            } catch (PaymentException ee) {

            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            binder = null;
        }
    };

    @SuppressLint("SetTextI18n")
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
            tv_total_price.setText(String.format("%.2f", t_price) + getResources().getString(R.string.SR_currency));

            tv_sc_total_price.setText(String.format("%.2f", t_price) + " " + getResources().getString(R.string.SR_currency));
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
                    alert.setMessage(getResources().getString(R.string.are_you_sure));
                    alert.setCancelable(false);
                    alert.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @SuppressLint("StaticFieldLeak")
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
                            progressDialog.setIndeterminate(true);
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            if (flag_cart.equals("3")) {
                                try {
                                    replace_gift_voucher.put("replace_active_gift_voucher_id", voucher_id);
                                    replace_gift_voucher.put("voucher_gift_id", voucher_gift_send_id);
                                    replace_gift_voucher.put("replace_voucher_id", ExchangeAddBalanceActivity.voucher_id);
                                    replace_gift_voucher.put("transaction_id", "");
                                    replace_gift_voucher.put("transaction_price", "0");
                                    replace_gift_voucher.put("card_id", "");
                                    replace_gift_voucher.put("wallet", price);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                CallReplaceGiftVoucher();

                            } else if (flag_cart.equals("2")) {
                                try {
                                    jsonObject.put("replace_active_voucher_id", voucher_id);
                                    jsonObject.put("voucher_payment_detail_id", VoucherDetailActivity.v_payment_id);
                                    jsonObject.put("replace_voucher_id", ExchangeAddBalanceActivity.voucher_id);
                                    jsonObject.put("card_id", arrayList.get(0).getId());
                                    jsonObject.put("transaction_id", DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString());
                                    jsonObject.put("transaction_price", Double.parseDouble(main_price));
                                    jsonObject.put("wallet", sessionManager.getUserDetails().get(SessionManager.Wallet));
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
                                            voucher_id = "";
                                            VoucherDetailActivity.v_image = "";
                                            ExchangeAddBalanceActivity.main_price = 0;
                                            ExchangeAddBalanceActivity.voucher_price = 0;
                                            ExchangeAddBalanceActivity.replace_price = 0;
                                            ExchangeAddBalanceActivity.temp_price = 0;
                                            startActivity(new Intent(CompletingPurchasingActivity.this, MainActivity.class));
                                            finishAffinity();
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
                                        if (response.isSuccessful()) {
                                            if (status.equals("1")) {
                                                sessionManager.setReminderStatus(false);
                                                NotificationHelper.cancelAlarmElapsed();
                                                NotificationHelper.disableBootReceiver(getApplicationContext());
                                                sessionManager.createLoginSession(sessionManager.getUserDetails().get(SessionManager.User_Token), sessionManager.getUserDetails().get(SessionManager.User_ID), sessionManager.getUserDetails().get(SessionManager.User_Name), sessionManager.getUserDetails().get(SessionManager.User_Email), sessionManager.getUserDetails().get(SessionManager.USER_MOBILE), sessionManager.getUserDetails().get(SessionManager.USER_COUNTRY_ID), sessionManager.getUserDetails().get(SessionManager.USER_Profile), response.body().getWallet(), sessionManager.getUserDetails().get(SessionManager.UserType));
                                                succesful_cardview.setVisibility(View.VISIBLE);
                                                scrollview_pay.setVisibility(View.GONE);
                                                Toast.makeText(CompletingPurchasingActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                finishAffinity();
                                                startActivity(new Intent(CompletingPurchasingActivity.this, MainActivity.class));
                                            } else {
                                                scrollview_pay.setVisibility(View.GONE);
                                                error_cardview.setVisibility(View.VISIBLE);
                                                Toast.makeText(CompletingPurchasingActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(CompletingPurchasingActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
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
                        }
                    }).setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();

                } else if (Double.parseDouble(sessionManager.getUserDetails().get(SessionManager.Wallet)) == price) {
                    if (flag_cart.equals("1")) {
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
                                    sessionManager.setReminderStatus(false);
                                    NotificationHelper.cancelAlarmElapsed();
                                    NotificationHelper.disableBootReceiver(getApplicationContext());
                                    sessionManager.createLoginSession(sessionManager.getUserDetails().get(SessionManager.User_Token), sessionManager.getUserDetails().get(SessionManager.User_ID), sessionManager.getUserDetails().get(SessionManager.User_Name), sessionManager.getUserDetails().get(SessionManager.User_Email), sessionManager.getUserDetails().get(SessionManager.USER_MOBILE), sessionManager.getUserDetails().get(SessionManager.USER_COUNTRY_ID), sessionManager.getUserDetails().get(SessionManager.USER_Profile), response.body().getWallet(), sessionManager.getUserDetails().get(SessionManager.UserType));
                                    succesful_cardview.setVisibility(View.VISIBLE);
                                    scrollview_pay.setVisibility(View.GONE);
                                    Toast.makeText(CompletingPurchasingActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    finishAffinity();
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
                    } else if (flag_cart.equals("3")) {
                        try {
                            replace_gift_voucher.put("replace_active_gift_voucher_id", voucher_id);
                            replace_gift_voucher.put("voucher_gift_id", voucher_gift_send_id);
                            replace_gift_voucher.put("replace_voucher_id", ExchangeAddBalanceActivity.voucher_id);
                            replace_gift_voucher.put("transaction_id", "");
                            replace_gift_voucher.put("transaction_price", "0");
                            replace_gift_voucher.put("card_id", "");
                            replace_gift_voucher.put("wallet", price);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        CallReplaceGiftVoucher();

                    } else {
                        try {
                            jsonObject.put("replace_active_voucher_id", voucher_id);
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
                                    voucher_id = "";
                                    VoucherDetailActivity.v_image = "";
                                    ExchangeAddBalanceActivity.main_price = 0;
                                    ExchangeAddBalanceActivity.voucher_price = 0;
                                    ExchangeAddBalanceActivity.replace_price = 0;
                                    ExchangeAddBalanceActivity.temp_price = 0;
                                    startActivity(new Intent(CompletingPurchasingActivity.this, MainActivity.class));
                                    finishAffinity();
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
                } else {
                    main_pay_cardview.setVisibility(View.GONE);
                    scrollview_pay.setVisibility(View.VISIBLE);
                }
            }
        });

        getAllcardList();
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

    public void getAllcardList() {
        Log.i("token_session", sessionManager.getUserDetails().get(SessionManager.User_Token));
        Call<GetCardListModel> getCardListModelCall = apiService.GET_CARD_LIST_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token));
        getCardListModelCall.enqueue(new Callback<GetCardListModel>() {
            @Override
            public void onResponse(Call<GetCardListModel> call, Response<GetCardListModel> response) {
                String status = response.body().getStatus();
                if (status.equals("1")) {
                    arrayList = response.body().getData();
                    recyclerView.setAdapter(new SavedCardListAdapter(arrayList, CompletingPurchasingActivity.this, apiInterface, progressDialog, sessionManager, price, new SavedCardListAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(final GetCardListItemModel item) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(CompletingPurchasingActivity.this, R.style.CustomDialogFragment);
                            alert.setMessage(getResources().getString(R.string.are_you_sure));
                            alert.setCancelable(false);
                            alert.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                                @SuppressLint("StaticFieldLeak")
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    card_ID = item.getId();
                                    card_cvv = item.getSecurityNumber();
                                    card_holder_name = item.getHolderName();
                                    card_expiry_date = item.getExpiryDate();
                                    card_date = card_expiry_date.split("/");
                                    card_date[0] = card_date[0].trim();
                                    card_no = item.getCardNumber();

                                    transaction_main_price = price - Double.parseDouble(sessionManager.getUserDetails().get(SessionManager.Wallet));
                                    JSONObject jobj = new JSONObject();
                                    String checkValue = String.valueOf(transaction_main_price);

                                    if (checkValue.substring(0, 1).equals("0")) {

                                        try {

                                            jobj.put("price", 0 + String.format(Locale.US, "%.2f", transaction_main_price));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        try {
                                            jobj.put("price", String.format(Locale.US, "%.2f", transaction_main_price));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }


                                    Call<GetCheckoutIDModel> getCheckoutIDModelCall = apiService.GET_CHECKOUT_ID_MODEL_CALL(jobj.toString());
                                    getCheckoutIDModelCall.enqueue(new Callback<GetCheckoutIDModel>() {
                                        @Override
                                        public void onResponse(Call<GetCheckoutIDModel> call, Response<GetCheckoutIDModel> response) {
                                            checkoutId = response.body().getId();

                                            if (card_no.matches(ptVisa)) {
                                                checkcredit = "VISA";
                                            } else if (card_no.matches(ptMasterCard)) {
                                                checkcredit = "MASTER";
                                            } else {
                                                checkcredit = "VISA";
                                            }

                                            try {
                                                binder.requestCheckoutInfo(checkoutId);

                                            } catch (PaymentException e) {
                                                e.printStackTrace();
                                            }

                                            //testPurchase();

                                        }

                                        @Override
                                        public void onFailure(Call<GetCheckoutIDModel> call, Throwable t) {
                                            Toast.makeText(CompletingPurchasingActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            }).setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog alertDialog = alert.create();
                            alertDialog.show();
                        }
                    }));
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
    public void onCheckoutIdReceived(String checkoutId) {
        super.onCheckoutIdReceived(checkoutId);

        if (checkoutId != null) {
            this.checkoutId = checkoutId;
            requestCheckoutInfo(checkoutId);
        }
    }

    private void requestCheckoutInfo(String checkoutId) {
        if (binder != null) {
            try {
                binder.requestCheckoutInfo(checkoutId);
                showProgressDialog(R.string.progress_message_checkout_info);
            } catch (PaymentException e) {
                showAlertDialog(e.getMessage());
            }
        }
    }

    private void pay(String checkoutId) {
        try {

            PaymentParams paymentParams = createPaymentParams(checkoutId);
            //paymentParams.setShopperResultUrl(getString(R.string.custom_ui_callback_scheme) + "://result");
            Transaction transaction = new Transaction(paymentParams);

            binder.submitTransaction(transaction);
            showProgressDialog(R.string.progress_message_processing_payment);

        } catch (PaymentException e) {
            showErrorDialog(e.getError());
        }
    }

    private PaymentParams createPaymentParams(String checkoutId) throws PaymentException {


        return new CardPaymentParams(
                checkoutId,
                Constants.Config.CARD_BRAND,
                card_no,
                card_holder_name,
                card_date[0],
                "20" + card_date[1],
                card_cvv
        );
    }

    @Override
    public void brandsValidationRequestSucceeded(BrandsValidation brandsValidation) {

    }

    @Override
    public void brandsValidationRequestFailed(PaymentError paymentError) {

    }

    @Override
    public void imagesRequestSucceeded(ImagesRequest imagesRequest) {

    }

    @Override
    public void imagesRequestFailed() {

    }

    @Override
    public void paymentConfigRequestSucceeded(final CheckoutInfo checkoutInfo) {
        hideProgressDialog();

        if (checkoutInfo == null) {
            //showErrorDialog(getString(R.string.error_message));
            showErrorDialog(getString(R.string.error_message));

            return;
        }

        /* Get the resource path from checkout info to request the payment status later. */
        //resourcePath = checkoutInfo.getResourcePath();
        resourcePath = checkoutId;


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showConfirmationDialog(
                        String.valueOf(checkoutInfo.getAmount()),
                        checkoutInfo.getCurrencyCode()
                );
            }
        });
    }

    private void showConfirmationDialog(String amount, String currency) {
        new android.support.v7.app.AlertDialog.Builder(this)
                .setMessage(String.format(getString(R.string.message_payment_confirmation), amount, currency))
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pay(checkoutId);
                    }
                })
                .setNegativeButton(R.string.button_cancel, null)
                .setCancelable(false)
                .show();
    }

    @Override
    public void paymentConfigRequestFailed(PaymentError paymentError) {
        hideProgressDialog();
        showErrorDialog(paymentError);
    }

    @Override
    public void transactionCompleted(Transaction transaction) {
        hideProgressDialog();

        if (transaction == null) {
            //showErrorDialog(getString(R.string.error_message));
            showErrorDialog(transaction.getRedirectUrl());
            return;
        }

        if (transaction.getTransactionType() == TransactionType.SYNC) {
            /* check the status of synchronous transaction */
            //requestPaymentStatus(resourcePath);
            getpaystatus();
        } else {
            /* wait for the callback in the onNewIntent() */
            showProgressDialog(R.string.progress_message_please_wait);
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(transaction.getRedirectUrl())));
        }
    }

    @Override
    public void transactionFailed(Transaction transaction, PaymentError paymentError) {
        hideProgressDialog();
        showErrorDialog(paymentError);
    }

    private void showErrorDialog(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showAlertDialog(message);
            }
        });
    }

    private void showErrorDialog(PaymentError paymentError) {
        showErrorDialog(paymentError.getErrorMessage());
    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getScheme().equals("livemosab")) {
            checkoutId = intent.getData().getQueryParameter("id");
            getpaystatus();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case CheckoutActivity.RESULT_OK:
                //transaction completed
                Transaction transaction = data.getParcelableExtra(CheckoutActivity.CHECKOUT_RESULT_TRANSACTION);

                //resource path if needed
                String resourcePath = data.getStringExtra(CheckoutActivity.CHECKOUT_RESULT_RESOURCE_PATH);

                if (transaction.getTransactionType() == TransactionType.SYNC) {
                    //  check the result of synchronous transaction
                } else {
                    //wait for the asynchronous transaction callback in the onNewIntent()
                }
                break;
            case CheckoutActivity.RESULT_CANCELED:
                //shopper canceled the checkout process
                Toast.makeText(getBaseContext(), "canceled", Toast.LENGTH_LONG).show();
                break;
            case CheckoutActivity.RESULT_ERROR:
                //error occurred

                PaymentError error = data.getParcelableExtra(CheckoutActivity.CHECKOUT_RESULT_ERROR);

                Toast.makeText(getBaseContext(), "error", Toast.LENGTH_LONG).show();

                Log.e("errorrr", String.valueOf(error.getErrorInfo()));

                Log.e("errorrr2", String.valueOf(error.getErrorCode()));

                Log.e("errorrr3", error.getErrorMessage());

                Log.e("errorrr4", String.valueOf(error.describeContents()));

        }
    }


    public String formatNumber(int decimals, double number) {
        StringBuilder sb = new StringBuilder(decimals + 3);
        sb.append("#.");
        for (int i = 0; i < decimals; i++) {
            sb.append("0");
        }
        return new DecimalFormat(sb.toString()).format(number);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllcardList();
    }

    public void callSimplePurcahaseVoucher() {
        Call<PaymentTransactionModel> paymentTransactionModelCall = apiService.PAYMENT_TRANSACTION_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), jsonObject.toString());
        paymentTransactionModelCall.enqueue(new Callback<PaymentTransactionModel>() {
            @Override
            public void onResponse(Call<PaymentTransactionModel> call, Response<PaymentTransactionModel> response) {
                Log.i("main_message", response.body().getMessage());
                String status = response.body().getStatus();
                progressDialog.dismiss();
                if (status.equals("1")) {
                    sessionManager.setReminderStatus(false);
                    NotificationHelper.cancelAlarmElapsed();
                    NotificationHelper.disableBootReceiver(getApplicationContext());
                    sessionManager.createLoginSession(sessionManager.getUserDetails().get(SessionManager.User_Token), sessionManager.getUserDetails().get(SessionManager.User_ID), sessionManager.getUserDetails().get(SessionManager.User_Name), sessionManager.getUserDetails().get(SessionManager.User_Email), sessionManager.getUserDetails().get(SessionManager.USER_MOBILE), sessionManager.getUserDetails().get(SessionManager.USER_COUNTRY_ID), sessionManager.getUserDetails().get(SessionManager.USER_Profile), response.body().getWallet(), sessionManager.getUserDetails().get(SessionManager.UserType));
                    succesful_cardview.setVisibility(View.VISIBLE);
                    scrollview_pay.setVisibility(View.GONE);
                    Toast.makeText(CompletingPurchasingActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CompletingPurchasingActivity.this, MainActivity.class));
                    finish();

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

    public void CallReplaceGiftVoucher() {

        Call<ReplaceGiftVoucherModel> replaceGiftVoucherModelCall = apiService.REPLACE_GIFT_VOUCHER_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), replace_gift_voucher.toString());

        replaceGiftVoucherModelCall.enqueue(new Callback<ReplaceGiftVoucherModel>() {
            @Override
            public void onResponse(Call<ReplaceGiftVoucherModel> call, Response<ReplaceGiftVoucherModel> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    if (status.equals("1")) {
                        sessionManager.createLoginSession(sessionManager.getUserDetails().get(SessionManager.User_Token), sessionManager.getUserDetails().get(SessionManager.User_ID), sessionManager.getUserDetails().get(SessionManager.User_Name), sessionManager.getUserDetails().get(SessionManager.User_Email), sessionManager.getUserDetails().get(SessionManager.USER_MOBILE), sessionManager.getUserDetails().get(SessionManager.USER_COUNTRY_ID), sessionManager.getUserDetails().get(SessionManager.USER_Profile), response.body().getWallet(), sessionManager.getUserDetails().get(SessionManager.UserType));
                        finishAffinity();
                        startActivity(new Intent(CompletingPurchasingActivity.this, MainActivity.class));

                    } else {
                        Toast.makeText(CompletingPurchasingActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CompletingPurchasingActivity.this, "Please Try Again!!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReplaceGiftVoucherModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(CompletingPurchasingActivity.this, "Please Try Again!!!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void callReplaceVoucher() {
        Call<ReplaceVoucherModel> replaceVoucherModelCall = apiService.REPLACE_VOUCHER_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), jsonObject.toString());
        replaceVoucherModelCall.enqueue(new Callback<ReplaceVoucherModel>() {
            @Override
            public void onResponse(Call<ReplaceVoucherModel> call, Response<ReplaceVoucherModel> response) {
                Log.i("response_main", response.body().getStatus());
                progressDialog.dismiss();
                String status = response.body().getStatus();
                if (status.equals("1")) {
                    sessionManager.createLoginSession(sessionManager.getUserDetails().get(SessionManager.User_Token), sessionManager.getUserDetails().get(SessionManager.User_ID), sessionManager.getUserDetails().get(SessionManager.User_Name), sessionManager.getUserDetails().get(SessionManager.User_Email), sessionManager.getUserDetails().get(SessionManager.USER_MOBILE), sessionManager.getUserDetails().get(SessionManager.USER_COUNTRY_ID), sessionManager.getUserDetails().get(SessionManager.USER_Profile), response.body().getWallet(), sessionManager.getUserDetails().get(SessionManager.UserType));
                    finishAffinity();
                    startActivity(new Intent(CompletingPurchasingActivity.this, MainActivity.class));

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

    private void getpaystatus() {

        JSONObject jobj = new JSONObject();
        try {
            //jobj.put("checkout_id", resourcePath);
            jobj.put("checkout_id", checkoutId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Call<PaymentStatusModel> paymentStatusModelCall = apiService.PAYMENT_STATUS_MODEL_CALL(jobj.toString());
        paymentStatusModelCall.enqueue(new Callback<PaymentStatusModel>() {
            @Override
            public void onResponse(Call<PaymentStatusModel> call, Response<PaymentStatusModel> response) {
          /*      Log.i("response", response.body().getResult().getCode());

                Toast.makeText(getApplicationContext(), response.body().getDescriptor(), Toast.LENGTH_LONG).show();*/
                if (response.body().getResult().getCode().equals("000.000.000")) {

                    progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    if (flag_cart.equals("1")) {

                        try {
                            jsonObject.put("transaction_id", response.body().getId());
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
                        callSimplePurcahaseVoucher();
                    } else if (flag_cart.equals("3")) {

                        try {
                            replace_gift_voucher.put("replace_active_gift_voucher_id", voucher_id);
                            replace_gift_voucher.put("voucher_gift_id", voucher_gift_send_id);
                            replace_gift_voucher.put("replace_voucher_id", ExchangeAddBalanceActivity.voucher_id);
                            replace_gift_voucher.put("transaction_id", response.body().getId());
                            replace_gift_voucher.put("card_id", card_ID);
                            replace_gift_voucher.put("wallet", Double.parseDouble(sessionManager.getUserDetails().get(SessionManager.Wallet)));
                            if (Double.parseDouble(main_price) > Double.parseDouble(sessionManager.getUserDetails().get(SessionManager.Wallet))) {
                                replace_gift_voucher.put("transaction_price", Double.parseDouble(main_price) - Double.parseDouble(sessionManager.getUserDetails().get(SessionManager.Wallet)));
                            } else {
                                replace_gift_voucher.put("transaction_price", Double.parseDouble(sessionManager.getUserDetails().get(SessionManager.Wallet)) - Double.parseDouble(main_price));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        CallReplaceGiftVoucher();
                    } else {
                        try {
                            jsonObject.put("replace_active_voucher_id", voucher_id);
                            jsonObject.put("voucher_payment_detail_id", VoucherDetailActivity.v_payment_id);
                            jsonObject.put("replace_voucher_id", ExchangeAddBalanceActivity.voucher_id);
                            jsonObject.put("card_id", card_ID);
                            jsonObject.put("transaction_id", response.body().getId());
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
                        callReplaceVoucher();
                    }
                } else {
                    showErrorDialog(response.body().getResult().getDescription());
                }
            }

            @Override
            public void onFailure(Call<PaymentStatusModel> call, Throwable t) {
                Toast.makeText(CompletingPurchasingActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void testPurchase() {
        JSONObject jobj = new JSONObject();
        try {
            //jobj.put("checkout_id", resourcePath);
            jobj.put("checkout_id", checkoutId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Call<PaymentStatusModel> paymentStatusModelCall = apiService.PAYMENT_STATUS_MODEL_CALL(jobj.toString());
        paymentStatusModelCall.enqueue(new Callback<PaymentStatusModel>() {
            @Override
            public void onResponse(Call<PaymentStatusModel> call, Response<PaymentStatusModel> response) {

                progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.show();
                if (flag_cart.equals("1")) {
                    try {
                        jsonObject.put("transaction_id", response.body().getId());
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
                                sessionManager.setReminderStatus(false);
                                NotificationHelper.cancelAlarmElapsed();
                                NotificationHelper.disableBootReceiver(getApplicationContext());
                                sessionManager.createLoginSession(sessionManager.getUserDetails().get(SessionManager.User_Token), sessionManager.getUserDetails().get(SessionManager.User_ID), sessionManager.getUserDetails().get(SessionManager.User_Name), sessionManager.getUserDetails().get(SessionManager.User_Email), sessionManager.getUserDetails().get(SessionManager.USER_MOBILE), sessionManager.getUserDetails().get(SessionManager.USER_COUNTRY_ID), sessionManager.getUserDetails().get(SessionManager.USER_Profile), response.body().getWallet(), sessionManager.getUserDetails().get(SessionManager.UserType));
                                succesful_cardview.setVisibility(View.VISIBLE);
                                scrollview_pay.setVisibility(View.GONE);
                                Toast.makeText(CompletingPurchasingActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(CompletingPurchasingActivity.this, MainActivity.class));
                                finish();

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
                } else if (flag_cart.equals("3")) {
                    try {
                        replace_gift_voucher.put("replace_active_gift_voucher_id", voucher_id);
                        replace_gift_voucher.put("voucher_gift_id", voucher_gift_send_id);
                        replace_gift_voucher.put("replace_voucher_id", ExchangeAddBalanceActivity.voucher_id);
                        replace_gift_voucher.put("transaction_id", response.body().getId());
                        replace_gift_voucher.put("card_id", card_ID);
                        replace_gift_voucher.put("wallet", Double.parseDouble(sessionManager.getUserDetails().get(SessionManager.Wallet)));
                        if (Double.parseDouble(main_price) > Double.parseDouble(sessionManager.getUserDetails().get(SessionManager.Wallet))) {
                            replace_gift_voucher.put("transaction_price", Double.parseDouble(main_price) - Double.parseDouble(sessionManager.getUserDetails().get(SessionManager.Wallet)));
                        } else {
                            replace_gift_voucher.put("transaction_price", Double.parseDouble(sessionManager.getUserDetails().get(SessionManager.Wallet)) - Double.parseDouble(main_price));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    CallReplaceGiftVoucher();
                } else {
                    try {
                        jsonObject.put("replace_active_voucher_id", voucher_id);
                        jsonObject.put("voucher_payment_detail_id", VoucherDetailActivity.v_payment_id);
                        jsonObject.put("replace_voucher_id", ExchangeAddBalanceActivity.voucher_id);
                        jsonObject.put("card_id", card_ID);
                        jsonObject.put("transaction_id", response.body().getId());
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
                                finishAffinity();
                                startActivity(new Intent(CompletingPurchasingActivity.this, MainActivity.class));

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

            @Override
            public void onFailure(Call<PaymentStatusModel> call, Throwable t) {
                Toast.makeText(CompletingPurchasingActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
