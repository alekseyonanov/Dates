package com.nollpointer.dates.game;


import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nollpointer.dates.activity.MainActivity;
import com.nollpointer.dates.R;


public class GameFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_game, container, false);

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
        Toolbar toolbar = mainView.findViewById(R.id.game_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        ImageView image = mainView.findViewById(R.id.statistics_dummy_crane);
        image.setImageResource(R.drawable.ic_crane);

        Button button = mainView.findViewById(R.id.statistics_dummy_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getContext().getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        return mainView;
    }

    @Override
    public void onStart() {
        super.onStart();
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.hideBottomNavigationView();
    }

    @Override
    public void onStop() {
        super.onStop();
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.showBottomNavigationView();
    }



    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = space/2;
            outRect.right = space/2;
            outRect.bottom = space;

        }
    }

}
