package com.yakrm.codeclinic.yakrm.Fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yakrm.codeclinic.yakrm.Adapter.AuctionOtherAcceptAdapter;
import com.yakrm.codeclinic.yakrm.Adapter.MyVoucherAuctionAdapter;
import com.yakrm.codeclinic.yakrm.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyVouchersAuctionFragment extends Fragment {


    TextView tv_active, tv_ended;

    RecyclerView recyclerView_active, recyclerView_ended;
    AuctionOtherAcceptAdapter auctionOtherAcceptAdapter;
    MyVoucherAuctionAdapter myVoucherAuctionAdapter;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> arrayList_ended = new ArrayList<>();

    public MyVouchersAuctionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_vouchers_auction, container, false);

        tv_active = view.findViewById(R.id.tv_active);
        tv_ended = view.findViewById(R.id.tv_ended);

        recyclerView_active = view.findViewById(R.id.recyclerView_active);
        recyclerView_ended = view.findViewById(R.id.recyclerView_ended);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView_active.setLayoutManager(layoutManager);
        recyclerView_active.setHasFixedSize(true);
        recyclerView_active.setNestedScrollingEnabled(false);
        recyclerView_active.smoothScrollToPosition(0);

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView_ended.setLayoutManager(layoutManager2);
        recyclerView_ended.setHasFixedSize(true);
        recyclerView_ended.setNestedScrollingEnabled(false);
        recyclerView_ended.smoothScrollToPosition(0);

        tv_active.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                tv_active.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tv_active.setTextColor(getResources().getColor(R.color.white));
                tv_ended.setBackgroundColor(getResources().getColor(R.color.transparent));
                tv_ended.setTextColor(getResources().getColor(R.color.black));

                recyclerView_active.setVisibility(View.VISIBLE);
                recyclerView_ended.setVisibility(View.GONE);

            }
        });

        tv_ended.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                tv_ended.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                tv_ended.setTextColor(getResources().getColor(R.color.white));
                tv_active.setBackgroundColor(getResources().getColor(R.color.transparent));
                tv_active.setTextColor(getResources().getColor(R.color.black));

                recyclerView_active.setVisibility(View.GONE);
                recyclerView_ended.setVisibility(View.VISIBLE);
                myVoucherAuctionAdapter = new MyVoucherAuctionAdapter(arrayList_ended, getActivity());
                recyclerView_ended.setAdapter(myVoucherAuctionAdapter);
            }
        });

        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");
        arrayList.add("");

        arrayList_ended.add("");
        arrayList_ended.add("");
        arrayList_ended.add("");
        arrayList_ended.add("");
        arrayList_ended.add("");
        arrayList_ended.add("");

        auctionOtherAcceptAdapter = new AuctionOtherAcceptAdapter(arrayList, getActivity());
        recyclerView_active.setAdapter(auctionOtherAcceptAdapter);


        return view;
    }

}
