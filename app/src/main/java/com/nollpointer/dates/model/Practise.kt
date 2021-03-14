package com.nollpointer.dates.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author Onanov Aleksey (@onanov)
 */
@Parcelize
data class Practise(
        var practise: Int = 0,
        var mode: Int = 0,
        var type: Int = 0,
        var dates: List<Date> = emptyList(),
        var isTestMode: Boolean = false,
        var results: List<PractiseResult> = emptyList(),
) : Parcelable {

    companion object {
        const val CARDS = 0
        const val VOICE = 1
        const val TEST = 2
        const val TRUE_FALSE = 3
        const val LINK = 4
        const val SORT = 5
        const val DISTRIBUTION = 6

        // TODO: 13.03.2021 Сделать специальную аннотацию 
        const val TYPE_DATE = 0
        const val TYPE_EVENT = 1
        const val TYPE_MIXED = 2
    }
}