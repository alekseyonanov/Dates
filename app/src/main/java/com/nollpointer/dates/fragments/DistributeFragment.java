package com.nollpointer.dates.fragments;


import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nollpointer.dates.R;
import com.nollpointer.dates.adapters.DistributionCardsAdapter;
import com.nollpointer.dates.adapters.StatisticsCardsAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class DistributeFragment extends Fragment {


    public DistributeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View mainView = inflater.inflate(R.layout.fragment_distribute, container, false);

        RecyclerView recyclerView = mainView.findViewById(R.id.distribution_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new DistributionCardsAdapter());


        ItemTouchHelper.Callback callback =
                new ItemSwipeTouchHelper(recyclerView);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new SpacesItemDecoration(16));


        return mainView;
    }




    public class ItemSwipeTouchHelper extends ItemTouchHelper.Callback {

        RecyclerView recyclerView;

        public ItemSwipeTouchHelper(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int swipeFlags =ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            return makeMovementFlags(0, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                              RecyclerView.ViewHolder target) {
            //((StatisticsCardsAdapter) recyclerView.getAdapter()).onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            ((DistributionCardsAdapter) recyclerView.getAdapter()).onItemDismiss(viewHolder.getAdapterPosition());

        }

    }


    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        private int halfSpace;

        public SpacesItemDecoration(int space) {
            this.halfSpace = space / 2;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

//            if (parent.getPaddingLeft() != halfSpace) {
//                parent.setPadding(halfSpace, halfSpace, halfSpace, halfSpace);
//                parent.setClipToPadding(false);
//            }

            outRect.top = halfSpace;
            outRect.bottom = halfSpace;
//            outRect.left = halfSpace;
//            outRect.right = halfSpace;
        }
    }

}
