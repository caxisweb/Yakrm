package com.yakrm.codeclinic.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yakrm.codeclinic.Activities.FavouritesActivity;
import com.yakrm.codeclinic.Activities.MainActivity;
import com.yakrm.codeclinic.Activities.UploadVouchersActivity;
import com.yakrm.codeclinic.Activities.VoucherEndedActivity;
import com.yakrm.codeclinic.Activities.VoucherWillEndActivity;
import com.yakrm.codeclinic.Adapter.MyWalletAdapter;
import com.yakrm.codeclinic.Models.ActiveVoucherListItemModel;
import com.yakrm.codeclinic.Models.ActiveVoucherListModel;
import com.yakrm.codeclinic.R;
import com.yakrm.codeclinic.Retrofit.API;
import com.yakrm.codeclinic.Retrofit.RestClass;
import com.yakrm.codeclinic.Utils.Connection_Detector;
import com.yakrm.codeclinic.Utils.SessionManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyWalletTabFragment extends Fragment {

    RecyclerView recyclerView;
    MyWalletAdapter myWalletAdapter;
    List<ActiveVoucherListItemModel> arrayList = new ArrayList<>();
    TextView tv_upload_voucher;
    LinearLayout llayout_voucher_ended_valid, llayout_fav_vouchers, llayout_active_vouchers, llayout_voucher_ended_done;
    JSONObject jsonObject = new JSONObject();
    API apiService;
    ProgressDialog progressDialog;
    SessionManager sessionManager;

    public MyWalletTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_wallet_tab, container, false);

        llayout_fav_vouchers = view.findViewById(R.id.llayout_fav_vouchers);
        llayout_voucher_ended_done = view.findViewById(R.id.llayout_voucher_ended_done);
        llayout_active_vouchers = view.findViewById(R.id.llayout_active_vouchers);
        llayout_voucher_ended_valid = view.findViewById(R.id.llayout_voucher_ended_valid);
        recyclerView = view.findViewById(R.id.recyclerView);
        tv_upload_voucher = view.findViewById(R.id.tv_upload_voucher);
        sessionManager = new SessionManager(getActivity());
        apiService = RestClass.getClient().create(API.class);
        progressDialog = new ProgressDialog(getActivity());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);


        tv_upload_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UploadVouchersActivity.class));
            }
        });

        llayout_voucher_ended_valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), VoucherEndedActivity.class));
            }
        });

        llayout_fav_vouchers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FavouritesActivity.class));
            }
        });

        llayout_active_vouchers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), VoucherWillEndActivity.class));
            }
        });

        llayout_voucher_ended_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.viewPager.setCurrentItem(2);
            }
        });

        if (Connection_Detector.isInternetAvailable(getActivity())) {
            progressDialog.setMessage("Please Wait");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            Call<ActiveVoucherListModel> activeVoucherListModelCall = apiService.ACTIVE_VOUCHER_LIST_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token));
            activeVoucherListModelCall.enqueue(new Callback<ActiveVoucherListModel>() {
                @Override
                public void onResponse(Call<ActiveVoucherListModel> call, Response<ActiveVoucherListModel> response) {
                    progressDialog.dismiss();
                    String status = response.body().getStatus();
                    if (status.equals("1")) {
                        arrayList = response.body().getData();
                        myWalletAdapter = new MyWalletAdapter(arrayList, getActivity());
                        recyclerView.setAdapter(myWalletAdapter);
                    } else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ActiveVoucherListModel> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

}
