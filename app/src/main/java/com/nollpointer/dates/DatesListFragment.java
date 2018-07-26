package com.nollpointer.dates;


import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class DatesListFragment extends Fragment implements StartPosition, DatesCardsAdapter.Listener {
    private MainActivity ctx;
    private DatesCardsAdapter adapter;
    private RecyclerView recycler;
    private List<Date> dates;

    public static DatesListFragment newInstance(List<Date> list){
        DatesListFragment fragment = new DatesListFragment();
        fragment.dates = list;
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dates_list, container, false);
        recycler = view.findViewById(R.id.recyclerView_dates);
        ctx = (MainActivity) getActivity();
        recycler.setLayoutManager(new LinearLayoutManager(ctx));
        int mode = ctx.getMode();
        Resources resources = ctx.getResources();
        adapter = new DatesCardsAdapter(dates, mode, resources.getStringArray(R.array.centuries), resources.getStringArray(R.array.centuries_easy));
        adapter.setListener(this);
        recycler.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter.getFontSize() == DatesCardsAdapter.DEFAULT_TEXT_SIZE)
            ctx.changeToolbarItemsVisibility(true, false);
        else
            ctx.changeToolbarItemsVisibility(true, true);
    }

    public void refresh() {
        adapter.refresh(ctx.getCursor());
    }

    public boolean setAdapterFontSize(int m) {
        return adapter.changeFontSize(m);
    }

    @Override
    public void goToStartPosition() {
        recycler.scrollToPosition(0);
    }

    @Override
    public void onItemClick(int position) {
        Cursor cursor = ctx.getCursor();
        cursor.moveToPosition(position);
        String request = cursor.getString(2);
        MoreInfoDialog more = MoreInfoDialog.newInstance(request);
        more.show(ctx.getSupportFragmentManager(), null);
    }

}
