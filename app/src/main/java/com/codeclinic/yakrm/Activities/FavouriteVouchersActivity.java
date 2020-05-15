package com.codeclinic.yakrm.Activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codeclinic.yakrm.Adapter.FavVouchersAdapter;
import com.codeclinic.yakrm.R;
import com.franmontiel.localechanger.LocaleChanger;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;

import java.util.ArrayList;

public class FavouriteVouchersActivity extends AppCompatActivity {
    ArrayList arrayList = new ArrayList();
    RecyclerView recyclerView;
    FavVouchersAdapter favVouchersAdapter;
    ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_vouchers);

        img_back = findViewById(R.id.img_back);
        String language = String.valueOf(getResources().getConfiguration().locale);
        if (language.equals("ar")) {
            img_back.setImageDrawable(getResources().getDrawable(R.drawable.back_right_img));
        }
        recyclerView = findViewById(R.id.recyclerView);
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

        arrayList.add("STABUCKSCARD");
        arrayList.add("Coca Cola ");
        arrayList.add("Adidas Inc. ");
        arrayList.add("XBOX");
        arrayList.add("STABUCKSCARD");
        arrayList.add("Coca Cola ");
        arrayList.add("Adidas Inc. ");
        arrayList.add("XBOX");

        favVouchersAdapter = new FavVouchersAdapter(arrayList, this);
        recyclerView.setAdapter(favVouchersAdapter);

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
