package com.nollpointer.dates.ui.practiseselect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.nollpointer.dates.R
import com.nollpointer.dates.annotation.*
import com.nollpointer.dates.databinding.FragmentSetDetailsBinding
import com.nollpointer.dates.model.Date
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.ui.activity.MainActivity
import com.nollpointer.dates.ui.cards.CardsFragment
import com.nollpointer.dates.ui.distribution.DistributionFragment
import com.nollpointer.dates.ui.link.LinkFragment
import com.nollpointer.dates.ui.sort.SortFragment
import com.nollpointer.dates.ui.test.TestFragment
import com.nollpointer.dates.ui.truefalse.TrueFalseFragment
import com.nollpointer.dates.ui.view.BaseFragment
import com.nollpointer.dates.ui.voice.VoiceFragment

/**
 * Экран "Детали практики по датам"
 *
 * @author Onanov Aleksey (@onanov)
 */
open class SetDetailsFragment : BaseFragment() {

    private lateinit var singleSelectAdapter: SingleSelectAdapter
    private lateinit var multiSelectAdapter: MutliSelectAdapter

    private var mode = 0

    private var isSingleItemSelected = false
    private var isMultiItemsSelected = false

    private lateinit var practise: Practise

    private var _binding: FragmentSetDetailsBinding? = null
    private val binding: FragmentSetDetailsBinding
        get() = _binding!!

    override val statusBarColorRes = R.color.colorPrimary

    override val isStatusBarLight = false

    override val isBottomNavigationViewHidden = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            practise = it.getParcelable<Practise>(PRACTISE) as Practise
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentSetDetailsBinding.inflate(inflater, container, false)

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        singleSelectAdapter = SingleSelectAdapter(resources.getTextArray(R.array.pick_type)).apply {
            onItemHasSelected = { isSelected ->
                isSingleItemSelected = isSelected
                binding.select.isEnabled = isSingleItemSelected && isMultiItemsSelected
            }
        }
        multiSelectAdapter = MutliSelectAdapter(
                when (practise.mode) {
                    FULL -> resources.getTextArray(R.array.centuries_full)
                    else -> resources.getTextArray(R.array.centuries_easy)
                }
        ).apply {
            onAnyItemSelected = { isSelected ->
                isMultiItemsSelected = isSelected
                binding.select.isEnabled = isSingleItemSelected && isMultiItemsSelected
            }
            onItemsSelected = { list ->
                var count = 0
                getDatesRange(list).forEach {
                    count += it.last - it.first + 1
                }
                binding.count.text = getString(R.string.date_count, count)
            }
        }

        binding.recyclerViewMulti.apply {
            adapter = multiSelectAdapter
            layoutManager = LinearLayoutManager(this.context)
            isNestedScrollingEnabled = false
        }

        binding.recyclerViewType.apply {
            adapter = singleSelectAdapter
            layoutManager = LinearLayoutManager(this.context)
            isNestedScrollingEnabled = false
        }

        binding.select.setOnClickListener {
            practise.isTestMode = binding.switchMode.isChecked
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

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getDatesRange(centuries: List<Int>): List<IntRange> {
        val pairList = mutableListOf<IntRange>()
        centuries.forEach {
            if (mode == FULL)
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

        @JvmStatic
        fun newInstance(practise: Practise) =
                SetDetailsFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(PRACTISE, practise)
                    }
                }
    }
}