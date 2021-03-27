package com.nollpointer.dates.model

import androidx.annotation.DrawableRes

/**
 * Модель стартового экрана
 *
 * @author Onanov Aleksey (@onanov)
 */
data class IntroPage(
        val title: String,
        var description: String,
        @DrawableRes val imageRes: Int,
)
