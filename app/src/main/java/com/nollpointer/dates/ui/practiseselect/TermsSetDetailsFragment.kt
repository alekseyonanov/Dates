package com.nollpointer.dates.ui.practiseselect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nollpointer.dates.R
import com.nollpointer.dates.model.Date
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.ui.activity.MainActivity
import com.nollpointer.dates.ui.cards.CardsFragment
import com.nollpointer.dates.ui.distribution.DistributionFragment
import com.nollpointer.dates.ui.link.LinkFragment
import com.nollpointer.dates.ui.sort.SortFragment
import com.nollpointer.dates.ui.test.TestFragment
import com.nollpointer.dates.ui.truefalse.TrueFalseFragment
import com.nollpointer.dates.ui.voice.VoiceFragment
import kotlinx.android.synthetic.main.fragment_set_details.*

class TermsSetDetailsFragment : Fragment() {

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_set_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDetailsToolbar.setNavigationOnClickListener {
            fragmentManager!!.popBackStack()
        }

        singleSelectAdapter = SingleSelectAdapter(resources.getTextArray(R.array.pick_type)).apply {
            onItemSelected = { isSelected ->
                isSingleItemSelected = isSelected
                setDetailsSelectButton.isEnabled = isSingleItemSelected && isMultiItemsSelected
            }
        }
        multiSelectAdapter = MutliSelectAdapter(resources.getTextArray(R.array.terms_titles))
                .apply {
                    onAnyItemSelected = { isSelected ->
                        isMultiItemsSelected = isSelected
                        setDetailsSelectButton.isEnabled = isSingleItemSelected && isMultiItemsSelected
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
                datesList.addAll(mainActivity.dates.subList(it.first, it.second))
            }

            practise.dates = datesList

            val fragment = when (practise.practise) {
                Practise.CARDS -> CardsFragment.newInstance(practise)
                Practise.VOICE -> VoiceFragment.newInstance(practise)
                Practise.TEST -> TestFragment.newInstance(practise)
                Practise.TRUE_FALSE -> TrueFalseFragment.newInstance(practise)
                Practise.LINK -> LinkFragment.newInstance(practise)
                Practise.SORT -> SortFragment.newInstance(practise)
                Practise.DISTRIBUTION -> DistributionFragment.newInstance(practise)
                else -> CardsFragment.newInstance(practise)
            }

            fragmentManager?.beginTransaction()?.replace(R.id.frameLayout, fragment)?.commit()
        }

    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity?)!!.hideBottomNavigationView()
    }

    private fun getDatesRange(centuries: List<Int>): List<Pair<Int, Int>> {
        val pairList = mutableListOf<Pair<Int, Int>>()
        centuries.forEach {
            if (mode == MainActivity.FULL_DATES_MODE)
                pairList.add(when (it) {
                    0 -> Pair(0, 21)
                    1 -> Pair(21, 41)
                    2 -> Pair(41, 85)
                    3 -> Pair(85, 118)
                    4 -> Pair(118, 160)
                    5 -> Pair(160, 209)
                    6 -> Pair(209, 256)
                    7 -> Pair(256, 298)
                    8 -> Pair(298, 348)
                    else -> Pair(348, 406)
                })
            else
                pairList.add(when (it) {
                    0 -> Pair(0, 48)
                    else -> Pair(48, 95)
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