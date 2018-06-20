package com.nollpointer.dates;


import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DatesFragment extends Fragment implements StartPosition, DatesCardsAdapter.Listener{
    private MainActivity ctx;
    private DatesCardsAdapter adapter;
    private RecyclerView recycler;
    private Cursor cursor;
    int last_dy = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recycler = (RecyclerView) inflater.inflate(R.layout.fragment_dates, container, false);
        ctx = (MainActivity) getActivity();
        recycler.setLayoutManager(new LinearLayoutManager(ctx));
        cursor = ctx.getCursor();
        int mode = ctx.getMode();
        adapter = new DatesCardsAdapter(cursor,mode,ctx.getResources().getStringArray(R.array.centuries),ctx.getResources().getStringArray(R.array.centuries_easy));
        adapter.setListener(this);
        recycler.setAdapter(adapter);

        return recycler;
    }

    @Override
    public void onResume() {
        super.onResume();
        ctx.changeToolbarItemsVisibility(true,false);
        ctx.getSupportActionBar().setTitle(R.string.title_dates);
        ctx.updateBottomNavigationView(R.id.navigetion_dates);
    }

    public void refresh() {
        adapter.refresh(ctx.getCursor());
    }

    public boolean setAdapterFontSize(int m){
        return adapter.changeFontSize(m);
    }

    @Override
    public void goToStartPosition() {
        recycler.scrollToPosition(0);
    }

    @Override
    public void onItemClick(int position) {
        if(ctx.getMode() == MainActivity.EASY_DATES_MODE){
            final MoreInfoDialog more = new MoreInfoDialog();
            cursor.moveToPosition(position);
            String request = cursor.getString(2);
            App.getApi().getData(request).enqueue(new Callback<WikipediaResponseModel>() {
                @Override
                public void onResponse(Call<WikipediaResponseModel> call, Response<WikipediaResponseModel> response) {
                    WikipediaResponseModel wiki = response.body();
                    more.setInfo(wiki.getExtractHtml(),wiki.getContentUrls().getMobile().getPage());
                }

                @Override
                public void onFailure(Call<WikipediaResponseModel> call, Throwable t) {
                    Log.wtf("ERROR",t.getMessage());
                    Snackbar.make(recycler,"ERROR",Snackbar.LENGTH_SHORT).show();
                }
            });
            more.show(ctx.getSupportFragmentManager(),null);
        }
    }
}