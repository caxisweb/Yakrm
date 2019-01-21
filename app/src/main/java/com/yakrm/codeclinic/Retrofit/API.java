package com.yakrm.codeclinic.Retrofit;

import com.yakrm.codeclinic.Models.AddVoucherToCartModel;
import com.yakrm.codeclinic.Models.AllVouchersListModel;
import com.yakrm.codeclinic.Models.CartListModel;
import com.yakrm.codeclinic.Models.LoginModel;
import com.yakrm.codeclinic.Models.ProfileImageUpload;
import com.yakrm.codeclinic.Models.ProfileUpdateModel;
import com.yakrm.codeclinic.Models.RegistrationModel;
import com.yakrm.codeclinic.Models.RegistrationStep2Model;
import com.yakrm.codeclinic.Models.RemoveCartItemModel;
import com.yakrm.codeclinic.Models.VerifyOTPModel;
import com.yakrm.codeclinic.Models.VoucherDetailsListModel;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface API {

    @Headers("Content-Type: application/json")
    @POST("registration_step_1")
    Call<RegistrationModel> REGISTRATION_MODEL_CALL(@Body String Body);

    @Headers("Content-Type: application/json")
    @POST("verify_otp")
    Call<VerifyOTPModel> VERIFY_OTP_MODEL_CALL(@Header("Authorization") String header, @Body String Body);

    @Headers("Content-Type: application/json")
    @POST("registration_step_2")
    Call<RegistrationStep2Model> REGISTRATION_STEP_2_MODEL_CALL(@Header("Authorization") String header, @Body String Body);

    @Headers("Content-Type: application/json")
    @POST("login")
    Call<LoginModel> LOGIN_MODEL_CALL(@Body String Body);

    @Headers("Content-Type: application/json")
    @POST("update_user_profile")
    Call<ProfileUpdateModel> PROFILE_UPDATE_MODEL_CALL(@Header("Authorization") String header, @Body String Body);

    @Multipart
    @POST("profile_image_upload")
    Call<ProfileImageUpload> PROFILE_IMAGE_UPLOAD_CALL(@Header("Authorization") String header, @Part MultipartBody.Part image);

    @Headers("Content-Type: application/json")
    @GET("getAllActiveVoucher")
    Call<AllVouchersListModel> ALL_VOUCHERS_LIST_MODEL_CALL(@Header("Authorization") String header);

    @Headers("Content-Type: application/json")
    @POST("getAllVoucherByVendorId")
    Call<VoucherDetailsListModel> VOUCHER_DETAILS_LIST_MODEL_CALL(@Header("Authorization") String header, @Body String Body);

    @Headers("Content-Type: application/json")
    @POST("add_to_cart")
    Call<AddVoucherToCartModel> ADD_VOUCHER_TO_CART_MODEL_CALL(@Header("Authorization") String header, @Body String Body);

    @Headers("Content-Type: application/json")
    @GET("get_all_cart_list")
    Call<CartListModel> CART_LIST_MODEL_CALL(@Header("Authorization") String header);

    @Headers("Content-Type: application/json")
    @POST("remove_voucher_from_cart")
    Call<RemoveCartItemModel> REMOVE_CART_ITEM_MODEL_CALL(@Header("Authorization") String header, @Body String Body);
}
