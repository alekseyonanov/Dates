package com.nollpointer.dates.fragments;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.nollpointer.dates.Date;
import com.nollpointer.dates.adapters.DatesCardsAdapter;
import com.nollpointer.dates.MainActivity;
import com.nollpointer.dates.dialogs.MessageDeveloperDialog;
import com.nollpointer.dates.dialogs.MoreInfoDialog;
import com.nollpointer.dates.R;
import com.nollpointer.dates.StartPosition;

import java.util.ArrayList;
import java.util.List;

import static com.nollpointer.dates.constants.DatesCategoryConstants.ALL;

public class DatesFragment extends Fragment implements StartPosition, DatesCardsAdapter.Listener {
    private MainActivity ctx;
    private DatesCardsAdapter adapter;
    private TabLayout tabLayout;
    private RecyclerView recycler;
    private List<Date> dates;

    private AppBarLayout appBarLayout;

    private Toolbar toolbar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dates, container, false);
        recycler = view.findViewById(R.id.dates_recycler_view);
        ctx = (MainActivity) getActivity();
        tabLayout = view.findViewById(R.id.id_tabs);
        toolbar = view.findViewById(R.id.dates_toolbar);
        appBarLayout = view.findViewById(R.id.id_appbar);
        dates = ctx.getDateList();
        tabLayout.setSelectedTabIndicatorColor(ctx.getCurrentColor());
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (tab.getPosition() != ALL) {
                    ArrayList<Date> list = new ArrayList<>();
                    for (Date date : dates) {
                        if (date.getType() == position)
                            list.add(date);
                    }
                    adapter.refresh(list, position);
                } else
                    adapter.refresh(dates, ALL);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                goToStartPosition();
            }
        });

        initializeMenu();

        ((MainActivity) getActivity()).showBottomNavigationView();

        Resources resources = getResources();
        adapter = new DatesCardsAdapter(dates, ctx.getMode(), resources.getStringArray(R.array.centuries), resources.getStringArray(R.array.centuries_easy));
        adapter.setListener(this);

        LinearLayoutManager linearLayout = new LinearLayoutManager(ctx);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recycler.getContext(),
                linearLayout.getOrientation());

        recycler.setLayoutManager(linearLayout);
        recycler.addItemDecoration(dividerItemDecoration);
        recycler.setAdapter(adapter);
        return view;
    }

    private void initializeMenu() {
        toolbar.inflateMenu(R.menu.dates_menu);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.dates_app_bar_search) {
                    ((AppCompatActivity) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new SearchFragment()).addToBackStack(null).commit();
                } else {
                    new MessageDeveloperDialog().show(getFragmentManager(), null);
                }
                return true;
            }
        });

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToStartPosition();
                appBarLayout.setExpanded(true);
            }
        });

//        Menu menu = toolbar.getMenu();
//        final MenuItem switchItem = menu.findItem(R.id.dates_app_bar_switch);
//        final SearchView searchView = ((SearchView) menu.findItem(R.id.dates_app_bar_search).getActionView());
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//
//        menu.findItem(R.id.dates_app_bar_search).setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem menuItem) {
//
//                switchItem.setVisible(false);
//                return true;
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
//                switchItem.setVisible(true);
//                return true;
//            }
//        });
//        searchView.setOnSearchClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });

//        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                switchItem.setVisible(true);
//                return false;
//            }
//        });

//        EditText editText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
//        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                return false;
//            }
//        });
//
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//
//                return true;
//            }
//        });
    }

    @Override
    public void onItemClick(Date clickedDate) {
        String request = clickedDate.getRequest();
        MoreInfoDialog more = MoreInfoDialog.newInstance(request);
        more.show(ctx.getSupportFragmentManager(), null);
    }

    public void refresh() {
        this.dates = ctx.getDateList();

        final int selectedTab = tabLayout.getSelectedTabPosition();
        if (selectedTab == ALL)
            adapter.refresh(dates);
        else {
            adapter.change_top_texts();
            ArrayList<Date> list = new ArrayList<>();
            for (Date date : dates) {
                if (date.getType() == selectedTab)
                    list.add(date);
            }
            adapter.refresh(list, selectedTab);
        }
    }

    @Override
    public void goToStartPosition() {
        recycler.smoothScrollToPosition(0);
    }

    public void setTabLayoutIndicatorColor(int color) {
        tabLayout.setSelectedTabIndicatorColor(color);
    }
}