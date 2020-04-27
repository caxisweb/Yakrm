package com.codeclinic.yakrm.DeliveryServiceFragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.se.omapi.Session;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.codeclinic.yakrm.Activities.LoginActivity;
import com.codeclinic.yakrm.DeliveryModel.PlaceOrderModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.SessionManager;
import com.codeclinic.yakrm.Utils.UserDeliveryInfo;
import com.schibstedspain.leku.LocationPickerActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.schibstedspain.leku.LocationPickerActivityKt.LATITUDE;
import static com.schibstedspain.leku.LocationPickerActivityKt.LOCATION_ADDRESS;
import static com.schibstedspain.leku.LocationPickerActivityKt.LONGITUDE;
import static com.schibstedspain.leku.LocationPickerActivityKt.TIME_ZONE_DISPLAY_NAME;
import static com.schibstedspain.leku.LocationPickerActivityKt.TIME_ZONE_ID;
import static com.schibstedspain.leku.LocationPickerActivityKt.TRANSITION_BUNDLE;
import static com.schibstedspain.leku.LocationPickerActivityKt.ZIPCODE;

public class NewOrderFragment extends Fragment {

    SessionManager sessionManager;
    ProgressDialog progressDialog;
    API apiService;

    private View mainView;

    private int MAP_BUTTON_REQUEST_CODE = 1;
    private int MAP_LOCATION_OPTION = 1;//1 for Pickup and 2 for Destination

    CardView btn_addproduct, btn_home_address, btn_shop_address;
    LinearLayout lv_productlist,lv_order;
    EditText edt_pname, edt_pqty;
    TextView tv_home_address, tv_shop_address;

    ArrayList<String> product_name = new ArrayList<>();
    ArrayList<String> product_qty = new ArrayList<>();

    String str_home_address;
    double str_home_lat=0, str_home_long=0;

