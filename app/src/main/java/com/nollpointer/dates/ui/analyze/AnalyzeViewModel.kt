package com.nollpointer.dates.ui.analyze

import androidx.hilt.lifecycle.ViewModelInject
import com.nollpointer.dates.other.AppNavigator
import com.nollpointer.dates.other.BaseViewModel

/**
 * ViewModel экрана "Анализ"
 *
 * @author Onanov Aleksey (@onanov)
 */
class AnalyzeViewModel @ViewModelInject constructor(
        private val navigator: AppNavigator,
) : BaseViewModel() {


    override fun onStart() {

    }

    fun onArrowBackClicked() {
        navigator.navigateBack()
    }


}