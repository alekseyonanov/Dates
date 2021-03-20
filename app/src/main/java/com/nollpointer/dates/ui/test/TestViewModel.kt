package com.nollpointer.dates.ui.test

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nollpointer.dates.model.Date
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.other.AppNavigator
import com.nollpointer.dates.other.BaseViewModel
import com.nollpointer.dates.ui.test.model.QuestionModel
import com.nollpointer.dates.ui.test.model.ResultModel
import kotlin.random.Random

/**
 * ViewModel экрана практики "Тестирование"
 *
 * @author Onanov Aleksey (@onanov)
 */
class TestViewModel @ViewModelInject constructor(
        private val navigator: AppNavigator) : BaseViewModel() {

    private val _controlButtonsVisibilityLiveData = MutableLiveData(false)
    val controlButtonsVisibilityLiveData: LiveData<Boolean> = _controlButtonsVisibilityLiveData

    private val _rightWrongAnswersLiveData = MutableLiveData<Pair<Int, Int>>()
    val rightWrongAnswersLiveData: LiveData<Pair<Int, Int>> = _rightWrongAnswersLiveData

    private val _questionLiveData = MutableLiveData<QuestionModel>()
    val questionLiveData: LiveData<QuestionModel> = _questionLiveData

    // TODO: 13.03.2021 Отрефачить и здесь, чтобы при заходе на экран сохранялось предыдущее состояние
    private val _resultsLiveData = MutableLiveData<ResultModel>()
    val resultsLiveData: LiveData<ResultModel> = _resultsLiveData

    lateinit var practise: Practise

    private lateinit var currentQuestion: QuestionModel
    private var rightAnswers = 0
    private var wrongAnswers = 0

    override fun onStart() {
        generateQuestion()
    }

    private fun generateQuestion() {
        val startPosition = Random.Default.nextInt(practise.dates.size - 15)
        val answers = practise.dates.subList(startPosition, startPosition + 15).shuffled().subList(0, 4)
        val question = answers.random()

        val questionTitle = with(question) {
            if (practise.type == Practise.TYPE_DATE) {
                if (containsMonth) "${date}\n${month}" else date
            } else {
                event
            }
        }
        currentQuestion = QuestionModel(questionTitle, question, answers, practise.type)
        _questionLiveData.value = currentQuestion
        _rightWrongAnswersLiveData.value = rightAnswers to wrongAnswers
        _controlButtonsVisibilityLiveData.value = false
    }

    fun onAnswerClicked(date: Date) {
        val clickedPosition = currentQuestion.answers.indexOf(date)
        val rightAnswerPosition = currentQuestion.answers.indexOf(currentQuestion.questionDate)
        if (clickedPosition == rightAnswerPosition) rightAnswers++ else wrongAnswers++
        _resultsLiveData.value = ResultModel(rightAnswerPosition, clickedPosition)
        _controlButtonsVisibilityLiveData.value = true
    }

    fun onDetailsClicked(date: Date) {
        navigator.navigateToDatesDetails(date)
    }

    // TODO: 13.03.2021 Отрефачить этот момент. При тестировании не должен быть кликабельным
    fun onTitleClicked() {
        navigator.navigateToDatesDetails(currentQuestion.questionDate)
    }

    fun onNextClicked() {
        generateQuestion()
    }

    fun onAnalyzeClicked() {
        navigator.navigateToAnalyze()
    }

    fun onArrowBackClicked() {
        navigator.navigateBack()
    }

    fun onSettingsClicked() {
        navigator.navigateToPractiseSettings(practise)
    }
}