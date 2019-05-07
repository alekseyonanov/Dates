package com.nollpointer.dates.practise;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.ChipGroup;
import com.nollpointer.dates.R;
import com.nollpointer.dates.activity.MainActivity;
import com.nollpointer.dates.cards.CardsFragment;
import com.nollpointer.dates.distribute.DistributeFragment;
import com.nollpointer.dates.other.Date;
import com.nollpointer.dates.other.Misc;
import com.nollpointer.dates.sort.SortFragment;
import com.nollpointer.dates.test.TestFragment;
import com.nollpointer.dates.truefalse.TrueFalseFragment;
import com.nollpointer.dates.voice.VoiceFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static android.view.View.NO_ID;
import static com.nollpointer.dates.activity.MainActivity.EASY_DATES_MODE;
import static com.nollpointer.dates.activity.MainActivity.FULL_DATES_MODE;
import static com.nollpointer.dates.activity.MainActivity.MODE;
import static com.nollpointer.dates.practise.PractiseConstants.CARDS;
import static com.nollpointer.dates.practise.PractiseConstants.DIFFICULTY_EASY;
import static com.nollpointer.dates.practise.PractiseConstants.DIFFICULTY_HARD;
import static com.nollpointer.dates.practise.PractiseConstants.DIFFICULTY_MEDIUM;
import static com.nollpointer.dates.practise.PractiseConstants.DISTRIBUTE;
import static com.nollpointer.dates.practise.PractiseConstants.SORT;
import static com.nollpointer.dates.practise.PractiseConstants.TEST;
import static com.nollpointer.dates.practise.PractiseConstants.TEST_MODE;
import static com.nollpointer.dates.practise.PractiseConstants.TRUE_FALSE;
import static com.nollpointer.dates.practise.PractiseConstants.VOICE;

public class PractiseDetailsPickerFragment extends Fragment {

    PractiseDetailsPickerAdapter typeAdapter;
    PractiseDetailsPickerAdapter centuryAdapter;

    ChipGroup difficultyChipGroup;

    private static final String PRACTISE = "practise";
    private static final String TAG = "PractiseDetailsPicker";

    private int mode;


    public static PractiseDetailsPickerFragment newInstance(String practise, boolean practiseMode, int mode) {
        PractiseDetailsPickerFragment practiseDetailsPickerFragment = new PractiseDetailsPickerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PRACTISE, practise);
        bundle.putInt(MODE, mode);
        bundle.putBoolean(TEST_MODE, practiseMode);
        practiseDetailsPickerFragment.setArguments(bundle);
        return practiseDetailsPickerFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_practise_details_picker, container, false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        Bundle bundle = getArguments();
        boolean saveCurrentState = preferences.getBoolean("save_current_state", true);
        mode = bundle.getInt(MODE, FULL_DATES_MODE);
        final String practise = bundle.getString(PRACTISE);

        Toolbar toolbar = mainView.findViewById(R.id.practise_details_picker_toolbar);
        toolbar.inflateMenu(R.menu.practise_details_picker_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.practise_details_picker_random)
                    setRandomValues(isLockedType(practise));


