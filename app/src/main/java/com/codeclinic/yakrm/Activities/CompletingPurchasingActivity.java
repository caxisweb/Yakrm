package com.codeclinic.yakrm.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.codeclinic.yakrm.Adapter.SavedCardListAdapter;
import com.codeclinic.yakrm.Models.GetCardListItemModel;
import com.codeclinic.yakrm.Models.GetCardListModel;
import com.codeclinic.yakrm.Models.PaymentTransactionModel;
import com.codeclinic.yakrm.Models.PrepareTransactionProcessModel;
import com.codeclinic.yakrm.Models.ReplaceVoucherModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.PaymentRestClass;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.SessionManager;
import com.paytabs.paytabs_sdk.payment.ui.activities.SecurePaymentActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class CompletingPurchasingActivity extends AppCompatActivity {

    CardView main_pay_cardview, succesful_cardview, error_cardview;
    Button btn_cmplt_pay;
    ScrollView scrollview_pay;
    public static String card_ID;
    LinearLayout payment_layout;
    LinearLayout rl_pay_pal, rl_mastercard, rl_visa;
    TextView tv_total_price, tv_sc_total_price, tv_wallet_amount;
    RecyclerView recyclerView;
    JSONObject jsonObject = new JSONObject();
    String total_price;

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

                } else {
                    main_pay_cardview.setVisibility(View.GONE);
                    scrollview_pay.setVisibility(View.VISIBLE);
                }
            }
        });
        Retrofit retrofit = PaymentRestClass.getClient();
        apiInterface = retrofit.create(API.class);

        rl_pay_pal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(CompletingPurchasingActivity.this, R.style.CustomDialogFragment);
                alert.setMessage("Are you Sure?");
                alert.setCancelable(false);
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        card_type_select = "2";
                        progressDialog.setMessage("Please Wait");
                        progressDialog.setIndeterminate(true);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                /*        try {
                            jsonObject.put("secret_key", "ex2SHCqdgtJlrF2gp5fGCis3tUGh5EkjcmcTZD7g6RCxwEOWJ3Cml4qOY664KroXOBQNeY3lPFTlkHh4KUq6YQVXW22HtrFh2w4g");
                            jsonObject.put("merchant_email", "ahmed@yakrm.com");
                            jsonObject.put("amount", price);
                            jsonObject.put("title", "Paytabs");
                            jsonObject.put("cc_first_name", "jay");
                            jsonObject.put("cc_last_name", "pokar");
                            jsonObject.put("card_exp", "2106");
                            jsonObject.put("cvv", "123");
                            jsonObject.put("card_number", "4000000000000002");
                            jsonObject.put("original_assignee_code", "SDK");
                            jsonObject.put("currency", "SAR");
                            jsonObject.put("email", "ahmed@yakrm.com");
                            jsonObject.put("skip_email", "1");
                            jsonObject.put("phone_number", "00966551432252");
                            jsonObject.put("order_id", "123456");
                            jsonObject.put("product_name", "Product 1, Product 2");
                            jsonObject.put("customer_email", "jaypokarjdp@gmail.com");
                            jsonObject.put("country_billing", "BHR");
                            jsonObject.put("address_billing", "Flat 1,Building 123, Road 2345");
                            jsonObject.put("city_billing", "Manama");
                            jsonObject.put("state_billing", "Manama");
                            jsonObject.put("postal_code_billing", "00973");
                            jsonObject.put("country_shipping", "BHR");
                            jsonObject.put("address_shipping", "Flat 1,Building 123, Road 2345");
                            jsonObject.put("city_shipping", "Manama");
                            jsonObject.put("state_shipping", "Manama");
                            jsonObject.put("postal_code_shipping", "00973");
                            jsonObject.put("exchange_rate", "1");
                            jsonObject.put("is_tokenization", "TRUE");
                            jsonObject.put("is_existing_customer", "no");
                            jsonObject.put("pt_token", null);
                            jsonObject.put("pt_customer_email", null);
                            jsonObject.put("pt_customer_password", null);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
*/
                        Call<PrepareTransactionProcessModel> prepareTransactionProcessModelCall = apiInterface.prepareTransaction("ex2SHCqdgtJlrF2gp5fGCis3tUGh5EkjcmcTZD7g6RCxwEOWJ3Cml4qOY664KroXOBQNeY3lPFTlkHh4KUq6YQVXW22HtrFh2w4g", "ahmed@yakrm.com", price, "Paytabs", "jay", "pokar", "2106", "123", "4000000000000002", "SDK", "SAR", "ahmed@yakrm.com", "1", "00966551432252", "123456", "Product 1, Product 2", "jaypokarjdp@gmail.com", "BHR", "Flat 1,Building 123, Road 2345", "Manama", "Manama", "00973", "BHR", "Flat 1,Building 123, Road 2345", "Manama", "Manama", "00973", "1", "TRUE", "no", null, null, null);
                        prepareTransactionProcessModelCall.enqueue(new Callback<PrepareTransactionProcessModel>() {
                            @Override
                            public void onResponse(Call<PrepareTransactionProcessModel> call, Response<PrepareTransactionProcessModel> response) {
                                progressDialog.dismiss();
                                Intent in = new Intent(CompletingPurchasingActivity.this, SecurePaymentActivity.class);
                                JSONObject jsonObjectData = new JSONObject();

                                try {
                                    jsonObjectData.put("merchant_email", "ahmed@yakrm.com");
                                    jsonObjectData.put("merchant_secret", "ex2SHCqdgtJlrF2gp5fGCis3tUGh5EkjcmcTZD7g6RCxwEOWJ3Cml4qOY664KroXOBQNeY3lPFTlkHh4KUq6YQVXW22HtrFh2w4g");
                                    jsonObjectData.put("processor", "1");
                                    jsonObjectData.put("acsUrl", response.body().getPayerAuthEnrollReply().getAcsURL());
                                    jsonObjectData.put("paReq", response.body().getPayerAuthEnrollReply().getPaReq());
                                    jsonObjectData.put("xid", response.body().getPayerAuthEnrollReply().getXid());
                                    jsonObjectData.put("local_transaction_id", response.body().getLocalTransactionId());
                                    jsonObjectData.put("merchant_id", response.body().getMerchantId());
                                    jsonObjectData.put("rating", "3");
                                    jsonObjectData.put("signature", "test");
                                    jsonObjectData.put("amount", price);
                                    jsonObjectData.put("currency", "SAR");
                                    jsonObjectData.put("store_name", "Yakrm");
                                    jsonObjectData.put("cc_holder_name", "jay pokar");
                                    jsonObjectData.put("is_tokenization", "TRUE");
                                } catch (JSONException var19) {
                                    var19.printStackTrace();
                                }

                                in.putExtra("data", jsonObjectData.toString());
                                in.putExtra("language", "en");

                                startActivityForResult(in, 1001);
                            }

                            @Override
                            public void onFailure(Call<PrepareTransactionProcessModel> call, Throwable t) {
                                progressDialog.dismiss();
                                Log.i("response", t.toString());
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
        getAllcardList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1001) {
      /*      Log.e("Tag", data.getStringExtra(PaymentParams.RESPONSE_CODE));
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
