package com.nollpointer.dates.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nollpointer.dates.R
import com.nollpointer.dates.model.Date
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.model.Practise.Companion.TYPE_DATE
import com.nollpointer.dates.model.Practise.Companion.TYPE_MIXED
import com.nollpointer.dates.model.PractiseResult
import com.nollpointer.dates.ui.analyze.AnalyzeFragment
import com.nollpointer.dates.ui.details.dates.DatesDetailsFragment
import com.nollpointer.dates.ui.practise.PractiseSettingsFragment
import com.nollpointer.dates.ui.practiseresult.PractiseResultFragment
import com.nollpointer.dates.ui.view.BaseFragment
import com.nollpointer.dates.ui.view.TestAnswerButton
import kotlinx.android.synthetic.main.fragment_test.*
import java.util.*
import kotlin.collections.ArrayList


/**
 * @author Onanov Aleksey (@onanov)
 */
class TestFragment : BaseFragment() {

    private lateinit var answerButtons: List<TestAnswerButton>
    private lateinit var dates: List<Date>
    private lateinit var currentDate: Date
    private var currentAnswerDatesList = ArrayList<Date>()

    private var type = 0
    private var isTestMode = false
    private var currentType = type

    private var practiseResults = ArrayList<PractiseResult>()

    private var questionNumber = 0
    private var rightAnswersCount = 0
    private var wrongAnswersCount = 0

    private lateinit var practise: Practise

    private val listener = { testAnswerButton: TestAnswerButton ->

        val position = answerButtons.indexOf(testAnswerButton)
        val isAnsweredRight = currentDate == currentAnswerDatesList[position]
        testAnswerButton.setResult(isAnsweredRight)

        val correctAnswerPosition = currentAnswerDatesList.indexOf(currentDate)
        answerButtons[correctAnswerPosition].setResult(true)

        practiseResults.add(PractiseResult(
                questionDate = currentDate,
                answerDate = currentAnswerDatesList[position],
                isCorrect = isAnsweredRight
        ))

        if (isAnsweredRight)
            rightAnswersCount++
        else
            wrongAnswersCount++

        testRightAnswersChip.text = rightAnswersCount.toString()
        testWrongAnswersChip.text = wrongAnswersCount.toString()

        showControlButtons()

        answerButtons.forEach {
            if (!isTestMode) {
                it.setDetailsMode()
            } else {
                it.isEnabled = false
            }
        }

    }

    override fun getStatusBarColorRes() = R.color.colorBackground

    override fun isStatusBarLight() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            practise = it.getParcelable<Practise>(TEST) as Practise
            isTestMode = practise.isTestMode
            type = practise.type
            dates = practise.dates
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Appodeal.setBannerViewId(R.id.appodealBannerView)

        testBack.setOnClickListener {
            requireActivity()
                    .supportFragmentManager
                    .popBackStack()
        }
        testSettings.setOnClickListener {
            requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frameLayout, PractiseSettingsFragment.newInstance(practise))
                    .addToBackStack(null)
                    .commit()
        }
        testNextButton.setOnClickListener {
            if (isTestMode.and(practiseResults.size == 20)) {
                requireActivity()
                        .supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frameLayout, PractiseResultFragment.newInstance())
                        .addToBackStack(null)
                        .commit()
            } else {
                generateAndSetInfo()
            }
        }
        testAnalyzeButton.setOnClickListener {
            requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frameLayout,
                            AnalyzeFragment.newInstance(practise, practiseResults))
                    .addToBackStack(null)
                    .commit()
        }

        answerButtons = mutableListOf<TestAnswerButton>().apply {
            add(testButton0)
            add(testButton1)
            add(testButton2)
            add(testButton3)
            forEach {
                it.setOnAnswerButtonClickListener = listener
                it.setOnDetailsClickListener = { date ->
                    requireActivity().supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.frameLayout, DatesDetailsFragment.newInstance(date))
                            .addToBackStack(null)
                            .commit()
                }
            }
        }

        if (currentAnswerDatesList.isEmpty()) {
            generateAndSetInfo()
        } else {
            setPreviousInfo()
        }
    }

    private fun generateDatesQuestionList(): List<Date> {
        val random = Random()
        val list = dates.shuffled(random)
        return list.subList(0, 4)
    }

    private fun generateAndSetInfo() {
        questionNumber++
        currentAnswerDatesList.clear()
        currentAnswerDatesList.addAll(generateDatesQuestionList())
        currentDate = currentAnswerDatesList[Random().nextInt(4)]
        currentType = if (type == TYPE_MIXED) Random().nextInt(2) else type
        resetButtons()
        setQuestions()
        hideControlButtons()

        testQuestionNumberChip.text = questionNumber.toString()
    }

    private fun setPreviousInfo() {

        testRightAnswersChip.text = rightAnswersCount.toString()
        testWrongAnswersChip.text = wrongAnswersCount.toString()
        testQuestionNumberChip.text = questionNumber.toString()

        showControlButtons()

        resetButtons()

        setQuestions()


        val result = practiseResults.last()
        val selectedPosition = currentAnswerDatesList.indexOf(result.answerDate)
        answerButtons[selectedPosition].setResult(result.isCorrect)

        val correctAnswerPosition = currentAnswerDatesList.indexOf(currentDate)
        answerButtons[correctAnswerPosition].setResult(true)

        answerButtons.forEach {
            it.setDetailsMode()
        }
    }

    private fun resetButtons() {
        currentAnswerDatesList.withIndex().forEach {
            answerButtons[it.index].setDate(currentType, it.value)
            answerButtons[it.index].isClickable = true
        }
    }

    private fun setQuestions() {

        if (currentType == TYPE_DATE) {
            testQuestion.text =
                    if (currentDate.containsMonth)
                        "${currentDate.date}\n${currentDate.month}"
                    else
                        currentDate.date
        } else {
            testQuestion.text = currentDate.event
        }
    }

    private fun showControlButtons(){
        testNextButton.show()
        testAnalyzeButton.show()
    }

    private fun hideControlButtons(){
        testNextButton.hide()
        testAnalyzeButton.hide()
    }

    /*    override fun onStop() {
        super.onStop()
        Appodeal.hide(activity!!, Appodeal.BANNER_VIEW)
    }

    override fun onStart() {
        super.onStart()
        Appodeal.show(activity!!, Appodeal.BANNER_VIEW)
    }*/

    companion object {

        private const val TEST = "Test"

        @JvmStatic
        fun newInstance(practise: Practise) =
                TestFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(TEST, practise)
                    }
                }
    }
}