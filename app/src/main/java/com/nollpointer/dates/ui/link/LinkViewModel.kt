package com.nollpointer.dates.ui.link

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.nollpointer.dates.model.Date
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.model.PractiseInfo
import com.nollpointer.dates.other.AppNavigator
import com.nollpointer.dates.other.BaseViewModel

/**
 * @author Onanov Aleksey (@onanov)
 */
class LinkViewModel @ViewModelInject constructor(
        private val navigator: AppNavigator) : BaseViewModel() {

    lateinit var practise: Practise

    val infoLiveData = MutableLiveData<PractiseInfo>()
    val controlsVisibilityLiveData = MutableLiveData<Boolean>()
    val checkEnabilityLiveData = MutableLiveData<Boolean>()
    val questionsLiveData = MutableLiveData<List<Date>>()
    val possibleAnswersLiveData = MutableLiveData<List<Date>>()

    private var rightAnswers = 0
    private var wrongAnswers = 0
    private var questionNumber = 1

    override fun onStart() {
        generateQuestions()
    }

    private fun generateQuestions() {
        possibleAnswersLiveData.value = practise.dates.shuffled().take(5)
        questionsLiveData.value = possibleAnswersLiveData.value!!.take(4).shuffled()
        infoLiveData.value = PractiseInfo(questionNumber, rightAnswers, wrongAnswers)
    }

    fun onArrowBackClicked() {
        navigator.navigateBack()
    }

    fun onSettingsClicked() {
        navigator.navigateToPractiseSettings(practise)
    }

    fun onNextClicked() {
        questionNumber++
        generateQuestions()
    }

    fun onAnalyzeClicked() {
        navigator.navigateToAnalyze()
    }

    fun onCheckClicked() {
        controlsVisibilityLiveData.value = true
    }


}