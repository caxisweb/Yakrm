package com.codeclinic.yakrm.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codeclinic.yakrm.Activities.MainActivity;
import com.codeclinic.yakrm.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SupportContactFragment extends android.app.Fragment {


    public SupportContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_support_contact, container, false);
        MainActivity.back_flag = 1;
        return view;
    }

}
