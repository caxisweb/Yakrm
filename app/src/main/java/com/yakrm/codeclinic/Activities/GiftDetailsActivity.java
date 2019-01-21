package com.yakrm.codeclinic.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yakrm.codeclinic.Adapter.GiftDetailListAdapter;
import com.yakrm.codeclinic.Models.VoucherDetailsListItemModel;
import com.yakrm.codeclinic.Models.VoucherDetailsListModel;
import com.yakrm.codeclinic.R;
import com.yakrm.codeclinic.Retrofit.API;
import com.yakrm.codeclinic.Retrofit.RestClass;
import com.yakrm.codeclinic.Utils.Connection_Detector;
import com.yakrm.codeclinic.Utils.ImageURL;
import com.yakrm.codeclinic.Utils.SessionManager;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GiftDetailsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    GiftDetailListAdapter giftDetailListAdapter;
    List<VoucherDetailsListItemModel> arrayList = new ArrayList<>();
    ArrayList<String> ar_add_cart_value = new ArrayList<>();
    API apiService;
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    Button btn_complete;
    TextView tv_brand_name, tv_description;
    ImageView img_brand_img, img_back, img_fav, img_share;
    String vendor_id;

    JSONObject jsonObject = new JSONObject();

    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_details);

        recyclerView = findViewById(R.id.recyclerView);
        img_back = findViewById(R.id.img_back);
        img_fav = findViewById(R.id.img_fav);
        img_share = findViewById(R.id.img_share);
        img_brand_img = findViewById(R.id.img_brand_img);
        btn_complete = findViewById(R.id.btn_complete);

        tv_brand_name = findViewById(R.id.tv_brand_name);
        tv_description = findViewById(R.id.tv_description);

        sessionManager = new SessionManager(this);
        apiService = RestClass.getClient().create(API.class);
        progressDialog = new ProgressDialog(this);
        vendor_id = getIntent().getStringExtra("vendor_id");
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .color(getResources().getColor(R.color.dot_light_intro))
                .sizeResId(R.dimen.divider)
                .build());


        if (Connection_Detector.isInternetAvailable(this)) {
            progressDialog.setMessage("Please Wait");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

            try {
                jsonObject.put("vendor_id", vendor_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Call<VoucherDetailsListModel> voucherDetailsListModelCall = apiService.VOUCHER_DETAILS_LIST_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), jsonObject.toString());
            voucherDetailsListModelCall.enqueue(new Callback<VoucherDetailsListModel>() {
                @Override
                public void onResponse(Call<VoucherDetailsListModel> call, Response<VoucherDetailsListModel> response) {
                    progressDialog.dismiss();
                    String status = response.body().getStatus();
                    if (status.equals("1")) {
                        Picasso.with(GiftDetailsActivity.this).load(ImageURL.Vendor_brand_image + response.body().getBrandImage()).into(img_brand_img);
                        tv_brand_name.setText(response.body().getBrandName());
                        tv_description.setText(response.body().getDescription());
                        arrayList = response.body().getVouchers();
                        for (int i = 0; i < arrayList.size(); i++) {
                            ar_add_cart_value.add("0");
                        }
                        giftDetailListAdapter = new GiftDetailListAdapter(arrayList, GiftDetailsActivity.this, apiService, ar_add_cart_value, sessionManager);
                        recyclerView.setAdapter(giftDetailListAdapter);
                    } else {
                        Toast.makeText(GiftDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<VoucherDetailsListModel> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(GiftDetailsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }


        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LayoutInflater inflater = getLayoutInflater();
                dialogBuilder = new AlertDialog.Builder(GiftDetailsActivity.this);
                final View dialogView = inflater.inflate(R.layout.custom_share_layout, null);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setCancelable(true);

                alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });

        img_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GiftDetailsActivity.this, FavouritesActivity.class));
            }
        });

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toasty.custom(GiftDetailsActivity.this, getResources().getString(R.string.Added_to_the_buying_basket_successfully), getResources().getDrawable(R.mipmap.ic_tick_inside), getResources().getColor(R.color.toast_color), 2000, true, true).show();
                startActivity(new Intent(GiftDetailsActivity.this, CartActivity.class));
            }
        });

    }
}
