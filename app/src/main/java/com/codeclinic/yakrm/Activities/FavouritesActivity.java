package com.codeclinic.yakrm.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.codeclinic.yakrm.Adapter.FavListAdapter;
import com.codeclinic.yakrm.Models.FavouritesListItemModel;
import com.codeclinic.yakrm.Models.FavouritesListModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.Connection_Detector;
import com.codeclinic.yakrm.Utils.SessionManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouritesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView img_back;
    FavListAdapter favListAdapter;
    List<FavouritesListItemModel> arrayList = new ArrayList<>();
    API apiService;
    JSONObject jsonObject = new JSONObject();
    ProgressDialog progressDialog;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        recyclerView = findViewById(R.id.recyclerView);
        img_back = findViewById(R.id.img_back);
        String language = String.valueOf(getResources().getConfiguration().locale);
        if (language.equals("ar")) {
            img_back.setImageDrawable(getResources().getDrawable(R.drawable.back_right_img));
        }
        sessionManager = new SessionManager(this);
        progressDialog = new ProgressDialog(this);
        apiService = RestClass.getClient().create(API.class);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

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

            Call<FavouritesListModel> favouritesListModelCall = apiService.FAVOURITES_LIST_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token));
            favouritesListModelCall.enqueue(new Callback<FavouritesListModel>() {
                @Override
                public void onResponse(Call<FavouritesListModel> call, Response<FavouritesListModel> response) {
                    progressDialog.dismiss();
                    String status = response.body().getStatus();
                    if (status.equals("1")) {
                        arrayList = response.body().getData();
                        favListAdapter = new FavListAdapter(arrayList, FavouritesActivity.this, apiService, sessionManager);
                        recyclerView.setAdapter(favListAdapter);
                    } else {
                        Toast.makeText(FavouritesActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<FavouritesListModel> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(FavouritesActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });


        }
    }
}
