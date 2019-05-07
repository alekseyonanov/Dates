package com.nollpointer.dates.practise;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nollpointer.dates.R;
import com.nollpointer.dates.cards.CardsFragment;
import com.nollpointer.dates.distribute.DistributeFragment;
import com.nollpointer.dates.other.Date;
import com.nollpointer.dates.sort.SortFragment;
import com.nollpointer.dates.test.TestFragment;
import com.nollpointer.dates.truefalse.TrueFalseFragment;
import com.nollpointer.dates.voice.VoiceFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.nollpointer.dates.practise.PractiseConstants.CARDS;
import static com.nollpointer.dates.practise.PractiseConstants.DATES;
import static com.nollpointer.dates.practise.PractiseConstants.DIFFICULTY;
import static com.nollpointer.dates.practise.PractiseConstants.DISTRIBUTE;
import static com.nollpointer.dates.practise.PractiseConstants.SORT;
import static com.nollpointer.dates.practise.PractiseConstants.TEST;
import static com.nollpointer.dates.practise.PractiseConstants.TEST_MODE;
import static com.nollpointer.dates.practise.PractiseConstants.TRUE_FALSE;
import static com.nollpointer.dates.practise.PractiseConstants.TYPE;
import static com.nollpointer.dates.practise.PractiseConstants.VOICE;

public class PractiseResultFragment extends Fragment {
    private static final String RESULTS_LIST = "results_list";
    private static final String PRACTISE = "practise";

    private List<PractiseResult> practiseResults;

    public static PractiseResultFragment newInstance(String practise, ArrayList<PractiseResult> list, Bundle arguments) {

        Bundle args = new Bundle();

        int type = arguments.getInt(TYPE);
        ArrayList<Date> dates = arguments.getParcelableArrayList(DATES);
        int difficulty = arguments.getInt(DIFFICULTY);
        boolean isTestMode = arguments.getBoolean(TEST_MODE);

        args.putParcelableArrayList(RESULTS_LIST, list);
        args.putString(PRACTISE,practise);

        args.putBoolean(TEST_MODE, isTestMode);
        args.putInt(TYPE, type);
        args.putInt(DIFFICULTY, difficulty);
        args.putParcelableArrayList(DATES, dates);

        PractiseResultFragment fragment = new PractiseResultFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_practise_result, container, false);

        practiseResults = getArguments().getParcelableArrayList(RESULTS_LIST);
        String practise = getArguments().getString(PRACTISE);

        RecyclerView recyclerView = mainView.findViewById(R.id.results_recycler_view);

