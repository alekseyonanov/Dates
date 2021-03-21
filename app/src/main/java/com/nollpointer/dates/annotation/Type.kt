package com.nollpointer.dates.annotation

import androidx.annotation.IntDef

/**
 * Аннотация для типа практики
 *
 * @author Onanov Aleksey (@onanov)
 */
@IntDef(DATES, EVENTS, MIXED)
@Retention(AnnotationRetention.SOURCE)
internal annotation class Type

const val DATES = 0
const val EVENTS = 1
const val MIXED = 2