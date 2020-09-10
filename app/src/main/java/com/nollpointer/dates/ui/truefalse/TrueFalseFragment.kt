package com.nollpointer.dates.ui.truefalse

import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.appodeal.ads.Appodeal
import com.nollpointer.dates.R
import com.nollpointer.dates.model.Date
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.model.PractiseResult
import com.nollpointer.dates.ui.practiseresult.PractiseResultFragment
import kotlinx.android.synthetic.main.fragment_true_false.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author Onanov Aleksey (@onanov)
 */
class TrueFalseFragment : Fragment() {

    private lateinit var dates: List<Date>

    private var isLocked = false
    private var isTrue = false
    private val practiseResults = ArrayList<PractiseResult>()
    private var isTestMode = false
    private var delay = 900

    private val listener = View.OnClickListener { v ->
        if (isLocked)
            return@OnClickListener
        lockAnswerButtons()
        var rightAnswersCount = trueFalseRightAnswersChip.text.toString().toInt()
        var wrongAnswersCount = trueFalseWrongAnswersChip.text.toString().toInt()
        val isCorrect = isTrue && v == trueFalseTrueButton || !isTrue && v == trueFalseFalseButton
        if (isCorrect) {
            showCorrectImage()
            rightAnswersCount++
        } else {
            showMistakeImage()
            wrongAnswersCount++
        }
//        if (isTestMode) {
//            val practiseResult = PractiseResult(trueFalseDate.text.toString() + " – " + trueFalseEvent.text, isCorrect)
//            practiseResults.add(practiseResult)
//        }
        if (rightAnswersCount + wrongAnswersCount == 20 && isTestMode)
            fragmentManager!!.beginTransaction().replace(R.id.frameLayout, PractiseResultFragment.newInstance(TRUE_FALSE, practiseResults, arguments!!)).commit()
        trueFalseRightAnswersChip.text = rightAnswersCount.toString()
        trueFalseWrongAnswersChip.text = wrongAnswersCount.toString()
        Handler().postDelayed({ generateAndSetInfo() }, delay.toLong())
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
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_true_false, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Appodeal.setBannerViewId(R.id.appodealBannerView_true)
        trueFalseTrueButton.setOnClickListener(listener)
        trueFalseFalseButton.setOnClickListener(listener)

        trueFalseBack.apply {
            //setImageResource(R.drawable.ic_arrow_back_white)
            setOnClickListener { fragmentManager!!.popBackStack() }
        }

        trueFalseSettings.apply {
            //setImageResource(R.drawable.ic_settings)
            setOnClickListener {
//                val settingsDialog = PractiseSettingsDialog.newInstance(delay)
//                settingsDialog.setListener(object : PractiseSettingsDialog.Listener {
//                    override fun onDelayPicked(delay: Int) {
//                        setDelay(delay)
//                    }
//                })
//                settingsDialog.show(activity!!.supportFragmentManager, null)

            }
        }

//        trueFalseHelp.apply {
//            setImageResource(R.drawable.ic_help)
//            setOnClickListener {
//                val helpDialog = PractiseHelpDialog.newInstance(R.string.help_true_false)
//                helpDialog.show(activity!!.supportFragmentManager, null)
//            }
//        }

        delay = getDelay()
        //generateAndSetInfo()

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
        if (questionDate.containsMonth) date += ", " + questionDate.month
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
        trueFalseDate.text = Html.fromHtml(date)
        trueFalseEvent.text = event
        val rightAnswersCount = trueFalseRightAnswersChip.text.toString().toInt()
        val wrongAnswersCount = trueFalseWrongAnswersChip.text.toString().toInt()
        trueFalseQuestionNumberChip.text = "#" + (rightAnswersCount + wrongAnswersCount + 1)
    }

    private fun showCorrectImage() {
        trueFalseResult.setImageResource(R.drawable.ic_correct)
        trueFalseResult.visibility = View.VISIBLE
    }

    private fun showMistakeImage() {
        trueFalseResult.setImageResource(R.drawable.ic_mistake)
        trueFalseResult.visibility = View.VISIBLE
    }

    private fun hideResultImage() {
        trueFalseResult.visibility = View.INVISIBLE
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

        private const val TRUE_FALSE = "TrueFalse"

        fun newInstance(practise: Practise) =
                TrueFalseFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(TRUE_FALSE, practise)
                    }
                }
    }
}