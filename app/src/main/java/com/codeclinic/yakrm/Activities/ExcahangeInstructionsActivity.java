package com.codeclinic.yakrm.Activities;

import android.app.ProgressDialog;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ExcahangeInstructionsActivity extends AppCompatActivity {

    ImageView img_back;
    API apiService;
    ProgressDialog progressDialog;
    TextView tv_instruction;
    String language;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excahange_instructions);

        img_back = findViewById(R.id.img_back);
        tv_instruction = findViewById(R.id.tv_instruction);
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
            progressDialog.setMessage(getResources().getString(R.string.please_wait));
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
                                tv_instruction.setText(Html.fromHtml(response.body().getInstruction_arab(), Html.FROM_HTML_MODE_COMPACT));
                            } else {
                                tv_instruction.setText(Html.fromHtml(response.body().getInstruction_english(), Html.FROM_HTML_MODE_COMPACT));
                            }
                        } else {
                            if (language.equals("ar")) {
                                tv_instruction.setText(Html.fromHtml(response.body().getInstruction_arab()));
                            } else {
                                tv_instruction.setText(Html.fromHtml(response.body().getInstruction_english()));
                            }

                        }
                    }
                }

                @Override
                public void onFailure(Call<AboutApplicationModel> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(ExcahangeInstructionsActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }
}
