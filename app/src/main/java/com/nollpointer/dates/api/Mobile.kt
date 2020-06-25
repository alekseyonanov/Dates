package com.nollpointer.dates.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Mobile {
    @SerializedName("page")
    @Expose
    var page: String? = null

}