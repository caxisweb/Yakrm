package com.yakrm.codeclinic.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.yakrm.codeclinic.Adapter.FavListAdapter;
import com.yakrm.codeclinic.R;

import java.util.ArrayList;

public class FavouritesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView img_back;
    FavListAdapter favListAdapter;
    ArrayList<String> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        recyclerView = findViewById(R.id.recyclerView);
        img_back = findViewById(R.id.img_back);

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

        arrayList.add("STABUCKSCARD");
        arrayList.add("Coca cola");
        arrayList.add("H&M ");

        favListAdapter = new FavListAdapter(arrayList, this);
        recyclerView.setAdapter(favListAdapter);
    }
}
