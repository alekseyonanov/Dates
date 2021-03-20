package com.nollpointer.dates.other

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Базовый класс ViewModel с CompositeDisposable для Disposable внутри подклассов.
 *
 * @author Onanov Aleksey (@onanov)
 */
abstract class BaseViewModel : ViewModel() {

    private val compositeDisposable by lazy(::CompositeDisposable)

    private var isStarted: Boolean = false

    fun start() {
        if (!isStarted) {
            isStarted = true
            onStart()
        }
    }

    protected fun Disposable.disposeOnCleared(): Disposable {
        compositeDisposable.add(this)
        return this
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onCleared()
    }

    protected abstract fun onStart()

}