                return true;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });


        Button practiseButton = mainView.findViewById(R.id.practise_details_picker_button);
        practiseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPractise();
            }
        });

        Resources resources = getResources();

        CheckedTextView centuryTitle = mainView.findViewById(R.id.centuryTitle);
        centuryTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckedTextView view = (CheckedTextView) v;
                if (view.isChecked()) {
                    if (!centuryAdapter.getCenturies().equals(generateFullCenturiesList(mode)))
                        view.setChecked(false);
                } else {
                    view.setChecked(true);
                    centuryAdapter.setCenturies(generateFullCenturiesList(mode));
                    centuryAdapter.notifyDataSetChanged();
                }
            }
        });

        RecyclerView typeRecyclerView = mainView.findViewById(R.id.typeRecyclerView);
        typeAdapter = new PractiseDetailsPickerAdapter(resources.getTextArray(R.array.pick_type),
                PractiseDetailsPickerAdapter.TYPE);

        typeRecyclerView.setAdapter(typeAdapter);
        typeAdapter.setTitleCheckedTextView(centuryTitle);
        typeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });


        final RecyclerView centuryRecyclerView = mainView.findViewById(R.id.centuriesRecyclerView);
        centuryAdapter = new PractiseDetailsPickerAdapter(
                mode == FULL_DATES_MODE ? resources.getTextArray(R.array.centuries) : resources.getTextArray(R.array.centuries_easy),
                PractiseDetailsPickerAdapter.CENTURY);
        centuryAdapter.setTitleCheckedTextView(centuryTitle);
        centuryRecyclerView.setAdapter(centuryAdapter);
        centuryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        difficultyChipGroup = mainView.findViewById(R.id.difficulty_chip_group);
        difficultyChipGroup.check(R.id.difficulty_chip_easy);
        difficultyChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            int previousSelection = R.id.difficulty_chip_easy;

            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int id) {

                if (id == NO_ID)
                    chipGroup.check(previousSelection);
                else
                    previousSelection = id;

            }
        });


        //TODO добавить выбор сразу всех веков по кнопке
        if (saveCurrentState) {
            int type = preferences.getInt("practise_type", 0);
            ArrayList<Integer> list;
            if (mode == FULL_DATES_MODE)
                list = Misc.getIntegerListFromString(preferences.getString("practise_centuries_full", null));
            else
                list = Misc.getIntegerListFromString(preferences.getString("practise_centuries_easy", null));

            if (isLockedType(practise))
                setLockedType(practise);
            else
                typeAdapter.setType(type);

            centuryAdapter.setCenturies(list);
            if ((list.size() == 10 && mode == FULL_DATES_MODE) || (mode == EASY_DATES_MODE && list.size() == 2))
                centuryTitle.setChecked(true);
        } else {
            if (isLockedType(practise))
                setLockedType(practise);
        }

        return mainView;
    }

    private ArrayList<Integer> generateFullCenturiesList(int mode) {
        ArrayList<Integer> list = new ArrayList<>();
        int max = mode == FULL_DATES_MODE ? 10 : 2;
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }

        return list;
    }

    private void setLockedType(String practise) {
        switch (practise) {
            case CARDS:
                typeAdapter.setType(1);
                break;
            case VOICE:
                typeAdapter.setType(0);
                break;
            case TRUE_FALSE:
                typeAdapter.setType(2);
                break;
            case SORT:
                typeAdapter.setType(1);
                break;
            case DISTRIBUTE:
                typeAdapter.setType(1);
                break;
        }
        typeAdapter.setLocked();
    }

    private boolean isLockedType(String practise) {
        return practise.equals(CARDS) || practise.equals(TRUE_FALSE) || practise.equals(SORT) || practise.equals(DISTRIBUTE) || practise.equals(VOICE);
    }


    public void startPractise() {
        int type = typeAdapter.getType();
        List<Integer> centuries = centuryAdapter.getCenturies();

        new SaveCurrentState(getContext(), type, centuries, mode).execute();

        if (centuries.size() == 0)
            return;

        ArrayList<Date> dates = getListForPractise(centuries);

        String practise = getArguments().getString(PRACTISE);
        boolean isTestMode = getArguments().getBoolean(TEST_MODE);
        Fragment fragment;

        switch (practise) {
            case CARDS:
                fragment = CardsFragment.newInstance(dates, type);
                break;
            case VOICE:
                fragment = VoiceFragment.newInstance(dates, type, getDifficulty(), isTestMode);
                break;
            case TEST:
                fragment = TestFragment.newInstance(dates, type, getDifficulty(), isTestMode);
                break;
            case TRUE_FALSE:
                fragment = TrueFalseFragment.newInstance(dates, getDifficulty(), isTestMode);
                break;
            case SORT:
                fragment = SortFragment.newInstance(dates, getDifficulty(), isTestMode);
                break;
            case DISTRIBUTE:
                fragment = new DistributeFragment();
                break;
            default:
                fragment = CardsFragment.newInstance(dates, type);
        }

        getFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();

    }

    private int getDifficulty() {
        int checkedId = difficultyChipGroup.getCheckedChipId();

        switch (checkedId) {
            case R.id.difficulty_chip_easy:
                return DIFFICULTY_EASY;
            case R.id.difficulty_chip_medium:
                return DIFFICULTY_MEDIUM;
            case R.id.difficulty_chip_hard:
                return DIFFICULTY_HARD;
        }

        return DIFFICULTY_EASY;
    }

    public void setRandomValues(boolean isTypeLocked) {
        centuryAdapter.makeRandomValues();
        setRandomDifficulty();
        if (!isTypeLocked)
            typeAdapter.makeRandomValues();
    }

    private void setRandomDifficulty() {
        Random random = new Random(System.currentTimeMillis());
        difficultyChipGroup.check(difficultyChipGroup.getChildAt(random.nextInt(3)).getId());
    }


    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).hideBottomNavigationView();
    }

    private ArrayList<Date> getListForPractise(List<Integer> centuriesList) {
        MainActivity mainActivity = (MainActivity) getActivity();
        ArrayList<Date> dates = mainActivity.getDates();
        ArrayList<Date> practiseList = new ArrayList<>();
        if ((mode == FULL_DATES_MODE && centuriesList.size() == 10) || (mode == EASY_DATES_MODE && centuriesList.size() == 2)) // Если выбраны все даты
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


    protected static class SaveCurrentState extends AsyncTask<Void, Void, Void> {
        private int type = -1;
        private List<Integer> centuries;
        private Context context;
        private int mode;

        SaveCurrentState(Context context, int type, List<Integer> centuries, int mode) {
            this.context = context;
            this.type = type;
            this.centuries = centuries;
            this.mode = mode;
        }

        SaveCurrentState(Context context, List<Integer> centuries) {
            this.context = context;
            this.centuries = centuries;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

            String numberString = Misc.getStringFromIntegerList(centuries);

            if (type != -1)
                editor.putInt("practise_type", type);
            if (mode == FULL_DATES_MODE)
                editor.putString("practise_centuries_full", numberString);
            else
                editor.putString("practise_centuries_easy", numberString);


            editor.apply();
            return null;
        }
    }

}
