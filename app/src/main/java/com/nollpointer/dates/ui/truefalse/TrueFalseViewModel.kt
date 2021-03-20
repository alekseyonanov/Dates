package com.nollpointer.dates.ui.truefalse

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nollpointer.dates.model.Date
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.other.AppNavigator
import com.nollpointer.dates.other.BaseViewModel
import com.nollpointer.dates.ui.truefalse.model.ResultModel
import kotlin.random.Random

/**
 * ViewModel экрана практики "Правда/Ложь"
 *
 * @author Onanov Aleksey (@onanov)
 */
class TrueFalseViewModel @ViewModelInject constructor(
        private val navigator: AppNavigator) : BaseViewModel() {

    private val _controlButtonsVisibilityLiveData = MutableLiveData(false)
    val controlButtonsVisibilityLiveData: LiveData<Boolean> = _controlButtonsVisibilityLiveData

    private val _rightWrongAnswersLiveData = MutableLiveData<Pair<Int, Int>>()
    val rightWrongAnswersLiveData: LiveData<Pair<Int, Int>> = _rightWrongAnswersLiveData

    private val _questionLiveData = MutableLiveData<Pair<String, String>>()
    val questionLiveData: LiveData<Pair<String, String>> = _questionLiveData

    // TODO: 13.03.2021 Отрефачить и здесь, чтобы при заходе на экран сохранялось предыдущее состояние
    private val _resultLiveData = MutableLiveData<ResultModel>()
    val resultLiveData: LiveData<ResultModel> = _resultLiveData

    lateinit var practise: Practise

    private var rightAnswers = 0
    private var wrongAnswers = 0

    private lateinit var currentQuestion: Pair<Date, Date>

    override fun onStart() {
        generateQuestion()
    }

    private fun generateQuestion() {
        val isTrue = Random.nextBoolean()
        val date = practise.dates.random()
        val event = if (isTrue) date else practise.dates.filter { it != date }.random()
        currentQuestion = date to event

        _questionLiveData.value = createQuestion()
        _rightWrongAnswersLiveData.value = rightAnswers to wrongAnswers
        _controlButtonsVisibilityLiveData.value = false
    }

    private fun createQuestion(): Pair<String, String> {
        val date = with(currentQuestion.first) {
            if (containsMonth) "$date, $month" else date
        }
        val event = currentQuestion.second.event
        return date to event
    }

    private fun checkAnswer(isCorrect: Boolean) {
        val correctResult = currentQuestion.first == currentQuestion.second
        val userResult = correctResult && isCorrect
        if (userResult) rightAnswers++ else wrongAnswers++
        _resultLiveData.value = ResultModel(userResult, correctResult)
        _controlButtonsVisibilityLiveData.value = true
    }

    fun onArrowBackClicked() {
        navigator.navigateBack()
    }

    fun onAnalyzeClicked() {
        navigator.navigateToAnalyze()
    }

    fun onSettingsClicked() {
        navigator.navigateToPractiseSettings(practise)
    }

    fun onDateClicked() {
        navigator.navigateToDatesDetails(currentQuestion.first)
    }

    fun onEventClicked() {
        navigator.navigateToDatesDetails(currentQuestion.second)
    }

    fun onNextClicked() {
        generateQuestion()
    }

    fun onTrueClicked() {
        checkAnswer(true)
    }

    fun onFalseClicked() {
        checkAnswer(false)
    }
}