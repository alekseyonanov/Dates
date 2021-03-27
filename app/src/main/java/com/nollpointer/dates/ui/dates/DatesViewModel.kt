package com.nollpointer.dates.ui.dates

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nollpointer.dates.annotation.FULL
import com.nollpointer.dates.model.Date
import com.nollpointer.dates.other.AppNavigator
import com.nollpointer.dates.other.BaseViewModel
import com.nollpointer.dates.ui.activity.MainActivity
import dagger.hilt.android.qualifiers.ActivityContext

/**
 * ViewModel экрана "Даты"
 *
 * @author Onanov Aleksey (@onanov)
 */
class DatesViewModel @ViewModelInject constructor(
        @ActivityContext private val context: Context,
        private val navigator: AppNavigator,
) : BaseViewModel() {

    private val activity: MainActivity
        get() = context as MainActivity


    private val _showSearchLiveData = MutableLiveData<Boolean>(false)
    val showSearchLiveData: LiveData<Boolean> = _showSearchLiveData

    lateinit var dates: List<Date>
    var mode = FULL

    override fun onStart() {
        mode = activity.mode
        dates = activity.dates
    }

    fun onDateClicked(date: Date) {
        navigator.navigateToDatesDetails(date)
    }

    fun onSearchClicked() {
        _showSearchLiveData.value = true
    }

    fun onSearchArrowBackClicked() {
        _showSearchLiveData.value = false
    }

}