package com.nollpointer.dates.model

/**
 * Модель связки
 *
 * @author Onanov Aleksey (@onanov)
 */
data class LinkModel(
        val title: String,
        val color: Int
) {
    companion object {
        const val BLUE = 0
        const val RED = 1
        const val GREEN = 2
        const val YELLOW = 3
        const val GREY = 4
    }
}
