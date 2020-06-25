package com.nollpointer.dates.statistics

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.nollpointer.dates.R

class StatisticsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        val mainView = inflater.inflate(R.layout.fragment_statistics, container, false)
        val recyclerView: RecyclerView = mainView.findViewById(R.id.statistics_recycler_view)
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        recyclerView.layoutManager = staggeredGridLayoutManager
        recyclerView.adapter = StatisticsCardsAdapter()
        val callback: ItemTouchHelper.Callback = SimpleItemTouchHelperCallback()
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recyclerView)
        recyclerView.addItemDecoration(SpacesItemDecoration(16))
        //        ImageView image = mainView.findViewById(R.id.statistics_dummy_crane);
//        image.setImageResource(R.drawable.ic_crane);
//
//        Button button = mainView.findViewById(R.id.statistics_dummy_button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final String appPackageName = getContext().getPackageName(); // getPackageName() from Context or Activity object
//                try {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
//                } catch (android.content.ActivityNotFoundException anfe) {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
//                }
//            }
//        });
        return mainView
    }

    inner class SimpleItemTouchHelperCallback : ItemTouchHelper.Callback() {
        override fun isLongPressDragEnabled(): Boolean {
            return true
        }

        //        @Override
//        public boolean isItemViewSwipeEnabled() {
//            return true;
//        }
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            return makeMovementFlags(dragFlags, 0)
        }

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder): Boolean {
            (recyclerView.adapter as StatisticsCardsAdapter?)!!.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
    }

    inner class SpacesItemDecoration(space: Int) : ItemDecoration() {

        private val halfSpace = space / 2

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.top = halfSpace
            outRect.bottom = halfSpace
            outRect.left = halfSpace
            outRect.right = halfSpace
        }

    }
}