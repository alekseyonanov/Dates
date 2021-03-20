package com.nollpointer.dates.ui.cards

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.nollpointer.dates.model.Date
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.other.AppNavigator
import com.nollpointer.dates.other.BaseViewModel
import java.util.*

/**
 * ViewModel экрана практики "Карточки"
 *
 * @author Onanov Aleksey (@onanov)
 */
class CardsViewModel @ViewModelInject constructor(
        private val navigator: AppNavigator
) : BaseViewModel() {

    //region LiveData
    val questionLiveData = MutableLiveData<String>()

    lateinit var practise: Practise
    private lateinit var currentDate: Date
    private var isDateQuestion = false

    override fun onStart() {
        setQuestion()
    }

    fun onNextClicked() {
        setQuestion()
    }

    fun onDescriptionClicked() {
        setAnswer()
    }

    fun onArrowBackClicked() {
        navigator.navigateBack()
    }

    fun onSettingsClicked() {
        navigator.navigateToPractiseSettings(practise)
    }

    private fun setAnswer() {
        questionLiveData.value =
                if (currentDate.containsMonth) {
                    "${currentDate.date}, ${currentDate.month}\n${currentDate.event}"
                } else {
                    "${currentDate.date}\n${currentDate.event}"
                }
    }

    private fun setQuestion() {
        setRandomDate()
        questionLiveData.value =
                if (isDateQuestion) {
                    if (currentDate.containsMonth) {
                        "${currentDate.date}, ${currentDate.month}"
                    } else {
                        currentDate.date
                    }
                } else {
                    currentDate.event
                }
    }

    private fun setRandomDate() {
        val random = Random()
        val position = random.nextInt(practise.dates.size)
        currentDate = practise.dates[position]
        if (practise.type == Practise.TYPE_MIXED)
            isDateQuestion = random.nextBoolean()
    }
}