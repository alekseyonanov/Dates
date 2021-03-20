package com.nollpointer.dates.ui.link

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nollpointer.dates.model.Date
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.model.PractiseInfo
import com.nollpointer.dates.other.AppNavigator
import com.nollpointer.dates.other.BaseViewModel

/**
 * ViewModel экрана практики "Связка"
 *
 * @author Onanov Aleksey (@onanov)
 */
class LinkViewModel @ViewModelInject constructor(
        private val navigator: AppNavigator,
) : BaseViewModel() {

    private val _controlButtonsVisibilityLiveData = MutableLiveData(false)
    val controlButtonsVisibilityLiveData: LiveData<Boolean> = _controlButtonsVisibilityLiveData

    private val _infoLiveData = MutableLiveData<PractiseInfo>()
    val infoLiveData: LiveData<PractiseInfo> = _infoLiveData

    private val _questionLiveData = MutableLiveData<List<Date>>()
    val questionLiveData: LiveData<List<Date>> = _questionLiveData

    private val _possibleAnswersLiveData = MutableLiveData<List<Date>>()
    val possibleAnswersLiveData: LiveData<List<Date>> = _possibleAnswersLiveData

    // TODO: 13.03.2021 Отрефачить и здесь, чтобы при заходе на экран сохранялось предыдущее состояние
    private val _resultLiveData = MutableLiveData<List<Int>>()
    val resultLiveData: LiveData<List<Int>> = _resultLiveData

    private val _checkEnabilityLiveData = MutableLiveData<Boolean>()
    val checkEnabilityLiveData: LiveData<Boolean> = _checkEnabilityLiveData

    lateinit var practise: Practise

    private lateinit var questions: List<Date>
    private lateinit var possibleAnswers: List<Date>

    private lateinit var answers: List<Date>

    private var rightAnswers = 0
    private var wrongAnswers = 0
    private var questionNumber = 1

    override fun onStart() {
        generateQuestions()
    }

    private fun generateQuestions() {
        possibleAnswers = practise.dates.shuffled().take(ANSWERS_COUNT).shuffled()
        questions = possibleAnswers.take(QUESTIONS_COUNT).shuffled()

        _possibleAnswersLiveData.value = possibleAnswers
        _questionLiveData.value = questions

        _infoLiveData.value = PractiseInfo(questionNumber, rightAnswers, wrongAnswers)
        _controlButtonsVisibilityLiveData.value = false
    }

    private fun check() {
        if (questions == answers) {
            rightAnswers++
        } else {
            wrongAnswers++
        }
        val list = mutableListOf<Int>()
        answers.forEach {
            list.add(questions.indexOf(it))
        }
        _resultLiveData.value = list
    }

    fun onAnswersListChanged(answers: List<Date>) {
        this.answers = answers
        _checkEnabilityLiveData.value = answers.size == ANSWERS_COUNT
    }

    fun onDateClicked(date: Date) {
        navigator.navigateToDatesDetails(date)
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
        check()
        _controlButtonsVisibilityLiveData.value = true
    }

    companion object {
        private const val QUESTIONS_COUNT = 4
        private const val ANSWERS_COUNT = 5
    }

}