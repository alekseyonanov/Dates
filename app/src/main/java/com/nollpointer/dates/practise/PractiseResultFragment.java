package com.nollpointer.dates.practise;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PractiseResultFragment extends Fragment {
    private static final String RESULTS_LIST = "results_list";

    private List<PractiseResult> practiseResults;

    public static PractiseResultFragment newInstance(ArrayList<PractiseResult> list) {

        Bundle args = new Bundle();

        args.putParcelableArrayList(RESULTS_LIST, list);

        PractiseResultFragment fragment = new PractiseResultFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_practise_result, container, false);

        practiseResults = getArguments().getParcelableArrayList(RESULTS_LIST);

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
            }
        });

        TextView markTextView = mainView.findViewById(R.id.results_mark_text_view);
        setMark(markTextView);

        return mainView;
    }

    private void setMark(TextView markTextView) {
        int count = 0;
        for (PractiseResult result : practiseResults) {
            if (result.isCorrect())
                count++;
        }

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
        markTextView.setCompoundDrawablesWithIntrinsicBounds(drawable,0,0,0);
    }

    private String getMark(int count) {
        Random random = new Random();
        double number = count * 5. / 20;

        return String.format("%.1f", number);
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
