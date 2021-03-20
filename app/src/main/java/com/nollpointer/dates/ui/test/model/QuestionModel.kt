package com.nollpointer.dates.ui.test.model

import com.nollpointer.dates.model.Date

/**
 * Модель вопроса практики
 *
 * @author Onanov Aleksey (@onanov)
 */
data class QuestionModel(
        val question: String,
        // TODO: 13.03.2021 Придумать более оптимальное решение
        val questionDate: Date,
        val answers: List<Date>,
        val type: Int,
)