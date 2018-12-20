package com.yakrm.codeclinic.yakrm.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yakrm.codeclinic.yakrm.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReplaceTabFragment extends Fragment {


    public ReplaceTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_replace_tab, container, false);
    }

}
