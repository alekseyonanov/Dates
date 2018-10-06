package com.nollpointer.dates.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContentUrls {

    @SerializedName("mobile")
    @Expose
    private Mobile mobile;

    public Mobile getMobile() {
        return mobile;
    }

    public void setMobile(Mobile mobile) {
        this.mobile = mobile;
    }

}