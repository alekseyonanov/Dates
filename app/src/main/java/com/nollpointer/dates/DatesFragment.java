package com.nollpointer.dates;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
        Resources resources = getResources();
        adapter = new DatesCardsAdapter(dates, ctx.getMode(), resources.getStringArray(R.array.centuries), resources.getStringArray(R.array.centuries_easy));
        adapter.setListener(this);

        LinearLayoutManager linearLayout = new LinearLayoutManager(ctx);

        DividerItemDecoration dividerItemDecoration =new DividerItemDecoration(recycler.getContext(),
                linearLayout.getOrientation());

        recycler.setLayoutManager(linearLayout);
        recycler.addItemDecoration(dividerItemDecoration);
        recycler.setAdapter(adapter);
        return view;
    }

    @Override
    public void onItemClick(Date clickedDate) {
        String request = clickedDate.getRequest();
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

    }

    public void refresh() {
        this.dates = ctx.getDateList();

        final int selectedTab = tabLayout.getSelectedTabPosition();
        if(selectedTab == ALL)
            adapter.refresh(dates);
        else{
            adapter.change_top_texts();
            ArrayList<Date> list = new ArrayList<>();
            for(Date date: dates){
                if(date.getType() == selectedTab)
                    list.add(date);
            }
            adapter.refresh(list,selectedTab);
        }
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
}