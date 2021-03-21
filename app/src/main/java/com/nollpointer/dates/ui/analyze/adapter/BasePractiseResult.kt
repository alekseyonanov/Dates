package com.nollpointer.dates.ui.analyze.adapter

import com.nollpointer.dates.model.Date

/**
 * Sealed классы с вариантами результатов практики
 *
 * @author Onanov Aleksey (@onanov)
 */
sealed class BasePractiseResult(open val type: Int)

data class TestPractiseResult(
        val question: Date,
        val answers: List<Date>,
        val selectedDate: Date,
        val correctDate: Date,
        override val type: Int,
) : BasePractiseResult(type)

data class TrueFalsePractiseResult(
        val date: Date,
        val event: Date,
        val isCorrectAnswer: Boolean,
        override val type: Int,
) : BasePractiseResult(type)
