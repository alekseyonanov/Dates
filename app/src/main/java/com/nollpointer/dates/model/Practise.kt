package com.nollpointer.dates.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Practise(var practise: Int,
                    var mode: Int,
                    var type: Int = 0,
                    var dates: List<Date> = emptyList(),
                    var isTestMode: Boolean = false) : Parcelable {



    companion object{
        const val CARDS = 0
        const val VOICE = 1
        const val TEST = 2
        const val TRUE_FALSE = 3
        const val LINK = 4
        const val SORT = 5
        const val DISTRIBUTION = 6

        const val TYPE_DATE = 0
        const val TYPE_EVENT = 1
        const val TYPE_MIXED = 2
    }
}