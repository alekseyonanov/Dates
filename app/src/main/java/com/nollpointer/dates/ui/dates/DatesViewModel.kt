package com.nollpointer.dates.ui.dates

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nollpointer.dates.model.Date
import com.nollpointer.dates.other.AppNavigator
import com.nollpointer.dates.other.BaseViewModel
import com.nollpointer.dates.ui.activity.MainActivity

/**
 * ViewModel экрана "Даты"
 *
 * @author Onanov Aleksey (@onanov)
 */
class DatesViewModel @ViewModelInject constructor(
        private val navigator: AppNavigator,
) : BaseViewModel() {

    private val _showSearchLiveData = MutableLiveData<Boolean>(false)
    val showSearchLiveData: LiveData<Boolean> = _showSearchLiveData

    lateinit var dates: List<Date>
    var mode = MainActivity.FULL_DATES_MODE

    override fun onStart() {

    }

    fun onDateClicked(date: Date) {
        navigator.navigateToDatesDetails(date)
    }

}