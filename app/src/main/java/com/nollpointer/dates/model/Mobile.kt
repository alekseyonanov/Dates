package com.nollpointer.dates.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author Onanov Aleksey (@onanov)
 */
class Mobile {
    @SerializedName("page")
    @Expose
    var page: String? = null

}