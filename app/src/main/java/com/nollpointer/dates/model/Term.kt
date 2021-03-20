package com.nollpointer.dates.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

/**
 * Модель Термина
 *
 * @author Onanov Aleksey (@onanov)
 */
@Parcelize
data class Term(var term: String = "Абсолютизм",
                var description: String = "Форма правления, при которой монарху принадлежит неограниченная верховная власть",
                var request: String = "Прерывание") : Parcelable {

    operator fun contains(q: String): Boolean {
        val query = q.toLowerCase(Locale.ROOT)
        return term.toLowerCase(Locale.ROOT).contains(query) || description.toLowerCase(Locale.ROOT).contains(query)
    }
}