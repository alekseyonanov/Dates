package com.nollpointer.dates.ui.practiseresult

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nollpointer.dates.R
import com.nollpointer.dates.model.PractiseResult

class PractiseResultFragment : Fragment() {

    private lateinit var practiseResults: ArrayList<PractiseResult>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val mainView = inflater.inflate(R.layout.fragment_practise_result, container, false)
//        val prePractiseResults: ArrayList<PractiseResult?>? = arguments!!.getParcelableArrayList(RESULTS_LIST)
//        practiseResults = ArrayList()
//        prePractiseResults!!.forEach {
//            practiseResults.add(it as PractiseResult)
//        }
//        val practise = arguments!!.getString(PRACTISE)
//        val recyclerView: RecyclerView = mainView.findViewById(R.id.results_recycler_view)
//        val adapter = PractiseResultCardsAdapter(practiseResults)
//        val linearLayout: LinearLayoutManager = object : LinearLayoutManager(context) {
//            override fun canScrollVertically(): Boolean {
//                return false
//            }
//        }
//        val dividerItemDecoration = DividerItemDecoration(recyclerView.context,
//                linearLayout.orientation)
//        recyclerView.layoutManager = linearLayout
//        recyclerView.addItemDecoration(dividerItemDecoration)
//        recyclerView.adapter = adapter
//        val toolbar: Toolbar = mainView.findViewById(R.id.results_toolbar)
//        toolbar.setNavigationOnClickListener { fragmentManager!!.popBackStack() }
//        val exitButton = mainView.findViewById<Button>(R.id.results_exit)
//        exitButton.setOnClickListener { fragmentManager!!.popBackStack() }
//        val resetButton = mainView.findViewById<Button>(R.id.results_reset)
//        resetButton.setOnClickListener {
//            //Toast.makeText(getContext(), "Mate kudasai((", Toast.LENGTH_SHORT).show();
//            startPractise()
//        }
//        val markTextView = mainView.findViewById<TextView>(R.id.results_mark_text_view)
//        setMark(markTextView)
//        SaveCurrentMark(context, getPractiseSaveTitle(practise), getMarkValue(correctAnswerCount)).execute()
        return mainView
    }
/*
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
        when (count) {
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
    }*/

    companion object {
        private const val RESULTS_LIST = "results_list"
        private const val PRACTISE = "practise"

        @JvmStatic
        fun newInstance(practise: String? = "", list: java.util.ArrayList<PractiseResult> = ArrayList<PractiseResult>(), arguments: Bundle = Bundle()) =
                PractiseResultFragment().apply {

                }
    }
}