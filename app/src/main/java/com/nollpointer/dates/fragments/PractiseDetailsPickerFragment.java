package com.nollpointer.dates.fragments;


import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nollpointer.dates.Date;
import com.nollpointer.dates.MainActivity;
import com.nollpointer.dates.R;
import com.nollpointer.dates.adapters.PractiseDetailsPickerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.nollpointer.dates.MainActivity.EASY_DATES_MODE;
import static com.nollpointer.dates.MainActivity.FULL_DATES_MODE;
import static com.nollpointer.dates.constants.PractiseConstants.CARDS;
import static com.nollpointer.dates.constants.PractiseConstants.SORT;
import static com.nollpointer.dates.constants.PractiseConstants.TEST;
import static com.nollpointer.dates.constants.PractiseConstants.TRUE_FALSE;

public class PractiseDetailsPickerFragment extends Fragment {

    PractiseDetailsPickerAdapter typeAdapter;
    PractiseDetailsPickerAdapter centuryAdapter;

    private static final String PRACTISE = "practise";
    private static final String TAG = "PractiseDetailsPicker";


    public static PractiseDetailsPickerFragment newInstance(String practise) {
        PractiseDetailsPickerFragment practiseDetailsPickerFragment = new PractiseDetailsPickerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PRACTISE, practise);
        practiseDetailsPickerFragment.setArguments(bundle);
        return practiseDetailsPickerFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_practise_details_picker, container, false);

        Toolbar toolbar = mainView.findViewById(R.id.practise_details_picker_toolbar);
        toolbar.inflateMenu(R.menu.practise_details_picker_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.practise_details_picker_random:
                        setRandomValues();
                        break;
                }
                return true;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        Resources resources = getResources();

        RecyclerView typeRecyclerView = mainView.findViewById(R.id.typeRecyclerView);
        typeAdapter = new PractiseDetailsPickerAdapter(resources.getTextArray(R.array.pick_type),
                PractiseDetailsPickerAdapter.TYPE);

        typeRecyclerView.setAdapter(typeAdapter);
        typeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });


        RecyclerView centuryRecyclerView = mainView.findViewById(R.id.centuriesRecyclerView);
        centuryAdapter = new PractiseDetailsPickerAdapter(resources.getTextArray(R.array.centuries),
                PractiseDetailsPickerAdapter.CENTURY);
        centuryRecyclerView.setAdapter(centuryAdapter);
        centuryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        Button practiseButton = mainView.findViewById(R.id.practise_details_picker_button);
        practiseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPractise();
            }
        });

        return mainView;
    }

    public void startPractise() {
        int type = typeAdapter.getPickedType();
        List<Integer> centuries = centuryAdapter.getPickedCenturies();

        if(centuries.size() == 0)
            return;

        ArrayList<Date> dates = getListForPractise(centuries);

        String practise = getArguments().getString(PRACTISE);
        Fragment fragment;


        switch (practise) {
            case CARDS:
                fragment = CardsFragment.newInstance(dates, type);
                break;
            case TEST:
                fragment = TestFragment.newInstance(dates, type,false);
                break;
            case TRUE_FALSE:
                fragment = TrueFalseFragment.newInstance(dates,false);
                break;
            case SORT:
                fragment = SortFragment.newInstance(dates, false);
                break;
            default:
                fragment = CardsFragment.newInstance(dates, type);
        }


        getFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();

    }

    public void setRandomValues() {
        typeAdapter.makeRandomValues();
        centuryAdapter.makeRandomValues();
    }


    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).hideBottomNavigationView();
    }

    private ArrayList<Date> getListForPractise(List<Integer> centuriesList) {
        MainActivity mainActivity = (MainActivity) getActivity();
        ArrayList<Date> dates = mainActivity.getDateList();
        ArrayList<Date> practiseList = new ArrayList<>();
        int mode = mainActivity.getMode();
        if ((mode == FULL_DATES_MODE && centuriesList.contains(10)) || (mode == EASY_DATES_MODE && centuriesList.contains(2))) // Если выбраны все даты
            return dates;
        Pair<Integer, Integer> pair;
        Collections.sort(centuriesList);
        for (Integer number : centuriesList) {
            pair = getDatesRange(number, mode);
            practiseList.addAll(dates.subList(pair.first, pair.second));
        }
        return practiseList;
    }

    private Pair<Integer, Integer> getDatesRange(int pickedCentury, int mode) {
        int start = 0, end = 0;
        if (mode == FULL_DATES_MODE) {
            switch (pickedCentury) {
                case 0:
                    start = 0;
                    end = start + 21;
                    break;
                case 1:
                    start = 21;
                    end = start + 20;
                    break;
                case 2:
                    start = 41;
                    end = start + 35;
                    break;
                case 3:
                    start = 76;
                    end = start + 31;
                    break;
                case 4:
                    start = 107;
                    end = start + 40;
                    break;
                case 5:
                    start = 147;
                    end = start + 48;
                    break;
                case 6:
                    start = 195;
                    end = start + 48;
                    break;
                case 7:
                    start = 242;
                    end = start + 42;
                    break;
                case 8:
                    start = 284;
                    end = start + 50;
                    break;
                case 9:
                    start = 334;
                    end = start + 50;
                    break;
            }
        } else {
            switch (pickedCentury) {
                case 0:
                    start = 0;
                    end = start + 48;
                    break;
                case 1:
                    start = 48;
                    end = start + 47;
                    break;
            }
        }
        return new Pair<>(start, end);
    }

}
