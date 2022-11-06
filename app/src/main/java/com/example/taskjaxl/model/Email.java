package com.example.taskjaxl.model;

import com.google.gson.annotations.SerializedName;

public class Email {

    @SerializedName("email")
    private String email;

    public Email(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
