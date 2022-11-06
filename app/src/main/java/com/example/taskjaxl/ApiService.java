package com.example.taskjaxl;

import com.example.taskjaxl.model.Email;
import com.example.taskjaxl.model.OTP;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("login")
    Call<Email> login(@Body Email email);

    @POST("verify")
    Call<OTP> verifyOtp(@Body OTP otp);

    @POST("requestAnotherOtp")
    Call<Email> requestAnotherOtp(@Body Email email);


}
