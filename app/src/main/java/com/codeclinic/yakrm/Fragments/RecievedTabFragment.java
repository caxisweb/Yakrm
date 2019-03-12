package com.codeclinic.yakrm.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codeclinic.yakrm.Adapter.RecievedListAdapter;
import com.codeclinic.yakrm.Models.RecievedGftListItemModel;
import com.codeclinic.yakrm.Models.RecievedGiftListModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.Connection_Detector;
import com.codeclinic.yakrm.Utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecievedTabFragment extends Fragment {


    RecyclerView recyclerView;
    List<RecievedGftListItemModel> arrayList = new ArrayList<>();
    RecievedListAdapter recievedListAdapter;
    ProgressDialog progressDialog;
    API apiService;
    SessionManager sessionManager;

    public RecievedTabFragment() {

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

        if (Connection_Detector.isInternetAvailable(getActivity())) {
            progressDialog.setMessage("Please Wait");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            Call<RecievedGiftListModel> recievedGiftListModelCall = apiService.RECIEVED_GIFT_LIST_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token));
            recievedGiftListModelCall.enqueue(new Callback<RecievedGiftListModel>() {
                @Override
                public void onResponse(Call<RecievedGiftListModel> call, Response<RecievedGiftListModel> response) {
                    progressDialog.dismiss();
                    String status = response.body().getStatus();
                    if (status.equals("1")) {
                        arrayList = response.body().getData();
                        recievedListAdapter = new RecievedListAdapter(arrayList, getActivity());
                        recyclerView.setAdapter(recievedListAdapter);
                    } else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RecievedGiftListModel> call, Throwable t) {
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
