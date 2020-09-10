package com.nollpointer.dates.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class TermsList {
    @SerializedName("terms")
    @Expose
    var terms: List<Term> = emptyList()

}