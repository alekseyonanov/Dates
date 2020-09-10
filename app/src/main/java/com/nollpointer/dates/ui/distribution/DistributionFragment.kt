package com.nollpointer.dates.ui.distribution

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.nollpointer.dates.R
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.ui.dialog.PractiseHelpDialog
import com.nollpointer.dates.ui.dialog.PractiseSettingsDialog

/**
 * @author Onanov Aleksey (@onanov)
 */
class DistributionFragment : Fragment() {

    private lateinit var practise: Practise

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            practise = it.getParcelable<Practise>(DISTRIBUTION) as Practise
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        val mainView = inflater.inflate(R.layout.fragment_distribution, container, false)
        val backButton = mainView.findViewById<ImageButton>(R.id.testBack)
        val settingsButton = mainView.findViewById<ImageButton>(R.id.testSettings)
        val helpButton = mainView.findViewById<ImageButton>(R.id.testHelp)
        //Appodeal.setBannerViewId(R.id.appodealBannerView)
        backButton.setImageResource(R.drawable.ic_arrow_back_white)
        backButton.setOnClickListener {
            fragmentManager!!.popBackStack()
        }
        settingsButton.setImageResource(R.drawable.ic_settings)
        settingsButton.setOnClickListener {
            val settingsDialog = PractiseSettingsDialog.newInstance(900)
            settingsDialog.setListener(object : PractiseSettingsDialog.Listener {
                override fun onDelayPicked(delay: Int) { //setDelay(delay);
                }
            })
            settingsDialog.show(activity!!.supportFragmentManager, null)
        }
        helpButton.setImageResource(R.drawable.ic_help)
        helpButton.setOnClickListener {
            val helpDialog = PractiseHelpDialog.newInstance(R.string.help_practise)
            helpDialog.show(activity!!.supportFragmentManager, null)
        }
        val image = mainView.findViewById<ImageView>(R.id.statistics_dummy_crane)
        image.setImageResource(R.drawable.ic_crane)
        val button = mainView.findViewById<Button>(R.id.statistics_dummy_button)
        button.setOnClickListener {
            val appPackageName = context!!.packageName // getPackageName() from Context or Activity object
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
            } catch (anfe: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
            }
        }
        //        RecyclerView recyclerView = mainView.findViewById(R.id.distribution_recycler_view);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(new DistributionCardsAdapter());
//
//
//        ItemTouchHelper.Callback callback =
//                new ItemSwipeTouchHelper(recyclerView);
//        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
//        touchHelper.attachToRecyclerView(recyclerView);
//
//        recyclerView.addItemDecoration(new SpacesItemDecoration(16));
        return mainView
    }

    companion object {

        private const val DISTRIBUTION = "Distribution"

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