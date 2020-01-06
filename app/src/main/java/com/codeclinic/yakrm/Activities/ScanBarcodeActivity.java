package com.codeclinic.yakrm.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codeclinic.yakrm.Models.ScanVoucherModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Utils.BaseScannerActivity;
import com.codeclinic.yakrm.Utils.SessionManager;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanBarcodeActivity extends BaseScannerActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    API apiService;
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    JSONObject jsonObject = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);
        setupToolbar();

        ViewGroup contentFrame = findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);

        apiService = UploadVouchersActivity.RestClasses.getClient().create(API.class);
        sessionManager = new SessionManager(this);
        progressDialog = new ProgressDialog(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {

        String main_value = rawResult.getText().substring(0, rawResult.getText().length() - 1);
        String str_v_type = rawResult.getText().substring(rawResult.getText().length() - 1);
        switch (str_v_type) {
            case "p":
                str_v_type = "@purchase_voucher";
                break;
            case "r":
                str_v_type = "@replace_voucher";
                break;
            default:
                str_v_type = "@gift_voucher";
                break;
        }
        try {
            jsonObject.put("scan_code", main_value + str_v_type + "@" + UploadVoucherDataActivity.brand_id);
            jsonObject.put("is_scan", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("details", jsonObject.toString());
        progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<ScanVoucherModel> scanVoucherModelCall = apiService.SCAN_VOUCHER_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), jsonObject.toString());
        scanVoucherModelCall.enqueue(new Callback<ScanVoucherModel>() {
            @Override
            public void onResponse(Call<ScanVoucherModel> call, Response<ScanVoucherModel> response) {
                progressDialog.dismiss();
                if (response.body().getStatus().equals("1")) {
                    Log.i("status_details", "success");
                    UploadVouchersActivity.str_scanned = "1";
                    UploadVoucherDataActivity.voucher_number = response.body().getScanCode();
                    UploadVoucherDataActivity.voucher_value = response.body().getVoucherPrice();
                    UploadVoucherDataActivity.pin_number = response.body().getPinCode();
                    UploadVoucherDataActivity.exp_date = response.body().getExpiredAt();
                    finish();
                    String language = String.valueOf(getResources().getConfiguration().locale);
                    if (language.equals("ar")) {
                        Toast.makeText(ScanBarcodeActivity.this, "لقد تم بنجاح مسح القسيمة", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ScanBarcodeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mScannerView.resumeCameraPreview(ScanBarcodeActivity.this);
                        }
                    }, 2000);
                    String language = String.valueOf(getResources().getConfiguration().locale);
                    if (language.equals("ar")) {
                        Toast.makeText(ScanBarcodeActivity.this, "لقد تم استخدام القسيمة", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ScanBarcodeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ScanVoucherModel> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ScanBarcodeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
