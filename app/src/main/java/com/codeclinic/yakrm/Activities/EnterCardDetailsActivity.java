package com.codeclinic.yakrm.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.codeclinic.yakrm.Adapter.StoredCardListAdapter;
import com.codeclinic.yakrm.Models.AddCardDetailsModel;
import com.codeclinic.yakrm.Models.GetCardListItemModel;
import com.codeclinic.yakrm.Models.GetCardListModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.Connection_Detector;
import com.codeclinic.yakrm.Utils.SessionManager;
import com.yekmer.cardlib.TwoDigitsCardTextWatcher;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterCardDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView img_back, img_mada_select, img_paypal_select, img_visa_select;
    EditText edt_ex_date, edt_card_no, edt_cvv, edt_name;
    String card_select = "3";
    Integer[] imageArray = {R.drawable.visa, R.drawable.mastercard};
    String name_regex = "^((?:[A-Za-z]+ ?){1,3})$";
    List<GetCardListItemModel> arrayList = new ArrayList<>();
    RecyclerView recyclerView;
    CardView card_list_llayout;
    ScrollView scrollview_pay;

    API apiService;
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    JSONObject jsonObject = new JSONObject();

    Button btn_add, btn_add_another;
    ArrayList<String> listOfPattern = new ArrayList<>();
    String str_card_status = "0";

    public ArrayList<String> listOfPattern() {

        String ptVisa = "^4[0-9]{6,}$";

        listOfPattern.add(ptVisa);

        String ptMasterCard = "^5[1-5][0-9]{5,}$";

        listOfPattern.add(ptMasterCard);

        String ptDiscover = "^6(?:011|5[0-9]{2})[0-9]{3,}$";

        listOfPattern.add(ptDiscover);

        String ptAmeExp = "^3[47][0-9]{5,}$";

        listOfPattern.add(ptAmeExp);

        return listOfPattern;
    }

    public boolean isEmpty(CharSequence character) {
        return character == null || character.length() == 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_card_details);

        Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);

        listOfPattern();

        card_list_llayout = findViewById(R.id.card_list_llayout);
        scrollview_pay = findViewById(R.id.scrollview_pay);

        edt_ex_date = findViewById(R.id.edt_ex_date);
        edt_card_no = findViewById(R.id.edt_card_no);
        edt_cvv = findViewById(R.id.edt_cvv);
        edt_name = findViewById(R.id.edt_name);
        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        img_back = findViewById(R.id.img_back);
        img_mada_select = findViewById(R.id.img_mada_select);
        img_paypal_select = findViewById(R.id.img_paypal_select);
        img_visa_select = findViewById(R.id.img_visa_select);


        progressDialog = new ProgressDialog(this);
        sessionManager = new SessionManager(this);

        apiService = RestClass.getClient().create(API.class);

        btn_add = findViewById(R.id.btn_add);
        btn_add_another = findViewById(R.id.btn_add_another);


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

        btn_add_another.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card_list_llayout.setVisibility(View.GONE);
                scrollview_pay.setVisibility(View.VISIBLE);
            }
        });

        img_visa_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_visa_select.setBackground(getResources().getDrawable(R.drawable.card_selected_background));
                img_mada_select.setBackground(getResources().getDrawable(R.drawable.card_details_item_background));
                card_select = "3";
            }
        });


        img_mada_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_visa_select.setBackground(getResources().getDrawable(R.drawable.card_details_item_background));
                img_mada_select.setBackground(getResources().getDrawable(R.drawable.card_selected_background));
                card_select = "1";
            }
        });

        edt_card_no.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    String ccNum = s.toString();
                    for (String p : listOfPattern) {
                        if (ccNum.matches(p)) {
                            if (p.equals("^4[0-9]{6,}$")) {
                                edt_card_no.setCompoundDrawablesWithIntrinsicBounds(0, 0, imageArray[0], 0);
                                str_card_status = "1";
                            } else if (p.equals("^5[1-5][0-9]{5,}$")) {
                                edt_card_no.setCompoundDrawablesWithIntrinsicBounds(0, 0, imageArray[1], 0);
                                str_card_status = "1";
                            } else {
                                edt_card_no.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            }
                            break;
                        }
                    }

                } else {
                    str_card_status = "0";
                    edt_card_no.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (Connection_Detector.isInternetAvailable(EnterCardDetailsActivity.this)) {
                        if (isEmpty(edt_name.getText().toString())) {
                            Toast.makeText(EnterCardDetailsActivity.this, getResources().getString(R.string.PleaseEnterCardHolderName), Toast.LENGTH_SHORT).show();
                        } else if (!edt_name.getText().toString().matches(name_regex)) {
                            Toast.makeText(EnterCardDetailsActivity.this, getResources().getString(R.string.PleaseEnterValidName), Toast.LENGTH_SHORT).show();
                        } else if (isEmpty(edt_card_no.getText().toString())) {
                            Toast.makeText(EnterCardDetailsActivity.this, getResources().getString(R.string.PleaseEnterCardNumber), Toast.LENGTH_SHORT).show();
                        } else if (edt_card_no.getText().toString().length() != 16) {
                            Toast.makeText(EnterCardDetailsActivity.this, getResources().getString(R.string.PleaseEnterCorrectCardNo), Toast.LENGTH_SHORT).show();
                        } else if (str_card_status.equals("0")) {
                            Toast.makeText(EnterCardDetailsActivity.this, getResources().getString(R.string.pleaseentervalidcardnumber), Toast.LENGTH_SHORT).show();
                        } else if (isEmpty(edt_ex_date.getText().toString())) {
                            Toast.makeText(EnterCardDetailsActivity.this, getResources().getString(R.string.PleaseEnterCardExpirydate), Toast.LENGTH_SHORT).show();
                        } else if (Integer.parseInt(edt_ex_date.getText().toString().substring(3, 5)) >= Integer.parseInt(String.valueOf(year).substring(2, 4))) {
                            Toast.makeText(EnterCardDetailsActivity.this, getResources().getString(R.string.pleasecheckexpirydate), Toast.LENGTH_SHORT).show();
                        } else if (Integer.parseInt(edt_ex_date.getText().toString().substring(0, 2)) >= month + 1) {
                            Toast.makeText(EnterCardDetailsActivity.this, getResources().getString(R.string.pleasecheckexpirydate), Toast.LENGTH_SHORT).show();
                        } else if (isEmpty(edt_cvv.getText().toString())) {
                            Toast.makeText(EnterCardDetailsActivity.this, getResources().getString(R.string.PleaseEnterSecurityNumber), Toast.LENGTH_SHORT).show();
                        } else if (edt_cvv.getText().toString().length() != 3) {
                            Toast.makeText(EnterCardDetailsActivity.this, getResources().getString(R.string.PleaseEnterCorrectSecurityNumber), Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.setMessage("Please Wait");
                            progressDialog.setIndeterminate(true);
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            try {
                                jsonObject.put("payment_method", card_select);
                                jsonObject.put("holder_name", edt_name.getText().toString());
                                jsonObject.put("card_number", edt_card_no.getText().toString());
                                jsonObject.put("expiry_date", edt_ex_date.getText().toString());
                                jsonObject.put("security_number", edt_cvv.getText().toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Call<AddCardDetailsModel> addCardDetailsModelCall = apiService.ADD_CARD_DETAILS_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), jsonObject.toString());
                            addCardDetailsModelCall.enqueue(new Callback<AddCardDetailsModel>() {
                                @Override
                                public void onResponse(Call<AddCardDetailsModel> call, Response<AddCardDetailsModel> response) {
                                    progressDialog.dismiss();
                                    String status = response.body().getStatus();
                                    if (status.equals("1")) {
                                        Toast.makeText(EnterCardDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(EnterCardDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<AddCardDetailsModel> call, Throwable t) {
                                    progressDialog.dismiss();
                                    Toast.makeText(EnterCardDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(EnterCardDetailsActivity.this, R.string.err_no_internet, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        edt_ex_date.addTextChangedListener(new TwoDigitsCardTextWatcher(edt_ex_date, month, year));
        getAllcardList();
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
                    StoredCardListAdapter storedCardListAdapter = new StoredCardListAdapter(arrayList, EnterCardDetailsActivity.this);
                    recyclerView.setAdapter(storedCardListAdapter);
                } else {
                    Toast.makeText(EnterCardDetailsActivity.this, "No Card is Added yet", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetCardListModel> call, Throwable t) {
                Toast.makeText(EnterCardDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
