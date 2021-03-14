package com.nollpointer.dates.ui.sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nollpointer.dates.R
import com.nollpointer.dates.databinding.FragmentSortBinding
import com.nollpointer.dates.model.Date
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.ui.test.model.ResultModel
import com.nollpointer.dates.ui.view.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

/**
 * @author Onanov Aleksey (@onanov)
 */
@AndroidEntryPoint
class SortFragment : BaseFragment() {


    private var _binding: FragmentSortBinding? = null
    private val binding: FragmentSortBinding
        get() = _binding!!

    private val adapter = SortCardsAdapter()

    private val viewModel by viewModels<SortViewModel>()

    override val statusBarColorRes = R.color.colorBackground

    override val isStatusBarLight = true

    override val isBottomNavigationViewHidden = true

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentSortBinding.inflate(inflater, container, false)

        binding.recyclerView.apply {
            ItemTouchHelper(SortItemTouchHelperCallback()).attachToRecyclerView(this)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            this@SortFragment.adapter.answerClickListener = viewModel::onAnswerClicked
            adapter = this@SortFragment.adapter
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
        }

        //Appodeal.setBannerViewId(R.id.appodealBannerView_sort)

        binding.check.setOnClickListener {
            viewModel.onCheckClicked()
        }
        binding.arrowBack.setOnClickListener {
            viewModel.onArrowBackClicked()
        }
        binding.settings.setOnClickListener {
            viewModel.onSettingsClicked()
        }
        binding.next.setOnClickListener {
            viewModel.onNextClicked()
        }
        binding.analyze.setOnClickListener {
            viewModel.onAnalyzeClicked()
        }

        viewModel.apply {
            practise = requireArguments().getParcelable<Practise>(SORT) as Practise
            controlButtonsVisibilityLiveData.observe({ lifecycle }, ::showControlButtons)
            rightWrongAnswersLiveData.observe({ lifecycle }, ::setRightWrongAnswers)
            questionLiveData.observe({ lifecycle }, ::setQuestion)
            resultsLiveData.observe({ lifecycle }, ::setResult)
            start()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showControlButtons(isVisible: Boolean) {
        if (isVisible) {
            binding.next.show()
            binding.analyze.show()
        } else {
            binding.next.hide()
            binding.analyze.hide()
        }
    }

    private fun setRightWrongAnswers(pair: Pair<Int, Int>) {
        binding.rightAnswers.text = pair.first.toString()
        binding.wrongAnswers.text = pair.second.toString()
        binding.questionNumber.text = (pair.first + pair.second + 1).toString()
    }

    private fun setResult(result: ResultModel) {

    }

    private fun setQuestion(answerDates: List<Date>) {
        adapter.dates = answerDates
        adapter.notifyDataSetChanged()
    }

    internal inner class SortItemTouchHelperCallback : ItemTouchHelper.Callback() {
        override fun isLongPressDragEnabled(): Boolean {
            return true
        }

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

        @JvmStatic
        fun newInstance(practise: Practise) =
                SortFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(SORT, practise)
                    }
                }
    }
}