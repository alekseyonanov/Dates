package com.nollpointer.dates.ui.distribution

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
import com.nollpointer.dates.databinding.FragmentDistributionBinding
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.model.PractiseInfo
import com.nollpointer.dates.ui.view.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author Onanov Aleksey (@onanov)
 */
@AndroidEntryPoint
class DistributionFragment : BaseFragment() {

    private var _binding: FragmentDistributionBinding? = null
    private val binding: FragmentDistributionBinding
        get() = _binding!!

    private val adapter = DistributionAdapter()

    private val viewModel by viewModels<DistributionViewModel>()

    override val statusBarColorRes = R.color.colorBackground

    override val isStatusBarLight = true

    override val isBottomNavigationViewHidden = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentDistributionBinding.inflate(inflater, container, false)

        binding.arrowBack.setOnClickListener {
            viewModel.onArrowBackClicked()
        }

        binding.settings.setOnClickListener {
            viewModel.onSettingsClicked()
        }

        binding.analyze.setOnClickListener {
            viewModel.onAnalyzeClicked()
        }

        binding.next.setOnClickListener {
            viewModel.onNextClicked()
        }

        binding.check.setOnClickListener {
            viewModel.onCheckClicked()
        }

        binding.recyclerView.apply {
            val callback: ItemTouchHelper.Callback = ItemSwipeTouchHelper().apply {
                onItemSwiped = viewModel::onItemSwiped
            }
            ItemTouchHelper(callback).attachToRecyclerView(this)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            adapter = this@DistributionFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.apply {
            practise = requireArguments().getParcelable<Practise>(DISTRIBUTION) as Practise
            controlsVisibilityLiveData.observe({ lifecycle }, ::showControls)
            infoLiveData.observe({ lifecycle }, ::setPractiseInfo)
            checkEnabilityLiveData.observe({ lifecycle }, ::setCheckEnable)
            questionsLiveData.observe({ lifecycle }, ::setQuestions)
            start()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showControls(isVisible: Boolean) {
        if (isVisible) {
            binding.analyze.visibility = View.VISIBLE
            binding.next.visibility = View.VISIBLE
        } else {
            binding.analyze.visibility = View.INVISIBLE
            binding.next.visibility = View.INVISIBLE
        }
    }

    private fun setPractiseInfo(practiseInfo: PractiseInfo) {
        binding.wrongAnswers.text = practiseInfo.wrongAnswers.toString()
        binding.rightAnswers.text = practiseInfo.rightAnswers.toString()
        binding.questionNumber.text = practiseInfo.questionNumber.toString()
    }

    private fun setCheckEnable(isEnabled: Boolean) {
        binding.check.isEnabled = isEnabled
    }

    private fun setQuestions(items: List<String>) {
        adapter.items = items.toMutableList()
    }

    companion object {
        private const val DISTRIBUTION = "Distribution"

        @JvmStatic
        fun newInstance(practise: Practise) =
                DistributionFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(DISTRIBUTION, practise)
                    }
                }
    }

    inner class ItemSwipeTouchHelper : ItemTouchHelper.Callback() {

        var onItemSwiped: ((String, Int) -> Unit)? = null

        override fun isItemViewSwipeEnabled(): Boolean {
            return true
        }

        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            return makeMovementFlags(0, swipeFlags)
        }

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            onItemSwiped?.invoke(adapter.items[viewHolder.adapterPosition], direction) //TODO продумать оптимальный вариант
            adapter.onItemDismiss(viewHolder.adapterPosition)
        }

    }

}