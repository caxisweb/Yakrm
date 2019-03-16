package com.codeclinic.yakrm.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bottlerocketstudios.barcode.generation.ui.BarcodeView;
import com.codeclinic.yakrm.Models.ScanVoucherModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Utils.ImageURL;
import com.codeclinic.yakrm.Utils.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UploadVoucherDataActivity extends AppCompatActivity {

    LinearLayout layout_scan_img, llayout_scan, llayout_scan_detail, llayout_manual_voucher_details;
    Button btn_add_note1, btn_done;
    public static String name, brand_id, voucher_number, pin_number, voucher_value, exp_date;
    EditText edt_voucher_no;
    public static Activity pre_activity;
    TextView tv_item_name, tv_enter_manualy, tv_voucher_no, tv_pin_no, tv_voucher_price, tv_exp_date;
    JSONObject jsonObject = new JSONObject();
    ProgressDialog progressDialog;
    ImageView img_back, img_brand;
    private static final int ZXING_CAMERA_PERMISSION = 13;
    private Class<?> mClss;
    BarcodeView barcodeView;
    API apiService;
    String final_date;

    SessionManager sessionManager;

    public boolean isEmpty(CharSequence character) {
        return character == null || character.length() == 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_voucher_data);

        apiService = UploadVouchersActivity.RestClasses.getClient().create(API.class);

        tv_item_name = findViewById(R.id.tv_item_name);
        tv_enter_manualy = findViewById(R.id.tv_enter_manualy);

        llayout_manual_voucher_details = findViewById(R.id.llayout_manual_voucher_details);
        llayout_scan_detail = findViewById(R.id.llayout_scan_detail);
        llayout_scan = findViewById(R.id.llayout_scan);
        layout_scan_img = findViewById(R.id.layout_scan_img);
        barcodeView = findViewById(R.id.generation_barcode_image);

        img_back = findViewById(R.id.img_back);
        img_brand = findViewById(R.id.img_brand);

        tv_voucher_no = findViewById(R.id.tv_voucher_no);
        tv_pin_no = findViewById(R.id.tv_pin_no);
        tv_voucher_price = findViewById(R.id.tv_voucher_price);
        tv_exp_date = findViewById(R.id.tv_exp_date);

        edt_voucher_no = findViewById(R.id.edt_voucher_no);

        progressDialog = new ProgressDialog(this);
        sessionManager = new SessionManager(this);

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

        tv_item_name.setText(getIntent().getStringExtra("name"));
        Picasso.with(this).load(ImageURL.Vendor_brand_image + getIntent().getStringExtra("image")).into(img_brand);
        name = getIntent().getStringExtra("name");
        brand_id = getIntent().getStringExtra("brand_id");

        btn_add_note1 = findViewById(R.id.btn_add_note1);
        btn_done = findViewById(R.id.btn_done);

        layout_scan_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  llayout_scan.setVisibility(View.GONE);
                llayout_scan_detail.setVisibility(View.VISIBLE);*/
                launchActivity(ScanBarcodeActivity.class);
            }
        });

        btn_add_note1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          /*      llayout_scan_detail.setVisibility(View.GONE);
                llayout_manual_voucher_details.setVisibility(View.VISIBLE);
           *//*     Intent intent = new Intent(UploadVoucherDataActivity.this, MainActivity.class);
                intent.putExtra("view_pos", "4");
                startActivity(intent);*//*
                finish();*/
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty(edt_voucher_no.getText().toString())) {
                    Toast.makeText(UploadVoucherDataActivity.this, "Enter Voucher Number", Toast.LENGTH_SHORT).show();
                } else {
                    String main_value = edt_voucher_no.getText().toString().substring(0, edt_voucher_no.getText().toString().length() - 1);
                    String str_v_type = edt_voucher_no.getText().toString().substring(edt_voucher_no.getText().toString().length() - 1);
                    switch (str_v_type) {
                        case "p":
                            str_v_type = "@purchase_voucher";
                            break;
                        case "replace_voucher":
                            str_v_type = "@replace_voucher";
                            break;
                        default:
                            str_v_type = "@gift_voucher";
                            break;
                    }
                    try {
                        jsonObject.put("scan_code", main_value + str_v_type + "@" + brand_id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.i("details", jsonObject.toString());
                    progressDialog.setMessage("Please Wait");
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    Call<ScanVoucherModel> scanVoucherModelCall = apiService.SCAN_VOUCHER_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), jsonObject.toString());
                    scanVoucherModelCall.enqueue(new Callback<ScanVoucherModel>() {
                        @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
                        @Override
                        public void onResponse(Call<ScanVoucherModel> call, Response<ScanVoucherModel> response) {
                            progressDialog.dismiss();
                            if (response.body().getStatus().equals("1")) {
                                Log.i("status_details", "success");
                                finish();
                                voucher_number = response.body().getScanCode();
                                voucher_value = response.body().getVoucherPrice();
                                pin_number = response.body().getPinCode();
                                exp_date = response.body().getExpiredAt();

                                barcodeView.setBarcodeText(voucher_number);
                                tv_voucher_no.setText(voucher_number);
                                tv_pin_no.setText(pin_number);
                                tv_voucher_price.setText(voucher_value + " " + getResources().getString(R.string.SR_currency));

                                SimpleDateFormat spf = null;
                                Date newDate = null;
                                try {
                                    String date = exp_date.substring(0, exp_date.indexOf(" "));
                                    final_date = date.trim();
                                    spf = new SimpleDateFormat("yyyy-MM-dd");
                                    newDate = spf.parse(final_date);
                                    spf = new SimpleDateFormat("dd MMMM yyyy");
                                    final_date = spf.format(newDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                tv_exp_date.setText(final_date);

                                llayout_manual_voucher_details.setVisibility(View.GONE);
                                llayout_scan_detail.setVisibility(View.VISIBLE);

                                Toast.makeText(UploadVoucherDataActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UploadVoucherDataActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ScanVoucherModel> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(UploadVoucherDataActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        tv_enter_manualy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llayout_scan.setVisibility(View.GONE);
                llayout_manual_voucher_details.setVisibility(View.VISIBLE);
            }
        });
    }

    public void launchActivity(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, clss);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZXING_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mClss != null) {
                        Intent intent = new Intent(this, mClss);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
    @Override
    protected void onResume() {
        super.onResume();
        if (UploadVouchersActivity.str_scanned.equals("1")) {
            UploadVouchersActivity.str_scanned = "0";
            llayout_scan_detail.setVisibility(View.VISIBLE);
            llayout_scan.setVisibility(View.GONE);
            llayout_manual_voucher_details.setVisibility(View.GONE);
            tv_voucher_no.setText(voucher_number);
            tv_pin_no.setText(pin_number);
            tv_voucher_price.setText(voucher_value + " " + getResources().getString(R.string.SR_currency));
            SimpleDateFormat spf = null;
            Date newDate = null;
            try {
                String date = exp_date.substring(0, exp_date.indexOf(" "));
                final_date = date.trim();
                spf = new SimpleDateFormat("yyyy-MM-dd");
                newDate = spf.parse(final_date);
                spf = new SimpleDateFormat("dd-MMM-yyyy");
                final_date = spf.format(newDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            tv_exp_date.setText(final_date);

        }
    }
}
