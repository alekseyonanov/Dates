package com.nollpointer.dates.ui.distribution

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.nollpointer.dates.R
import com.nollpointer.dates.databinding.FragmentDistributionBinding
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.ui.practise.PractiseSettingsFragment
import com.nollpointer.dates.ui.view.BaseFragment

/**
 * @author Onanov Aleksey (@onanov)
 */
class DistributionFragment : BaseFragment() {

    private lateinit var practise: Practise

    private var _binding: FragmentDistributionBinding? = null
    private val binding: FragmentDistributionBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            practise = it.getParcelable<Practise>(DISTRIBUTION) as Practise
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View { // Inflate the layout for this fragment

        _binding = FragmentDistributionBinding.inflate(inflater, container, false)

        binding.arrowBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.settings.setOnClickListener {
            requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frameLayout, PractiseSettingsFragment.newInstance(practise))
                    .addToBackStack(null)
                    .commit()
        }

        return binding.root
    }

    override fun getStatusBarColorRes() = R.color.colorBackground

    override fun isStatusBarLight() = true

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    inner class ItemSwipeTouchHelper(var recyclerView: RecyclerView) : ItemTouchHelper.Callback() {
        override fun isItemViewSwipeEnabled(): Boolean {
            return true
        }

        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            return makeMovementFlags(0, swipeFlags)
        }

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder): Boolean { //((StatisticsCardsAdapter) recyclerView.getAdapter()).onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            (recyclerView.adapter as DistributionCardsAdapter?)!!.onItemDismiss(viewHolder.adapterPosition)
        }

    }

    inner class SpacesItemDecoration(space: Int) : ItemDecoration() {
        private val halfSpace = space / 2
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) { //            if (parent.getPaddingLeft() != halfSpace) {
//                parent.setPadding(halfSpace, halfSpace, halfSpace, halfSpace);
//                parent.setClipToPadding(false);
//            }
            outRect.top = halfSpace
            outRect.bottom = halfSpace
            //            outRect.left = halfSpace;
//            outRect.right = halfSpace;
        }
    }
}