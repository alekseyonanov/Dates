package com.nollpointer.dates.annotation

import androidx.annotation.IntDef

/**
 * Аннотация для адаптера экрана "Анализ"
 *
 * @author Onanov Aleksey (@onanov)
 */
@IntDef(TEST, TRUE_FALSE)
@Retention(AnnotationRetention.SOURCE)
annotation class AnswerViewType