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

public class PractiseFragment extends Fragment implements TestMenuCardsAdapter.Listener{
    RecyclerView rc;
    private int pressedPosition;
    private Runnable rnb;
    private final Handler h = new Handler();
    private MainActivity mMainActivity;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rc = (RecyclerView) inflater.inflate(R.layout.fragment_practise, container, false);
        TestMenuCardsAdapter t = new TestMenuCardsAdapter(getResources().getStringArray(R.array.tests),
                getResources().getStringArray(R.array.tests_description),new int[]{R.mipmap.ic_dates_cards_round,-1,R.mipmap.ic_tests_round,R.mipmap.ic_tests_real_round,-1,R.mipmap.ic_true_false_inf_round,R.mipmap.ic_true_false_real_round,-1,R.mipmap.ic_sort_infinite_round,R.mipmap.ic_sort_real_round});
        t.setListner(this);
        mMainActivity = (MainActivity)getActivity();
        rc.setAdapter(t);
        rc.setLayoutManager(new LinearLayoutManager(getActivity()));
        return rc;
    }
    public void onResume() {
        super.onResume();
        MainActivity mAc = mMainActivity;
        mAc.getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>" + getResources().getString(R.string.title_tests) + "</font>"));
        BottomNavigationView bn = mAc.getBn();
        if(!mAc.getSupportActionBar().isShowing()) {
            ((LinearLayout)mAc.findViewById(R.id.container_main)).addView(bn);
            mAc.getSupportActionBar().show();
            //mAc.setTheme(R.style.AppTheme);
            mAc.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if(bn.getSelectedItemId() != R.id.navigation_tests)
            bn.setSelectedItemId(R.id.navigation_tests);
    }
    public void onClick(int position){
        pressedPosition = position;
        if(position == 5 || position == 6 || position == 8 || position==9) {
            mMainActivity.typePicked(1);
        }else {
            TypePickDialog c = new TypePickDialog();
            c.show(((MainActivity) getActivity()).getSupportFragmentManager(), "1");
        }
    }
    public void setStartPosition(){
        rc.scrollToPosition(0);
    }
    public void startPractise(ArrayList<Integer> arrayList,int type){
        MainActivity mAc =(MainActivity) getActivity();
        ((LinearLayout)mAc.findViewById(R.id.container_main)).removeView(mAc.getBn());
        switch (pressedPosition){
            case 0:
                mAc.getFragmentManager().beginTransaction().replace(R.id.frameLayout,new CardsShowDown().setCenturies(arrayList,type,mAc.getMode())).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
                break;
            case 2:
                mAc.getFragmentManager().beginTransaction().replace(R.id.frameLayout,new TestFragment().setCenturies(arrayList,type,mAc.getMode(),true)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
                break;
            case 3:
                showAds();
                mAc.getFragmentManager().beginTransaction().replace(R.id.frameLayout,new TestFragment().setCenturies(arrayList,type,mAc.getMode(),false)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
                break;
            case 5:
                mAc.getFragmentManager().beginTransaction().replace(R.id.frameLayout,new TrueFalseFragment().setCenturies(arrayList,type,mAc.getMode(),true)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
                break;
            case 6:
                showAds();
                mAc.getFragmentManager().beginTransaction().replace(R.id.frameLayout,new TrueFalseFragment().setCenturies(arrayList,type,mAc.getMode(),false)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
                break;
            case 8:
                mAc.getFragmentManager().beginTransaction().replace(R.id.frameLayout,new SortFragment().setCenturies(arrayList,type,mAc.getMode(),true)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
                break;
            case 9:
                showAds();
                mAc.getFragmentManager().beginTransaction().replace(R.id.frameLayout,new SortFragment().setCenturies(arrayList,type,mAc.getMode(),false)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
                break;
            default:
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            h.removeCallbacks(rnb);
        }catch (Exception e){}
    }

    public void showAds(){
        if(Appodeal.isLoaded(Appodeal.INTERSTITIAL))
            Appodeal.show(mMainActivity,Appodeal.INTERSTITIAL);
    }

}
