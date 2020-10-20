package com.nollpointer.dates.ui.truefalse

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.nollpointer.dates.R
import com.nollpointer.dates.model.Date
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.ui.analyze.AnalyzeFragment
import com.nollpointer.dates.ui.details.dates.DatesDetailsFragment
import com.nollpointer.dates.ui.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_true_false.*
import java.util.*

/**
 * @author Onanov Aleksey (@onanov)
 */
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

    private val listener = View.OnClickListener { view ->
        if (isLocked)
            return@OnClickListener
        isCorrect = (isTrue && view == trueFalseTrueButton) || (!isTrue && view == trueFalseFalseButton)
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

        trueFalseRightAnswersChip.text = rightAnswersCount.toString()
        trueFalseWrongAnswersChip.text = wrongAnswersCount.toString()
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

    override fun getStatusBarColorRes() = R.color.colorBackground

    override fun isStatusBarLight() = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_true_false, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Appodeal.setBannerViewId(R.id.appodealBannerView_true)
        trueFalseTrueButton.setOnClickListener(listener)
        trueFalseFalseButton.setOnClickListener(listener)

        trueFalseBack.setOnClickListener { requireActivity().supportFragmentManager.popBackStack() }

        trueFalseSettings.setOnClickListener { }

        trueFalseNextButton.setOnClickListener {
            generateAndSetInfo()
        }

        trueFalseAnalyzeButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.frameLayout, AnalyzeFragment())
        }

        if (questionNumber != 0) {
            setPreviousInfo()
        } else {
            generateAndSetInfo()

        }

    }

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
        trueFalseDate.text = date
        trueFalseEvent.text = event
        trueFalseQuestionNumberChip.text = questionNumber.toString()
    }

    private fun setPreviousInfo() {
        trueFalseDate.text = if (questionDate.containsMonth) questionDate.date + ", " + questionDate.month else questionDate.date

        if (isTrue) {
            showEqualsImage(isCorrect)
            trueFalseEvent.text = questionDate.event
        } else {
            showNotEqualsImage(isCorrect)
            trueFalseEvent.text = questionEvent.event
        }

        trueFalseRightAnswersChip.text = rightAnswersCount.toString()
        trueFalseWrongAnswersChip.text = wrongAnswersCount.toString()
        trueFalseQuestionNumberChip.text = questionNumber.toString()
        isLocked = true
        showControlButtons()
        updateTrueFalseButtons()
    }

    private fun showEqualsImage(isCorrect: Boolean) {
        trueFalseResult.setImageResource(R.drawable.ic_equals)
        trueFalseResult.imageTintList = getResultImageTint(isCorrect)
        trueFalseResult.visibility = View.VISIBLE
    }

    private fun showNotEqualsImage(isCorrect: Boolean) {
        trueFalseResult.setImageResource(R.drawable.ic_equals_not)
        trueFalseResult.imageTintList = getResultImageTint(isCorrect)
        trueFalseResult.visibility = View.VISIBLE
    }

    private fun hideResultImage() {
        trueFalseResult.visibility = View.GONE
    }

    private fun hideControlButtons() {
        trueFalseAnalyzeButton.hide()
        trueFalseNextButton.hide()
    }

    private fun showControlButtons() {
        trueFalseAnalyzeButton.show()
        trueFalseNextButton.show()
    }

    private fun updateTrueFalseButtons() {
        trueFalseTrueButton.apply {
            setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_help, 0, 0, 0)
            setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, DatesDetailsFragment.newInstance(questionDate))
                        .addToBackStack(null)
                        .commit()
            }
            text = getString(R.string.true_false_question_date)
        }
        trueFalseFalseButton.apply {
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
            trueFalseFalseButton.visibility = View.GONE
        }
    }

    private fun rollbackTrueFalseButtons() {
        trueFalseTrueButton.apply {
            setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
            setOnClickListener(listener)
            text = getString(R.string.true_button)
            visibility = View.VISIBLE
        }
        trueFalseFalseButton.apply {
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