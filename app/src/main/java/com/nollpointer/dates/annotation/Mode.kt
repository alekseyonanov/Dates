package com.nollpointer.dates.annotation

import androidx.annotation.IntDef

/**
 * Аннотация для режима дат
 *
 * @author Onanov Aleksey (@onanov)
 */
@IntDef(FULL, EASY)
@Retention(AnnotationRetention.SOURCE)
internal annotation class Mode

const val FULL = 0
const val EASY = 1