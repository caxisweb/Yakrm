package com.codeclinic.yakrm.Fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codeclinic.yakrm.Adapter.ActiveAuctionAdapter;
import com.codeclinic.yakrm.Adapter.AuctionWinAdapter;
import com.codeclinic.yakrm.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class OtherAuctionsFragment extends Fragment {

    TextView tv_active, tv_win, tv_lost;

    RecyclerView recyclerView_active, recyclerView_win, recyclerView_lost;
    ActiveAuctionAdapter activeAuctionAdapter;
    AuctionWinAdapter auctionWinAdapter;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> arrayList_win = new ArrayList<>();
    ArrayList<String> arrayList_lost = new ArrayList<>();

    public OtherAuctionsFragment() {
        // Required empty public constructor
    }


    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_auctions, container, false);

        tv_active = view.findViewById(R.id.tv_active);
        tv_win = view.findViewById(R.id.tv_win);
        tv_lost = view.findViewById(R.id.tv_lost);

        recyclerView_active = view.findViewById(R.id.recyclerView_active);
        recyclerView_win = view.findViewById(R.id.recyclerView_win);
        recyclerView_lost = view.findViewById(R.id.recyclerView_lost);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView_active.setLayoutManager(layoutManager);
        recyclerView_active.setHasFixedSize(true);
        recyclerView_active.setNestedScrollingEnabled(false);
        recyclerView_active.smoothScrollToPosition(0);

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView_win.setLayoutManager(layoutManager2);
        recyclerView_win.setHasFixedSize(true);
        recyclerView_win.setNestedScrollingEnabled(false);
        recyclerView_win.smoothScrollToPosition(0);

        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView_lost.setLayoutManager(layoutManager3);
        recyclerView_lost.setHasFixedSize(true);
        recyclerView_lost.setNestedScrollingEnabled(false);
        recyclerView_win.smoothScrollToPosition(0);


        tv_active.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                tv_active.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tv_active.setTextColor(getResources().getColor(R.color.white));
                tv_win.setBackgroundColor(getResources().getColor(R.color.transparent));
                tv_win.setTextColor(getResources().getColor(R.color.black));
                tv_lost.setBackgroundColor(getResources().getColor(R.color.transparent));
                tv_lost.setTextColor(getResources().getColor(R.color.black));
                recyclerView_active.setVisibility(View.VISIBLE);
                recyclerView_win.setVisibility(View.GONE);
                recyclerView_lost.setVisibility(View.GONE);


            }
        });

        tv_win.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                tv_win.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tv_win.setTextColor(getResources().getColor(R.color.white));
                tv_active.setBackgroundColor(getResources().getColor(R.color.transparent));
                tv_active.setTextColor(getResources().getColor(R.color.black));
                tv_lost.setBackgroundColor(getResources().getColor(R.color.transparent));
                tv_lost.setTextColor(getResources().getColor(R.color.black));
                recyclerView_active.setVisibility(View.GONE);
                recyclerView_win.setVisibility(View.VISIBLE);
                recyclerView_lost.setVisibility(View.GONE);
                auctionWinAdapter = new AuctionWinAdapter(arrayList_win, getActivity());
                recyclerView_win.setAdapter(auctionWinAdapter);

            }
        });

        tv_lost.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                tv_lost.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tv_lost.setTextColor(getResources().getColor(R.color.white));
                tv_active.setBackgroundColor(getResources().getColor(R.color.transparent));
                tv_active.setTextColor(getResources().getColor(R.color.black));
                tv_win.setBackgroundColor(getResources().getColor(R.color.transparent));
                tv_win.setTextColor(getResources().getColor(R.color.black));
                recyclerView_active.setVisibility(View.GONE);
                recyclerView_win.setVisibility(View.GONE);
                recyclerView_lost.setVisibility(View.VISIBLE);
                auctionWinAdapter = new AuctionWinAdapter(arrayList_win, getActivity());
                recyclerView_lost.setAdapter(auctionWinAdapter);


            }
        });
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");

        arrayList_win.add("");
        arrayList_win.add("");
        arrayList_win.add("");
        arrayList_win.add("");
        arrayList_win.add("");
        arrayList_win.add("");

        activeAuctionAdapter = new ActiveAuctionAdapter(arrayList, getActivity());
        recyclerView_active.setAdapter(activeAuctionAdapter);

        return view;

    }

}
