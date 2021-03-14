package com.nollpointer.dates.ui.truefalse

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.nollpointer.dates.R
import com.nollpointer.dates.databinding.FragmentTrueFalseBinding
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.ui.truefalse.model.ResultModel
import com.nollpointer.dates.ui.view.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author Onanov Aleksey (@onanov)
 */
@AndroidEntryPoint
class TrueFalseFragment : BaseFragment() {

    private var _binding: FragmentTrueFalseBinding? = null
    private val binding: FragmentTrueFalseBinding
        get() = _binding!!

    private val viewModel by viewModels<TrueFalseViewModel>()

    override val statusBarColorRes = R.color.colorBackground

    override val isStatusBarLight = true

    override val isBottomNavigationViewHidden = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentTrueFalseBinding.inflate(inflater, container, false)

        //Appodeal.setBannerViewId(R.id.appodealBannerView_true)
        binding.trueButton.setOnClickListener {
            viewModel.onTrueClicked()
        }
        binding.falseButton.setOnClickListener {
            viewModel.onFalseClicked()
        }

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

        binding.date.setOnClickListener {
            viewModel.onDateClicked()
        }

        binding.event.setOnClickListener {
            viewModel.onEventClicked()
        }

        viewModel.apply {
            practise = requireArguments().getParcelable<Practise>(TRUE_FALSE) as Practise
            controlButtonsVisibilityLiveData.observe({ lifecycle }, ::showControlButtons)
            rightWrongAnswersLiveData.observe({ lifecycle }, ::setRightWrongAnswers)
            questionLiveData.observe({ lifecycle }, ::setQuestion)
            resultLiveData.observe({ lifecycle }, ::setResult)
            start()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setRightWrongAnswers(pair: Pair<Int, Int>) {
        binding.rightAnswers.text = pair.first.toString()
        binding.wrongAnswers.text = pair.second.toString()
        binding.questionNumber.text = (pair.first + pair.second + 1).toString()
    }

    private fun hideResultImage() {
        binding.result.visibility = View.GONE
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

    private fun setResult(result: ResultModel) {
        if (result.isEquals) {
            showEqualsImage(result.isCorrect)
        } else {
            showNotEqualsImage(result.isCorrect)
        }

        updateTrueFalseButtons(result.isEquals)
    }

    private fun setQuestion(pair: Pair<String, String>) {
        binding.date.text = pair.first
        binding.event.text = pair.second

        rollbackTrueFalseButtons()
        hideResultImage()
    }

    private fun updateTrueFalseButtons(isEquals: Boolean) {
        binding.trueButton.apply {
            setOnClickListener {
                viewModel.onDateClicked()
            }
            text = getString(R.string.true_false_question_date)
        }
        binding.falseButton.apply {
            setOnClickListener {
                viewModel.onEventClicked()
            }
            text = getString(R.string.true_false_question_event)
        }
        if (isEquals) {
            binding.falseButton.visibility = View.GONE
        }
    }

    private fun rollbackTrueFalseButtons() {
        binding.trueButton.apply {
            text = getString(R.string.true_button)
            setOnClickListener {
                viewModel.onTrueClicked()
            }
            visibility = View.VISIBLE
        }
        binding.falseButton.apply {
            text = getString(R.string.false_button)
            setOnClickListener {
                viewModel.onFalseClicked()
            }
            visibility = View.VISIBLE
        }
    }

    private fun showEqualsImage(isCorrect: Boolean) {
        binding.result.setImageResource(R.drawable.ic_equals)
        binding.result.imageTintList = getResultImageTint(isCorrect)
        binding.result.visibility = View.VISIBLE
    }

    private fun showNotEqualsImage(isCorrect: Boolean) {
        binding.result.setImageResource(R.drawable.ic_equals_not)
        binding.result.imageTintList = getResultImageTint(isCorrect)
        binding.result.visibility = View.VISIBLE
    }

    private fun getResultImageTint(isCorrect: Boolean): ColorStateList {
        return ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(),
                        if (isCorrect) R.color.colorTest else R.color.colorPrimary
                )
        )
    }

    companion object {
        private const val TRUE_FALSE = "TrueFalse"

        @JvmStatic
        fun newInstance(practise: Practise) =
                TrueFalseFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(TRUE_FALSE, practise)
                    }
                }
    }
}