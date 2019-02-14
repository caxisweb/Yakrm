package com.codeclinic.yakrm.Retrofit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PaymentRestClass {
    private static Retrofit retrofit = null;

    public PaymentRestClass() {
    }

    public static Retrofit getClient() {
        OkHttpClient client = (new OkHttpClient.Builder()).build();
        retrofit = (new retrofit2.Retrofit.Builder()).baseUrl("https://www.paytabs.com").addConverterFactory(GsonConverterFactory.create()).client(client).build();
        return retrofit;
    }
}
