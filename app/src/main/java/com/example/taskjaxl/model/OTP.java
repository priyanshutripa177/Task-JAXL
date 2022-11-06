package com.example.taskjaxl.model;

import com.google.gson.annotations.SerializedName;

public class OTP {

    @SerializedName("entered_Otp")
    private String otp;

    public OTP(String otp) {
        this.otp = otp;
    }

    public String getOtp() {
        return otp;
    }
}
