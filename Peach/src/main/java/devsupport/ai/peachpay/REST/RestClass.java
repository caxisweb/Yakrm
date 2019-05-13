package devsupport.ai.peachpay.REST;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by bhatt on 6/20/2017.
 */

public class RestClass {

    public static final String BASE_URL = "http://test.yakrm.com/api/";//http://www.codeclinic.in/demo/yakrm/api/ http://yakrm.com/api/
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


/*    private static OkHttpClient Client() {


        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .header("Header Keys", "")
                        .header("Consumer Key", "")
                        .header("Consumer Secret", "")
                        .header("Access Token", "")
                        .header("Access Token Secret", ""); // <-- this is the important line

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        return httpClient.build();

    }*/

}
