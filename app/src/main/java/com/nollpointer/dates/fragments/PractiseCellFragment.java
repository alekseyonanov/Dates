package com.nollpointer.dates.fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appodeal.ads.Appodeal;
import com.flurry.android.FlurryAgent;
import com.nollpointer.dates.dialogs.CenturyPickDialog;
import com.nollpointer.dates.Date;
import com.nollpointer.dates.MainActivity;
import com.nollpointer.dates.R;
import com.nollpointer.dates.StartPosition;
import com.nollpointer.dates.adapters.PractiseCardsAdapter;
import com.nollpointer.dates.dialogs.TypePickDialog;

import java.util.ArrayList;
import java.util.Collections;

import static com.nollpointer.dates.MainActivity.EASY_DATES_MODE;
import static com.nollpointer.dates.MainActivity.FULL_DATES_MODE;
import static com.nollpointer.dates.constants.PractiseConstants.CARDS;
import static com.nollpointer.dates.constants.PractiseConstants.DISTRIBUTE;
import static com.nollpointer.dates.constants.PractiseConstants.SORT;
import static com.nollpointer.dates.constants.PractiseConstants.TEST;
import static com.nollpointer.dates.constants.PractiseConstants.TRUE_FALSE;


public class PractiseCellFragment extends Fragment implements PractiseCardsAdapter.Listener, CenturyPickDialog.NoticeDialogListener, TypePickDialog.Listener, StartPosition {
    RecyclerView recycler;
    private int pressedPosition;
    private MainActivity mMainActivity;
    private int type = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_cell_practise, container, false);
        recycler = (RecyclerView) mainView.findViewById(R.id.practise_recycler_view);
        PractiseCardsAdapter adapter = new PractiseCardsAdapter(getResources().getStringArray(R.array.practise_titles),
                getResources().getStringArray(R.array.practise_description),
                new int[]{R.drawable.ic_cards, R.drawable.ic_test, R.drawable.ic_true_false,
                R.drawable.ic_sort,R.drawable.ic_distribution},
                new int[]{R.drawable.ic_practise_background_cards,R.drawable.ic_practise_background_test,R.drawable.ic_practise_background_true_false,
                        R.drawable.ic_practise_background_sort,R.drawable.ic_practise_background_distribution});
        adapter.setListener(this);
        mMainActivity = (MainActivity) getActivity();
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(mMainActivity));

        return mainView;
    }

    public void onResume() {
        super.onResume();
//        mMainActivity.updateBottomNavigationView(R.id.navigation_dates);
//        mMainActivity.showBottomNavigationView();
//
//        if (mMainActivity.isFirstTime(PRACTISE))
//            new MaterialShowcaseView.Builder(mMainActivity)
//                    .setTarget(recycler)
//                    .setDelay(300)
//                    .setContentText(R.string.tutorial_practise)
//                    .setDismissText(R.string.got_it)
//                    .setDismissOnTouch(true)
//                    .setDismissTextColor(Color.GREEN)
//                    .setMaskColour(getResources().getColor(R.color.colorMask))
//                    .setShape(new NoShape())
//                    .show();
    }

    public void onClick(int position) {
        String practise;
        switch (position) {
            case 0:
                practise = CARDS;
                break;
            case 1:
                practise = TEST;
                break;
            case 2:
                practise = TRUE_FALSE;
                break;
            case 3:
                practise = SORT;
                break;
            case 4:
                practise = DISTRIBUTE;
                break;
            default:
                return;
        }
        mMainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, PractiseDetailsPickerFragment.newInstance(practise,mMainActivity.getMode())).addToBackStack(null).commit();

    }

    @Override
    public void typePicked(int type) {
        this.type = type;
        CenturyPickDialog centuries = new CenturyPickDialog();
        centuries.setListener(this);
        centuries.show(mMainActivity.getSupportFragmentManager(), Integer.toString(mMainActivity.getMode()));
    }

    @Override
    public void onButtonClicked(ArrayList<Integer> dialog) {
        if (dialog != null)
            startPractise(dialog);
    }

    public void startPractise(ArrayList<Integer> arrayList) {
        MainActivity mAc = mMainActivity;
        mAc.hideBottomNavigationView();
        Fragment fragment;
        String event;
        ArrayList<Date> dates = getListForPractise(arrayList);
        switch (pressedPosition) {
            case 0:
                fragment = CardsFragment.newInstance(dates, type);
                event = "CardsFragment";
                break;
            case 2:
                fragment = TestFragment.newInstance(dates, type, false);
                event = "Test";
                break;
            case 3:
                showAds();
                fragment = TestFragment.newInstance(dates, type, true);
                event = "Test_20";
                break;
            case 5:
                fragment = TrueFalseFragment.newInstance(dates, false);
                event = "TrueFalse";
                break;
            case 6:
                showAds();
                fragment = TrueFalseFragment.newInstance(dates, true);
                event = "TrueFalse_20";
                break;
            case 8:
                fragment = SortFragment.newInstance(dates, false);
                event = "Sort";
                break;
            case 9:
                showAds();
                fragment = SortFragment.newInstance(dates, true);
                event = "Sort_20";
                break;
            default:
                return;
        }
        FlurryAgent.logEvent(event);
        setFragmentToPractise(fragment);
    }

    private void setFragmentToPractise(Fragment fragment) {
        mMainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
    }

    public void showAds() {
        if (Appodeal.isLoaded(Appodeal.INTERSTITIAL))
            Appodeal.show(mMainActivity, Appodeal.INTERSTITIAL);
    }

    @Override
    public void goToStartPosition() {
        recycler.scrollToPosition(0);
    }

    private ArrayList<Date> getListForPractise(ArrayList<Integer> arrayList) {
        ArrayList<Date> dates = mMainActivity.getDates();
        ArrayList<Date> practiseList = new ArrayList<>();
        int mode = mMainActivity.getMode();
        if ((mode == FULL_DATES_MODE && arrayList.contains(10)) || (mode == EASY_DATES_MODE && arrayList.contains(2))) // Если выбраны все даты
            return dates;
        Pair<Integer, Integer> pair;
        Collections.sort(arrayList);
        for (Integer number : arrayList) {
            pair = getDatesRange(number, mode);
            practiseList.addAll(dates.subList(pair.first, pair.second));
        }
        return practiseList;
    }

    private Pair<Integer, Integer> getDatesRange(int pickedCentury, int mode) {
        int start = 0, end = 0;
        if (mode == FULL_DATES_MODE) {
            switch (pickedCentury) {
                case 0:
                    start = 0;
                    end = start + 21;
                    break;
                case 1:
                    start = 21;
                    end = start + 20;
                    break;
                case 2:
                    start = 41;
                    end = start + 35;
                    break;
                case 3:
                    start = 76;
                    end = start + 31;
                    break;
                case 4:
                    start = 107;
                    end = start + 40;
                    break;
                case 5:
                    start = 147;
                    end = start + 48;
                    break;
                case 6:
                    start = 195;
                    end = start + 48;
                    break;
                case 7:
                    start = 242;
                    end = start + 42;
                    break;
                case 8:
                    start = 284;
                    end = start + 50;
                    break;
                case 9:
                    start = 334;
                    end = start + 50;
                    break;
            }
        } else {
            switch (pickedCentury) {
                case 0:
                    start = 0;
                    end = start + 48;
                    break;
                case 1:
                    start = 48;
                    end = start + 47;
                    break;
            }
        }
        return new Pair<>(start, end);
    }
}