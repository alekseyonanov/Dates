package com.nollpointer.dates;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.appodeal.ads.Appodeal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class TestFragment extends Fragment {
    private Cursor crs;
    private int RightButton, RightAnswers = 0, WrongAnswers = 0, type = 0;
    private Button btn[];
    private TextView ItextView, RtextView, WtextView;
    private String[] info = new String[5];
    private boolean clicked = false, all = false;
    private int[] position, bound;
    private Handler mHandler;
    private Runnable rnb;
    private ArrayList<Integer> questions;
    private ArrayList<String> test = new ArrayList<>();
    private int bestResult = 0;

    @Override
    public void onStart() {
        super.onStart();
        Appodeal.show(getActivity(), Appodeal.BANNER_VIEW);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        GridLayout view;
        if(Build.VERSION.SDK_INT == 19)
            view = (GridLayout) inflater.inflate(R.layout.fragment_test_low_api, container, false);
        else
            view =(GridLayout) inflater.inflate(R.layout.fragment_test, container, false);
        MainActivity mAc = (MainActivity) getActivity();
        mAc.getSupportActionBar().hide();
        crs = mAc.getCursor();
        ItextView = view.findViewById(R.id.test_info);
        RtextView = view.findViewById(R.id.right_answers);
        WtextView = view.findViewById(R.id.wrong_answers);
        btn = new Button[4];
        Appodeal.setBannerViewId(R.id.appodealBannerView);
        Arrays.fill(info,"");
        btn[0] = view.findViewById(R.id.test_Btn0);
        btn[1] = view.findViewById(R.id.test_Btn1);
        btn[2] = view.findViewById(R.id.test_Btn2);
        btn[3] = view.findViewById(R.id.test_Btn3);
        mHandler = new Handler();
        if(questions != null)
            rnb = new Runnable() {
                @Override
                public void run() {
                    if(RightAnswers + WrongAnswers==20)
                        setResultScreen();
                    else
                        setQuestions();
                }
            };
        else
            rnb = new Runnable() {
                @Override
                public void run() {
                    setQuestions();
                }
            };
        setTestInfo();
        setQuestions();
        for (int i = 0; i < 4; i++) {
            btn[i].setTag(i);
            btn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!clicked) {
                        clicked = true;
                        CheckResult((int) view.getTag());
                    }
                }
            });
        }
        return view;
    }

    private void setTestInfo() {
        Random rnd = new Random();
        RightButton = rnd.nextInt(4);
        int pick;
        switch (type){
            case 0:
                pick = 0;
                break;
            case 1:
                pick = 1;
                break;
            default:
                pick = rnd.nextInt(10) > 4 ? 1 : 0;
        }
        int currentPick;
        currentPick = rnd.nextInt(position.length);
        int Answer;
        if(questions != null){
            if(RightAnswers+WrongAnswers!=20){
                do{
                    Answer = position[currentPick] + rnd.nextInt(bound[currentPick]);
                }while (questions.contains(Answer));
                questions.add(Answer);
            }else
                Answer = position[currentPick] + rnd.nextInt(bound[currentPick]);
        }else
            Answer = position[currentPick] + rnd.nextInt(bound[currentPick]);
        int[] ch = new int[4];
        Arrays.fill(ch,-1);
        for (int i = 0; i < 4; i++) {
            if (i == RightButton) {
                crs.moveToPosition(Answer);
                info[4] = crs.getString(Math.abs(pick-1)).trim();
                info[i] = crs.getString(pick).trim();
                test.add(crs.getString(0).trim());
                continue;
            }
            boolean blen = true;
            ch[i] = Math.abs(Answer + (rnd.nextInt(14) - 7));
            if (ch[i] > position[currentPick] + bound[currentPick] - 1) {
                ch[i] = 2 * (position[currentPick] + bound[currentPick] - 1) - ch[i];
            }
            if (ch[i] < position[currentPick])
                ch[i] = 2*position[currentPick] - ch[i];
            for (int j = 0; j < 4; j++) {
                if (j == i)
                    continue;
                if (ch[i] == ch[j] || ch[i] == Answer) {
                    i--;
                    blen = false;
                    break;
                }
            }
            if(!blen)
                continue;
            crs.moveToPosition(ch[i]);
            String cur_info = crs.getString(pick).trim();
            String date_test = crs.getString(0).trim();
            for (int j = 0; j < 5; j++)
                if (info[j].equals(cur_info) || cur_info.equals(info[4]) || test.contains(date_test)) {
                    i--;
                    blen = false;
                    break;
                }
            if(blen) {
                    info[i] = cur_info;
                    test.add(date_test);
                }
        }
    }

    public void CheckResult(int position) {
        if (position == RightButton) {
            RightAnswers++;
        } else {
            WrongAnswers++;
            btn[position].setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        }
        btn[RightButton].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        mHandler.postDelayed(rnb, 950); ///////
        setTestInfo();
    }

    public void setQuestions() {
        ItextView.setText(info[4]);
        WtextView.setText(Integer.toString(WrongAnswers));
        RtextView.setText(Integer.toString(RightAnswers));
        clicked = false;
        for (int i = 0; i < 4; i++) {
            btn[i].setText(info[i]);
            btn[i].setBackgroundColor(getResources().getColor(android.R.color.white));
        }
        Arrays.fill(info,"");
        test.clear();
    }

    public TestFragment setCenturies(ArrayList<Integer> arrayList,int picked_pos,int mode,boolean infinitive) {
        type = picked_pos;
        if(!infinitive)
            questions = new ArrayList<>();
        if(mode ==0) {
            if (arrayList.contains(10))
                for (int i = 0; i < 10; i++)
                    arrayList.add(i, i);
            arrayList.remove(Integer.valueOf(10));
            position = new int[arrayList.size()];
            bound = new int[arrayList.size()];
            for (int i = 0; i < arrayList.size(); i++) {
                switch (arrayList.get(i)) {
                    case 0:
                        position[i] = 0;
                        bound[i] = 21;
                        break;
                    case 1:
                        position[i] = 21;
                        bound[i] = 20;
                        break;
                    case 2:
                        position[i] = 41;
                        bound[i] = 35;
                        break;
                    case 3:
                        position[i] = 76;
                        bound[i] = 31;
                        break;
                    case 4:
                        position[i] = 107;
                        bound[i] = 40;
                        break;
                    case 5:
                        position[i] = 147;
                        bound[i] = 48;
                        break;
                    case 6:
                        position[i] = 195;
                        bound[i] = 48;
                        break;
                    case 7:
                        position[i] = 242;
                        bound[i] = 42;
                        break;
                    case 8:
                        position[i] = 284;
                        bound[i] = 50;
                        break;
                    case 9:
                        position[i] = 334;
                        bound[i] = 50;
                        break;
                }
            }
        }else{
            if (arrayList.contains(2))
                for (int i = 0; i < 2; i++)
                    arrayList.add(i, i);
            arrayList.remove(Integer.valueOf(2));
            position = new int[arrayList.size()];
            bound = new int[arrayList.size()];
            for (int i = 0; i < arrayList.size(); i++) {
                switch (arrayList.get(i)) {
                    case 0:
                        position[i] = 0;
                        bound[i] = 48;
                        break;
                    case 1:
                        position[i] = 48;
                        bound[i] = 47;
                        break;
                }
            }
        }
        return this;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            mHandler.removeCallbacks(rnb);
        }catch (Exception e){}
    }

    @Override
    public void onStop() {
        super.onStop();
        Appodeal.hide(getActivity(), Appodeal.BANNER_VIEW);
    }

    public void setResultScreen(){
        btn[1].setText(R.string.reset_button);
        btn[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartInfo();
            }
        });
        btn[0].setClickable(false);
        //btn[0].setVisibility(View.INVISIBLE);
        btn[2].setEnabled(false);
        btn[2].setVisibility(View.INVISIBLE);
        btn[3].setText(R.string.exit_button);
        btn[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getFragmentManager().popBackStack();
            }
        });
        btn[1].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        btn[3].setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        WtextView.setText(Integer.toString(WrongAnswers));
        RtextView.setText(Integer.toString(RightAnswers));
        int cur_best = bestResult;
        if(RightAnswers > bestResult) {
            bestResult = RightAnswers;
        }
        String mark;
        int color;
        if(RightAnswers < 5) {
            mark = getString(R.string.mark_2);
            color = getResources().getColor(android.R.color.holo_red_light);
        }else if (RightAnswers < 10) {
            mark = getString(R.string.mark_3);
            color = getResources().getColor(android.R.color.holo_red_light);
        }else if (RightAnswers < 15) {
            mark = getString(R.string.mark_4);
            color = getResources().getColor(android.R.color.holo_green_light);
        }else if (RightAnswers < 20) {
            mark = getString(R.string.mark_5);
            color = getResources().getColor(android.R.color.holo_green_light);
        }else if (RightAnswers==20) {
            mark = getString(R.string.mark_20);
            color = getResources().getColor(android.R.color.holo_green_light);
        }else {
            mark = "WOW";
            color = getResources().getColor(android.R.color.holo_green_light);
        }
        btn[0].setBackgroundColor(getResources().getColor(R.color.colorBackgroundTest));
        btn[0].setTextColor(color);
        btn[0].setText(mark);
        if(cur_best == 0)
            ItextView.setText(getString(R.string.current_result) + " " + Integer.toString(RightAnswers) + "\n\n\n"
                    + getString(R.string.cur_mark));
        else
            ItextView.setText(getString(R.string.current_result) + " " + Integer.toString(RightAnswers)
                    + "\n\n" + getString(R.string.best_result) + " " + Integer.toString(cur_best) + "\n\n\n"
                    + getString(R.string.cur_mark) + " "  + mark);


    }

    public void restartInfo(){
        if(Appodeal.isLoaded(Appodeal.INTERSTITIAL))
            Appodeal.show(getActivity(),Appodeal.INTERSTITIAL);
        WrongAnswers = RightAnswers = 0;
        View.OnClickListener o = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!clicked) {
                    clicked = true;
                    CheckResult((int) view.getTag());
                }
            }
        };
        btn[1].setOnClickListener(o);
        btn[3].setOnClickListener(o);
        questions.clear();
        btn[1].setText(R.string.reset_button);
        btn[0].setClickable(true);
        btn[0].setTextColor(getResources().getColor(android.R.color.black));
        //btn[0].setVisibility(View.VISIBLE);
        btn[2].setEnabled(true);
        btn[2].setVisibility(View.VISIBLE);
        btn[3].setText(R.string.exit_button);
        //setTestInfo();
        setQuestions();
    }
}