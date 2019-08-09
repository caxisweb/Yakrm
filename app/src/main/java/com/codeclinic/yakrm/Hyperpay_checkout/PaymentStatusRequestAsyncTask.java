package com.codeclinic.yakrm.Hyperpay_checkout;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Represents an async task to request a payment status from the server.
 */
public class PaymentStatusRequestAsyncTask extends AsyncTask<String, Void, String> {

    private PaymentStatusRequestListener listener;

    public PaymentStatusRequestAsyncTask(PaymentStatusRequestListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        if (params.length != 1) {
            return null;
        }

        String resourcePath = params[0];

        if (resourcePath != null) {
            return requestPaymentStatus(resourcePath);
        }

        return null;
    }

    @Override
    protected void onPostExecute(String paymentStatus) {
        if (listener != null) {
            if (paymentStatus == null) {
                listener.onErrorOccurred();

                return;
            }

            listener.onPaymentStatusReceived(paymentStatus);
        }
    }

    private String requestPaymentStatus(String resourcePath) {
        if (resourcePath == null) {
            return null;
        }

        URL url;
        String urlString;
        HttpURLConnection connection = null;
        String paymentStatus = null;

        try {
            urlString = Constants.BASE_URL + "paymentstatus/" + resourcePath;
            //URLEncoder.encode(resourcePath, "UTF-8");
            //urlString = "https://oppwa.com"+resourcePath;

            Log.d(Constants.LOG_TAG, "Status request url: " + urlString);

            url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(Constants.CONNECTION_TIMEOUT);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            paymentStatus = stringBuffer.toString();

            /*JsonReader jsonReader = new JsonReader(
                    new InputStreamReader(connection.getInputStream(), "UTF-8"));

            jsonReader.beginObject();

            Log.i("payment status response",jsonReader.toString());

            while (jsonReader.hasNext()) {

                if (jsonReader.nextName().equals("result")) {
                    paymentStatus = jsonReader.nextString();
                } else {
                    jsonReader.skipValue();
                }
            }

            jsonReader.endObject();
            jsonReader.close();*/

            Log.d(Constants.LOG_TAG, "Status: " + paymentStatus);
        } catch (Exception e) {
            Log.e(Constants.LOG_TAG, "Error: ", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return paymentStatus;
    }
}
