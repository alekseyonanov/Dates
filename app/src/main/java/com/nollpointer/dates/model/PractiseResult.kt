package com.nollpointer.dates.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Модель результата практики
 *
 * @author Onanov Aleksey (@onanov)
 */
@Parcelize
class PractiseResult(
        var questionDate: Date,
        var answerDate: Date,
        var isCorrect: Boolean) : Parcelable