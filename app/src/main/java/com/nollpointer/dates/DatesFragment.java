package com.nollpointer.dates;


import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static com.nollpointer.dates.DatesCategoryConstants.ALL;
import static com.nollpointer.dates.DatesCategoryConstants.MANAGEMENT;
import static com.nollpointer.dates.DatesCategoryConstants.REVOLUTION;
import static com.nollpointer.dates.DatesCategoryConstants.WAR;

public class DatesFragment extends Fragment implements StartPosition {
    private MainActivity ctx;
    private LocalPagerAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Date> dates;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dates, container, false);
        //recycler = view.findViewById(R.id.recyclerView_dates);
        ctx = (MainActivity) getActivity();
        tabLayout = view.findViewById(R.id.id_tabs);
        viewPager = view.findViewById(R.id.viewpager_dates);
        tabLayout.setupWithViewPager(viewPager);
        dates = ctx.getDateList();
        tabLayout.setSelectedTabIndicatorColor(Color.YELLOW);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                goToStartPosition();
            }
        });
        //recycler.setLayoutManager(new LinearLayoutManager(ctx));
        final Cursor cursor = ctx.getCursor();
        int mode = ctx.getMode();
        Resources resources = ctx.getResources();
        String[] titles={"Все","Правления","Войны","Реформы","Восстания"};
        adapter = new LocalPagerAdapter(getChildFragmentManager(),dates,titles);
        viewPager.setAdapter(adapter);
        //adapter = new DatesCardsAdapter(ctx.getDateList(), mode, resources.getStringArray(R.array.centuries), resources.getStringArray(R.array.centuries_easy));
        //adapter.setListener(this);
        //recycler.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        if(adapter.getFontSize() == DatesCardsAdapter.DEFAULT_TEXT_SIZE)
//            ctx.changeToolbarItemsVisibility(true, false);
//        else
//            ctx.changeToolbarItemsVisibility(true, true);
        ctx.getSupportActionBar().setTitle(R.string.title_dates);
        ctx.updateBottomNavigationView(R.id.navigetion_dates);

        Log.wtf("END",System.currentTimeMillis() + "");
    }

    public void refresh() {
        adapter.refresh(ctx.getDateList());
    }

    public boolean setAdapterFontSize(int m) {
        return true;//adapter.changeFontSize(m);
    }

    @Override
    public void goToStartPosition(){
        //recycler.scrollToPosition(0);
    }

    public class LocalPagerAdapter extends FragmentStatePagerAdapter {
        private List<Date> pagerData;
        private String[] titles;

        public LocalPagerAdapter(FragmentManager fm, List<Date> pagerData,String[] titles) {
            super(fm);
            this.pagerData = pagerData;
            this.titles = titles;
        }

        @Override
        public DatesListFragment getItem(int position) {
            DatesListFragment fragment;
            if(position != ALL) {
                ArrayList<Date> dates_list = new ArrayList<>();
                for (Date date : pagerData) {
                    if (date.getType() == position)
                        dates_list.add(date);
                }
                fragment = DatesListFragment.newInstance(dates_list);
            }else
                fragment = DatesListFragment.newInstance(pagerData);
            return fragment;

        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        public void refresh(List<Date> list){
            pagerData = list;
            notifyDataSetChanged();
        }
    }


}