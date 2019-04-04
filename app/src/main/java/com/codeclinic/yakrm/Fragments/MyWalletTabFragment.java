package com.codeclinic.yakrm.Fragments;


import android.annotation.SuppressLint;
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

import com.codeclinic.yakrm.Activities.FavouritesActivity;
import com.codeclinic.yakrm.Activities.MainActivity;
import com.codeclinic.yakrm.Activities.UploadVouchersActivity;
import com.codeclinic.yakrm.Activities.VoucherEndedActivity;
import com.codeclinic.yakrm.Adapter.MyWalletAdapter;
import com.codeclinic.yakrm.Models.WalletActiveListItemModel;
import com.codeclinic.yakrm.Models.WalletActiveListModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.Connection_Detector;
import com.codeclinic.yakrm.Utils.SessionManager;

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
    List<WalletActiveListItemModel> arrayList = new ArrayList<>();
    TextView tv_upload_voucher, tv_wallet_amount;
    LinearLayout llayout_voucher_ended_valid, llayout_fav_vouchers, llayout_active_vouchers, llayout_voucher_ended_done;
    JSONObject jsonObject = new JSONObject();
    API apiService;
    ProgressDialog progressDialog;
    SessionManager sessionManager;


    String admin_discount;

    public MyWalletTabFragment() {
        // Required empty public constructor
    }


    @SuppressLint("SetTextI18n")
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
        tv_wallet_amount = view.findViewById(R.id.tv_wallet_amount);
        sessionManager = new SessionManager(getActivity());
        apiService = RestClass.getClient().create(API.class);
        progressDialog = new ProgressDialog(getActivity());

        tv_wallet_amount.setText(sessionManager.getUserDetails().get(SessionManager.Wallet).replaceAll("1", getResources().getString(R.string.one))
                .replaceAll("2", getResources().getString(R.string.two))
                .replaceAll("3", getResources().getString(R.string.three))
                .replaceAll("4", getResources().getString(R.string.four))
                .replaceAll("5", getResources().getString(R.string.five))
                .replaceAll("6", getResources().getString(R.string.six))
                .replaceAll("7", getResources().getString(R.string.seven))
                .replaceAll("8", getResources().getString(R.string.eight))
                .replaceAll("9", getResources().getString(R.string.nine))
                .replaceAll("0", getResources().getString(R.string.zero)) + " " + getResources().getString(R.string.SR_currency));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);


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
                MainActivity.viewPager.setCurrentItem(2);
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
            Call<WalletActiveListModel> walletActiveListModelCall = apiService.WALLET_ACTIVE_LIST_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token));
            walletActiveListModelCall.enqueue(new Callback<WalletActiveListModel>() {
                @Override
                public void onResponse(Call<WalletActiveListModel> call, Response<WalletActiveListModel> response) {
                    progressDialog.dismiss();
                    String status = response.body().getStatus();
                    if (status.equals("1")) {
                        arrayList = response.body().getData();
                        admin_discount = response.body().getAdminProfitDis();
                        myWalletAdapter = new MyWalletAdapter(arrayList, getActivity(), admin_discount);
                        recyclerView.setAdapter(myWalletAdapter);
                    } else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<WalletActiveListModel> call, Throwable t) {
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
