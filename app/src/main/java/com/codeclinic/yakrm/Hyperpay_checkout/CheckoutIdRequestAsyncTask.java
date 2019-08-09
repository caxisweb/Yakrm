package com.codeclinic.yakrm.Hyperpay_checkout;

import android.os.AsyncTask;
import android.util.Log;

import com.codeclinic.yakrm.Models.GetCheckoutIDModel;
import com.codeclinic.yakrm.Retrofit.API;
import com.codeclinic.yakrm.Retrofit.RestClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Represents an async task to request a checkout id from the server.
 */
public class CheckoutIdRequestAsyncTask extends AsyncTask<String, Void, String> {

    String checkoutId;
    private CheckoutIdRequestListener listener;

    public CheckoutIdRequestAsyncTask(CheckoutIdRequestListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        if (params.length != 2) {
            return null;
        }

        String amount = params[0];
        String currency = params[1];

        return requestCheckoutId(amount);
    }

    @Override
    protected void onPostExecute(String checkoutId) {
        if (listener != null) {
            listener.onCheckoutIdReceived(checkoutId);
        }
    }

    private String requestCheckoutId(String amount) {

        API apiService = RestClass.getClient().create(API.class);
        JSONObject jobj = new JSONObject();

        try {
            jobj.put("price", amount);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Call<GetCheckoutIDModel> getCheckoutIDModelCall = apiService.GET_CHECKOUT_ID_MODEL_CALL(jobj.toString());
        getCheckoutIDModelCall.enqueue(new Callback<GetCheckoutIDModel>() {
            @Override
            public void onResponse(Call<GetCheckoutIDModel> call, Response<GetCheckoutIDModel> response) {
                checkoutId = response.body().getId();

            }

            @Override
            public void onFailure(Call<GetCheckoutIDModel> call, Throwable t) {
                Log.i("checkout_id", t.getMessage());
                checkoutId = "0";
            }
        });


        return checkoutId;
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}