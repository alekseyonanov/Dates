package com.nollpointer.dates.ui.sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nollpointer.dates.R
import com.nollpointer.dates.databinding.FragmentSortBinding
import com.nollpointer.dates.model.Date
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.model.PractiseResult
import com.nollpointer.dates.ui.view.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

/**
 * @author Onanov Aleksey (@onanov)
 */
@AndroidEntryPoint
class SortFragment : BaseFragment() {

    private lateinit var dates: List<Date>
    private var adapter = SortCardsAdapter()
    private lateinit var correctAnswerSequence: List<Int>

    private var isTestMode = false
    private var isLocked = false

    private val practiseResults = ArrayList<PractiseResult>()

    private var _binding: FragmentSortBinding? = null
    private val binding: FragmentSortBinding
        get() = _binding!!

    override val statusBarColorRes = R.color.colorBackground

    override val isStatusBarLight = true

    override val isBottomNavigationViewHidden = true

    private val listener = View.OnClickListener {
        if (isLocked) return@OnClickListener
        lockAnswerButtons()
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
                              savedInstanceState: Bundle?): View {

        _binding = FragmentSortBinding.inflate(inflater, container, false)

        binding.recyclerView.apply {
            val callback: ItemTouchHelper.Callback = SortItemTouchHelperCallback()
            ItemTouchHelper(callback).attachToRecyclerView(this)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            adapter = this@SortFragment.adapter
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
        }

        //Appodeal.setBannerViewId(R.id.appodealBannerView_sort)

        binding.check.setOnClickListener(listener)
        binding.arrowBack.setOnClickListener { requireActivity().supportFragmentManager.popBackStack() }
        binding.settings.setOnClickListener {

        }

        generateAndSetInfo()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun generateAndSetInfo() {
        val initialDates = generateDatesList(3)
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

    private fun setInfo(answerDates: List<Date>) {
        adapter.dates = answerDates
        adapter.notifyDataSetChanged()
    }

    private fun getSequence(initial: List<Date?>, mixed: List<Date?>): List<Int> {
        val list = ArrayList<Int>()
        for (date in mixed) {
            val number = initial.indexOf(date)
            list.add(number)
        }
        return list
    }

    private fun generateDatesList(count: Int): List<Date> {
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

    private fun getShuffledDatesList(datesList: List<Date>): List<Date> {
        val list = ArrayList(datesList)
        list.shuffle()
        return list
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