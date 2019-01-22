package com.nollpointer.dates.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.nollpointer.dates.Date;
import com.nollpointer.dates.MainActivity;
import com.nollpointer.dates.R;
import com.nollpointer.dates.views.DateSearchView;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private EditText editText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_search, container, false);

        viewPager = mainView.findViewById(R.id.search_fragment_view_pager);
        tabLayout = mainView.findViewById(R.id.search_fragment_tab_layout);
        toolbar = mainView.findViewById(R.id.search_fragment_toolbar);
        editText = mainView.findViewById(R.id.search_fragment_edit_text);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        toolbar.inflateMenu(R.menu.search_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.search_clear) {
                    editText.setText("", TextView.BufferType.EDITABLE);
                    return true;
                } else
                    return false;
            }
        });

        //editText.findFocus();
        editText.requestFocus();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        viewPager.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    hideKeyboardFrom();
            }
        });

        List<Date> dates = ((MainActivity) getActivity()).getDateList();

        ArrayList<DateSearchView> list = new ArrayList<>();
        DateSearchView view = new DateSearchView(getContext());
        view.setDates(dates);
        list.add(view);

        view = new DateSearchView(getContext());
        view.setDates(dates);
        list.add(view);

        viewPager.setAdapter(new SearchPagerAdapter(list, getResources().getStringArray(R.array.search_titles)));

        return mainView;
    }

    public void hideKeyboardFrom() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
    }


    class SearchPagerAdapter extends PagerAdapter {

        List<DateSearchView> list;
        String[] titles;

        public SearchPagerAdapter(List<DateSearchView> list, String[] titles) {
            this.list = list;
            this.titles = titles;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

}
