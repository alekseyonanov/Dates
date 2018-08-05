package com.nollpointer.dates;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appodeal.ads.Appodeal;

import java.util.ArrayList;
import java.util.Random;

import static com.nollpointer.dates.PractiseConstants.DATES;
import static com.nollpointer.dates.PractiseConstants.MIXED;
import static com.nollpointer.dates.PractiseConstants.ONLY_DATES;
import static com.nollpointer.dates.PractiseConstants.ONLY_EVENTS;
import static com.nollpointer.dates.PractiseConstants.TEST_MODE;
import static com.nollpointer.dates.PractiseConstants.TYPE;


public class TestFragment extends Fragment {
    private int RightButton, RightAnswers = 0, WrongAnswers = 0, type = 0;
    private Button answerButtons[];
    private TextView questionView, rightAnswersView, wrongAnswersView;
    private Handler mHandler;
    private Runnable refreshRunnable;
    private ArrayList<Integer> uniqueQuestionIndexes;
    private int bestResult = 0;
    private ProgressBar progressBar;

    private ArrayList<Date> dates;
    private ArrayList<Date> questions;

    private boolean testMode;
    private boolean isDateQuestion;
    private boolean clicked = false;

    private View.OnClickListener buttonClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            CheckResult((int) view.getTag());
        }
    };

    public static TestFragment newInstance(ArrayList<Date> dates,int type, boolean testMode){
        TestFragment test = new TestFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(TEST_MODE,testMode);
        bundle.putInt(TYPE, type);
        bundle.putParcelableArrayList(DATES,dates);
        test.setArguments(bundle);
        return test;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        if(Build.VERSION.SDK_INT == 19)
            view = inflater.inflate(R.layout.fragment_test_low_api, container, false);
        else
            view = inflater.inflate(R.layout.fragment_test, container, false);
        questionView = view.findViewById(R.id.test_info);
        rightAnswersView = view.findViewById(R.id.right_answers);
        wrongAnswersView = view.findViewById(R.id.wrong_answers);
        progressBar = view.findViewById(R.id.test_progressbar);
        answerButtons = new Button[4];
        Appodeal.setBannerViewId(R.id.appodealBannerView);
        answerButtons[0] = view.findViewById(R.id.test_Btn0);
        answerButtons[1] = view.findViewById(R.id.test_Btn1);
        answerButtons[2] = view.findViewById(R.id.test_Btn2);
        answerButtons[3] = view.findViewById(R.id.test_Btn3);
        for (int i = 0; i < 4; i++) {
            answerButtons[i].setTag(i);
            answerButtons[i].setOnClickListener(buttonClickListener);
        }
        refreshAnswerCount();

        mHandler = new Handler();
        refreshRunnable = new Runnable() {
            @Override
            public void run() {
                if(testMode && (WrongAnswers + RightAnswers == 20))
                    setResultScreen();
                else
                    setQuestions();
            }
        };

        Bundle saved = getArguments();
        type = saved.getInt(TYPE);
        testMode = saved.getBoolean(TEST_MODE);
        dates = saved.getParcelableArrayList(DATES);
        questions = new ArrayList<>(4);
        switch (type){
            case ONLY_DATES:
                isDateQuestion = true;
                break;
            case ONLY_EVENTS:
                isDateQuestion = false;
                break;
        }
        if(testMode) {
            progressBar.setVisibility(View.VISIBLE);
            uniqueQuestionIndexes = new ArrayList<>(20);
        }
        setTestInfo();
        setQuestions();

        return view;
    }

    private void setTestInfo() {
        Random random = new Random();
        int length = dates.size();
        RightButton = random.nextInt(4);
        for(int i=0;i<4;i++){
            int r = random.nextInt(length);
            Date date = (dates.get(r));
            if(isDateAlreadyUsed(date,i))
                i--;                       //Повторить итерацию заново
            else
                questions.add(date);

            if(testMode && i == RightButton) { //В случае режима теста
                if (uniqueQuestionIndexes.contains(r))
                    i--;
                else
                    uniqueQuestionIndexes.add(r);
            }

        }
        if(type == MIXED)
            isDateQuestion = random.nextBoolean();
    }



    private boolean isDateAlreadyUsed(Date date,int index){   //Проверить, если дата уже есть в списке или если событие с такой же датой уже присутствует
        if(questions.contains(date))
            return true;
        for(int i=0;i<index;i++){
            if(questions.get(i).isSameDate(date))
                return true;
        }
        return false;
    }

    public void setQuestions() {
        Date date;
        for (int i = 0; i < 4; i++) {
            date = questions.get(i);
            if(isDateQuestion)
                answerButtons[i].setText(date.getDate());
            else
                answerButtons[i].setText(date.getEvent());
            answerButtons[i].setBackgroundColor(getResources().getColor(android.R.color.white));
        }
        if(isDateQuestion)
            questionView.setText(questions.get(RightButton).getEvent());
        else
            questionView.setText(questions.get(RightButton).getDate());
        clicked = false;
    }


    public void CheckResult(int position) {
        if(clicked)
            return;
        else
            clicked = true;
        if (position == RightButton) {
            RightAnswers++;
        } else {
            WrongAnswers++;
            answerButtons[position].setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        }
        refreshAnswerCount();
        if(testMode)
            progressBar.incrementProgressBy(1);
        answerButtons[RightButton].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        questions.clear();
        mHandler.postDelayed(refreshRunnable, 950); ///////
        setTestInfo();
    }

    private void refreshAnswerCount(){
        wrongAnswersView.setText(Integer.toString(WrongAnswers));
        rightAnswersView.setText(Integer.toString(RightAnswers));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mHandler.removeCallbacks(refreshRunnable);
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

    public void setResultScreen(){
        answerButtons[1].setText(R.string.reset_button);
        answerButtons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartInfo();
            }
        });
        answerButtons[0].setClickable(false);
        answerButtons[2].setEnabled(false);
        answerButtons[2].setVisibility(View.INVISIBLE);
        answerButtons[3].setText(R.string.exit_button);
        answerButtons[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        answerButtons[1].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        answerButtons[1].setClickable(true);
        answerButtons[3].setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        answerButtons[3].setClickable(true);
        wrongAnswersView.setText(Integer.toString(WrongAnswers));
        rightAnswersView.setText(Integer.toString(RightAnswers));
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
        answerButtons[0].setBackgroundColor(getResources().getColor(R.color.colorBackgroundTest));
        answerButtons[0].setTextColor(color);
        answerButtons[0].setText(mark);
        if(cur_best == 0)
            questionView.setText(getString(R.string.current_result) + " " + Integer.toString(RightAnswers) + "\n\n\n"
                    + getString(R.string.cur_mark));
        else
            questionView.setText(getString(R.string.current_result) + " " + Integer.toString(RightAnswers)
                    + "\n\n" + getString(R.string.best_result) + " " + Integer.toString(cur_best) + "\n\n\n"
                    + getString(R.string.cur_mark) + " "  + mark);


    }

    public void restartInfo(){
        if(Appodeal.isLoaded(Appodeal.INTERSTITIAL))
            Appodeal.show(getActivity(),Appodeal.INTERSTITIAL);
        WrongAnswers = RightAnswers = 0;
        progressBar.setProgress(0);
        answerButtons[1].setOnClickListener(buttonClickListener);
        answerButtons[3].setOnClickListener(buttonClickListener);
        uniqueQuestionIndexes.clear();
        answerButtons[0].setClickable(true);
        answerButtons[0].setTextColor(getResources().getColor(android.R.color.black));
        answerButtons[2].setEnabled(true);
        answerButtons[2].setVisibility(View.VISIBLE);
        answerButtons[3].setText(R.string.exit_button);
        setQuestions();
    }
}