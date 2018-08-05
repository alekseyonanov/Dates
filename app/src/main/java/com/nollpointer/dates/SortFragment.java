package com.nollpointer.dates;


import android.support.v4.app.Fragment;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appodeal.ads.Appodeal;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.shape.NoShape;

import static com.nollpointer.dates.MainActivity.DATES;
import static com.nollpointer.dates.MainActivity.SORT;
import static com.nollpointer.dates.MainActivity.SORT_CHECK;

import static com.nollpointer.dates.PractiseConstants.BOUND;
import static com.nollpointer.dates.PractiseConstants.INFINITIVE;
import static com.nollpointer.dates.PractiseConstants.POSITION;
import static com.nollpointer.dates.PractiseConstants.TEST_MODE;
import static com.nollpointer.dates.PractiseConstants.TYPE;

public class SortFragment extends Fragment {
    private TextView RightAnswers, WrongAnswers;
    private TextView instructions;
    private ProgressBar progressBar;
    private SortCardsControl cardsControl;


    private int[] RightSequence = new int[3];
    private List<String> events;
    private int Answer = 123;
    private int CheckModeRightSequence = -1;
    private int right_answers_count=0,wrong_answers_count=0,best_result = 0;

    private ArrayList<Date> dates;
    private Button check_button;

    //private boolean isResultScreenOn = false;
    private boolean isCheckMode = false;
    private boolean testMode;
    private View mainView;
    //private boolean isFirstTimeCheck = true;



    public static SortFragment newInstance(ArrayList<Date> dates,boolean testMode){
        SortFragment sort = new SortFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(TEST_MODE,testMode);
        bundle.putParcelableArrayList(DATES,dates);
        sort.setArguments(bundle);
        return sort;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(Build.VERSION.SDK_INT == 19)
            mainView = inflater.inflate(R.layout.fragment_sort_low_api, container, false);
        else
            mainView = inflater.inflate(R.layout.fragment_sort, container, false);
        MainActivity ctx = (MainActivity) getActivity();

        Bundle saved = getArguments();
        dates = saved.getParcelableArrayList(DATES);
        testMode = saved.getBoolean(TEST_MODE);

        initViews();

        if(testMode)
            progressBar.setVisibility(View.VISIBLE);

        setQuestionInfo();
        setQuestions();

        if(ctx.isFirstTime(SORT))
            new MaterialShowcaseView.Builder(ctx)
                    .setTarget(mainView)
                    .setDelay(200)
                    .setContentText(R.string.tutorial_sort)
                    .setDismissText(R.string.got_it)
                    .setDismissOnTouch(true)
                    .setDismissTextColor(Color.GREEN)
                    .setMaskColour(getResources().getColor(R.color.colorMask))
                    .setShape(new NoShape())
                    .show();
        return mainView;
    }

    private void initViews(){
        RightAnswers = mainView.findViewById(R.id.right_answers);
        WrongAnswers = mainView.findViewById(R.id.wrong_answers);
        instructions = mainView.findViewById(R.id.instruction_sort);
        progressBar = mainView.findViewById(R.id.sort_progressbar);
        check_button = mainView.findViewById(R.id.sort_check);
        cardsControl = SortCards.newInstance(mainView.findViewById(R.id.sort_main_content));

        RightAnswers.setText("0");
        WrongAnswers.setText("0");

        check_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
            }
        });

        Appodeal.setBannerViewId(R.id.appodealBannerView_sort);
    }

    public void check(){
        if (isCheckMode) {
            check_button.setText(R.string.check_button);
            setQuestions();
        } else {
            boolean isCorrect = cardsControl.check();
            incrementScore(isCorrect);
            check_button.setText(R.string.next_sort);
            setQuestionInfo();
        }
        isCheckMode = !isCheckMode;
        cardsControl.setCheckMode(isCheckMode);
    }

    public void incrementScore(boolean isCorrect){
        if(isCorrect)
            right_answers_count++;
        else
            wrong_answers_count++;
        WrongAnswers.setText(Integer.toString(wrong_answers_count));
        RightAnswers.setText(Integer.toString(right_answers_count));
    }

    @Override
    public void onStart() {
        super.onStart();
        Appodeal.show(getActivity(), Appodeal.BANNER_VIEW);
    }

    @Override
    public void onStop() {
        super.onStop();
        Appodeal.hide(getActivity(), Appodeal.BANNER_VIEW);
    }


//    private void refreshPage(){
//        int color = getResources().getColor(android.R.color.white);
//        for(CardView c:cards)
//            c.setBackgroundColor(color);
//        check_button.setClickable(true);
////        if(!isInfinitive && wrong_answers_count+right_answers_count==20)
////            setResultScreen();
////        else
////            setQuestions();
//    }

    private void setQuestionInfo(){
        Random random = new Random();
        ArrayList<Date> list = new ArrayList<>(3);
        int length = dates.size();
        for(int i=0;i<3;i++){
            int rand = random.nextInt(length);
            Date date = dates.get(rand);
            if(list.contains(date) || date.isContinuous()) {
                i--;
                continue;
            }
            list.add(date);
            RightSequence[i] = rand;
        }
        prepareEventList(list);
        prepareRightSequence();
    }

    private void prepareEventList(List<Date> dateList){
        ArrayList<String> eventList = new ArrayList<>(3);
        for(Date date:dateList){
            eventList.add(date.getEvent());
        }
        events = eventList;
    }

    private void prepareRightSequence(){
        int maxIndex = 0;
        int minIndex = 0;
        int middleIndex = 0;
        for(int i=0;i<3;i++){
            if(RightSequence[maxIndex] < RightSequence[i])
                maxIndex = i;
            if(RightSequence[minIndex] > RightSequence[i])
                minIndex = i;
        }
        for(int i =0;i<3;i++)
            if(i != minIndex && i != maxIndex) {
                middleIndex = i;
                break;
            }

        RightSequence[minIndex] = 1;
        RightSequence[middleIndex] = 2;
        RightSequence[maxIndex] = 3;
    }

    private void setQuestions(){
        cardsControl.setAnswerSequence(RightSequence);
        cardsControl.setQuestions(events);

        Log.wtf("TEST","" + RightSequence[0] + RightSequence[1] + RightSequence[2]);
    }
