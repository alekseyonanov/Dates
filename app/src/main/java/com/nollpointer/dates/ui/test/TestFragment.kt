package com.nollpointer.dates.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.nollpointer.dates.R
import com.nollpointer.dates.databinding.FragmentTestBinding
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
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList


/**
 * @author Onanov Aleksey (@onanov)
 */
@AndroidEntryPoint
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

    private var _binding: FragmentTestBinding? = null
    private val binding: FragmentTestBinding
        get() = _binding!!

    private val viewModel by viewModels<TestViewModel>()

    override val statusBarColorRes = R.color.colorBackground

    override val isStatusBarLight = true

    override val isBottomNavigationViewHidden = true

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

        binding.rightAnswers.text = rightAnswersCount.toString()
        binding.wrongAnswers.text = wrongAnswersCount.toString()

        showControlButtons()

        answerButtons.forEach {
            if (!isTestMode) {
                it.setDetailsMode()
            } else {
                it.isEnabled = false
            }
        }

    }

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
                              savedInstanceState: Bundle?): View {

        _binding = FragmentTestBinding.inflate(inflater, container, false)

        //Appodeal.setBannerViewId(R.id.appodealBannerView)

        binding.arrowBack.setOnClickListener {
            viewModel.onArrowBackClicked()
        }
        binding.settings.setOnClickListener {
            requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frameLayout, PractiseSettingsFragment.newInstance(practise))
                    .addToBackStack(null)
                    .commit()
        }
        binding.next.setOnClickListener {
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
        binding.analyze.setOnClickListener {
            requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frameLayout,
                            AnalyzeFragment.newInstance(practise, practiseResults))
                    .addToBackStack(null)
                    .commit()
        }

        answerButtons = mutableListOf<TestAnswerButton>().apply {
            add(binding.button0)
            add(binding.button1)
            add(binding.button2)
            add(binding.button3)
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

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

        binding.questionNumber.text = questionNumber.toString()
    }

    private fun setPreviousInfo() {

        binding.rightAnswers.text = rightAnswersCount.toString()
        binding.wrongAnswers.text = wrongAnswersCount.toString()
        binding.questionNumber.text = questionNumber.toString()

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
        binding.question.text =
                if (currentType == TYPE_DATE) {
                    if (currentDate.containsMonth)
                        "${currentDate.date}\n${currentDate.month}"
                    else
                        currentDate.date
                } else {
                    currentDate.event
                }
    }

    private fun showControlButtons() {
        binding.next.show()
        binding.analyze.show()
    }

    private fun hideControlButtons() {
        binding.next.hide()
        binding.analyze.hide()
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