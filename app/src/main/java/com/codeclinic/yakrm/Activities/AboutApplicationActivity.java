package com.codeclinic.yakrm.Activities;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codeclinic.yakrm.Models.AboutApplicationModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.Connection_Detector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AboutApplicationActivity extends AppCompatActivity {

    ImageView img_back;
    TextView tv_terms_condition, tv_about;
    API apiService;
    ProgressDialog progressDialog;
    String language;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_application);

        img_back = findViewById(R.id.img_back);
        tv_terms_condition = findViewById(R.id.tv_terms_condition);
        tv_about = findViewById(R.id.tv_about);
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
            progressDialog.setMessage("Please Wait");
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
                                tv_about.setText(Html.fromHtml(response.body().getAboutApplicationArab(), Html.FROM_HTML_MODE_COMPACT));
                                tv_terms_condition.setText(Html.fromHtml(response.body().getTermsAndConditionArab(), Html.FROM_HTML_MODE_COMPACT));
                            } else {
                                tv_about.setText(Html.fromHtml(response.body().getAboutApplicationEnglish(), Html.FROM_HTML_MODE_COMPACT));
                                tv_terms_condition.setText(Html.fromHtml(response.body().getTermsAndConditionEnglish(), Html.FROM_HTML_MODE_COMPACT));
                            }
                        } else {
                            if (language.equals("ar")) {
                                tv_about.setText(Html.fromHtml(response.body().getAboutApplicationArab()));
                                tv_terms_condition.setText(Html.fromHtml(response.body().getTermsAndConditionArab()));
                            } else {
                                tv_about.setText(Html.fromHtml(response.body().getAboutApplicationEnglish()));
                                tv_terms_condition.setText(Html.fromHtml(response.body().getTermsAndConditionEnglish()));
                            }

                        }
                    }
                }

                @Override
                public void onFailure(Call<AboutApplicationModel> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(AboutApplicationActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "No internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
}
