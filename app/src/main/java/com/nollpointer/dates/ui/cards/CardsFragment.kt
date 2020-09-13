package com.nollpointer.dates.ui.cards

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nollpointer.dates.R
import com.nollpointer.dates.model.Date
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.model.Practise.Companion.TYPE_MIXED
import com.nollpointer.dates.other.PractiseConstants
import com.nollpointer.dates.ui.practise.PractiseSettingsFragment
import com.nollpointer.dates.ui.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_cards.*
import java.util.*

/**
 * @author Onanov Aleksey (@onanov)
 */
class CardsFragment : BaseFragment() {

    private lateinit var dates: List<Date>
    private lateinit var currentDate: Date

    private var type = 0
    private var isDateQuestion = false

    private val random = Random(System.currentTimeMillis())

    private lateinit var practise: Practise

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            practise = it.getParcelable<Practise>(CARDS) as Practise
            type = practise.type
            dates = practise.dates
        }
    }

    override fun getStatusBarColorRes() = android.R.color.white

    override fun isStatusBarLight() = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cards, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            cardsText.breakStrategy = Layout.BREAK_STRATEGY_SIMPLE
        cardsNextButton.setOnClickListener { setQuestion() }
        cardsDescriptionButton.setOnClickListener { setAnswer() }

        isDateQuestion = when (type) {
            PractiseConstants.ONLY_DATES -> true
            else -> false
        }

        cardsBack.setOnClickListener {
            fragmentManager?.popBackStack()
        }

        cardsSettings.setOnClickListener {
            fragmentManager?.beginTransaction()?.replace(R.id.frameLayout, PractiseSettingsFragment.newInstance(practise))?.addToBackStack(null)?.commit()
        }

        setQuestion()

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        try {
            val ft = parentFragmentManager.beginTransaction()
            ft.detach(this).attach(this).commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setAnswer() {
        cardsText.text =
                if (currentDate.containsMonth) {
                    "${currentDate.date}, ${currentDate.month}\n${currentDate.event}"
                } else {
                    "${currentDate.date}\n${currentDate.event}"
                }
    }

    private fun setQuestion() {
        setRandomDate()
        if (isDateQuestion) {
            cardsText.text =
                    if (currentDate.containsMonth) {
                        "${currentDate.date}, ${currentDate.month}"
                    } else {
                        currentDate.date
                    }
        } else {
            cardsText.text = currentDate.event
        }
    }

    private fun setRandomDate() {
        val random = Random()
        val position = random.nextInt(dates.size)
        currentDate = dates[position]
        if (type == TYPE_MIXED)
            isDateQuestion = random.nextBoolean()
    }

    companion object {
        private const val CARDS = "Cards"

        @JvmStatic
        fun newInstance(practise: Practise) = CardsFragment().apply {
            arguments = Bundle(1).apply {
                putParcelable(CARDS, practise)
            }
        }
    }
}