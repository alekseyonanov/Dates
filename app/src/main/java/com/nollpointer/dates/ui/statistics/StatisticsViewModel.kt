package com.nollpointer.dates.ui.statistics

import androidx.hilt.lifecycle.ViewModelInject
import com.nollpointer.dates.other.AppNavigator
import com.nollpointer.dates.other.BaseViewModel

/**
 * View model экрана "Статистики"
 *
 * @author Onanov Aleksey (@onanov)
 */
class StatisticsViewModel @ViewModelInject constructor(
        private val navigator: AppNavigator,
) : BaseViewModel() {

    override fun onStart() {

    }

}