    String str_store_address;
    double str_store_lat=0, str_store_long=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.new_order_fragment, null);

        sessionManager=new SessionManager(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        apiService = RestClass.getClientDelivery().create(API.class);

        lv_productlist = mainView.findViewById(R.id.lv_productlist);

        btn_addproduct = mainView.findViewById(R.id.btn_add_product);
        btn_home_address = mainView.findViewById(R.id.btn_home_address);
        btn_shop_address = mainView.findViewById(R.id.btn_shop_address);

        edt_pname = mainView.findViewById(R.id.edt_product);
        edt_pqty = mainView.findViewById(R.id.edt_quantity);

        tv_home_address = mainView.findViewById(R.id.tv_home_address);
        tv_shop_address = mainView.findViewById(R.id.tv_shop_address);

        lv_order = mainView.findViewById(R.id.lv_order);

        btn_addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    String name = edt_pname.getText().toString().trim();
                    String qty = edt_pqty.getText().toString().trim();

                    if (name.equals("")) {
                        edt_pname.setError("Please enter Product name");
                    } else if (qty.equals("")) {
                        edt_pqty.setError("Please enter Quantity");
                    } else if (qty.equals("0")) {
                        edt_pqty.setError("0 Quantity is not acceptable");
                    } else {

                        edt_pqty.setText("");
                        edt_pname.setText("");

                        final View custLayout = getActivity().getLayoutInflater().inflate(R.layout.custome_product_view, null);

                        TextView tv_product = (TextView) custLayout.findViewById(R.id.tv_productname);
                        TextView tv_qty = (TextView) custLayout.findViewById(R.id.tv_qty);
                        ImageView img_remove = (ImageView) custLayout.findViewById(R.id.img_remove);

                        tv_product.setText(name);
                        tv_qty.setText(qty);

                        img_remove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {

                                    product_name.remove(lv_productlist.indexOfChild(custLayout));
                                    product_qty.remove(lv_productlist.indexOfChild(custLayout));
                                    lv_productlist.removeView(custLayout);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                        product_name.add(name);
                        product_qty.add(qty);

                        lv_productlist.addView(custLayout);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        });



        btn_home_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    MAP_LOCATION_OPTION = 1;
                    openMapPicker();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btn_shop_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    MAP_LOCATION_OPTION = 2;
                    openMapPicker();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        lv_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(product_name.size()==0){
                    Toast.makeText(getActivity(),"Please Add Product",Toast.LENGTH_LONG).show();
                }else if(str_home_address.equals("")){
                    Toast.makeText(getActivity(),"Please select Delivery address",Toast.LENGTH_LONG).show();
                }else{

                    try {

                        progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
                        progressDialog.setIndeterminate(true);
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        JSONObject data=new JSONObject();
                        data.put("user_address",str_home_address);
                        data.put("user_latitude",str_home_lat);
                        data.put("user_longitude",str_home_long);

                        data.put("shop_address",str_store_address);
                        data.put("shop_latitude",str_store_lat);
                        data.put("shop_longitude",str_store_long);

                        JSONArray productlist = new JSONArray();

                        for(int i=0;i<product_name.size();i++){

                            JSONObject products=new JSONObject();
                            products.put("product_title",product_name.get(i));
                            products.put("quantity",product_qty.get(i));
                            productlist.put(products);
                        }

                        data.put("order_detail",productlist);

                        Log.i("order_data",data.toString());

                        Call<PlaceOrderModel> placeOrder=apiService.placeOrder(sessionManager.getUserDetails().get(SessionManager.User_Token),data.toString());
                        placeOrder.enqueue(new Callback<PlaceOrderModel>() {
                            @Override
                            public void onResponse(Call<PlaceOrderModel> call, Response<PlaceOrderModel> response) {

                                progressDialog.dismiss();

                                if(response.body().getStatus().equals("1")){
                                    Toast.makeText(getActivity(),response.body().getMessage(),Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getActivity(),response.body().getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<PlaceOrderModel> call, Throwable t) {
                                Toast.makeText(getActivity(),"server error",Toast.LENGTH_LONG).show();
                            }
                        });

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        });

        return mainView;
    }

    public void openMapPicker() {
        Intent locationPickerIntent = new LocationPickerActivity.Builder().withLocation(UserDeliveryInfo.latitude, UserDeliveryInfo.longitude)
                .withGeolocApiKey("AIzaSyDuio1SeutMjrH3YpZux-J7ahjolmQohrM")
                .withGooglePlacesEnabled()
                .withGoogleTimeZoneEnabled()
                .withUnnamedRoadHidden()
                .build(getActivity())
                .putExtra("test", "this is a test");
        startActivityForResult(locationPickerIntent, MAP_BUTTON_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            Log.d("RESULT****", "OK");
            if (requestCode == 1) {

                if (MAP_LOCATION_OPTION == 1) {
                    str_home_lat = data.getDoubleExtra(LATITUDE, 0.0);
                } else {
                    str_store_lat = data.getDoubleExtra(LATITUDE, 0.0);
                }


                if (MAP_LOCATION_OPTION == 1) {
                    str_home_long = data.getDoubleExtra(LONGITUDE, 0.0);
                } else {
                    str_store_long = data.getDoubleExtra(LONGITUDE, 0.0);
                }

                String address = data.getStringExtra(LOCATION_ADDRESS);

                Log.d("ADDRESS****", address);

                String postalcode = data.getStringExtra(ZIPCODE);

                Log.d("POSTALCODE****", postalcode);

                Bundle bundle = data.getBundleExtra(TRANSITION_BUNDLE);

                Log.d("BUNDLE TEXT****", bundle.getString("test"));

                String timeZoneId = data.getStringExtra(TIME_ZONE_ID);
                if (timeZoneId != null) {
                    Log.d("TIME ZONE ID****", timeZoneId);
                }
                String timeZoneDisplayName = data.getStringExtra(TIME_ZONE_DISPLAY_NAME);
                if (timeZoneDisplayName != null) {
                    Log.d("TIME ZONE NAME****", timeZoneDisplayName);
                }
                if (MAP_LOCATION_OPTION == 1) {
                    str_home_address = address;
                    tv_home_address.setText(address);
                } else {
                    str_store_address = address;
                    tv_shop_address.setText(address);
                }
            }


        }
        if (resultCode == Activity.RESULT_CANCELED) {
            Log.d("RESULT****", "CANCELLED");
        }
    }
}
