package com.codeclinic.yakrm.Retrofit;

import com.codeclinic.yakrm.Models.ActiveVoucherListModel;
import com.codeclinic.yakrm.Models.AddCardDetailsModel;
import com.codeclinic.yakrm.Models.AddToFavouritesModel;
import com.codeclinic.yakrm.Models.AddVoucherToCartModel;
import com.codeclinic.yakrm.Models.AllVouchersListModel;
import com.codeclinic.yakrm.Models.CartListModel;
import com.codeclinic.yakrm.Models.ChangePasswordModel;
import com.codeclinic.yakrm.Models.FavouritesListModel;
import com.codeclinic.yakrm.Models.FilterListModel;
import com.codeclinic.yakrm.Models.FriendMobileNumberModel;
import com.codeclinic.yakrm.Models.GetCardListModel;
import com.codeclinic.yakrm.Models.GetMerchantInfoModel;
import com.codeclinic.yakrm.Models.LoginModel;
import com.codeclinic.yakrm.Models.PaymentTransactionModel;
import com.codeclinic.yakrm.Models.PrepareTransactionProcessModel;
import com.codeclinic.yakrm.Models.ProfileImageUpload;
import com.codeclinic.yakrm.Models.ProfileUpdateModel;
import com.codeclinic.yakrm.Models.RegistrationModel;
import com.codeclinic.yakrm.Models.RegistrationStep2Model;
import com.codeclinic.yakrm.Models.RemoveCartItemModel;
import com.codeclinic.yakrm.Models.ReplaceVoucherModel;
import com.codeclinic.yakrm.Models.SendVoucherToFriendModel;
import com.codeclinic.yakrm.Models.TransactionsRecordModel;
import com.codeclinic.yakrm.Models.VerifyOTPModel;
import com.codeclinic.yakrm.Models.VoucherDetailsListModel;

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
    @POST("filter_voucher")
    Call<FilterListModel> FILTER_LIST_MODEL_CALL(@Header("Authorization") String header, @Body String Body);

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

    @Headers("Content-Type: application/json")
    @POST("change_password")
    Call<ChangePasswordModel> CHANGE_PASSWORD_MODEL_CALL(@Header("Authorization") String header, @Body String Body);

    @Headers("Content-Type: application/json")
    @POST("add_payment_cards")
    Call<AddCardDetailsModel> ADD_CARD_DETAILS_MODEL_CALL(@Header("Authorization") String header, @Body String Body);

    @Headers("Content-Type: application/json")
    @GET("get_all_cards_ofusers")
    Call<GetCardListModel> GET_CARD_LIST_MODEL_CALL(@Header("Authorization") String header);

    @FormUrlEncoded
    @POST("/apiv3/get_merchant_info")
    Call<GetMerchantInfoModel> getMerchantInfo(@Field("merchant_email") String var1, @Field("secret_key") String var2, @Field("get_logo") String var3, @Field("amount") double var4, @Field("currency_code") String var6, @Field("is_tokenization") String var7, @Field("is_existing_customer") String var8, @Field("pt_token") String var9, @Field("pt_customer_email") String var10, @Field("pt_customer_password") String var11);

    @FormUrlEncoded
    @POST("/apiv3/prepare_transaction")
    Call<PrepareTransactionProcessModel> prepareTransaction(@Field("secret_key") String var1, @Field("merchant_email") String var2, @Field("amount") double var3, @Field("title") String var5, @Field("cc_first_name") String var6, @Field("cc_last_name") String var7, @Field("card_exp") String var8, @Field("cvv") String var9, @Field("card_number") String var10, @Field("original_assignee_code") String var11, @Field("currency") String var12, @Field("email") String var13, @Field("skip_email") String var14, @Field("phone_number") String var15, @Field("order_id") String var16, @Field("product_name") String var17, @Field("customer_email") String var18, @Field("country_billing") String var19, @Field("address_billing") String var20, @Field("city_billing") String var21, @Field("state_billing") String var22, @Field("postal_code_billing") String var23, @Field("country_shipping") String var24, @Field("address_shipping") String var25, @Field("city_shipping") String var26, @Field("state_shipping") String var27, @Field("postal_code_shipping") String var28, @Field("exchange_rate") String var29, @Field("is_tokenization") String var30, @Field("is_existing_customer") String var31, @Field("pt_token") String var32, @Field("pt_customer_email") String var33, @Field("pt_customer_password") String var34);

    @Headers("Content-Type: application/json")
    @POST("replace_voucher")
    Call<ReplaceVoucherModel> REPLACE_VOUCHER_MODEL_CALL(@Header("Authorization") String header, @Body String Body);

}
