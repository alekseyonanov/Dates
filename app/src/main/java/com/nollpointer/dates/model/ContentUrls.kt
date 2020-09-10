package com.nollpointer.dates.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ContentUrls {
    @SerializedName("mobile")
    @Expose
    var mobile: Mobile? = null

}