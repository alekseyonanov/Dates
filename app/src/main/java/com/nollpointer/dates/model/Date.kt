package com.nollpointer.dates.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

/**
 * Модель Даты
 *
 * @author Onanov Aleksey (@onanov)
 */
@Parcelize
data class Date(
        var date: String = "",
        var event: String = "",
        var request: String = "",
        var type: Int = 1,
        var month: String = "#",
) : Parcelable, Comparable<Date> {

    override fun compareTo(other: Date): Int {
        val currentDate = date.toInt()
        val compareDate = other.date.toInt()
        return currentDate.compareTo(compareDate)
    }

    val dateFull: String
        get() = if (containsMonth) "$date, $month" else date

    val isContinuous: Boolean
        get() = date.contains("-") || date.contains("–") || date.contains("–") || date.contains(",") || date.contains("в")

    val containsMonth: Boolean
        get() = month != "#"

    operator fun contains(q: String): Boolean {
        val query = q.toLowerCase(Locale.ROOT)
        return date.toLowerCase(Locale.ROOT).contains(query) || event.toLowerCase(Locale.ROOT).contains(query) || containsMonth && month.toLowerCase(Locale.ROOT).contains(query)
    }

}