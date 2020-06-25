package com.nollpointer.dates.test

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.appodeal.ads.Appodeal
import com.google.android.material.chip.Chip
import com.nollpointer.dates.R
import com.nollpointer.dates.other.Date
import com.nollpointer.dates.other.PractiseHelpDialog
import com.nollpointer.dates.other.PractiseSettingsDialog
import com.nollpointer.dates.other.ResultDialog.ResultDialogCallbackListener
import com.nollpointer.dates.practise.PractiseConstants.DATES
import com.nollpointer.dates.practise.PractiseConstants.DIFFICULTY
import com.nollpointer.dates.practise.PractiseConstants.MIXED
import com.nollpointer.dates.practise.PractiseConstants.ONLY_DATES
import com.nollpointer.dates.practise.PractiseConstants.TEST
import com.nollpointer.dates.practise.PractiseConstants.TEST_MODE
import com.nollpointer.dates.practise.PractiseConstants.TYPE
import com.nollpointer.dates.practise.PractiseResult
import com.nollpointer.dates.practise.PractiseResultFragment.Companion.newInstance
import java.util.*

class TestFragment : Fragment(), ResultDialogCallbackListener {

    private lateinit var questionTextView: TextView
    private lateinit var questionNumberChip: Chip
    private lateinit var rightAnswersChip: Chip
    private lateinit var wrongAnswersChip: Chip
    private lateinit var answerButtons: List<Button>
    private lateinit var dates: ArrayList<Date>
    private lateinit var currentDate: Date

    private var type = 0
    private var isTestMode = false
    private var difficulty = 0
    private var currentType = type
    private val practiseResults = ArrayList<PractiseResult>()
    private val rightAnswerColor = -0xbc5fb9
    private val wrongAnswerColor = -0x48e3e4
    private var isLocked = false
    private var delay = 900

    private val listener = View.OnClickListener { v ->
        if (isLocked) return@OnClickListener
        lockAnswerButtons()
        val button = v as Button
        var rightAnswersCount = rightAnswersChip.text.toString().toInt()
        var wrongAnswersCount = wrongAnswersChip.text.toString().toInt()
        val buttonText = button.text.toString()
        var isCorrect = buttonText.contains(currentDate.date) || buttonText.contains(currentDate.event)
        if (currentDate.containsMonth() && currentType == ONLY_DATES) isCorrect = isCorrect and buttonText.contains(currentDate.month as String)
        if (isCorrect) {
            button.setBackgroundColor(rightAnswerColor)
            button.setTextColor(Color.WHITE)
            rightAnswersCount++
        } else {
            button.setBackgroundColor(wrongAnswerColor)
            button.setTextColor(Color.WHITE)
            wrongAnswersCount++
            for (answerButton in answerButtons) {
                val answerButtonText = answerButton.text.toString()
                var `is` = answerButtonText.contains(currentDate.date) || answerButtonText.contains(currentDate.event)
                if (currentDate.containsMonth() && currentType == ONLY_DATES) `is` = `is` and answerButtonText.contains(currentDate.month as String)
                if (`is`) {
                    answerButton.setBackgroundColor(rightAnswerColor)
                    answerButton.setTextColor(Color.WHITE)
                }
            }
        }
        if (isTestMode) {
            val practiseResult = PractiseResult(questionTextView.text.toString(), isCorrect)
            practiseResults.add(practiseResult)
        }
        if (rightAnswersCount + wrongAnswersCount == 20 && isTestMode) fragmentManager!!.beginTransaction().replace(R.id.frameLayout, newInstance(TEST, practiseResults, arguments!!)).commit()
        rightAnswersChip.text = rightAnswersCount.toString()
        wrongAnswersChip.text = wrongAnswersCount.toString()
        Handler().postDelayed({ generateAndSetInfo() }, delay.toLong())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val mainView = inflater.inflate(R.layout.fragment_test, container, false)
        initializeViews(mainView)
        val arguments = arguments
        type = arguments!!.getInt(TYPE)

        val preDates: ArrayList<Date?>? = arguments!!.getParcelableArrayList(DATES)
        dates = ArrayList()
        preDates!!.forEach {
            dates.add(it as Date)
        }

        difficulty = arguments.getInt(DIFFICULTY)
        isTestMode = arguments.getBoolean(TEST_MODE)
        delay = getDelay()
        generateAndSetInfo()
        return mainView
    }

