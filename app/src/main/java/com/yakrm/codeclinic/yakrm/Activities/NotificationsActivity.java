package com.yakrm.codeclinic.yakrm.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.yakrm.codeclinic.yakrm.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.zakariya.stickyheaders.StickyHeaderLayoutManager;

import java.util.ArrayList;

public class NotificationsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView img_back;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> title_arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        recyclerView = findViewById(R.id.recyclerView);
        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        recyclerView.setLayoutManager(new StickyHeaderLayoutManager());
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .color(getResources().getColor(R.color.dot_light_intro))
                .sizeResId(R.dimen.divider)
                .build());

        arrayList.add(getResources().getString(R.string.Yesterday));
        arrayList.add(getResources().getString(R.string.Yesterday));
        arrayList.add(getResources().getString(R.string.Yesterday));
        arrayList.add(getResources().getString(R.string.Dec_18));
        arrayList.add(getResources().getString(R.string.Dec_18));
        arrayList.add(getResources().getString(R.string.Dec_18));
        arrayList.add(getResources().getString(R.string.Dec_18));
        arrayList.add(getResources().getString(R.string.Dec_18));

        title_arrayList.add(getResources().getString(R.string.Yesterday));
        title_arrayList.add(getResources().getString(R.string.Dec_18));
/*
        NotificationAdapter notificationAdapter = new NotificationAdapter(arrayList,title_arrayList);
        recyclerView.setAdapter(notificationAdapter);*/
    }
}
