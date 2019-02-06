package com.codeclinic.yakrm.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.codeclinic.yakrm.Adapter.SimpleAdapter;
import com.codeclinic.yakrm.Adapter.SimpleSectionedRecyclerViewAdapter;
import com.codeclinic.yakrm.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView img_back;
    String[] arrayList = new String[12];
    ArrayList<String> title_arrayList = new ArrayList<>();
    SimpleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        recyclerView = findViewById(R.id.recyclerView);
        img_back = findViewById(R.id.img_back);
        String language = String.valueOf(getResources().getConfiguration().locale);
        if (language.equals("ar")) {
            img_back.setImageDrawable(getResources().getDrawable(R.drawable.back_right_img));
        }
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .color(getResources().getColor(R.color.dot_light_intro))
                .sizeResId(R.dimen.divider)
                .build());

        arrayList[0] = getResources().getString(R.string.System_Administrator);
        arrayList[1] = getResources().getString(R.string.System_Administrator);
        arrayList[2] = getResources().getString(R.string.System_Administrator);
        arrayList[3] = getResources().getString(R.string.System_Administrator);
        arrayList[4] = getResources().getString(R.string.System_Administrator);
        arrayList[5] = getResources().getString(R.string.System_Administrator);
        arrayList[6] = getResources().getString(R.string.System_Administrator);
        arrayList[7] = getResources().getString(R.string.System_Administrator);
        arrayList[8] = getResources().getString(R.string.System_Administrator);
        arrayList[9] = getResources().getString(R.string.System_Administrator);
        arrayList[10] = getResources().getString(R.string.System_Administrator);
        arrayList[11] = getResources().getString(R.string.System_Administrator);

        mAdapter = new SimpleAdapter(this, arrayList);


        //This is the code to provide a sectioned list
        List<SimpleSectionedRecyclerViewAdapter.Section> sections =
                new ArrayList<SimpleSectionedRecyclerViewAdapter.Section>();

        //Sections
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0, getResources().getString(R.string.Yesterday)));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(5, getResources().getString(R.string.Dec_18)));


        //Add your adapter to the sectionAdapter
        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
        SimpleSectionedRecyclerViewAdapter mSectionedAdapter = new
                SimpleSectionedRecyclerViewAdapter(this, R.layout.custom_notification_header_view, R.id.tv_header, mAdapter);
        mSectionedAdapter.setSections(sections.toArray(dummy));

        //Apply this adapter to the RecyclerView
        recyclerView.setAdapter(mSectionedAdapter);


    }
}
