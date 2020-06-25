package com.nollpointer.dates.practise

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appodeal.ads.Appodeal
import com.nollpointer.dates.R
import com.nollpointer.dates.cards.CardsFragment.Companion.newInstance
import com.nollpointer.dates.distribute.DistributeFragment
import com.nollpointer.dates.other.Date
import com.nollpointer.dates.practise.PractiseConstants.CARDS
import com.nollpointer.dates.practise.PractiseConstants.DATES
import com.nollpointer.dates.practise.PractiseConstants.DIFFICULTY
import com.nollpointer.dates.practise.PractiseConstants.DISTRIBUTE
import com.nollpointer.dates.practise.PractiseConstants.SORT
import com.nollpointer.dates.practise.PractiseConstants.TEST
import com.nollpointer.dates.practise.PractiseConstants.TEST_MODE
import com.nollpointer.dates.practise.PractiseConstants.TRUE_FALSE
import com.nollpointer.dates.practise.PractiseConstants.TYPE
import com.nollpointer.dates.practise.PractiseConstants.VOICE
import com.nollpointer.dates.sort.SortFragment
import com.nollpointer.dates.test.TestFragment
import com.nollpointer.dates.truefalse.TrueFalseFragment
import com.nollpointer.dates.voice.VoiceFragment
import java.util.*
import kotlin.collections.ArrayList

