package com.nollpointer.dates;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import static com.nollpointer.dates.DatesCategoryConstants.ALL;

public class DatesFragment extends Fragment implements StartPosition, DatesCardsAdapter.Listener {
    private MainActivity ctx;
    private DatesCardsAdapter adapter;
    private TabLayout tabLayout;
    private RecyclerView recycler;
    private List<Date> dates;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dates, container, false);
        recycler = view.findViewById(R.id.recyclerView_dates);
        ctx = (MainActivity) getActivity();
        tabLayout = view.findViewById(R.id.id_tabs);
        //viewPager = view.findViewById(R.id.viewpager_dates);
        //tabLayout.setupWithViewPager(viewPager);
        dates = ctx.getDateList();
        tabLayout.setSelectedTabIndicatorColor(ctx.getCurrentColor());
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if(tab.getPosition() != ALL){
                    ArrayList<Date> list = new ArrayList<>();
                    for(Date date: dates){
                        if(date.getType() == position)
                            list.add(date);
                    }
                    adapter.refresh(list,position);
                }else
                    adapter.refresh(dates,ALL);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                goToStartPosition();
            }
        });
        recycler.setLayoutManager(new LinearLayoutManager(ctx));
        //String[] titles={"Все","Правления","Войны","Реформы","Восстания"};
        Resources resources = getResources();
        //adapter = new LocalPagerAdapter(getChildFragmentManager(),dates,titles);
        //viewPager.setAdapter(adapter);
        Log.wtf("121",dates.size() + "");
        adapter = new DatesCardsAdapter(dates, ctx.getMode(), resources.getStringArray(R.array.centuries), resources.getStringArray(R.array.centuries_easy));
        adapter.setListener(this);
        recycler.setAdapter(adapter);
        return view;
    }

    @Override
    public void onItemClick(int position) {
        String request = dates.get(position).getRequest();
        MoreInfoDialog more = MoreInfoDialog.newInstance(request);
        more.show(ctx.getSupportFragmentManager(), null);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter.getFontSize() == DatesCardsAdapter.DEFAULT_TEXT_SIZE)
            ctx.changeToolbarItemsVisibility(true, false);
        else
            ctx.changeToolbarItemsVisibility(true, true);
        ctx.setActionBarTitle(R.string.title_dates);
        //ctx.updateBottomNavigationView(R.id.navigetion_dates);

    }

    public void refresh() {
        this.dates = ctx.getDateList();
        adapter.refresh(dates);
    }

    public boolean setAdapterFontSize(int m) {
        return adapter.changeFontSize(m);
    }

    @Override
    public void goToStartPosition(){
        recycler.scrollToPosition(0);
    }

    public void setTabLayoutIndicatorColor(int color){
        tabLayout.setSelectedTabIndicatorColor(color);
    }

//    public class LocalPagerAdapter extends FragmentStatePagerAdapter {
////        private List<Date> pagerData;
////        private String[] titles;
////        private TreeMap<Integer,StartPosition> startPositions = new TreeMap<>();
////
////        public LocalPagerAdapter(FragmentManager fm, List<Date> pagerData,String[] titles) {
////            super(fm);
////            this.pagerData = pagerData;
////            this.titles = titles;
////        }
////
////        @Override
////        public DatesListFragment getItem(int position) {
////            DatesListFragment fragment;
////            if(position != ALL) {
////                ArrayList<Date> dates_list = new ArrayList<>();
////                for (Date date : pagerData) {
////                    if (date.getType() == position)
////                        dates_list.add(date);
////                }
////                fragment = DatesListFragment.newInstance(dates_list,position);
////            }else
////                fragment = DatesListFragment.newInstance(pagerData,position);
////            if(!startPositions.containsValue(fragment))
////                startPositions.put(position,fragment);
////            return fragment;
////
////        }
////
////        @Override
////        public int getItemPosition(Object object) {
////            return POSITION_NONE;
////        }
////
////        @Override
////        public int getCount() {
////            return titles.length;
////        }
////
////        @Override
////        public CharSequence getPageTitle(int position) {
////            return titles[position];
////        }
////
////        public void refresh(List<Date> list){
////            pagerData = list;
////            notifyDataSetChanged();
////        }
////
////        @Override
////        public Object instantiateItem(ViewGroup container, int position) {
////            return super.instantiateItem(container, position);
////        }
////
////        public StartPosition getStartPosition(int position) {
////            if(startPositions.size() == 0)
////                return null;
////            return startPositions.get(position);
////        }
////    }


}