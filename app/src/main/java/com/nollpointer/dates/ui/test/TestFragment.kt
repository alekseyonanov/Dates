package com.nollpointer.dates.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.nollpointer.dates.R
import com.nollpointer.dates.databinding.FragmentTestBinding
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.ui.test.model.QuestionModel
import com.nollpointer.dates.ui.test.model.ResultModel
import com.nollpointer.dates.ui.view.BaseFragment
import com.nollpointer.dates.ui.view.TestAnswerButton
import dagger.hilt.android.AndroidEntryPoint


/**
 * Экран практики "Тестирование"
 *
 * @author Onanov Aleksey (@onanov)
 */
@AndroidEntryPoint
class TestFragment : BaseFragment() {

    private lateinit var testAnswerButtons: List<TestAnswerButton>

    private var _binding: FragmentTestBinding? = null
    private val binding: FragmentTestBinding
        get() = _binding!!

    private val viewModel by viewModels<TestViewModel>()

    override val statusBarColorRes = R.color.colorBackground

    override val isStatusBarLight = true

    override val isBottomNavigationViewHidden = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentTestBinding.inflate(inflater, container, false)

        //Appodeal.setBannerViewId(R.id.appodealBannerView)

        binding.arrowBack.setOnClickListener {
            viewModel.onArrowBackClicked()
        }
        binding.settings.setOnClickListener {
            viewModel.onSettingsClicked()
        }
        binding.next.setOnClickListener {
            viewModel.onNextClicked()
        }
        binding.analyze.setOnClickListener {
            viewModel.onAnalyzeClicked()
        }

        binding.question.setOnClickListener {
            viewModel.onTitleClicked()
        }

        testAnswerButtons = listOf(binding.button0, binding.button1, binding.button2, binding.button3)
        testAnswerButtons.forEach {
            it.setOnAnswerButtonClickListener = viewModel::onAnswerClicked
            it.setOnDetailsClickListener = viewModel::onDetailsClicked
        }

        viewModel.apply {
            practise = requireArguments().getParcelable<Practise>(TEST) as Practise
            controlButtonsVisibilityLiveData.observe({ lifecycle }, ::showControlButtons)
            rightWrongAnswersLiveData.observe({ lifecycle }, ::setRightWrongAnswers)
            questionLiveData.observe({ lifecycle }, ::setQuestion)
            resultsLiveData.observe({ lifecycle }, ::setResult)
            start()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showControlButtons(isVisible: Boolean) {
        if (isVisible) {
            binding.next.show()
            binding.analyze.show()
        } else {
            binding.next.hide()
            binding.analyze.hide()
        }
    }

    private fun setRightWrongAnswers(pair: Pair<Int, Int>) {
        binding.rightAnswers.text = pair.first.toString()
        binding.wrongAnswers.text = pair.second.toString()
        binding.questionNumber.text = (pair.first + pair.second + 1).toString()
    }

    private fun setQuestion(question: QuestionModel) {
        binding.question.text = question.question
        question.answers.zip(testAnswerButtons).forEach { (date, answerButton) ->
            answerButton.setDate(question.type, date)
        }
    }

    private fun setResult(result: ResultModel) {
        testAnswerButtons[result.rightAnswerPosition].setResult(true)
        // TODO: 13.03.2021 Отрефачить, не нравится такое решение 
        testAnswerButtons[result.clickedPosition].setResult(result.clickedPosition == result.rightAnswerPosition)

        testAnswerButtons.forEach { it.setDetailsMode() }
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