class PractiseResultFragment : Fragment() {
    private lateinit var practiseResults: ArrayList<PractiseResult>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val mainView = inflater.inflate(R.layout.fragment_practise_result, container, false)
        val prePractiseResults: ArrayList<PractiseResult?>? = arguments!!.getParcelableArrayList(RESULTS_LIST)
        practiseResults = ArrayList()
        prePractiseResults!!.forEach {
            practiseResults.add(it as PractiseResult)
        }
        val practise = arguments!!.getString(PRACTISE)
        val recyclerView: RecyclerView = mainView.findViewById(R.id.results_recycler_view)
        val adapter = PractiseResultCardsAdapter(practiseResults)
        val linearLayout: LinearLayoutManager = object : LinearLayoutManager(context) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        val dividerItemDecoration = DividerItemDecoration(recyclerView.context,
                linearLayout.orientation)
        recyclerView.layoutManager = linearLayout
        recyclerView.addItemDecoration(dividerItemDecoration)
        recyclerView.adapter = adapter
        val toolbar: Toolbar = mainView.findViewById(R.id.results_toolbar)
        toolbar.setNavigationOnClickListener { fragmentManager!!.popBackStack() }
        val exitButton = mainView.findViewById<Button>(R.id.results_exit)
        exitButton.setOnClickListener { fragmentManager!!.popBackStack() }
        val resetButton = mainView.findViewById<Button>(R.id.results_reset)
        resetButton.setOnClickListener {
            //Toast.makeText(getContext(), "Mate kudasai((", Toast.LENGTH_SHORT).show();
            startPractise()
        }
        val markTextView = mainView.findViewById<TextView>(R.id.results_mark_text_view)
        setMark(markTextView)
        SaveCurrentMark(context, getPractiseSaveTitle(practise), getMarkValue(correctAnswerCount)).execute()
        return mainView
    }

    private fun startPractise() {
        val arguments = arguments
        val type = arguments!!.getInt(TYPE)
        val preDates: ArrayList<Date?>? = arguments.getParcelableArrayList(DATES)

        val dates = ArrayList<Date>()
        preDates!!.forEach {
            dates.add(it as Date)
        }

        val difficulty = arguments.getInt(DIFFICULTY)
        val isTestMode = arguments.getBoolean(TEST_MODE)
        val practise = getArguments()!!.getString(PRACTISE)
        val fragment: Fragment
        fragment = when (practise) {
            CARDS -> newInstance(dates, type)
            VOICE -> VoiceFragment.newInstance(dates, type, difficulty, isTestMode)
            TEST -> TestFragment.newInstance(dates, type, difficulty, isTestMode)
            TRUE_FALSE -> TrueFalseFragment.newInstance(dates, difficulty, isTestMode)
            SORT -> SortFragment.newInstance(dates, difficulty, isTestMode)
            DISTRIBUTE -> DistributeFragment()
            else -> newInstance(dates, type)
        }
        if (isTestMode) showAds()
        fragmentManager!!.beginTransaction().replace(R.id.frameLayout, fragment).commit()
    }

    private fun getPractiseSaveTitle(practise: String?): String {
        val titles = resources.getStringArray(R.array.practise_marks_titles)
        val saveTitle: String
        saveTitle = when (practise) {
            CARDS -> titles[0]
            VOICE -> titles[1]
            TEST -> titles[2]
            TRUE_FALSE -> titles[3]
            SORT -> titles[4]
            DISTRIBUTE -> titles[5]
            else -> titles[0]
        }
        return saveTitle
    }

    private fun getMarkValue(result: Int): Int {
        return when (result) {
            in 0..12 -> 0
            in 13..16 -> 1
            else -> 2
        }
    }

    private val correctAnswerCount: Int
        get() {
            var count = 0
            for (result in practiseResults) {
                if (result.isCorrect) count++
            }
            return count
        }

    private fun setMark(markTextView: TextView) {
        val count = correctAnswerCount
        val mark: String
        val drawable: Int
        val color: String
        val colors = arrayOf("\"#B71C1C\"", "\"#FFEB3B\"", "\"#43a047\"")
        when(count){
            in 0..4 -> {
                mark = getString(R.string.mark_very_bad)
                drawable = R.drawable.ic_sentiment_very_bad
                color = colors[0]
            }
            in 5..8 -> {
                mark = getString(R.string.mark_bad)
                drawable = R.drawable.ic_sentiment_bad
                color = colors[0]
            }
            in 9..12 -> {
                mark = getString(R.string.mark_neutral)
                drawable = R.drawable.ic_sentiment_neutral
                color = colors[1]
            }
            in 13..16 -> {
                mark = getString(R.string.mark_good)
                drawable = R.drawable.ic_sentiment_good
                color = colors[2]
            }
            else -> {
                mark = getString(R.string.mark_very_good)
                drawable = R.drawable.ic_sentiment_very_good
                color = colors[2]
            }

        }
        markTextView.text = Html.fromHtml("Ваш результат: <font color=" + color + ">" + mark + "</font><br>Ваши баллы: <font color=" + color + ">" + getMark(count) + "</font>"
        )
        markTextView.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
    }

    private fun showAds() {
        if (Appodeal.isLoaded(Appodeal.INTERSTITIAL)) Appodeal.show(activity!!, Appodeal.INTERSTITIAL)
    }

    private fun getMark(count: Int): String {
        val number = count * 5.0 / 20
        return String.format("%.1f", number)
    }

    private class SaveCurrentMark internal constructor(private val context: Context?, private val practise: String, private val value: Int) : AsyncTask<Void, Void?, Void>() {
        override fun doInBackground(vararg voids: Void): Void? {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putInt(practise, value)
            editor.apply()
            return null
        }

    }

    companion object {
        private const val RESULTS_LIST = "results_list"
        private const val PRACTISE = "practise"
        @JvmStatic
        fun newInstance(practise: String?, list: java.util.ArrayList<PractiseResult>, arguments: Bundle): PractiseResultFragment {
            val args = Bundle()
            val type = arguments.getInt(TYPE)
            val preDates: ArrayList<Date?>? = arguments.getParcelableArrayList(DATES)

            val dates = ArrayList<Date>()
            preDates!!.forEach {
                dates.add(it as Date)
            }

            val difficulty = arguments.getInt(DIFFICULTY)
            val isTestMode = arguments.getBoolean(TEST_MODE)
            args.putParcelableArrayList(RESULTS_LIST, list)
            args.putString(PRACTISE, practise)
            args.putBoolean(TEST_MODE, isTestMode)
            args.putInt(TYPE, type)
            args.putInt(DIFFICULTY, difficulty)
            args.putParcelableArrayList(DATES, dates)
            val fragment = PractiseResultFragment()
            fragment.arguments = args
            return fragment
        }
    }
}