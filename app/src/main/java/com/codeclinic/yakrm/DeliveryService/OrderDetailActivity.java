package com.codeclinic.yakrm.DeliveryService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.codeclinic.yakrm.Activities.CartActivity;
import com.codeclinic.yakrm.Activities.CompletingPurchasingActivity;
import com.codeclinic.yakrm.DeliveryModel.OrderCancelModel;
import com.codeclinic.yakrm.DeliveryModel.OrderDetailResponseModel;
import com.codeclinic.yakrm.R;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.RestClass;
import com.codeclinic.yakrm.Utils.ImageURL;
import com.codeclinic.yakrm.Utils.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;

public class OrderDetailActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    SessionManager sessionManager;
    API apiService;
    LayoutInflater inflater;

    String order_id;
    double total_amount;

    CardView card_image,btn_payment;
    TextView tv_order_id,tv_order_status,tv_product_count,tv_home_address,tv_store_address,tv_notes;
    TextView tv_product_cost,tv_servicetax,tv_delivery_charge;
    TextView tv_delivery_boy,tv_delivery_contact;
    TextView tv_payment_status,tv_deliveryboy_status;
    TextView tv_total_cost;
    LinearLayout lv_productlist,lv_paynow,lv_footer,lv_payment_status;
    Button btn_chat,btn_cancel;
    ImageView img_product;
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
        inflater = LayoutInflater.from(this);

        Log.i("token",sessionManager.getUserDetails().get(SessionManager.User_Token));

        tv_order_id=findViewById(R.id.tv_order_id);
        tv_order_status=findViewById(R.id.tv_order_status);
        tv_product_count=findViewById(R.id.tv_product_count);
        tv_home_address=findViewById(R.id.tv_home_address);
        tv_store_address=findViewById(R.id.tv_store_address);
        tv_notes=findViewById(R.id.tv_notes);
        tv_total_cost=findViewById(R.id.tv_total_cost);
        tv_product_cost=findViewById(R.id.tv_product_cost);
        tv_servicetax=findViewById(R.id.tv_service_tax);
        tv_delivery_charge=findViewById(R.id.tv_delivery_charge);

        tv_delivery_boy=findViewById(R.id.tv_delivery_boy);
        tv_delivery_contact=findViewById(R.id.tv_delivery_contact);

        tv_payment_status=findViewById(R.id.tv_payment_status);
        tv_deliveryboy_status=findViewById(R.id.tv_deliveryboy_status);

        btn_payment=findViewById(R.id.btn_payment);
        btn_chat=findViewById(R.id.btn_chat);
        btn_cancel=findViewById(R.id.btn_cancle);

        lv_productlist=findViewById(R.id.lv_productlist);
        lv_paynow=findViewById(R.id.lv_paynow);
        lv_payment_status=findViewById(R.id.lv_payment_status);
        lv_footer=findViewById(R.id.lv_footer);

        card_image = findViewById(R.id.card_image);
        img_product=findViewById(R.id.img_product);


        btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(OrderDetailActivity.this, CompletePaymentActivity.class);
                intent.putExtra("price", String.valueOf("1"));
                intent.putExtra("order_id", order_id);
                startActivityForResult(intent,1);
            }
        });


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callOrderCancle();
            }
        });

        getOrderDetail();
    }

    void getOrderDetail(){

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

                        if(response.body().getNotes()!=null){
                            tv_notes.setText(response.body().getNotes());
                        }

                        for(int i=0;i<response.body().getOrderDetail().size();i++){

                            View custLayout = inflater.inflate(R.layout.custome_myproductlist_view, null, false);

                            TextView tv_product = (TextView) custLayout.findViewById(R.id.tv_productname);
                            TextView tv_qty = (TextView) custLayout.findViewById(R.id.tv_qty);

                            tv_product.setText(response.body().getOrderDetail().get(i).getProductTitle());
                            tv_qty.setText(response.body().getOrderDetail().get(i).getQuantity());

                            lv_productlist.addView(custLayout);
                        }

                        if(response.body().getPrice().equals("0")){
                            tv_order_status.setText(getString(R.string.pending));
                            tv_payment_status.setVisibility(View.VISIBLE);
                            tv_product_cost.setText("0"+ getString(R.string.Sr));
                            lv_paynow.setVisibility(View.GONE);
                        }else {

                            lv_paynow.setVisibility(View.VISIBLE);
                            tv_payment_status.setVisibility(View.GONE);
                            tv_delivery_charge.setText(response.body().getOrder_charge()+getString(R.string.Sr));
                            tv_product_cost.setText(response.body().getPrice()+ getString(R.string.Sr));
                            tv_servicetax.setText(response.body().getService_charge()+ getString(R.string.Sr));
                            total_amount=Double.parseDouble(response.body().getPrice())+Double.parseDouble(response.body().getService_charge())+Double.parseDouble(response.body().getOrder_charge());
                            tv_total_cost.setText(getString(R.string.total_cost)+" "+total_amount +getString(R.string.Sr));
                            btn_payment.setVisibility(View.VISIBLE);
                        }

                        if(response.body().getOrder_status().equals("1")){

                            tv_order_status.setText(getString(R.string.pending));
                            tv_deliveryboy_status.setVisibility(View.VISIBLE);
                            tv_delivery_boy.setVisibility(View.GONE);
                            tv_delivery_contact.setVisibility(View.GONE);
                            lv_paynow.setVisibility(View.GONE);
                            btn_chat.setVisibility(View.GONE);
                            btn_cancel.setVisibility(View.VISIBLE);

                        }else if(response.body().getOrder_status().equals("5")){

                            tv_order_status.setText(getString(R.string.delivered));
                            tv_deliveryboy_status.setVisibility(View.GONE);
                            tv_delivery_boy.setText(getString(R.string.name) + " : " + response.body().getName());
                            tv_delivery_contact.setText(getString(R.string.contact) + " : " + response.body().getPhone());
                            lv_paynow.setVisibility(View.VISIBLE);
                            btn_cancel.setVisibility(View.GONE);
                            btn_payment.setVisibility(View.GONE);
                            lv_payment_status.setVisibility(View.GONE);

                        }else if(response.body().getOrder_status().equals("6")){

                            tv_order_status.setText(getString(R.string.cancel));
                            tv_deliveryboy_status.setVisibility(View.GONE);
                            tv_delivery_boy.setText(getString(R.string.name) + " : " + response.body().getName());
                            tv_delivery_contact.setText(getString(R.string.contact) + " : " + response.body().getPhone());
                            lv_footer.setVisibility(View.GONE);

                        } else {

                            if(response.body().getOrder_status().equals("4")) {
                                tv_order_status.setText(getString(R.string.dispatch));
                            }else{
                                tv_order_status.setText(getString(R.string.accept));
                            }

                            lv_footer.setVisibility(View.VISIBLE);
                            tv_deliveryboy_status.setVisibility(View.GONE);
                            tv_delivery_boy.setText(getString(R.string.name) + " : " + response.body().getName());
                            tv_delivery_contact.setText(getString(R.string.contact) + " : " + response.body().getPhone());
                            btn_chat.setVisibility(View.VISIBLE);

                            if(response.body().getIs_payment_complete().equals("1")){
                                btn_payment.setVisibility(View.GONE);
                                lv_payment_status.setVisibility(View.VISIBLE);
                            }else{
                                btn_payment.setVisibility(View.VISIBLE);
                                lv_payment_status.setVisibility(View.GONE);
                            }
                        }

                        Log.i("image",ImageURL.produtList +response.body().getOrder_image());

                        if(isEmpty(response.body().getOrder_image())){
                            card_image.setVisibility(View.GONE);
                        }else{
                            card_image.setVisibility(View.VISIBLE);
                            Picasso.with(OrderDetailActivity.this).load(ImageURL.produtList +response.body().getOrder_image()).into(img_product);
                        }

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

    void callOrderCancle(){

        try {

            progressDialog.setMessage(getResources().getString(R.string.Please_Wait));
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();

            JSONObject data=new JSONObject();
            data.put("order_id",order_id);

            Call<OrderCancelModel> orderStatusChange=apiService.ORDER_CANCLE(sessionManager.getUserDetails().get(SessionManager.User_Token),data.toString());
            orderStatusChange.enqueue(new Callback<OrderCancelModel>() {
                @Override
                public void onResponse(Call<OrderCancelModel> call, Response<OrderCancelModel> response) {

                    progressDialog.dismiss();

                    if(response.body().getStatus().equals("1")){
                        finish();
                        Toast.makeText(OrderDetailActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(OrderDetailActivity.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<OrderCancelModel> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(OrderDetailActivity.this,"server error",Toast.LENGTH_LONG).show();
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1){
            if (resultCode == RESULT_OK) {
                order_id = data.getStringExtra("order_id");
                getOrderDetail();
            }
        }
    }

}
