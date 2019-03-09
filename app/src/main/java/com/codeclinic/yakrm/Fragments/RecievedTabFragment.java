package com.codeclinic.yakrm.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codeclinic.yakrm.Adapter.RecievedListAdapter;
import com.codeclinic.yakrm.Models.RecievedGftListItemModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.SessionManager;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecievedTabFragment extends Fragment {


    RecyclerView recyclerView;
    ArrayList<RecievedGftListItemModel> arrayList = new ArrayList<>();
    RecievedListAdapter recievedListAdapter;

    API apiService;
    ProgressDialog progressDialog;
    SessionManager sessionManager;

    public RecievedTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recieved_tab, container, false);

        apiService = RestClass.getClient().create(API.class);
        sessionManager = new SessionManager(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        recyclerView = view.findViewById(R.id.recyclerView);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);


   /*     recievedListAdapter = new RecievedListAdapter(arrayList, getActivity());
        recyclerView.setAdapter(recievedListAdapter);*/

        return view;
    }

}