        PractiseResultCardsAdapter adapter = new PractiseResultCardsAdapter(practiseResults);

        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayout.getOrientation());

        recyclerView.setLayoutManager(linearLayout);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);

        Toolbar toolbar = mainView.findViewById(R.id.results_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        Button exitButton = mainView.findViewById(R.id.results_exit);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        Button resetButton = mainView.findViewById(R.id.results_reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Mate kudasai((", Toast.LENGTH_SHORT).show();

                startPractise();



            }
        });

        TextView markTextView = mainView.findViewById(R.id.results_mark_text_view);
        setMark(markTextView);

        new SaveCurrentMark(getContext(),getPractiseSaveTitle(practise),getMarkValue(getCorrectAnswerCount())).execute();

        return mainView;
    }

    public void startPractise() {
        Bundle arguments = getArguments();

        int type = arguments.getInt(TYPE);
        ArrayList<Date> dates = arguments.getParcelableArrayList(DATES);
        int difficulty = arguments.getInt(DIFFICULTY);
        boolean isTestMode = arguments.getBoolean(TEST_MODE);

        String practise = getArguments().getString(PRACTISE);
        Fragment fragment;

        switch (practise) {
            case CARDS:
                fragment = CardsFragment.newInstance(dates, type);
                break;
            case VOICE:
                fragment = VoiceFragment.newInstance(dates, type, difficulty,isTestMode);
                break;
            case TEST:
                fragment = TestFragment.newInstance(dates, type, difficulty,isTestMode);
                break;
            case TRUE_FALSE:
                fragment = TrueFalseFragment.newInstance(dates, difficulty,isTestMode);
                break;
            case SORT:
                fragment = SortFragment.newInstance(dates, difficulty,isTestMode);
                break;
            case DISTRIBUTE:
                fragment = new DistributeFragment();
                break;
            default:
                fragment = CardsFragment.newInstance(dates, type);
        }

        getFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();

    }

    private String getPractiseSaveTitle(String practise) {
        String[] titles = getResources().getStringArray(R.array.practise_marks_titles);
        String saveTitle;

        switch (practise) {
            case CARDS:
                saveTitle = titles[0];
                break;
            case VOICE:
                saveTitle = titles[1];
                break;
            case TEST:
                saveTitle = titles[2];
                break;
            case TRUE_FALSE:
                saveTitle = titles[3];
                break;
            case SORT:
                saveTitle = titles[4];
                break;
            case DISTRIBUTE:
                saveTitle = titles[5];
                break;
            default:
                saveTitle = titles[0];
        }

        return saveTitle;
    }

    private int getMarkValue(int result) {
        int value = 0;

        if(result <= 12){
            value = 0;
        }else if(result <= 16)
            value = 1;
        else
            value = 2;

        return value;
    }

    private int getCorrectAnswerCount(){
        int count = 0;
        for (PractiseResult result : practiseResults) {
            if (result.isCorrect())
                count++;
        }
        return count;
    }

    private void setMark(TextView markTextView) {
        int count = getCorrectAnswerCount();

        String mark;
        int drawable;

        if (count < 5) {
            mark = getString(R.string.mark_very_bad);
            drawable = R.drawable.ic_sentiment_very_bad;
        } else if (count < 9) {
            mark = getString(R.string.mark_bad);
            drawable = R.drawable.ic_sentiment_bad;
        } else if (count < 13) {
            mark = getString(R.string.mark_neutral);
            drawable = R.drawable.ic_sentiment_neutral;
        } else if (count < 17) {
            mark = getString(R.string.mark_good);
            drawable = R.drawable.ic_sentiment_good;
        } else {
            mark = getString(R.string.mark_very_good);
            drawable = R.drawable.ic_sentiment_very_good;
        }
        markTextView.setText("Ваш результат: " + mark + "\nВаши баллы: " + getMark(count));
        markTextView.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0);
    }

    private String getMark(int count) {
        Random random = new Random();
        double number = count * 5. / 20;

        return String.format("%.1f", number);
    }

    protected static class SaveCurrentMark extends AsyncTask<Void, Void, Void> {
        private Context context;
        private String practise;
        private int value;

        SaveCurrentMark(Context context, String practise, int value) {
            this.context = context;
            this.practise = practise;
            this.value = value;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

            editor.putInt(practise, value);

            editor.apply();
            return null;
        }
    }


//    private class ResultAdapter extends PagerAdapter {
//
//        private Context context;
//
//        public ResultAdapter(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup collection, int position) {
//            //CustomPagerEnum customPagerEnum = CustomPagerEnum.values()[position];
//            LayoutInflater inflater = LayoutInflater.from(context);
//            ViewGroup layout;
//            if(position == 0){
//                layout = (ViewGroup) inflater.inflate(
//                       R.layout.result_main,
//                        collection, false);
//
//
//            }else{
//                layout = (ViewGroup) inflater.inflate(
//                        R.layout.result_statistics,
//                        collection, false);
//
//                LineChart lineChart = layout.findViewById(R.id.results_chart);
//
//                XAxis xAxis = lineChart.getXAxis();
//                xAxis.setDrawAxisLine(false);
//
//                YAxis yAxis = lineChart.getAxisLeft();
//                yAxis.setDrawAxisLine(false);
//                yAxis.setDrawGridLines(false);
//
//                lineChart.setDrawBorders(false);
//                lineChart.setDrawGridBackground(false);
//                lineChart.setDrawMarkers(false);
//                lineChart.setGridBackgroundColor(Color.TRANSPARENT);
//
//                ArrayList<Entry> entries = new ArrayList<>();
//                Random random = new Random();
//                for (int i = 0; i < 10; i++) {
//                    entries.add(new Entry(i,random.nextInt(10)));
//                }
//
//                LineDataSet dataSet = new LineDataSet(entries,"Chart");
//                dataSet.setColor(getResources().getColor(R.color.colorAccent));
//                dataSet.setValueTextColor(Color.BLACK);
//
//                LineData lineData = new LineData(dataSet);
//                lineChart.setData(lineData);
//
//            }
//
//
//            collection.addView(layout);
//
//            return layout;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup collection, int position, Object view) {
//            collection.removeView((View) view);
//        }
//
//        @Override
//        public int getCount() {
//            return 2;
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return view == object;
//        }
//
//        @Nullable
//        @Override
//        public CharSequence getPageTitle(int position) {
//            String title;
//            if(position == 0)
//                title = "Результат";
//            else
//                title = "Статистика";
//            return title;
//        }
//    }

}
