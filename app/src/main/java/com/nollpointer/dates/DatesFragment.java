package com.nollpointer.dates;


import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DatesFragment extends Fragment implements StartPosition, DatesCardsAdapter.Listener {
    private MainActivity ctx;
    private DatesCardsAdapter adapter;
    private RecyclerView recycler;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recycler = (RecyclerView) inflater.inflate(R.layout.fragment_dates, container, false);
        ctx = (MainActivity) getActivity();
        recycler.setLayoutManager(new LinearLayoutManager(ctx));
        Cursor cursor = ctx.getCursor();
        int mode = ctx.getMode();
        adapter = new DatesCardsAdapter(cursor, mode, ctx.getResources().getStringArray(R.array.centuries), ctx.getResources().getStringArray(R.array.centuries_easy));
        adapter.setListener(this);
        recycler.setAdapter(adapter);

        return recycler;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter.getFontSize() == DatesCardsAdapter.DEFAULT_TEXT_SIZE)
            ctx.changeToolbarItemsVisibility(true, false);
        else
            ctx.changeToolbarItemsVisibility(true, true);
        ctx.getSupportActionBar().setTitle(R.string.title_dates);
        ctx.updateBottomNavigationView(R.id.navigetion_dates);
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