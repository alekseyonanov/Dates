package com.nollpointer.dates.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nollpointer.dates.Date;
import com.nollpointer.dates.MainActivity;
import com.nollpointer.dates.R;
import com.nollpointer.dates.StartPosition;
import com.nollpointer.dates.adapters.DatesCardsAdapter;
import com.nollpointer.dates.dialogs.MessageDeveloperDialog;
import com.nollpointer.dates.dialogs.MoreInfoDialog;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.view.View.VISIBLE;
import static com.nollpointer.dates.constants.DatesCategoryConstants.ALL;

public class DatesFragment extends Fragment implements StartPosition, DatesCardsAdapter.Listener {
    private static final int RECOGNIZER_REQUEST_CODE = 1417;

    private MainActivity mainActivity;
    private DatesCardsAdapter adapter;
    private TabLayout tabLayout;
    private RecyclerView recycler;
    private List<Date> dates;

    private CardView searchContainer;
    private EditText searchEditText;

    private AppBarLayout appBarLayout;

    private Toolbar toolbar;

    private boolean isEditTextEmpty = true;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dates, container, false);
        recycler = view.findViewById(R.id.dates_recycler_view);
        mainActivity = (MainActivity) getActivity();
        tabLayout = view.findViewById(R.id.id_tabs);
        toolbar = view.findViewById(R.id.dates_toolbar);
        searchContainer = view.findViewById(R.id.dates_card_search);
        appBarLayout = view.findViewById(R.id.id_appbar);
        dates = mainActivity.getDateList();
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));
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

        initializeSearchView(view);
        initializeMenu();


        ((MainActivity) getActivity()).showBottomNavigationView();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String dateCardValue = preferences.getString("dates_card_type", "0");

        Resources resources = getResources();
        adapter = new DatesCardsAdapter(dates, mainActivity.getMode(), resources.getStringArray(R.array.centuries), resources.getStringArray(R.array.centuries_easy));
        if (dateCardValue.equals("0"))
            adapter.setViewIds(R.layout.dates_cards, R.layout.dates_cards_top_text);
        else
            adapter.setViewIds(R.layout.dates_card_2, R.layout.dates_card_2_top_text);
        adapter.setListener(this);

        LinearLayoutManager linearLayout = new LinearLayoutManager(mainActivity);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recycler.getContext(),
                linearLayout.getOrientation());

        recycler.setLayoutManager(linearLayout);
        recycler.addItemDecoration(dividerItemDecoration);
        recycler.setAdapter(adapter);

        recycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == SCROLL_STATE_DRAGGING) //TODO Optimize
                    ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(recyclerView.getWindowToken(), 0);
            }

        });

        return view;
    }

    private void initializeMenu() {
        toolbar.inflateMenu(R.menu.dates_menu);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.dates_app_bar_search) {
                    showSearch();
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
    }

    private void initializeSearchView(View mainView) {

        searchEditText = mainView.findViewById(R.id.dates_edit_text_search);

        final ImageView backButton = mainView.findViewById(R.id.dates_image_search_back);
        final ImageView multiButton = mainView.findViewById(R.id.dates_search_multi_button);

        backButton.setImageResource(R.drawable.ic_arrow_back_black);
        multiButton.setImageResource(R.drawable.ic_voice);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSearch();
            }
        });
        multiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditTextEmpty)
                    startVoiceSearch();
                else
                    searchEditText.setText("", TextView.BufferType.EDITABLE);
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isEditTextEmpty = count == 0;
                if (isEditTextEmpty)
                    multiButton.setImageResource(R.drawable.ic_voice);
                else
                    multiButton.setImageResource(R.drawable.ic_clear_black);

                search(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_UP) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER ||
                                keyCode == KeyEvent.KEYCODE_SEARCH)) {
                    ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(searchContainer.getWindowToken(), 0);

                    return true;
                }
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK && searchContainer.getVisibility() == VISIBLE) {
                    hideSearch();
                    return true;
                }
                return false;
            }
        });
    }

    private void showSearch() {
        appBarLayout.setExpanded(false);
        recycler.setNestedScrollingEnabled(false);
        searchContainer.setVisibility(View.VISIBLE);
        adapter.startSearchMode();
        ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                searchEditText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                searchEditText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
            }
        }, 200);
    }

    private void hideSearch() {
        appBarLayout.setExpanded(true);
        recycler.setNestedScrollingEnabled(true);
        searchContainer.setVisibility(View.GONE);
        searchEditText.setText("", TextView.BufferType.EDITABLE);
        adapter.stopSearchMode(dates);
        ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(searchContainer.getWindowToken(), 0);
        //mainActivity.showBottomNavigationView();
    }

    public void search(String query) {
        ArrayList<Date> list = new ArrayList<>();

        for (Date date : dates) {
            if (date.contains(query))
                list.add(date);
        }

        adapter.setDates(list);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOGNIZER_REQUEST_CODE)
            if (data.getExtras().containsKey(RecognizerIntent.EXTRA_RESULTS)) {
                ArrayList<String> text = data.getExtras().getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
                searchEditText.setText(text.get(0), TextView.BufferType.EDITABLE);
            }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startVoiceSearch() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getContext().getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);

        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, Long.valueOf(5000));
        intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, Long.valueOf(5000));

        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

        startActivityForResult(intent, RECOGNIZER_REQUEST_CODE);

    }

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

    @Override
    public void onItemClick(Date clickedDate) {
        String request = clickedDate.getRequest();
        MoreInfoDialog more = MoreInfoDialog.newInstance(request);
        more.show(mainActivity.getSupportFragmentManager(), null);
    }

    public void refresh() {
        this.dates = mainActivity.getDateList();

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