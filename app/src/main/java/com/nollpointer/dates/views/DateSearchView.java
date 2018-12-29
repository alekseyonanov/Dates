package com.nollpointer.dates.views;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import com.nollpointer.dates.Date;
import com.nollpointer.dates.adapters.SearchCardsAdapter;

import java.util.ArrayList;
import java.util.List;

public class DateSearchView extends RelativeLayout {

    List<Date> dates;
    RecyclerView recyclerView;

    public DateSearchView(Context context) {
        super(context);
        setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
        recyclerView = new RecyclerView(context);
        addView(recyclerView,new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
    }

    public void setDates(List<Date> dates){
        this.dates = dates;

        SearchCardsAdapter adapter = new SearchCardsAdapter(dates);
        adapter.setListener(new SearchCardsAdapter.Listener() {
            @Override
            public void onItemClick(Date clickedDate) {

            }
        });

        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                linearLayout.getOrientation());

        recyclerView.setLayoutManager(linearLayout);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);

    }



    public void search(String query) {
        ArrayList<Date> querySearch = new ArrayList<>();
        for (Date date : dates) {
            if(date.contains(query))
                querySearch.add(date);
        }
        SearchCardsAdapter adapter = ((SearchCardsAdapter) recyclerView.getAdapter());
        adapter.refreshList(querySearch);
    }
}
