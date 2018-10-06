package com.nollpointer.dates.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nollpointer.dates.Date;
import com.nollpointer.dates.R;

import java.util.ArrayList;

import static com.nollpointer.dates.constants.PractiseConstants.DATES;
import static com.nollpointer.dates.constants.PractiseConstants.TEST_MODE;
import static com.nollpointer.dates.constants.PractiseConstants.TYPE;


public class InsideBoundsFragment extends Fragment {
    private ArrayList<Date> dates;


    public static InsideBoundsFragment newInstance(ArrayList<Date> dates, int type, boolean testMode){
        InsideBoundsFragment fragment = new InsideBoundsFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(TEST_MODE,testMode);
        bundle.putInt(TYPE, type);
        bundle.putParcelableArrayList(DATES,dates);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inside_bounds, container, false);

        Bundle bundle = getArguments();
        dates = bundle.getParcelableArrayList(DATES);

        return view;
    }

}
