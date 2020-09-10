package com.nollpointer.dates.ui.sort

/**
 * @author Onanov Aleksey (@onanov)
 */
interface SortCardsControl {
    fun setQuestions(list: List<String?>?)
    fun setAnswerSequence(sequence: IntArray?)
    fun check(): Boolean
    fun setCheckMode(state: Boolean)
    fun setColors(g: Int, r: Int)
}