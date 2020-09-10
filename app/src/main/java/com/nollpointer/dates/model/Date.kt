package com.nollpointer.dates.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * @author Onanov Aleksey (@onanov)
 */
@Parcelize
data class Date constructor(var date: String = "",
                            var event: String = "",
                            var request: String = "",
                            var type: Int = 1,
                            var month: String = "#") : Parcelable, Comparable<Date> {

    override fun compareTo(other: Date): Int {
        val currentDate = date.toInt()
        val compareDate = other.date.toInt()
        return currentDate.compareTo(compareDate)
    }

    val isContinuous: Boolean
        get() = date.contains("-") || date.contains("–") || date.contains("–") || date.contains(",") || date.contains("в")

    val containsMonth: Boolean
        get() = month != "#"

    operator fun contains(q: String): Boolean {
        val query = q.toLowerCase(Locale.ROOT)
        return date.toLowerCase(Locale.ROOT).contains(query) || event.toLowerCase(Locale.ROOT).contains(query) || containsMonth && month.toLowerCase(Locale.ROOT).contains(query)
    }

}