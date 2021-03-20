package com.nollpointer.dates.ui.distribution

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.model.PractiseInfo
import com.nollpointer.dates.other.AppNavigator
import com.nollpointer.dates.other.BaseViewModel

/**
 * ViewModel экрана практики "Распределение"
 *
 * @author Onanov Aleksey (@onanov)
 */
class DistributionViewModel @ViewModelInject constructor(
        private val navigator: AppNavigator,
) : BaseViewModel() {

    private val _questionsLiveData = MutableLiveData<List<String>>()
    val questionsLiveData: LiveData<List<String>> = _questionsLiveData

    private val _infoLiveData = MutableLiveData<PractiseInfo>()
    val infoLiveData: LiveData<PractiseInfo> = _infoLiveData

    private val _rightWrongAnswersLiveData = MutableLiveData<Pair<Int, Int>>()
    val rightWrongAnswersLiveData: LiveData<Pair<Int, Int>> = _rightWrongAnswersLiveData

    private val _controlsVisibilityLiveData = MutableLiveData<Boolean>()
    val controlsVisibilityLiveData: LiveData<Boolean> = _controlsVisibilityLiveData

    private val _checkEnabilityLiveData = MutableLiveData<Boolean>()
    val checkEnabilityLiveData: LiveData<Boolean> = _checkEnabilityLiveData

    lateinit var practise: Practise

    override fun onStart() {
        generateQuestion()
    }

    private fun generateQuestion() {

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

    fun onItemSwiped(item: String, direction: Int) {

    }
}