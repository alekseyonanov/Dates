package com.nollpointer.dates.fragments;


import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nollpointer.dates.MainActivity;
import com.nollpointer.dates.R;
import com.nollpointer.dates.adapters.GameCardsAdapter;


public class GameFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_game, container, false);

        RecyclerView recyclerView = mainView.findViewById(R.id.game_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3){

        });
        recyclerView.setAdapter(new GameCardsAdapter());

        int spacingInPixels = 16;
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        TextView instructions = mainView.findViewById(R.id.game_instructions);
        instructions.setText(Html.fromHtml("<li>создание пунктуальности (никогда не будете никуда опаздывать);</li>" +
                "    <li>излечение от пунктуальности (никогда никуда не будете торопиться);</li>" +
                "    <li>изменение восприятия времени и часов.</li>"));

        Toolbar toolbar = mainView.findViewById(R.id.game_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
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
