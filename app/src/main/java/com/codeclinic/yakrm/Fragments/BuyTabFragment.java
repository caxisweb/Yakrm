package com.codeclinic.yakrm.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.codeclinic.yakrm.Activities.MainActivity;
import com.codeclinic.yakrm.Adapter.BuyTabListAdapter;
import com.codeclinic.yakrm.Adapter.FilterListAdapter;
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

import static com.codeclinic.yakrm.Activities.MainActivity.cat_arrayList_id;
import static com.codeclinic.yakrm.Activities.MainActivity.cat_arrayList_name;
import static com.codeclinic.yakrm.Activities.MainActivity.category_classification_array;


/**
 * A simple {@link Fragment} subclass.
 */
public class BuyTabFragment extends Fragment {

    RecyclerView recyclerView;
    BuyTabListAdapter buyTabListAdapter;
    FilterListAdapter filterListAdapter;
    List<AllVoucherListItemModel> arrayList = new ArrayList<>();
    List<FilterListItemModel> arrayList_filter = new ArrayList<>();
    API apiService;
    ProgressDialog progressDialog;
    LinearLayout layout_filter;
    SessionManager sessionManager;
    String login_flag = "0";
    JSONObject jsonObject = new JSONObject();

    public BuyTabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy_tab, container, false);
        setRetainInstance(true);
        sessionManager = new SessionManager(getActivity());
        layout_filter = view.findViewById(R.id.layout_filter);
        recyclerView = view.findViewById(R.id.recyclerView);
        apiService = RestClass.getClient().create(API.class);
        progressDialog = new ProgressDialog(getActivity());
        if (sessionManager.isLoggedIn()) {
            login_flag = "1";
        }

        if (Connection_Detector.isInternetAvailable(getActivity())) {

            progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
            if (MainActivity.filter_array == 0) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("is_login", login_flag);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Call<AllVouchersListModel> allVouchersListModelCall = apiService.ALL_VOUCHERS_LIST_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), jsonObject.toString());
                allVouchersListModelCall.enqueue(new Callback<AllVouchersListModel>() {
                    @Override
                    public void onResponse(Call<AllVouchersListModel> call, Response<AllVouchersListModel> response) {
                        progressDialog.dismiss();
                        int status = response.body().getStatus();
                        if (status == 1) {
                            if (login_flag.equals("0")) {
                                MainActivity.textCartItemCount.setVisibility(View.GONE);
                            } else {
                                MainActivity.textCartItemCount.setText(response.body().getTotal_cart_item());
                            }
                            arrayList = response.body().getData();
                            MainActivity.arrayList.clear();
                            cat_arrayList_id.clear();
                            cat_arrayList_name.clear();
                            category_classification_array.clear();
                            if (MainActivity.arrayList.size() == 0) {
                                MainActivity.arrayList = (ArrayList<GiftCategoryModel>) response.body().getGiftCategory();
                                for (int k = 0; k < MainActivity.arrayList.size(); k++) {
                                    String language = String.valueOf(getResources().getConfiguration().locale);
                                    cat_arrayList_id.add(MainActivity.arrayList.get(k).getId());
                                    if (language.equals("ar")) {
                                        MainActivity.cat_arrayList_name.add(MainActivity.arrayList.get(k).getArabGiftCategoryName());
                                    } else {
                                        MainActivity.cat_arrayList_name.add(MainActivity.arrayList.get(k).getGiftCategoryName());
                                    }

                                }
                                MainActivity.btn = new Button[MainActivity.arrayList.size()];
                                for (int i = 0; i < MainActivity.arrayList.size(); i++) {
                                    MainActivity.btn[i] = new Button(getActivity());
                                    final float scale = getResources().getDisplayMetrics().density;
                                    int pixels_height = (int) (30 * scale + 0.5f);
                                    int pixels_padding = (int) (10 * scale + 0.5f);
                                    MainActivity.btn[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, pixels_height));
                                    MainActivity.btn[i].setId(i);
                                    MainActivity.btn[i].setPadding(pixels_padding, 0, pixels_padding, 0);
                                    MainActivity.btn[i].setGravity(Gravity.CENTER);
                                    MainActivity.btn[i].setBackground(getResources().getDrawable(R.drawable.flow_layout_text_background));
                                    MainActivity.btn[i].setTextColor(getResources().getColor(R.color.white));
                                    MainActivity.btn[i].setTextSize(10);
                                    if (sessionManager.getLanguage("Language", "en").equals("en")) {
                                        MainActivity.btn[i].setText(MainActivity.arrayList.get(i).getGiftCategoryName());
                                    } else {
                                        MainActivity.btn[i].setText(MainActivity.arrayList.get(i).getArabGiftCategoryName());
                                    }
                                    MainActivity.btn[i].setTextColor(getResources().getColor(R.color.black));

                                    final Handler mHandler = new Handler();
                                    MainActivity.btn[i].setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Button button = (Button) view;
                                            boolean pressed = false;
                                            if (view.getTag() instanceof Boolean) {
                                                pressed = (boolean) view.getTag();
                                            }
                                            final boolean newPressed = !pressed;

                                            if (newPressed) {
                                                button.setTextColor(getResources().getColor(R.color.white));
                                                button.setBackground(getResources().getDrawable(R.drawable.flow_layout_pressed_background));
                                                category_classification_array.add(button.getText().toString());
                                            } else {
                                                button.setTextColor(getResources().getColor(R.color.black));
                                                button.setBackground(getResources().getDrawable(R.drawable.flow_layout_text_background));
                                                category_classification_array.remove(button.getText().toString());
                                            }
                                            // setTag to store state
                                            view.setTag(newPressed);
                                            final View vRun = view;
                                            Runnable run = new Runnable() {
                                                @Override
                                                public void run() {
                                                    vRun.setPressed(newPressed);
                                                }
                                            };
                                            mHandler.post(run);
                                            mHandler.postDelayed(run, 100);

                                        }
                                    });
                                    MainActivity.flowLayout.addView(MainActivity.btn[i]);
                                    if (!sessionManager.getLanguage("Language", "en").equals("en")) {
                                        MainActivity.flowLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                                    }
                                }
                            }
                            int spanCount = 2; // 3 columns
                            int spacing = 10; // 50px
                            boolean includeEdge = false;
                            recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
                            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                            buyTabListAdapter = new BuyTabListAdapter(arrayList, getActivity());
                            recyclerView.setAdapter(buyTabListAdapter);
                        } else {
                            //Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AllVouchersListModel> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), getResources().getString(R.string.Server_Error), Toast.LENGTH_SHORT).show();
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
                Call<FilterListModel> filterListModelCall = apiService.FILTER_LIST_MODEL_CALL(jsonObject.toString());
                filterListModelCall.enqueue(new Callback<FilterListModel>() {
                    @Override
                    public void onResponse(Call<FilterListModel> call, Response<FilterListModel> response) {
                        progressDialog.dismiss();
                        String status = response.body().getStatus();
                        if (status.equals("1")) {
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setNestedScrollingEnabled(false);
                            arrayList_filter = response.body().getData();
                            filterListAdapter = new FilterListAdapter(arrayList_filter, getActivity());
                            recyclerView.setAdapter(filterListAdapter);
                        } else {
                            Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            try {
                                jsonObject.put("is_login", login_flag);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Call<AllVouchersListModel> allVouchersListModelCall = apiService.ALL_VOUCHERS_LIST_MODEL_CALL(sessionManager.getUserDetails().get(SessionManager.User_Token), jsonObject.toString());
                            allVouchersListModelCall.enqueue(new Callback<AllVouchersListModel>() {
                                @Override
                                public void onResponse(Call<AllVouchersListModel> call, Response<AllVouchersListModel> response) {
                                    progressDialog.dismiss();
                                    int status = response.body().getStatus();
                                    if (status == 1) {
                                        if (login_flag.equals("0")) {
                                            MainActivity.textCartItemCount.setVisibility(View.GONE);
                                        } else {
                                            MainActivity.textCartItemCount.setText(response.body().getTotal_cart_item());
                                        }
                                        arrayList = response.body().getData();
                                        if (MainActivity.arrayList != null) {
                                            MainActivity.arrayList = (ArrayList<GiftCategoryModel>) response.body().getGiftCategory();
                                            for (int k = 0; k < MainActivity.arrayList.size(); k++) {
                                                cat_arrayList_id.add(MainActivity.arrayList.get(k).getId());
                                                MainActivity.cat_arrayList_name.add(MainActivity.arrayList.get(k).getGiftCategoryName());
                                            }
                                        }
                                        int spanCount = 2; // 3 columns
                                        int spacing = 10; // 50px
                                        boolean includeEdge = false;
                                        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
                                        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                                        buyTabListAdapter = new BuyTabListAdapter(arrayList, getActivity());
                                        recyclerView.setAdapter(buyTabListAdapter);
                                    } else {
                                        String language = String.valueOf(getResources().getConfiguration().locale);
                                        if (language.equals("ar")) {
                                            if (response.body().getArab_message() != null) {
                                                Toast.makeText(getActivity(), response.body().getArab_message(), Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<AllVouchersListModel> call, Throwable t) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), getResources().getString(R.string.Server_Error), Toast.LENGTH_SHORT).show();
                                }
                            });
                            MainActivity.filter_array = 0;
                        }
                    }

                    @Override
                    public void onFailure(Call<FilterListModel> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), getResources().getString(R.string.Server_Error), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.No_Internet_Connection), Toast.LENGTH_SHORT).show();
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
