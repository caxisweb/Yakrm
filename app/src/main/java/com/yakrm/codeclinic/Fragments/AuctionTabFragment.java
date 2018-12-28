package com.yakrm.codeclinic.Fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yakrm.codeclinic.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AuctionTabFragment extends Fragment {

    private TabLayout tabLayout;

    public AuctionTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auction_tab, container, false);
        tabLayout = view.findViewById(R.id.tablayout);


        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.Auctions_Of_Other)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.Auction_On_My_Vouchers)));

        Fragment fragment = null;
        fragment = new OtherAuctionsFragment();
        @SuppressLint({"NewApi", "LocalSuppress"}) FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabLayout.getSelectedTabPosition() == 0) {
                    Fragment fragment = null;
                    fragment = new OtherAuctionsFragment();
                    @SuppressLint({"NewApi", "LocalSuppress"}) FragmentManager fragmentManager = getChildFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
                } else if (tabLayout.getSelectedTabPosition() == 1) {
                    Fragment fragment = null;
                    fragment = new MyVouchersAuctionFragment();
                    @SuppressLint({"NewApi", "LocalSuppress"}) FragmentManager fragmentManager = getChildFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return view;
    }

}
