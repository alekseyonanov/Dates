package com.nollpointer.dates.cards

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.nollpointer.dates.R
import com.nollpointer.dates.other.Date
import com.nollpointer.dates.practise.PractiseConstants
import java.util.*

class CardsFragment : Fragment() {

    private var dates: ArrayList<Date>? = null
    private lateinit var currentDate: Date
    private lateinit var mainTextView: TextView

    private  var type = 0
    private  var isDateQuestion = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val mainView = inflater.inflate(R.layout.fragment_cards, container, false)

        val saved = arguments
        type = saved!!.getInt(PractiseConstants.TYPE)
        dates = saved.getParcelableArrayList(PractiseConstants.DATES)

        when (type) {
            PractiseConstants.ONLY_DATES -> isDateQuestion = true
            PractiseConstants.ONLY_EVENTS -> isDateQuestion = false
        }

        initializeViews(mainView)
        setQuestion()

        return mainView
    }

    private fun initializeViews(mainView: View) {
        mainTextView = mainView.findViewById(R.id.date_cards_text)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            mainTextView.breakStrategy = Layout.BREAK_STRATEGY_SIMPLE
        val nextButton = mainView.findViewById<Button>(R.id.cards_next_date)
        nextButton.setOnClickListener { setQuestion() }
        val descriptionButton = mainView.findViewById<Button>(R.id.cards_description_date)
        descriptionButton.setOnClickListener { setAnswer() }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        //        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            Toast.makeText(getContext(), "landscape", Toast.LENGTH_SHORT).show();
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            Toast.makeText(getContext(), "portrait", Toast.LENGTH_SHORT).show();
//        }
        try {
            val ft = parentFragmentManager.beginTransaction()
            ft.detach(this).attach(this).commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setAnswer() {
        mainTextView.text = currentDate.date + "\n" + currentDate.event
    }

    private fun setQuestion() {
        setRandomDate()
        if (isDateQuestion) mainTextView.text = currentDate.date else mainTextView.text = currentDate.event
    }

    private fun setRandomDate() {
        val random = Random()
        val x = random.nextInt(dates!!.size)
        currentDate = dates!![x]
        if (type == PractiseConstants.MIXED) isDateQuestion = random.nextBoolean()
    }

    companion object {
        @JvmStatic
        fun newInstance(dates: ArrayList<Date>, type: Int): CardsFragment {
            val test = CardsFragment()
            val bundle = Bundle()
            bundle.putInt(PractiseConstants.TYPE, type)
            bundle.putParcelableArrayList(PractiseConstants.DATES, dates)
            test.arguments = bundle
            return test
        }
    }
}