package com.nollpointer.dates.ui.practise

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.other.AppNavigator
import com.nollpointer.dates.other.BaseViewModel
import com.nollpointer.dates.other.ReportManager
import dagger.hilt.android.qualifiers.ActivityContext

/**
 * ViewModel экрана "Настройки практики"
 *
 * @author Onanov Aleksey (@onanov)
 */
class PractiseSettingsViewModel @ViewModelInject constructor(
        @ActivityContext private val context: Context,
        private val navigator: AppNavigator,
        private val reportManager: ReportManager,
) : BaseViewModel() {

    lateinit var practise: Practise

    override fun onStart() {

    }

    fun onArrowBackClicked() {
        navigator.navigateBack()
    }

    fun onReportClicked() {
        reportManager.reportPractise(practise)
    }
}