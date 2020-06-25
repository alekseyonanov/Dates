package com.nollpointer.dates.truefalse

import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.appodeal.ads.Appodeal
import com.google.android.material.chip.Chip
import com.nollpointer.dates.R
import com.nollpointer.dates.other.Date
import com.nollpointer.dates.other.PractiseHelpDialog
import com.nollpointer.dates.other.PractiseSettingsDialog
import com.nollpointer.dates.practise.PractiseConstants.DATES
import com.nollpointer.dates.practise.PractiseConstants.DIFFICULTY
import com.nollpointer.dates.practise.PractiseConstants.TEST_MODE
import com.nollpointer.dates.practise.PractiseConstants.TRUE_FALSE
import com.nollpointer.dates.practise.PractiseResult
import com.nollpointer.dates.practise.PractiseResultFragment
import java.util.*
import kotlin.collections.ArrayList

class TrueFalseFragment : Fragment() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var dateTextView: TextView
    private lateinit var eventTextView: TextView
    private lateinit var questionNumberChip: Chip
    private lateinit var rightAnswersChip: Chip
    private lateinit var wrongAnswersChip: Chip
    private lateinit var resultImage: ImageView
    private lateinit var dates: ArrayList<Date>

    private var isLocked = false
    private var isTrue = false
    private val practiseResults = ArrayList<PractiseResult>()
    private var difficulty = 0
    private var isTestMode = false
    private var delay = 900

    private val listener = View.OnClickListener { v ->
        if (isLocked) return@OnClickListener
        lockAnswerButtons()
        var rightAnswersCount = rightAnswersChip.text.toString().toInt()
        var wrongAnswersCount = wrongAnswersChip.text.toString().toInt()
        val isCorrect = isTrue && v == trueButton || !isTrue && v == falseButton
        if (isCorrect) {
            showCorrectImage()
            rightAnswersCount++
        } else {
            showMistakeImage()
            wrongAnswersCount++
        }
        if (isTestMode) {
            val practiseResult = PractiseResult(dateTextView.text.toString() + " – " + eventTextView.text, isCorrect)
            practiseResults.add(practiseResult)
        }
        if (rightAnswersCount + wrongAnswersCount == 20 && isTestMode) fragmentManager!!.beginTransaction().replace(R.id.frameLayout, PractiseResultFragment.newInstance(TRUE_FALSE, practiseResults, arguments!!)).commit()
        rightAnswersChip.text = rightAnswersCount.toString()
        wrongAnswersChip.text = wrongAnswersCount.toString()
        Handler().postDelayed({ generateAndSetInfo() }, delay.toLong())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val mainView = inflater.inflate(R.layout.fragment_true_false, container, false)
        val arguments = arguments

        val preDates: ArrayList<Date?>? = arguments!!.getParcelableArrayList(DATES)
        dates = ArrayList()
        preDates!!.forEach {
            dates.add(it as Date)
        }

        difficulty = arguments.getInt(DIFFICULTY)
        isTestMode = arguments.getBoolean(TEST_MODE)
        delay = getDelay()
        initializeViews(mainView)
        generateAndSetInfo()
        return mainView
    }

    private fun initializeViews(mainView: View) {
        Appodeal.setBannerViewId(R.id.appodealBannerView_true)
        dateTextView = mainView.findViewById(R.id.test_info_true_false_date)
        eventTextView = mainView.findViewById(R.id.test_info_true_false_event)
        val backButton = mainView.findViewById<ImageButton>(R.id.true_false_back_button)
        val settingsButton = mainView.findViewById<ImageButton>(R.id.true_false_settings_button)
        val helpButton = mainView.findViewById<ImageButton>(R.id.true_false_help_button)
        trueButton = mainView.findViewById(R.id.true_button)
        trueButton.setOnClickListener(listener)
        falseButton = mainView.findViewById(R.id.false_button)
        falseButton.setOnClickListener(listener)
        backButton.setImageResource(R.drawable.ic_arrow_back_white)
        backButton.setOnClickListener { fragmentManager!!.popBackStack() }
        settingsButton.setImageResource(R.drawable.ic_settings)
        settingsButton.setOnClickListener {
            val settingsDialog = PractiseSettingsDialog.newInstance(delay)
            settingsDialog.setListener(object : PractiseSettingsDialog.Listener {
                override fun onDelayPicked(delay: Int) {
                    setDelay(delay)
                }
            })
            settingsDialog.show(activity!!.supportFragmentManager, null)
        }
        helpButton.setImageResource(R.drawable.ic_help)
        helpButton.setOnClickListener {
            val helpDialog = PractiseHelpDialog.newInstance(R.string.help_true_false)
            helpDialog.show(activity!!.supportFragmentManager, null)
        }
        questionNumberChip = mainView.findViewById(R.id.trueFalseQuestionNumber)
        rightAnswersChip = mainView.findViewById(R.id.trueFalseRightAnswers)
        wrongAnswersChip = mainView.findViewById(R.id.trueFalseWrongAnswers)
        resultImage = mainView.findViewById(R.id.true_false_result_image)
    }

    override fun onStop() {
        super.onStop()
        Appodeal.hide(activity!!, Appodeal.BANNER_VIEW)
    }

    override fun onStart() {
        super.onStart()
        Appodeal.show(activity!!, Appodeal.BANNER_VIEW)
    }

    private fun lockAnswerButtons() {
        isLocked = true
    }

    private fun unlockAnswerButtons() {
        isLocked = false
    }

    private fun generateAndSetInfo() {
        isTrue = generateRandomBoolean()
        val questionDate = generateDate()
        var date = questionDate.date
        val event: String
        if (questionDate.containsMonth()) date += ", " + questionDate.month
        if (isTrue) event = questionDate.event else {
            var anotherDate = generateDate()
            while (anotherDate == questionDate) anotherDate = generateDate()
            event = anotherDate.event
        }
        hideResultImage()
        setInfo(date, event)
        unlockAnswerButtons()
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
        dateTextView.text = Html.fromHtml(date)
        eventTextView.text = event
        val rightAnswersCount = rightAnswersChip.text.toString().toInt()
        val wrongAnswersCount = wrongAnswersChip.text.toString().toInt()
        questionNumberChip.text = "#" + (rightAnswersCount + wrongAnswersCount + 1)
    }

    private fun showCorrectImage() {
        resultImage.setImageResource(R.drawable.ic_correct)
        resultImage.visibility = View.VISIBLE
    }

    private fun showMistakeImage() {
        resultImage.setImageResource(R.drawable.ic_mistake)
        resultImage.visibility = View.VISIBLE
    }

    private fun hideResultImage() {
        resultImage.visibility = View.INVISIBLE
    }

    private fun getDelay(): Int {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getInt("truefalse delay", 900)
    }

    private fun setDelay(delay: Int) {
        this.delay = delay
        val preferences = PreferenceManager.getDefaultSharedPreferences(context).edit()
        preferences.putInt("truefalse delay", delay)
        preferences.apply()
    }

    companion object {
        fun newInstance(dates: ArrayList<Date>, difficulty: Int, testMode: Boolean): TrueFalseFragment {
            val fragment = TrueFalseFragment()
            val bundle = Bundle()
            bundle.putBoolean(TEST_MODE, testMode)
            bundle.putInt(DIFFICULTY, difficulty)
            bundle.putParcelableArrayList(DATES, dates)
            fragment.arguments = bundle
            return fragment
        }
    }
}