package com.codeclinic.yakrm.DeliveryService;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.codeclinic.yakrm.DeliveryModel.OrderDetailResponseModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.SessionManager;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    SessionManager sessionManager;
    API apiService;

    String order_id;

    TextView tv_order_id,tv_order_status,tv_product_count,tv_home_address,tv_store_address;
    TextView tv_product_cost,tv_servicetax,tv_delivery_charge;
    TextView tv_delivery_boy,tv_delivery_contact;

    LinearLayout lv_productlist;

    ImageView img_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetail);

        Bundle b=getIntent().getExtras();
        order_id=b.getString("order_id");

        img_back=findViewById(R.id.img_back);
        String language = String.valueOf(getResources().getConfiguration().locale);
        if (language.equals("ar")) {
            img_back.setImageDrawable(getResources().getDrawable(R.drawable.back_right_img));
        }
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sessionManager = new SessionManager(this);
        progressDialog = new ProgressDialog(this);
        apiService = RestClass.getClientDelivery().create(API.class);
        final LayoutInflater inflater = LayoutInflater.from(this);

        tv_order_id=findViewById(R.id.tv_order_id);
        tv_order_status=findViewById(R.id.tv_order_status);
        tv_product_count=findViewById(R.id.tv_product_count);
        tv_home_address=findViewById(R.id.tv_home_address);
        tv_store_address=findViewById(R.id.tv_store_address);

        tv_product_cost=findViewById(R.id.tv_product_cost);
        tv_servicetax=findViewById(R.id.tv_service_tax);
        tv_delivery_charge=findViewById(R.id.tv_delivery_charge);

        tv_delivery_boy=findViewById(R.id.tv_delivery_boy);
        tv_delivery_contact=findViewById(R.id.tv_delivery_contact);

        lv_productlist=findViewById(R.id.lv_productlist);

        progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

        try {

            JSONObject data=new JSONObject();
            data.put("order_id",order_id);

            Call<OrderDetailResponseModel> orderDetail=apiService.OrderDetail(sessionManager.getUserDetails().get(SessionManager.User_Token),data.toString());
            orderDetail.enqueue(new Callback<OrderDetailResponseModel>() {
                @Override
                public void onResponse(Call<OrderDetailResponseModel> call, Response<OrderDetailResponseModel> response) {

                    progressDialog.dismiss();

                    if(response.body().getStatus().equals("1")){

                        tv_order_id.setText(getString(R.string.order_id)+" : "+response.body().getId());
                        tv_product_count.setText(getString(R.string.product)+" : "+response.body().getOrderDetail().size());
                        tv_home_address.setText(response.body().getUserAddress());
                        tv_store_address.setText(response.body().getShopAddress());

                        for(int i=0;i<response.body().getOrderDetail().size();i++){

                            View custLayout = inflater.inflate(R.layout.custome_myproductlist_view, null, false);

                            TextView tv_product = (TextView) custLayout.findViewById(R.id.tv_productname);
                            TextView tv_qty = (TextView) custLayout.findViewById(R.id.tv_qty);

                            tv_product.setText(response.body().getOrderDetail().get(i).getProductTitle());
                            tv_qty.setText(response.body().getOrderDetail().get(i).getQuantity());

                            lv_productlist.addView(custLayout);
                        }

                        tv_product_cost.setText(response.body().getPrice()+" "+getString(R.string.s_r));
                        tv_delivery_boy.setText(getString(R.string.name)+" : "+response.body().getName());
                        tv_delivery_contact.setText(getString(R.string.contact)+" : "+response.body().getPhone());

                    }else{
                        Toast.makeText(OrderDetailActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<OrderDetailResponseModel> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(OrderDetailActivity.this,"server error",Toast.LENGTH_LONG).show();
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
