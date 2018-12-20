package com.yakrm.codeclinic.yakrm.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_details);

        recyclerView = findViewById(R.id.recyclerView);

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

    }
}
