package com.codeclinic.yakrm.Retrofit;

import com.codeclinic.yakrm.Models.ActiveVoucherListModel;
import com.codeclinic.yakrm.Models.AddToFavouritesModel;
import com.codeclinic.yakrm.Models.AddVoucherToCartModel;
import com.codeclinic.yakrm.Models.AllVouchersListModel;
import com.codeclinic.yakrm.Models.CartListModel;
import com.codeclinic.yakrm.Models.FavouritesListModel;
import com.codeclinic.yakrm.Models.FriendMobileNumberModel;
import com.codeclinic.yakrm.Models.LoginModel;
import com.codeclinic.yakrm.Models.PaymentTransactionModel;
import com.codeclinic.yakrm.Models.ProfileImageUpload;
import com.codeclinic.yakrm.Models.ProfileUpdateModel;
import com.codeclinic.yakrm.Models.RegistrationModel;
import com.codeclinic.yakrm.Models.RegistrationStep2Model;
import com.codeclinic.yakrm.Models.RemoveCartItemModel;
import com.codeclinic.yakrm.Models.SendVoucherToFriendModel;
import com.codeclinic.yakrm.Models.TransactionsRecordModel;
import com.codeclinic.yakrm.Models.VerifyOTPModel;
import com.codeclinic.yakrm.Models.VoucherDetailsListModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    @POST("getAllVoucherByBrandId")
    Call<VoucherDetailsListModel> VOUCHER_DETAILS_LIST_MODEL_CALL(@Header("Authorization") String header, @Body String Body);

    @Headers("Content-Type: application/json")
    @POST("addremove_to_favourite")
    Call<AddToFavouritesModel> ADD_TO_FAVOURITES_MODEL_CALL(@Header("Authorization") String header, @Body String Body);

    @Headers("Content-Type: application/json")
    @GET("getAllFavouritesList")
    Call<FavouritesListModel> FAVOURITES_LIST_MODEL_CALL(@Header("Authorization") String header);

    @Headers("Content-Type: application/json")
    @POST("add_to_cart")
    Call<AddVoucherToCartModel> ADD_VOUCHER_TO_CART_MODEL_CALL(@Header("Authorization") String header, @Body String Body);

    @Headers("Content-Type: application/json")
    @GET("get_all_cart_list")
    Call<CartListModel> CART_LIST_MODEL_CALL(@Header("Authorization") String header);

    @Headers("Content-Type: application/json")
    @POST("remove_voucher_from_cart")
    Call<RemoveCartItemModel> REMOVE_CART_ITEM_MODEL_CALL(@Header("Authorization") String header, @Body String Body);

    @Headers("Content-Type: application/json")
    @POST("voucher_purchase")
    Call<PaymentTransactionModel> PAYMENT_TRANSACTION_MODEL_CALL(@Header("Authorization") String header, @Body String Body);

    @Headers("Content-Type: application/json")
    @GET("get_all_usertransaction")
    Call<TransactionsRecordModel> TRANSACTIONS_RECORD_MODEL_CALL(@Header("Authorization") String header);

    @Headers("Content-Type: application/json")
    @GET("get_active_voucher_ofuser")
    Call<ActiveVoucherListModel> ACTIVE_VOUCHER_LIST_MODEL_CALL(@Header("Authorization") String header);

    @Headers("Content-Type: application/json")
    @POST("find_contact_no")
    Call<FriendMobileNumberModel> FRIEND_MOBILE_NUMBER_MODEL_CALL(@Header("Authorization") String header, @Body String Body);

    @Multipart
    @POST("send_voucher_as_gift")
    Call<SendVoucherToFriendModel> SEND_VOUCHER_TO_FRIEND_MODEL_CALL(@Header("Authorization") String header, @Part("voucher_id") RequestBody voucher_id, @Part("phone") RequestBody phone, @Part("description") RequestBody desc, @Part("voucher_payment_detail_id") RequestBody voucher_payment_id);
}
