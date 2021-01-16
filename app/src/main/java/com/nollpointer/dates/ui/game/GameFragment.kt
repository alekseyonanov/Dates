package com.nollpointer.dates.ui.game

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.nollpointer.dates.R
import com.nollpointer.dates.ui.activity.MainActivity
import com.nollpointer.dates.ui.view.BaseFragment

/**
 * @author Onanov Aleksey (@onanov)
 */
class GameFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val mainView = inflater.inflate(R.layout.fragment_game, container, false)
        //        RecyclerView recyclerView = mainView.findViewById(R.id.game_recycler_view);
//        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3){
//
//        });
//        recyclerView.setAdapter(new GameCardsAdapter(new GameCardsAdapter.GameCardsListener() {
//            @Override
//            public void onGameStart() {
////                if(Appodeal.isLoaded(Appodeal.NON_SKIPPABLE_VIDEO))
////                    Appodeal.show(getActivity(),Appodeal.NON_SKIPPABLE_VIDEO);
//            }
//
//            @Override
//            public void onGameEnd(boolean result) {
//
//            }
//        }));
//
//        int spacingInPixels = 16;
//        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
//
//        TextView instructions = mainView.findViewById(R.id.game_instructions);
//        instructions.setText(Html.fromHtml("<li>Одна игра стоит один просмотр рекламы. Если вы выйдите из игры, не закончив ее, вам придется заново смотреть рекламу, чтобы попробовать заново</li>" +
//                "    <li>В каждой игре можно перевернуть 3 карты, после чего вы увидите, выиграли ли вы или нет. </li>" +
//                "    <li>Вы выигрываете, если получили 3 желтых звезды.</li>" +
//                "    <li>В случае выигрыша у вас отключается реклама на следующие 12 часов.</li>" +
//                "    <li>В игру можно играть сколько угодно, пока реклама прогружается</li>"));
//
        val toolbar: Toolbar = mainView.findViewById(R.id.game_toolbar)
        toolbar.setNavigationOnClickListener { requireActivity().supportFragmentManager.popBackStack() }
        val image = mainView.findViewById<ImageView>(R.id.statistics_dummy_crane)
        image.setImageResource(R.drawable.ic_crane)
        val button = mainView.findViewById<Button>(R.id.statistics_dummy_button)
        button.setOnClickListener {
            val appPackageName = requireContext().packageName // getPackageName() from Context or Activity object
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
            } catch (anfe: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
            }
        }
        return mainView
    }

    override fun getStatusBarColorRes() = R.color.colorPrimary

    override fun isStatusBarLight() = false

    override fun onStart() {
        super.onStart()
        val mainActivity = activity as MainActivity?
        mainActivity!!.hideBottomNavigationView()
    }

    override fun onStop() {
        super.onStop()
        val mainActivity = activity as MainActivity?
        mainActivity!!.showBottomNavigationView()
    }

    companion object {
        @JvmStatic
        fun newInstance() = GameFragment()
    }

    inner class SpacesItemDecoration(private val space: Int) : ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View,
                                    parent: RecyclerView, state: RecyclerView.State) {
            outRect.left = space / 2
            outRect.right = space / 2
            outRect.bottom = space
        }

    }
}