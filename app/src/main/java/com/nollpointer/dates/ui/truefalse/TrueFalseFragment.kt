package com.nollpointer.dates.ui.truefalse

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.nollpointer.dates.R
import com.nollpointer.dates.databinding.FragmentTrueFalseBinding
import com.nollpointer.dates.model.Date
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.ui.details.dates.DatesDetailsFragment
import com.nollpointer.dates.ui.view.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

/**
 * @author Onanov Aleksey (@onanov)
 */
@AndroidEntryPoint
class TrueFalseFragment : BaseFragment() {

    private lateinit var dates: List<Date>

    private var isLocked = false
    private var isTrue = false
    private var isCorrect = false
    private var isTestMode = false

    private var questionNumber = 0
    private var rightAnswersCount = 0
    private var wrongAnswersCount = 0

    private var questionDate = Date()
    private var questionEvent = Date()

    private var _binding: FragmentTrueFalseBinding? = null
    private val binding: FragmentTrueFalseBinding
        get() = _binding!!

    private val viewModel by viewModels<TrueFalseViewModel>()

    private val listener = View.OnClickListener { view ->
        if (isLocked)
            return@OnClickListener
        isCorrect = (isTrue && view == binding.trueButton) || (!isTrue && view == binding.falseButton)
        if (isCorrect) {
            rightAnswersCount++
        } else {
            wrongAnswersCount++
        }

        if (isTrue) {
            showEqualsImage(isCorrect)
        } else {
            showNotEqualsImage(isCorrect)
        }

        binding.rightCount.text = rightAnswersCount.toString()
        binding.wrongCount.text = wrongAnswersCount.toString()
        isLocked = true
        showControlButtons()
        updateTrueFalseButtons()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val practise = it.getParcelable<Practise>(TRUE_FALSE) as Practise
            isTestMode = practise.isTestMode
            dates = practise.dates
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentTrueFalseBinding.inflate(inflater, container, false)

        //Appodeal.setBannerViewId(R.id.appodealBannerView_true)
        binding.trueButton.setOnClickListener(listener)
        binding.falseButton.setOnClickListener(listener)

        binding.arrowBack.setOnClickListener { viewModel.onArrowBackClicked() }

        binding.settings.setOnClickListener { }

        binding.next.setOnClickListener {
            generateAndSetInfo()
        }

        binding.analyze.setOnClickListener {
            viewModel.onAnalyzeClicked()
        }

        if (questionNumber != 0) {
            setPreviousInfo()
        } else {
            generateAndSetInfo()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getStatusBarColorRes() = R.color.colorBackground

    override fun isStatusBarLight() = true

    private fun generateAndSetInfo() {
        questionNumber++
        isTrue = generateRandomBoolean()
        questionDate = generateDate()
        val date = if (questionDate.containsMonth) questionDate.date + ", " + questionDate.month else questionDate.date
        val event = if (isTrue) {
            questionDate.event
        } else {
            questionEvent = generateDate()
            while (questionEvent == questionDate) questionEvent = generateDate()
            questionEvent.event
        }
        rollbackTrueFalseButtons()
        hideResultImage()
        hideControlButtons()
        isLocked = false
        setInfo(date, event)
    }

    private fun generateRandomBoolean(): Boolean {
        val random = Random(System.currentTimeMillis())
        return random.nextBoolean()
    }

    private fun generateDate(): Date {
        val random = Random(System.currentTimeMillis())
        return dates[random.nextInt(dates.size)]
    }

    private fun setInfo(date: String, event: String) {
        binding.date.text = date
        binding.event.text = event
        binding.questionNumber.text = questionNumber.toString()
    }

    private fun setPreviousInfo() {
        binding.date.text = if (questionDate.containsMonth) questionDate.date + ", " + questionDate.month else questionDate.date

        if (isTrue) {
            showEqualsImage(isCorrect)
            binding.event.text = questionDate.event
        } else {
            showNotEqualsImage(isCorrect)
            binding.event.text = questionEvent.event
        }

        binding.rightCount.text = rightAnswersCount.toString()
        binding.wrongCount.text = wrongAnswersCount.toString()
        binding.questionNumber.text = questionNumber.toString()
        isLocked = true
        showControlButtons()
        updateTrueFalseButtons()
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

    private fun hideResultImage() {
        binding.result.visibility = View.GONE
    }

    private fun hideControlButtons() {
        binding.analyze.hide()
        binding.next.hide()
    }

    private fun showControlButtons() {
        binding.analyze.show()
        binding.next.show()
    }

    private fun updateTrueFalseButtons() {
        binding.trueButton.apply {
            setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_help, 0, 0, 0)
            setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, DatesDetailsFragment.newInstance(questionDate))
                        .addToBackStack(null)
                        .commit()
            }
            text = getString(R.string.true_false_question_date)
        }
        binding.falseButton.apply {
            setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_help, 0)
            setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, DatesDetailsFragment.newInstance(questionEvent))
                        .addToBackStack(null)
                        .commit()
            }
            text = getString(R.string.true_false_question_event)
        }
        if (isTrue) {
            binding.falseButton.visibility = View.GONE
        }
    }

    private fun rollbackTrueFalseButtons() {
        binding.trueButton.apply {
            setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
            setOnClickListener(listener)
            text = getString(R.string.true_button)
            visibility = View.VISIBLE
        }
        binding.falseButton.apply {
            setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
            setOnClickListener(listener)
            text = getString(R.string.false_button)
            visibility = View.VISIBLE
        }
    }

    private fun getResultImageTint(isCorrect: Boolean) = ColorStateList.valueOf(ContextCompat.getColor(context as Context,
            if (isCorrect) {
                R.color.colorTest
            } else {
                R.color.colorPrimary
            }
    )
    )

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