    private fun initializeViews(mainView: View) {
        val backButton = mainView.findViewById<ImageButton>(R.id.testBackButton)
        val settingsButton = mainView.findViewById<ImageButton>(R.id.testSettingsButton)
        val helpButton = mainView.findViewById<ImageButton>(R.id.testHelpButton)
        Appodeal.setBannerViewId(R.id.appodealBannerView)
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
            val helpDialog = PractiseHelpDialog.newInstance(R.string.help_test)
            helpDialog.show(activity!!.supportFragmentManager, null)
        }
        questionTextView = mainView.findViewById(R.id.testQuestion)
        questionNumberChip = mainView.findViewById(R.id.testQuestionNumber)
        rightAnswersChip = mainView.findViewById(R.id.testRightAnswers)
        wrongAnswersChip = mainView.findViewById(R.id.testWrongAnswers)
        val list = ArrayList<Button>()
        val group = mainView.findViewById<ViewGroup>(R.id.testButtonContainer)
        for (i in 0 until group.childCount) {
            val button = group.getChildAt(i) as Button
            button.setOnClickListener(listener)
            list.add(button)
        }
        answerButtons = list
    }

    private fun lockAnswerButtons() {
        isLocked = true
    }

    private fun unlockAnswerButtons() {
        isLocked = false
    }

    private fun generateAndSetInfo() {
        val questionDate = generateQuestion()
        val answerDates = generateAnswers(questionDate)
        setDefaultColorForAnswerButtons()
        setInfo(questionDate, answerDates)
        unlockAnswerButtons()
    }

    private fun setDefaultColorForAnswerButtons() {
        for (button in answerButtons) {
            button.setBackgroundColor(Color.WHITE)
            button.setTextColor(-0x7f7f80)
        }
    }

    private fun generateQuestion(): Date? {
        val random = Random(System.currentTimeMillis())
        currentDate = dates[random.nextInt(dates.size)]
        return currentDate
    }

    private fun generateAnswers(questionDate: Date?): List<Date?> {
        val random = Random(System.currentTimeMillis())
        val answerDates = ArrayList<Date?>()
        var i = 0
        while (i < 3) {
            val answerDate = dates[random.nextInt(dates.size)]
            if (questionDate == answerDate || answerDates.contains(answerDate)) { //TODO даты с одинаковым годом
                i--
                i++
                continue
            } else answerDates.add(answerDate)
            i++
        }
        answerDates.add(questionDate)
        answerDates.shuffle()
        return answerDates
    }

    private fun setInfo(questionDate: Date?, answerDates: List<Date?>) {
        currentType = if (type == MIXED) {
            Random().nextInt(2)
        } else type
        if (currentType == ONLY_DATES) {
            questionTextView.text = questionDate!!.event
            for (i in answerDates.indices) {
                val date = answerDates[i]
                var text = date!!.date
                if (date.containsMonth()) text += ", " + date.month
                answerButtons[i].text = text
            }
        } else {
            var text = questionDate!!.date
            if (questionDate.containsMonth()) {
                text += ", " + questionDate.month
            }
            questionTextView.text = text
            for (i in answerDates.indices) {
                answerButtons[i].text = answerDates[i]!!.event
            }
        }
        val rightAnswersCount = rightAnswersChip.text.toString().toInt()
        val wrongAnswersCount = wrongAnswersChip.text.toString().toInt()
        questionNumberChip.text = "#" + (rightAnswersCount + wrongAnswersCount + 1)
    }

    private fun getDelay(): Int {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getInt("test delay", 900)
    }

    private fun setDelay(delay: Int) {
        this.delay = delay
        val preferences = PreferenceManager.getDefaultSharedPreferences(context).edit()
        preferences.putInt("test delay", delay)
        preferences.apply()
    }

    override fun reset() {
        if (Appodeal.isLoaded(Appodeal.INTERSTITIAL)) Appodeal.show(activity!!, Appodeal.INTERSTITIAL)
    }

    override fun exit() {
        activity!!.supportFragmentManager.popBackStack()
    }

    override fun onStop() {
        super.onStop()
        Appodeal.hide(activity!!, Appodeal.BANNER_VIEW)
    }

    override fun onStart() {
        super.onStart()
        Appodeal.show(activity!!, Appodeal.BANNER_VIEW)
    }

    companion object {
        fun newInstance(dates: ArrayList<Date>, type: Int, difficulty: Int, testMode: Boolean): TestFragment {
            val test = TestFragment()
            val bundle = Bundle()
            bundle.putBoolean(TEST_MODE, testMode)
            bundle.putInt(TYPE, type)
            bundle.putInt(DIFFICULTY, difficulty)
            bundle.putParcelableArrayList(DATES, dates)
            test.arguments = bundle
            return test
        }
    }
}