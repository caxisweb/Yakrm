package com.codeclinic.yakrm.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.codeclinic.yakrm.Models.AboutApplicationModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.Connection_Detector;
import com.franmontiel.localechanger.LocaleChanger;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PrivayPolicyActivity extends AppCompatActivity {

    ImageView img_back;
    TextView tv_terms_condition;
    API apiService;
    ProgressDialog progressDialog;
    String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privay_policy);

        img_back = findViewById(R.id.img_back);
        tv_terms_condition = findViewById(R.id.tv_terms_condition);
        language = String.valueOf(getResources().getConfiguration().locale);
        apiService = RestClass.getClient().create(API.class);
        progressDialog = new ProgressDialog(this);
        if (language.equals("ar")) {
            img_back.setImageDrawable(getResources().getDrawable(R.drawable.back_right_img));
        }
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (Connection_Detector.isInternetAvailable(this)) {

            progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

            Call<AboutApplicationModel> aboutApplicationModelCall = apiService.ABOUT_APPLICATION_MODEL_CALL();
            aboutApplicationModelCall.enqueue(new Callback<AboutApplicationModel>() {
                @Override
                public void onResponse(Call<AboutApplicationModel> call, Response<AboutApplicationModel> response) {
                    progressDialog.dismiss();
                    String status = response.body().getStatus();
                    if (status.equals("1")) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            if (language.equals("ar")) {
                                tv_terms_condition.setText(Html.fromHtml(response.body().getTermsAndConditionArab(), Html.FROM_HTML_MODE_COMPACT));
                            } else {
                                tv_terms_condition.setText(Html.fromHtml(response.body().getTermsAndConditionEnglish(), Html.FROM_HTML_MODE_COMPACT));
                            }
                        } else {
                            if (language.equals("ar")) {
                                tv_terms_condition.setText(Html.fromHtml(response.body().getTermsAndConditionArab()));
                            } else {
                                tv_terms_condition.setText(Html.fromHtml(response.body().getTermsAndConditionEnglish()));
                            }

                        }
                    }
                }

                @Override
                public void onFailure(Call<AboutApplicationModel> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(PrivayPolicyActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleChanger.configureBaseContext(newBase);
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityRecreationHelper.onResume(this);
    }

    @Override
    protected void onDestroy() {
        ActivityRecreationHelper.onDestroy(this);
        super.onDestroy();
    }
}
