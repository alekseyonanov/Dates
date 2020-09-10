package com.nollpointer.dates.ui.sort

import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appodeal.ads.Appodeal
import com.google.android.material.chip.Chip
import com.nollpointer.dates.R
import com.nollpointer.dates.model.Date
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.model.PractiseResult
import com.nollpointer.dates.ui.dialog.PractiseHelpDialog
import com.nollpointer.dates.ui.dialog.PractiseSettingsDialog
import com.nollpointer.dates.ui.practiseresult.PractiseResultFragment
import java.util.*

/**
 * @author Onanov Aleksey (@onanov)
 */
class SortFragment : Fragment() {

    private lateinit var dates: List<Date>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SortCardsAdapter
    private lateinit var questionNumberChip: Chip
    private lateinit var rightAnswersChip: Chip
    private lateinit var wrongAnswersChip: Chip
    private lateinit var correctAnswerSequence: List<Int>

    private var isTestMode = false
    private var difficulty = 0
    private var isLocked = false

    private val practiseResults = ArrayList<PractiseResult>()
    private var delay = 900

    private val listener = View.OnClickListener {
        if (isLocked) return@OnClickListener
        lockAnswerButtons()
        var rightAnswersCount = rightAnswersChip.text.toString().toInt()
        var wrongAnswersCount = wrongAnswersChip.text.toString().toInt()
        val sequence = adapter.answerSequence
        Log.e("TAG", "onClick: $sequence")
        val isCorrect = sequence == correctAnswerSequence
        if (isCorrect) rightAnswersCount++ else wrongAnswersCount++
        showCorrectCards(correctAnswerSequence)
//        if (isTestMode) {
//            val practiseResult = PractiseResult(sequence.toString(), isCorrect)
//            practiseResults.add(practiseResult)
//        }
        if (rightAnswersCount + wrongAnswersCount == 20 && isTestMode) fragmentManager!!.beginTransaction().replace(R.id.frameLayout, PractiseResultFragment.newInstance(SORT, practiseResults, arguments as Bundle)).commit()
        rightAnswersChip.text = rightAnswersCount.toString()
        wrongAnswersChip.text = wrongAnswersCount.toString()
        Handler().postDelayed({ generateAndSetInfo() }, delay.toLong())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val practise = it.getParcelable<Practise>(SORT) as Practise
            isTestMode = practise.isTestMode
            dates = practise.dates
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val mainView = inflater.inflate(R.layout.fragment_sort, container, false)

        delay = getDelay()
        initializeViews(mainView)
        //generateAndSetInfo()
        return mainView
    }

    private fun initializeViews(mainView: View) {
        Appodeal.setBannerViewId(R.id.appodealBannerView_sort)
        recyclerView = mainView.findViewById(R.id.sort_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val callback: ItemTouchHelper.Callback = SortItemTouchHelperCallback()
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recyclerView)
        adapter = SortCardsAdapter()
        adapter.itemCount = 3 + difficulty
        recyclerView.adapter = adapter
        val checkButton = mainView.findViewById<Button>(R.id.sort_check)
        checkButton.setOnClickListener(listener)
        val backButton = mainView.findViewById<ImageButton>(R.id.sort_back_button)
        val settingsButton = mainView.findViewById<ImageButton>(R.id.sort_settings_button)
        val helpButton = mainView.findViewById<ImageButton>(R.id.sort_help_button)
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
            val helpDialog = PractiseHelpDialog.newInstance(R.string.help_sort)
            helpDialog.show(activity!!.supportFragmentManager, null)
        }
        questionNumberChip = mainView.findViewById(R.id.sortQuestionNumber)
        rightAnswersChip = mainView.findViewById(R.id.sortRightAnswers)
        wrongAnswersChip = mainView.findViewById(R.id.sortWrongAnswers)
    }

    private fun generateAndSetInfo() {
        val initialDates = generateDatesList(3 + difficulty)
        val mixedDates = getShuffledDatesList(initialDates)
        correctAnswerSequence = getSequence(initialDates, mixedDates)
        setInfo(mixedDates)
        unlockAnswerButtons()
    }

    private fun lockAnswerButtons() {
        isLocked = true
    }

    private fun unlockAnswerButtons() {
        isLocked = false
    }

    private fun setInfo(answerDates: List<Date?>) {
        adapter.setDates(answerDates as List<Date>)
        adapter.notifyDataSetChanged()
        val rightAnswersCount = rightAnswersChip.text.toString().toInt()
        val wrongAnswersCount = wrongAnswersChip.text.toString().toInt()
        questionNumberChip.text = "#" + (rightAnswersCount + wrongAnswersCount + 1).toString()
    }

    private fun getSequence(initial: List<Date?>, mixed: List<Date?>): List<Int> {
        val list = ArrayList<Int>()
        for (date in mixed) {
            val number = initial.indexOf(date)
            list.add(number)
        }
        return list
    }

    private fun showCorrectCards(rightSequence: List<Int>?) {
        for (i in 0 until difficulty + 3) {
            val view = recyclerView.findViewHolderForAdapterPosition(i)!!.itemView
            val imageView = view.findViewById<ImageView>(R.id.sortImage)
            val viewById = view.findViewById<View>(R.id.sortTextNumber) as TextView
            imageView.visibility = View.VISIBLE
            if (rightSequence!![i] + 1 == viewById.text.toString().toInt()) imageView.setImageResource(R.drawable.ic_correct) else imageView.setImageResource(R.drawable.ic_mistake)
        }
    }

    private fun generateDatesList(count: Int): List<Date?> {
        val list = ArrayList<Date>()
        val random = Random(System.currentTimeMillis())
        var i = 0
        while (i < count) {
            val date = dates[random.nextInt(dates.size)]
            if (list.contains(date) || date.isContinuous) { //TODO даты с одинаковым годом
                i--
                i++
                continue
            } else list.add(date)
            i++
        }
        list.sort()
        return list
    }

    private fun getShuffledDatesList(datesList: List<Date?>): List<Date?> {
        val list = ArrayList(datesList)
        list.shuffle()
        return list
    }

    private fun getDelay(): Int {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getInt("sort delay", 900)
    }

    private fun setDelay(delay: Int) {
        this.delay = delay
        val preferences = PreferenceManager.getDefaultSharedPreferences(context).edit()
        preferences.putInt("sort delay", delay)
        preferences.apply()
    }

    override fun onStop() {
        super.onStop()
        Appodeal.hide(activity!!, Appodeal.BANNER_VIEW)
    }

    override fun onStart() {
        super.onStart()
        Appodeal.show(activity!!, Appodeal.BANNER_VIEW)
    }

    internal inner class SortItemTouchHelperCallback : ItemTouchHelper.Callback() {
        override fun isLongPressDragEnabled(): Boolean {
            return true
        }

        //        @Override
//        public boolean isItemViewSwipeEnabled() {
//            return true;
//        }
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            return makeMovementFlags(dragFlags, 0)
        }

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder): Boolean {
            (recyclerView.adapter as SortCardsAdapter?)!!.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
    }

    companion object {

        private const val SORT = "Sort"

        fun newInstance(practise: Practise) =
                SortFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(SORT, practise)
                    }
                }
    }
}