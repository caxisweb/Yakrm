package com.yakrm.codeclinic.Retrofit;

import com.yakrm.codeclinic.Models.LoginModel;
import com.yakrm.codeclinic.Models.RegistrationModel;
import com.yakrm.codeclinic.Models.RegistrationStep2Model;
import com.yakrm.codeclinic.Models.VerifyOTPModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface API {

    @Headers("Content-Type: application/json")
    @POST("registration_step1")
    Call<RegistrationModel> REGISTRATION_MODEL_CALL(@Body String Body);

    @Headers("Content-Type: application/json")
    @POST("verify_otp")
    Call<VerifyOTPModel> VERIFY_OTP_MODEL_CALL(@Body String Body);

    @Headers("Content-Type: application/json")
    @POST("registration_step2")
    Call<RegistrationStep2Model> REGISTRATION_STEP_2_MODEL_CALL(@Body String Body);

    @Headers("Content-Type: application/json")
    @POST("login")
    Call<LoginModel> LOGIN_MODEL_CALL(@Body String Body);
}
