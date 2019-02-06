package com.codeclinic.yakrm.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.codeclinic.yakrm.Adapter.RecievedListAdapter;
import com.codeclinic.yakrm.R;

import java.util.ArrayList;

public class RecievedActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<String> arrayList = new ArrayList<>();
    ImageView imageView;
    RecievedListAdapter recievedListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieved);


        recyclerView = findViewById(R.id.recyclerView);
        imageView = findViewById(R.id.img_back);
        String language = String.valueOf(getResources().getConfiguration().locale);
        if (language.equals("ar")) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.back_right_img));
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);


        arrayList.add("");
        arrayList.add("");

        recievedListAdapter = new RecievedListAdapter(arrayList, this);
        recyclerView.setAdapter(recievedListAdapter);
    }
}