package com.yakrm.codeclinic.yakrm.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yakrm.codeclinic.yakrm.Activities.UploadVouchersActivity;
import com.yakrm.codeclinic.yakrm.Adapter.MyWalletAdapter;
import com.yakrm.codeclinic.yakrm.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyWalletTabFragment extends Fragment {

    RecyclerView recyclerView;
    MyWalletAdapter myWalletAdapter;
    ArrayList<String> arrayList = new ArrayList<>();
    TextView tv_upload_voucher;

    public MyWalletTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_wallet_tab, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        tv_upload_voucher = view.findViewById(R.id.tv_upload_voucher);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        arrayList.add("STARBUCKSCARD");
        arrayList.add("Coca Cola");
        arrayList.add("Adidas Inc.");
        arrayList.add("Xbox");

        myWalletAdapter = new MyWalletAdapter(arrayList, getActivity());
        recyclerView.setAdapter(myWalletAdapter);

        tv_upload_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UploadVouchersActivity.class));
            }
        });

        return view;
    }

}
