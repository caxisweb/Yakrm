package com.codeclinic.yakrm.DeliveryServiceFragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codeclinic.yakrm.DeliveryModel.OrderListModel;
import com.codeclinic.yakrm.DeliveryModel.OrderlistResponseModel;
import com.codeclinic.yakrm.DeliveryServiceAdepter.MyOrderAdepter;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrderFragment extends Fragment {

    SessionManager sessionManager;
    ProgressDialog progressDialog;
    API apiService;

    List<OrderListModel> myorderlist = new ArrayList<>();

    private View mainView;

    RecyclerView rc_orderlist;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.my_order_fragment, null);

        sessionManager = new SessionManager(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        apiService = RestClass.getClientDelivery().create(API.class);

        Log.i("tocken",sessionManager.getUserDetails().get(SessionManager.User_Token));

        rc_orderlist = mainView.findViewById(R.id.rc_orderlist);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rc_orderlist.setLayoutManager(layoutManager);
        rc_orderlist.setHasFixedSize(true);
        rc_orderlist.setNestedScrollingEnabled(true);


        getOrderList();

        return mainView;
    }

    void getOrderList(){
        progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Call<OrderlistResponseModel> getOrderList=apiService.GetOrderList(sessionManager.getUserDetails().get(SessionManager.User_Token));
        getOrderList.enqueue(new Callback<OrderlistResponseModel>() {
            @Override
            public void onResponse(Call<OrderlistResponseModel> call, Response<OrderlistResponseModel> response) {

                progressDialog.dismiss();

                if(response.body().getStatus().equals("1")){

                    myorderlist=response.body().getOrderlist();

                    MyOrderAdepter adepter = new MyOrderAdepter(myorderlist, getActivity());
                    rc_orderlist.setAdapter(adepter);

                }else{
                    if (sessionManager.getLanguage("Langauage", "en").equals("en")) {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), response.body().getArab_message(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<OrderlistResponseModel> call, Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(getActivity(),"server error",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getOrderList();
    }
}
