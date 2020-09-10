package com.nollpointer.dates.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author Onanov Aleksey (@onanov)
 */
class TermsList {
    @SerializedName("terms")
    @Expose
    var terms: List<Term> = emptyList()

}