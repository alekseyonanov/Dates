package com.nollpointer.dates.annotation

import androidx.annotation.IntDef

/**
 * Аннотация для вида практики
 *
 * @author Onanov Aleksey (@onanov)
 */
@IntDef(CARDS,
        VOICE,
        TEST,
        TRUE_FALSE,
        LINK,
        SORT,
        DISTRIBUTION
)
@Retention(AnnotationRetention.SOURCE)
internal annotation class Practise

const val CARDS = 0
const val VOICE = 1
const val TEST = 2
const val TRUE_FALSE = 3
const val LINK = 4
const val SORT = 5
const val DISTRIBUTION = 6