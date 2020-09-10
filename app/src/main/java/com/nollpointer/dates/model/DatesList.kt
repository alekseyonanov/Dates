package com.nollpointer.dates.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author Onanov Aleksey (@onanov)
 */
class DatesList {
    @SerializedName("dates")
    @Expose
    var dates: List<Date> = emptyList()
}