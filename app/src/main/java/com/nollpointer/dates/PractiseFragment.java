package com.nollpointer.dates;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.appodeal.ads.Appodeal;

import java.util.ArrayList;

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
        mMainActivity.getSupportActionBar().setTitle(R.string.title_tests);
        mMainActivity.updateBottomNavigationView(R.id.navigation_tests);
        if(!mMainActivity.getSupportActionBar().isShowing()) {
            mMainActivity.show_bottom_navigation_view();
            mMainActivity.getSupportActionBar().show();
            mMainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
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
            startPractise(dialog,type);
    }

    public void setStartPosition(){
        recycler.scrollToPosition(0);
    }

    public void startPractise(ArrayList<Integer> arrayList,int type){
        MainActivity mAc = mMainActivity;
        mAc.hide_bottom_navigation_view();
        Fragment fragment;
        switch (pressedPosition){
            case 0:
                fragment = new CardsShowDown().setCenturies(arrayList,type,mAc.getMode());
                break;
            case 2:
                fragment = new TestFragment().setCenturies(arrayList,type,mAc.getMode(),true);
                break;
            case 3:
                showAds();
                fragment = new TestFragment().setCenturies(arrayList,type,mAc.getMode(),false);
                break;
            case 5:
                fragment = new TrueFalseFragment().setCenturies(arrayList,type,mAc.getMode(),true);
                break;
            case 6:
                showAds();
                fragment = new TrueFalseFragment().setCenturies(arrayList,type,mAc.getMode(),false);
                break;
            case 8:
                fragment = new SortFragment().setCenturies(arrayList,type,mAc.getMode(),true);
                break;
            case 9:
                showAds();
                fragment = new SortFragment().setCenturies(arrayList,type,mAc.getMode(),false);
                break;
            default:
                return;
        }
        setFragmentToPractise(fragment);
    }

    private void setFragmentToPractise(Fragment fragment){
        mMainActivity.getFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
    }

    public void showAds(){
        if(Appodeal.isLoaded(Appodeal.INTERSTITIAL))
            Appodeal.show(mMainActivity,Appodeal.INTERSTITIAL);
    }

    @Override
    public void goToStartPosition() {
        recycler.scrollToPosition(0);
    }


}