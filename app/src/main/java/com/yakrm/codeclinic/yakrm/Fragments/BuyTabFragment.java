package com.yakrm.codeclinic.yakrm.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yakrm.codeclinic.yakrm.Adapter.BuyTabListAdapter;
import com.yakrm.codeclinic.yakrm.R;
import com.yakrm.codeclinic.yakrm.Utils.GridSpacingItemDecoration;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class BuyTabFragment extends Fragment {

    RecyclerView recyclerView;
    BuyTabListAdapter buyTabListAdapter;
    ArrayList<String> arrayList = new ArrayList<>();

    public BuyTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy_tab, container, false);

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

        return view;
    }

}
