package com.nollpointer.dates.ui.sort

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nollpointer.dates.model.Date
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.other.AppNavigator
import com.nollpointer.dates.other.BaseViewModel
import com.nollpointer.dates.ui.test.model.ResultModel
import kotlin.random.Random

/**
 * @author Onanov Aleksey (@onanov)
 */
class SortViewModel @ViewModelInject constructor(
        private val navigator: AppNavigator) : BaseViewModel() {

    private val _controlButtonsVisibilityLiveData = MutableLiveData(false)
    val controlButtonsVisibilityLiveData: LiveData<Boolean> = _controlButtonsVisibilityLiveData

    private val _rightWrongAnswersLiveData = MutableLiveData<Pair<Int, Int>>()
    val rightWrongAnswersLiveData: LiveData<Pair<Int, Int>> = _rightWrongAnswersLiveData

    private val _questionLiveData = MutableLiveData<List<Date>>()
    val questionLiveData: LiveData<List<Date>> = _questionLiveData

    // TODO: 13.03.2021 Отрефачить и здесь, чтобы при заходе на экран сохранялось предыдущее состояние
    private val _resultsLiveData = MutableLiveData<ResultModel>()
    val resultsLiveData: LiveData<ResultModel> = _resultsLiveData

    lateinit var practise: Practise

    private lateinit var currentAnswers: List<Date>
    private var rightAnswers = 0
    private var wrongAnswers = 0

    override fun onStart() {
        generateQuestion()
    }

    fun onArrowBackClicked() {
        navigator.navigateBack()
    }

    fun onAnalyzeClicked() {
        navigator.navigateToAnalyze()
    }

    fun onNextClicked() {
        generateQuestion()
    }

    fun onSettingsClicked() {
        navigator.navigateToPractiseSettings(practise)
    }

    fun onCheckClicked() {
        _controlButtonsVisibilityLiveData.value = true
    }

    fun onAnswerClicked(date: Date) {
        navigator.navigateToDatesDetails(date)
    }

    private fun generateQuestion() {
        val startPosition = Random.Default.nextInt(practise.dates.size - 15)
        currentAnswers = practise.dates.subList(startPosition, startPosition + 15).shuffled().subList(0, 3)

        _questionLiveData.value = currentAnswers
        _rightWrongAnswersLiveData.value = rightAnswers to wrongAnswers
        _controlButtonsVisibilityLiveData.value = false
    }

    fun onDetailsClicked(date: Date) {
        navigator.navigateToDatesDetails(date)
    }
}