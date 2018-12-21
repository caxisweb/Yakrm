package com.yakrm.codeclinic.yakrm.Activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.yakrm.codeclinic.yakrm.Adapter.GiftDetailListAdapter;
import com.yakrm.codeclinic.yakrm.Models.GiftDetailListItemModel;
import com.yakrm.codeclinic.yakrm.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;

public class GiftDetailsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    GiftDetailListAdapter giftDetailListAdapter;
    ArrayList<GiftDetailListItemModel> arrayList = new ArrayList<>();
    ArrayList<String> ar_value = new ArrayList<>();
    ArrayList<String> ar_discount = new ArrayList<>();
    ArrayList<String> ar_pay = new ArrayList<>();

    Button btn_complete;
    ImageView img_back, img_fav;


    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_details);

        recyclerView = findViewById(R.id.recyclerView);
        img_back = findViewById(R.id.img_back);
        img_fav = findViewById(R.id.img_fav);
        btn_complete = findViewById(R.id.btn_complete);

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

        ar_value.add(getResources().getString(R.string.SR));
        ar_value.add(getResources().getString(R.string.SR2));
        ar_value.add(getResources().getString(R.string.SR3));
        ar_value.add(getResources().getString(R.string.SR4));

        ar_discount.add(getResources().getString(R.string.S5));
        ar_discount.add(getResources().getString(R.string.S5));
        ar_discount.add(getResources().getString(R.string.S5));
        ar_discount.add(getResources().getString(R.string.S5));

        ar_pay.add(getResources().getString(R.string.SR));
        ar_pay.add(getResources().getString(R.string.SR2));
        ar_pay.add(getResources().getString(R.string.SR3));
        ar_pay.add(getResources().getString(R.string.SR4));

        for (int i = 0; i < ar_value.size(); i++) {
            GiftDetailListItemModel giftDetailListItemModel = new GiftDetailListItemModel(ar_value.get(i), ar_discount.get(i), ar_pay.get(i));
            arrayList.add(giftDetailListItemModel);
        }

        giftDetailListAdapter = new GiftDetailListAdapter(arrayList, this);
        recyclerView.setAdapter(giftDetailListAdapter);

        img_fav.setOnClickListener(new View.OnClickListener() {
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

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }
}
