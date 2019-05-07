package com.nollpointer.dates.distribute;


import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.appodeal.ads.Appodeal;
import com.nollpointer.dates.R;
import com.nollpointer.dates.other.PractiseHelpDialog;
import com.nollpointer.dates.other.PractiseSettingsDialog;

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


        ImageButton backButton = mainView.findViewById(R.id.testBackButton);
        final ImageButton settingsButton = mainView.findViewById(R.id.testSettingsButton);
        ImageButton helpButton = mainView.findViewById(R.id.testHelpButton);

        Appodeal.setBannerViewId(R.id.appodealBannerView);

        backButton.setImageResource(R.drawable.ic_arrow_back_white);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        settingsButton.setImageResource(R.drawable.ic_settings);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PractiseSettingsDialog settingsDialog = PractiseSettingsDialog.newInstance(900);
                settingsDialog.setListener(new PractiseSettingsDialog.Listener() {
                    @Override
                    public void onDelayPicked(int delay) {
                        //setDelay(delay);
                    }
                });
                settingsDialog.show(getActivity().getSupportFragmentManager(), null);
            }
        });

        helpButton.setImageResource(R.drawable.ic_help);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PractiseHelpDialog helpDialog = new PractiseHelpDialog();
                helpDialog.show(getActivity().getSupportFragmentManager(), null);
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
