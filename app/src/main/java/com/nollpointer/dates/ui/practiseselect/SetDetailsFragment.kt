package com.nollpointer.dates.ui.practiseselect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.nollpointer.dates.R
import com.nollpointer.dates.model.Date
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.model.Practise.Companion.CARDS
import com.nollpointer.dates.model.Practise.Companion.DISTRIBUTION
import com.nollpointer.dates.model.Practise.Companion.LINK
import com.nollpointer.dates.model.Practise.Companion.SORT
import com.nollpointer.dates.model.Practise.Companion.TEST
import com.nollpointer.dates.model.Practise.Companion.TRUE_FALSE
import com.nollpointer.dates.model.Practise.Companion.VOICE
import com.nollpointer.dates.ui.activity.MainActivity
import com.nollpointer.dates.ui.activity.MainActivity.Companion.FULL_DATES_MODE
import com.nollpointer.dates.ui.cards.CardsFragment
import com.nollpointer.dates.ui.distribution.DistributionFragment
import com.nollpointer.dates.ui.link.LinkFragment
import com.nollpointer.dates.ui.sort.SortFragment
import com.nollpointer.dates.ui.test.TestFragment
import com.nollpointer.dates.ui.truefalse.TrueFalseFragment
import com.nollpointer.dates.ui.view.BaseFragment
import com.nollpointer.dates.ui.voice.VoiceFragment
import kotlinx.android.synthetic.main.fragment_set_details.*

/**
 * @author Onanov Aleksey (@onanov)
 */
open class SetDetailsFragment : BaseFragment() {

    private lateinit var singleSelectAdapter: SingleSelectAdapter
    private lateinit var multiSelectAdapter: MutliSelectAdapter

    private var mode = 0

    private var isSingleItemSelected = false
    private var isMultiItemsSelected = false

    private lateinit var practise: Practise

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            practise = it.getParcelable<Practise>(PRACTISE) as Practise
        }
    }

    override fun getStatusBarColorRes() = R.color.colorPrimary

    override fun isStatusBarLight() = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_set_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDetailsToolbar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        singleSelectAdapter = SingleSelectAdapter(resources.getTextArray(R.array.pick_type)).apply {
            onItemHasSelected = { isSelected ->
                isSingleItemSelected = isSelected
                setDetailsSelectButton.isEnabled = isSingleItemSelected && isMultiItemsSelected
            }
        }
        multiSelectAdapter = MutliSelectAdapter(
                when (practise.mode) {
                    FULL_DATES_MODE -> resources.getTextArray(R.array.centuries_full)
                    else -> resources.getTextArray(R.array.centuries_easy)
                }
        ).apply {
            onAnyItemSelected = { isSelected ->
                isMultiItemsSelected = isSelected
                setDetailsSelectButton.isEnabled = isSingleItemSelected && isMultiItemsSelected
            }
            onItemsSelected = { list ->
                var count = 0
                getDatesRange(list).forEach {
                    count += it.last - it.first + 1
                }
                setDetailsCount.text = getString(R.string.date_count, count)
            }
        }

        setDetailsMultiRecyclerView.apply {
            adapter = multiSelectAdapter
            layoutManager = LinearLayoutManager(this.context)
            isNestedScrollingEnabled = false
        }

        setDetailsSingleRecyclerView.apply {
            adapter = singleSelectAdapter
            layoutManager = LinearLayoutManager(this.context)
            isNestedScrollingEnabled = false
        }

        setDetailsSelectButton.setOnClickListener {
            practise.isTestMode = setDetailsTestSwitch.isChecked
            practise.type = singleSelectAdapter.selectedItem

            val mainActivity = activity as MainActivity
            val centuries = multiSelectAdapter.selectedItems
            val datesList = mutableListOf<Date>()
            getDatesRange(centuries).forEach {
                datesList.addAll(mainActivity.dates.subList(it.first, it.last))
            }

            practise.dates = datesList

            val fragment = when (practise.practise) {
                CARDS -> CardsFragment.newInstance(practise)
                VOICE -> VoiceFragment.newInstance(practise)
                TEST -> TestFragment.newInstance(practise)
                TRUE_FALSE -> TrueFalseFragment.newInstance(practise)
                LINK -> LinkFragment.newInstance(practise)
                SORT -> SortFragment.newInstance(practise)
                DISTRIBUTION -> DistributionFragment.newInstance(practise)
                else -> CardsFragment.newInstance(practise)
            }

            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit()
        }

    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity?)!!.hideBottomNavigationView()
    }

    private fun getDatesRange(centuries: List<Int>): List<IntRange> {
        val pairList = mutableListOf<IntRange>()
        centuries.forEach {
            if (mode == FULL_DATES_MODE)
                pairList.add(when (it) {
                    0 -> IntRange(0, 21)
                    1 -> IntRange(22, 41)
                    2 -> IntRange(42, 85)
                    3 -> IntRange(86, 118)
                    4 -> IntRange(119, 160)
                    5 -> IntRange(161, 209)
                    6 -> IntRange(210, 256)
                    7 -> IntRange(257, 298)
                    8 -> IntRange(299, 348)
                    else -> IntRange(349, 405)
                })
            else
                pairList.add(when (it) {
                    0 -> IntRange(0, 48)
                    else -> IntRange(49, 94)
                })
        }
        return pairList
    }

    companion object {
        private const val PRACTISE = "Practise"

        fun newInstance(practise: Practise) =
                SetDetailsFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(PRACTISE, practise)
                    }
                }
    }
}