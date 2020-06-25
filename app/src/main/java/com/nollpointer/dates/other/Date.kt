package com.nollpointer.dates.other

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize
import java.util.*


@Parcelize
data class Date(var date: String,
                var event: String,
                var request: String,
                var type: Int,
                var month: String? = null) : Parcelable, Comparable<Date> {

    val isContinuous: Boolean
        get() = date.contains("-") || date.contains("–") || date.contains("–") || date.contains(",") || date.contains("в")

    fun containsMonth(): Boolean {
        return month != null
    }

    fun isSameDate(date: Date): Boolean {
        return this.date == date.date
    }


    operator fun contains(q: String): Boolean {
        val query = q.toLowerCase(Locale.ROOT)
        return date.toLowerCase(Locale.ROOT).contains(query) || event.toLowerCase(Locale.ROOT).contains(query) || containsMonth() && month!!.toLowerCase(Locale.ROOT).contains(query)
    }

    override fun compareTo(other: Date): Int {
        val currentDate = date.toInt()
        val compareDate = other.date.toInt()
        return currentDate.compareTo(compareDate)
    }
}