package com.codeclinic.yakrm.Retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PaymentRestClass {
    private static Retrofit retrofit = null;

    public PaymentRestClass() {
    }

    public static Retrofit getClient() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(80, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        OkHttpClient client = (new OkHttpClient.Builder()).build();
        retrofit = (new retrofit2.Retrofit.Builder()).baseUrl("https://www.paytabs.com").addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build();
        return retrofit;
    }


}
