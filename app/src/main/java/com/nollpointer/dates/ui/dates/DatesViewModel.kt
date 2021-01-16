package com.nollpointer.dates.ui.dates

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import com.nollpointer.dates.model.Date
import com.nollpointer.dates.other.AppNavigator
import com.nollpointer.dates.other.BaseViewModel
import dagger.hilt.android.qualifiers.ActivityContext

class DatesViewModel @ViewModelInject constructor(
        @ActivityContext private val context: Context,
        private val navigator: AppNavigator) : BaseViewModel() {

    override fun onStart() {

    }

    fun onDateClicked(date: Date) {

    }

}