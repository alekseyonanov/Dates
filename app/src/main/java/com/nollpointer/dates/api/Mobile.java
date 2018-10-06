package com.nollpointer.dates.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Mobile {

    @SerializedName("page")
    @Expose
    private String page;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

}
