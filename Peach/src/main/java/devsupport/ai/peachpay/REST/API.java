package devsupport.ai.peachpay.REST;

import devsupport.ai.peachpay.Models.GetCheckoutIDModel;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface API {

    @Headers("Content-Type: application/json")
    @POST("checkout")
    Call<GetCheckoutIDModel> GET_CHECKOUT_ID_MODEL_CALL(@Body String Body);


}
