package com.codeclinic.yakrm.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.codeclinic.yakrm.Adapter.SearchListAdapter;
import com.codeclinic.yakrm.Models.SearchListItemModel;
import com.codeclinic.yakrm.Models.SearchListModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.Connection_Detector;
import com.codeclinic.yakrm.Utils.GridSpacingItemDecoration;
import com.codeclinic.yakrm.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchedVoucherActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<SearchListItemModel> arrayList = new ArrayList<>();
    API apiService;
    ProgressDialog progressDialog;
    SessionManager sessionManager;
    JSONObject jsonObject = new JSONObject();
    ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_voucher);

        sessionManager = new SessionManager(this);
        progressDialog = new ProgressDialog(this);
        apiService = RestClass.getClient().create(API.class);

        img_back = findViewById(R.id.img_back);
        String language = String.valueOf(getResources().getConfiguration().locale);
        if (language.equals("ar")) {
            img_back.setImageDrawable(getResources().getDrawable(R.drawable.back_right_img));
        }
        recyclerView = findViewById(R.id.recyclerView);
        int spanCount = 2; // 3 columns
        int spacing = 10; // 50px
        boolean includeEdge = false;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (Connection_Detector.isInternetAvailable(this)) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("brand_name", getIntent().getStringExtra("search"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Call<SearchListModel> searchListModelCall = apiService.SEARCH_LIST_MODEL_CALL(jsonObject.toString());
            searchListModelCall.enqueue(new Callback<SearchListModel>() {
                @Override
                public void onResponse(Call<SearchListModel> call, Response<SearchListModel> response) {
                    progressDialog.dismiss();
                    if (response.body().getStatus().equals("1")) {
                        arrayList = response.body().getData();
                        SearchListAdapter searchListAdapter = new SearchListAdapter(arrayList, SearchedVoucherActivity.this);
                        recyclerView.setAdapter(searchListAdapter);
                    } else {
                        String language = String.valueOf(getResources().getConfiguration().locale);
                        if (language.equals("ar")) {
                            if (response.body().getArab_message() != null) {
                                Toast.makeText(SearchedVoucherActivity.this, response.body().getArab_message(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SearchedVoucherActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SearchedVoucherActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<SearchListModel> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(SearchedVoucherActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
