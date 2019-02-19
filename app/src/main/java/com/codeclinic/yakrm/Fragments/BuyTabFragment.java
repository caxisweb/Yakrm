package com.codeclinic.yakrm.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.codeclinic.yakrm.Activities.MainActivity;
import com.codeclinic.yakrm.Adapter.BuyTabListAdapter;
import com.codeclinic.yakrm.Models.AllVoucherListItemModel;
import com.codeclinic.yakrm.Models.AllVouchersListModel;
import com.codeclinic.yakrm.Models.FilterListItemModel;
import com.codeclinic.yakrm.Models.FilterListModel;
import com.codeclinic.yakrm.Models.GiftCategoryModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.Connection_Detector;
import com.codeclinic.yakrm.Utils.GridSpacingItemDecoration;
import com.codeclinic.yakrm.Utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class BuyTabFragment extends Fragment {

    RecyclerView recyclerView;
    BuyTabListAdapter buyTabListAdapter;
    List<AllVoucherListItemModel> arrayList = new ArrayList<>();
    List<FilterListItemModel> arrayList_filter = new ArrayList<>();
    API apiService;
    ProgressDialog progressDialog;
    LinearLayout layout_filter;
    SessionManager sessionManager;
    JSONObject jsonObject = new JSONObject();

    public BuyTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy_tab, container, false);
        sessionManager = new SessionManager(getActivity());
        layout_filter = view.findViewById(R.id.layout_filter);
        recyclerView = view.findViewById(R.id.recyclerView);
        apiService = RestClass.getClient().create(API.class);
        progressDialog = new ProgressDialog(getActivity());

        int spanCount = 2; // 3 columns
        int spacing = 10; // 50px
        boolean includeEdge = false;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));


        if (Connection_Detector.isInternetAvailable(getActivity())) {
            progressDialog.setMessage("Please Wait");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            if (MainActivity.filter_array == 0) {
                Call<AllVouchersListModel> allVouchersListModelCall = apiService.ALL_VOUCHERS_LIST_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token));
                allVouchersListModelCall.enqueue(new Callback<AllVouchersListModel>() {
                    @Override
                    public void onResponse(Call<AllVouchersListModel> call, Response<AllVouchersListModel> response) {
                        progressDialog.dismiss();
                        int status = response.body().getStatus();
                        if (status == 1) {
                            arrayList = response.body().getData();
                            if (MainActivity.arrayList != null) {
                                MainActivity.arrayList = (ArrayList<GiftCategoryModel>) response.body().getGiftCategory();
                            }
                            buyTabListAdapter = new BuyTabListAdapter(arrayList, getActivity());
                            recyclerView.setAdapter(buyTabListAdapter);
                        } else {
                            Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AllVouchersListModel> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                try {
                    jsonObject.put("gift_category_id", MainActivity.gift_category_id);
                    jsonObject.put("gift_type", MainActivity.gift_type);
                    jsonObject.put("gift_order", MainActivity.gift_order);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Call<FilterListModel> filterListModelCall = apiService.FILTER_LIST_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), jsonObject.toString());
                filterListModelCall.enqueue(new Callback<FilterListModel>() {
                    @Override
                    public void onResponse(Call<FilterListModel> call, Response<FilterListModel> response) {
                        progressDialog.dismiss();
                        String status = response.body().getStatus();
                        if (status.equals("1")) {
                            arrayList_filter = response.body().getData();
                        } else {
                            Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Call<AllVouchersListModel> allVouchersListModelCall = apiService.ALL_VOUCHERS_LIST_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token));
                            allVouchersListModelCall.enqueue(new Callback<AllVouchersListModel>() {
                                @Override
                                public void onResponse(Call<AllVouchersListModel> call, Response<AllVouchersListModel> response) {
                                    progressDialog.dismiss();
                                    int status = response.body().getStatus();
                                    if (status == 1) {
                                        arrayList = response.body().getData();
                                        if (MainActivity.arrayList != null) {
                                            MainActivity.arrayList = (ArrayList<GiftCategoryModel>) response.body().getGiftCategory();
                                        }
                                        buyTabListAdapter = new BuyTabListAdapter(arrayList, getActivity());
                                        recyclerView.setAdapter(buyTabListAdapter);
                                    } else {
                                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<AllVouchersListModel> call, Throwable t) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                                }
                            });
                            MainActivity.filter_array = 0;
                        }
                    }

                    @Override
                    public void onFailure(Call<FilterListModel> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }


        layout_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.drawer.isDrawerVisible(GravityCompat.END)) {
                    MainActivity.drawer.closeDrawer(GravityCompat.END);
                } else {
                    MainActivity.drawer.openDrawer(GravityCompat.END);
                }
            }
        });

        return view;
    }

}
