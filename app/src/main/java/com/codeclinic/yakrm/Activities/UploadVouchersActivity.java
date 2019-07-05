package com.codeclinic.yakrm.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codeclinic.yakrm.Adapter.UploadVouchersAdapter;
import com.codeclinic.yakrm.Models.BrandListItemModel;
import com.codeclinic.yakrm.Models.BrandListModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Utils.Connection_Detector;
import com.codeclinic.yakrm.Utils.SessionManager;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class UploadVouchersActivity extends AppCompatActivity {
    public static String str_scanned = "0";
    RecyclerView recyclerView;
    ImageView img_back;
    UploadVouchersAdapter uploadVouchersAdapter;
    List<BrandListItemModel> arrayList = new ArrayList<>();
    API apiService;
    TextView tv_signout;
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    TextView txt_name, txt_email, txt_phone;
    RoundedImageView vendor_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_vouchers);

        apiService = RestClasses.getClient().create(API.class);
        sessionManager = new SessionManager(this);
        progressDialog = new ProgressDialog(this);

        recyclerView = findViewById(R.id.recyclerView);
        img_back = findViewById(R.id.img_back);
        tv_signout = findViewById(R.id.tv_signout);
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

        tv_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(UploadVouchersActivity.this, R.style.CustomDialogFragment);
                alert.setMessage(getResources().getString(R.string.AreYouSureToLogout));
                alert.setCancelable(false);
                alert.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sessionManager.logoutUser();
                        //finish();
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

        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        txt_name = findViewById(R.id.tv_vendor_name);
        txt_email = findViewById(R.id.tv_vendor_email);
        txt_phone = findViewById(R.id.tv_vendor_phone);

        vendor_image = findViewById(R.id.img_vendor);

        if (Connection_Detector.isInternetAvailable(this)) {
            progressDialog.setMessage("Please Wait");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

            Call<BrandListModel> brandListModelCall = apiService.BRAND_LIST_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token));
            brandListModelCall.enqueue(new Callback<BrandListModel>() {
                @Override
                public void onResponse(Call<BrandListModel> call, Response<BrandListModel> response) {
                    progressDialog.dismiss();
                    String status = response.body().getStatus();
                    if (status.equals("1")) {

                        txt_name.setText(response.body().getVendorName());
                        txt_email.setText(response.body().getVendorEmail());
                        txt_phone.setText(response.body().getVendorMobile());

                        Picasso.with(UploadVouchersActivity.this).load("http://test.yakrm.com/assets/uploads/vendor_images/" + response.body().getVendorImage()).into(vendor_image);

                        arrayList = response.body().getData();
                        uploadVouchersAdapter = new UploadVouchersAdapter(arrayList, UploadVouchersActivity.this);
                        recyclerView.setAdapter(uploadVouchersAdapter);
                    } else {
                        if (status.equals("0")) {
                            arrayList = response.body().getData();
                            uploadVouchersAdapter = new UploadVouchersAdapter(arrayList, UploadVouchersActivity.this);
                            recyclerView.setAdapter(uploadVouchersAdapter);
                            Toast.makeText(UploadVouchersActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            sessionManager.clearsession();
                            sessionManager.logoutUser();
                        }

                    }
                }

                @Override
                public void onFailure(Call<BrandListModel> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(UploadVouchersActivity.this, getResources().getString(R.string.Server_Error), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(UploadVouchersActivity.this, getResources().getString(R.string.No_Internet_Connection), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (str_scanned.equals("1")) {
            str_scanned = "0";
            Toast.makeText(this, getResources().getString(R.string.VoucherScannedSuccessfully), Toast.LENGTH_SHORT).show();
        }
    }

    public static class RestClasses {

        private static final String BASE_URL = "http://test.yakrm.com/api_salesmen/";
        private static Retrofit retrofit = null;

        public static Retrofit getClient() {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;
        }
    }
}
