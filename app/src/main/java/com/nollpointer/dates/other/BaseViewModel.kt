package com.nollpointer.dates.other

import androidx.lifecycle.ViewModel

/**
 * @author Onanov Aleksey (@onanov)
 */
abstract class BaseViewModel : ViewModel() {
    /**
     * подключение логирования
     */

    private var isStarted: Boolean = false
    /**
     * старт
     */
    fun start() {
        if (!isStarted) {
            isStarted = true
            onStart()
        }
    }

    protected abstract fun onStart()

}