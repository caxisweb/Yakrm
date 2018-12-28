package com.yakrm.codeclinic.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yakrm.codeclinic.Activities.MainActivity;
import com.yakrm.codeclinic.Adapter.BuyTabListAdapter;
import com.yakrm.codeclinic.R;
import com.yakrm.codeclinic.Utils.GridSpacingItemDecoration;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class BuyTabFragment extends Fragment {

    RecyclerView recyclerView;
    BuyTabListAdapter buyTabListAdapter;
    ArrayList<String> arrayList = new ArrayList<>();

    LinearLayout layout_filter;

    public BuyTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy_tab, container, false);

        layout_filter = view.findViewById(R.id.layout_filter);
        recyclerView = view.findViewById(R.id.recyclerView);
        int spanCount = 2; // 3 columns
        int spacing = 10; // 50px
        boolean includeEdge = false;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        arrayList.add("STABUCKSCARD");
        arrayList.add("Adidas");
        arrayList.add("Coca cola");
        arrayList.add("XBOX");
        arrayList.add("H&M");
        arrayList.add("Saudi Arabic airlines");

        buyTabListAdapter = new BuyTabListAdapter(arrayList, getActivity());
        recyclerView.setAdapter(buyTabListAdapter);

        layout_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.drawer.isDrawerVisible(GravityCompat.END)) {
                    MainActivity.drawer.closeDrawer(GravityCompat.END);
                } else {
                    MainActivity.drawer.openDrawer(GravityCompat.END);
                }
            }
        });

        return view;
    }

}
