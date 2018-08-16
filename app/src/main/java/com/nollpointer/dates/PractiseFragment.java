package com.nollpointer.dates;


import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appodeal.ads.Appodeal;
import com.flurry.android.FlurryAgent;

import java.util.ArrayList;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.shape.NoShape;

import static com.nollpointer.dates.MainActivity.EASY_DATES_MODE;
import static com.nollpointer.dates.MainActivity.FULL_DATES_MODE;
import static com.nollpointer.dates.MainActivity.GDPR_SHOW;
import static com.nollpointer.dates.MainActivity.PRACTISE;


public class PractiseFragment extends Fragment implements TestMenuCardsAdapter.Listener, CenturyPickDialog.NoticeDialogListener, TypePickDialog.Listener, StartPosition{
    RecyclerView recycler;
    private int pressedPosition;
    private MainActivity mMainActivity;
    private int type = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recycler = (RecyclerView) inflater.inflate(R.layout.fragment_practise, container, false);
        TestMenuCardsAdapter adapter = new TestMenuCardsAdapter(getResources().getStringArray(R.array.tests),
                getResources().getStringArray(R.array.tests_description),new int[]{R.mipmap.ic_dates_cards_round,-1,R.mipmap.ic_tests_round,R.mipmap.ic_tests_real_round,-1,R.mipmap.ic_true_false_inf_round,R.mipmap.ic_true_false_real_round,-1,R.mipmap.ic_sort_infinite_round,R.mipmap.ic_sort_real_round});
        adapter.setListener(this);
        mMainActivity = (MainActivity)getActivity();
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(mMainActivity));
        return recycler;
    }

    public void onResume() {
        super.onResume();
        mMainActivity.changeToolbarItemsVisibility(false,false);
        mMainActivity.setActionBarTitle(R.string.title_tests);
        mMainActivity.updateBottomNavigationView(R.id.navigation_tests);
        if(!mMainActivity.isActionBarShowing()) {
            mMainActivity.show_bottom_navigation_view();
            mMainActivity.showActionBar();
            mMainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if(mMainActivity.isFirstTime(GDPR_SHOW))
            mMainActivity.startGDPR();

        if(mMainActivity.isFirstTime(PRACTISE))
            new MaterialShowcaseView.Builder(mMainActivity)
                    .setTarget(recycler)
                    .setDelay(300)
                    .setContentText(R.string.tutorial_practise)
                    .setDismissText(R.string.got_it)
                    .setDismissOnTouch(true)
                    .setDismissTextColor(Color.GREEN)
                    .setMaskColour(getResources().getColor(R.color.colorMask))
                    .setShape(new NoShape())
                    .show();
    }

    public void onClick(int position){
        pressedPosition = position;
        if(position == 5 || position == 6 || position == 8 || position==9) {
            typePicked(1);
        }else {
            TypePickDialog dialog = new TypePickDialog();
            dialog.setListener(this);
            dialog.show(mMainActivity.getSupportFragmentManager(), null);
        }
    }

    @Override
    public void typePicked(int type) {
        this.type = type;
        CenturyPickDialog centuries = new  CenturyPickDialog();
        centuries.setListener(this);
        centuries.show(mMainActivity.getSupportFragmentManager(),Integer.toString(mMainActivity.getMode()));
    }

    @Override
    public void onButtonClicked(ArrayList<Integer> dialog) {
        if(dialog != null)
            startPractise(dialog);
    }

    public void startPractise(ArrayList<Integer> arrayList){
        MainActivity mAc = mMainActivity;
        mAc.hide_bottom_navigation_view();
        mAc.hideActionBar();
        Fragment fragment;
        String event;
        ArrayList<Date> dates = getListForPractise(arrayList);
        switch (pressedPosition){
            case 0:
                fragment = CardsFragment.newInstance(dates,type);
                event = "CardsFragment";
                break;
            case 2:
                fragment = TestFragment.newInstance(dates,type,false);
                event = "Test";
                break;
            case 3:
                showAds();
                fragment = TestFragment.newInstance(dates,type,true);
                event = "Test_20";
                break;
            case 5:
                fragment = TrueFalseFragment.newInstance(dates,false);
                event = "TrueFalse";
                break;
            case 6:
                showAds();
                fragment = TrueFalseFragment.newInstance(dates,true);
                event = "TrueFalse_20";
                break;
            case 8:
                fragment = SortFragment.newInstance(dates,false);
                event = "Sort";
                break;
            case 9:
                showAds();
                fragment = SortFragment.newInstance(dates,true);
                event = "Sort_20";
                break;
            default:
                return;
        }
        FlurryAgent.logEvent(event);
        setFragmentToPractise(fragment);
    }

    private void setFragmentToPractise(Fragment fragment){
        mMainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
    }

    public void showAds(){
        if(Appodeal.isLoaded(Appodeal.INTERSTITIAL))
            Appodeal.show(mMainActivity,Appodeal.INTERSTITIAL);
    }

    @Override
    public void goToStartPosition() {
        recycler.scrollToPosition(0);
    }

    private ArrayList<Date> getListForPractise(ArrayList<Integer> arrayList){
        ArrayList<Date> dates = mMainActivity.getDateList();
        ArrayList<Date> practiseList = new ArrayList<>();
        int mode = mMainActivity.getMode();

        if((mode == FULL_DATES_MODE && arrayList.contains(10)) || (mode == EASY_DATES_MODE && arrayList.contains(2))) // Если выбраны все даты
            return dates;
        Pair<Integer,Integer> pair;
        for(Integer number: arrayList){
            pair = getDatesRange(number,mode);
            practiseList.addAll(dates.subList(pair.first,pair.second));
        }
        return practiseList;
    }

    private Pair<Integer,Integer> getDatesRange(int pickedCentury,int mode){
        int start = 0,end = 0;
        if(mode == FULL_DATES_MODE){
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
        }else{
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
        return new Pair<>(start,end);
    }
}