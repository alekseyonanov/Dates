package com.nollpointer.dates.other

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Класс для удобной работы с клавиатурой
 *
 * @author Onanov Aleksey (@onanov)
 */
object Keyboard {
    private val inputMethodManager = { view: View -> view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }

    /**
     * Скрыть клавиатуру
     **/
    fun hide(view: View) = inputMethodManager(view).hideSoftInputFromWindow(view.windowToken, 0)

    /**
     * Отобразить клавиатуру
     **/
    fun show(view: View) = inputMethodManager(view).showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}