//
//    private void OnGoingCheck(){
//        setColoredCards(CheckModeRightSequence);
//    }

//    private void setColoredCards(int RightSequence){
//        boolean[] ar = new boolean[3];
//        ar[0] = RightSequence/100 == Answer/100;
//        ar[1] = (RightSequence/10)%10 == (Answer/10)%10;
//        ar[2] = RightSequence%10 == Answer%10;
//        int colorR = getResources().getColor(android.R.color.holo_green_dark);
//        int colorW = getResources().getColor(android.R.color.holo_red_light);
//        for(int i =0;i<3;i++){
//            if(ar[i])
//                cards[i].setBackgroundColor(colorR);
//            else
//                cards[i].setBackgroundColor(colorW);
//        }
//    }

//    private void Check(){
//        if(Answer == RightSequence) {
//            right_answers_count++;
//            RightAnswers.setText(Integer.toString(right_answers_count));
//            int color = getResources().getColor(android.R.color.holo_green_dark);
//            for(CardView c:cards)
//                c.setBackgroundColor(color);
//        }else{
//            wrong_answers_count++;
//            WrongAnswers.setText(Integer.toString(wrong_answers_count));
//            setColoredCards(RightSequence);
//        }
//        if(!isInfinitive)
//            progressBar.incrementProgressBy(1);
//        CheckModeRightSequence = RightSequence;
//        if(isFirstTimeCheck) {
//            new MaterialShowcaseView.Builder(getActivity())
//                    .setTarget(mainView)
//                    .setDelay(150)
//                    .setContentText(R.string.tutorial_sort_check)
//                    .setDismissText(R.string.got_it)
//                    .setDismissOnTouch(true)
//                    .setDismissTextColor(Color.GREEN)
//                    .setMaskColour(getResources().getColor(R.color.colorMask))
//                    .setShape(new NoShape())
//                    .show();
//            isFirstTimeCheck = false;
//        }
//        setSequence();
//        setQuestionInfo();
//    }
//
//    private void setResultScreen(){
//        isResultScreenOn = true;
//        check_button.setVisibility(View.INVISIBLE);
//        check_button.setClickable(false);
//        Texts[1].setText(R.string.reset_button);
//        Texts[2].setText(R.string.exit_button);
//        cards[1].setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
//        cards[2].setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
//        int cur_best = best_result;
//        if(right_answers_count > best_result) {
//            best_result = right_answers_count ;
//        }
//        String mark;
//        int color;
//        if(right_answers_count < 5) {
//            mark = getString(R.string.mark_2);
//            color = getResources().getColor(android.R.color.holo_red_light);
//        }else if (right_answers_count < 10) {
//            mark = getString(R.string.mark_3);
//            color = getResources().getColor(android.R.color.holo_red_light);
//        }else if (right_answers_count < 15) {
//            mark = getString(R.string.mark_4);
//            color = getResources().getColor(android.R.color.holo_green_light);
//        }else if (right_answers_count < 20) {
//            mark = getString(R.string.mark_5);
//            color = getResources().getColor(android.R.color.holo_green_light);
//        }else if (right_answers_count==20) {
//            mark = getString(R.string.mark_20);
//            color = getResources().getColor(android.R.color.holo_green_light);
//        }else {
//            mark = "WOW";
//            color = getResources().getColor(android.R.color.holo_green_light);
//        }
//        Numbers[0].setText(Integer.toString(right_answers_count));
//        Numbers[1].setText(">");
//        Numbers[2].setText("<");
//        Texts[0].setText(mark);
//        Texts[0].setTextColor(color);
//        if(cur_best == 0)
//            instructions.setText(getString(R.string.current_result));
//        else
//            instructions.setText(getString(R.string.best_result) + cur_best + "\n\n" + getString(R.string.current_result));
//    }
//
//    private void resetInfo(){
//        if(Appodeal.isLoaded(Appodeal.INTERSTITIAL))
//            Appodeal.show(getActivity(),Appodeal.INTERSTITIAL);
//        wrong_answers_count=right_answers_count=0;
//        WrongAnswers.setText(Integer.toString(wrong_answers_count));
//        RightAnswers.setText(Integer.toString(right_answers_count));
//        isResultScreenOn = false;
//        check_button.setVisibility(View.VISIBLE);
//        check_button.setClickable(true);
//        ColorStateList cols = Texts[1].getTextColors();
//        Texts[0].setTextColor(cols);
//        cards[1].setBackgroundColor(getResources().getColor(android.R.color.white));
//        cards[2].setBackgroundColor(getResources().getColor(android.R.color.white));
//        instructions.setText(getString(R.string.sotr_text));
//        progressBar.setProgress(0);
//        setQuestions();
//    }
}
