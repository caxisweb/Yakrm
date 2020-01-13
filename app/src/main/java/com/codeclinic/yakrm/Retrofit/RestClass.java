package com.codeclinic.yakrm.Retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by bhatt on 6/20/2017.
 */

public class RestClass {

    //public static final String BASE_URL = "http://test.yakrm.com/";//http://www.codeclinic.in/demo/yakrm/api/ http://yakrm.com/api/
    public static final String BASE_URL = "https://www.yakrm.com/api/";//http://www.codeclinic.in/demo/yakrm/api/ http://yakrm.com/api/
    //public static final String BASE_URL = "http://test.yakrm.com/api/";//http://www.codeclinic.in/demo/yakrm/api/ http://yakrm.com/api/
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(40, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(httpClient)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())

                    .build();
        }
        return retrofit;
    }


}
