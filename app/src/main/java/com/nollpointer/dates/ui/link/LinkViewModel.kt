package com.nollpointer.dates.ui.link

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.model.PractiseInfo
import com.nollpointer.dates.other.AppNavigator
import com.nollpointer.dates.other.BaseViewModel
import dagger.hilt.android.qualifiers.ActivityContext

/**
 * @author Onanov Aleksey (@onanov)
 */
class LinkViewModel @ViewModelInject constructor(
        @ActivityContext private val context: Context,
        private val navigator: AppNavigator) : BaseViewModel() {

    lateinit var practise: Practise

    val infoLiveData = MutableLiveData<PractiseInfo>()
    val controlsVisibilityLiveData = MutableLiveData<Boolean>()
    val checkEnabilityLiveData = MutableLiveData<Boolean>()

    override fun onStart() {

    }

    fun onArrowBackClicked() {
        navigator.navigateBack()
    }

    fun onSettingsClicked() {
        navigator.navigateToPractiseSettings(practise)
    }

    fun onNextClicked() {

    }

    fun onAnalyzeClicked() {
        navigator.navigateToAnalyze()
    }

    fun onCheckClicked() {

    }


}