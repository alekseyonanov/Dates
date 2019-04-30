package com.nollpointer.dates.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.tabs.TabLayout;
import com.nollpointer.dates.R;
import com.nollpointer.dates.adapters.DatesCardsAdapter;
import com.nollpointer.dates.adapters.PractiseResultCardsAdapter;

import java.util.ArrayList;
import java.util.Random;

public class PractiseResultFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_practise_result, container, false);

        RecyclerView recyclerView = mainView.findViewById(R.id.results_recycler_view);

        PractiseResultCardsAdapter adapter = new PractiseResultCardsAdapter();

        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());

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

            }
        });

        return mainView;
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
