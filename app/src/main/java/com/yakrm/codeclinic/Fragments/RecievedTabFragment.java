package com.yakrm.codeclinic.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yakrm.codeclinic.Adapter.RecievedListAdapter;
import com.yakrm.codeclinic.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecievedTabFragment extends Fragment {


    RecyclerView recyclerView;
    ArrayList<String> arrayList = new ArrayList<>();
    RecievedListAdapter recievedListAdapter;

    public RecievedTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recieved_tab, container, false);


        recyclerView = view.findViewById(R.id.recyclerView);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);


        arrayList.add("");
        arrayList.add("");

        recievedListAdapter = new RecievedListAdapter(arrayList, getActivity());
        recyclerView.setAdapter(recievedListAdapter);

        return view;
    }

}
