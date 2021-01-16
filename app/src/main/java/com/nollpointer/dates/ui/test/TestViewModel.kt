package com.nollpointer.dates.ui.test

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import com.nollpointer.dates.other.AppNavigator
import com.nollpointer.dates.other.BaseViewModel
import dagger.hilt.android.qualifiers.ActivityContext

/**
 * @author Onanov Aleksey (@onanov)
 */
class TestViewModel @ViewModelInject constructor(
        @ActivityContext private val context: Context,
        private val navigator: AppNavigator) : BaseViewModel() {

    override fun onStart() {
    }

    fun onArrowBackClicked() {
        navigator.navigateBack()
    }

    fun onAnalyzeClicked() {
        navigator.navigateToAnalyze()
    }
}