package com.nollpointer.dates.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appodeal.ads.Appodeal;
import com.nollpointer.dates.Date;
import com.nollpointer.dates.R;
import com.nollpointer.dates.dialogs.ResultDialog;

import java.util.ArrayList;
import java.util.Random;

import static com.nollpointer.dates.constants.PractiseConstants.DATES;
import static com.nollpointer.dates.constants.PractiseConstants.MIXED;
import static com.nollpointer.dates.constants.PractiseConstants.ONLY_DATES;
import static com.nollpointer.dates.constants.PractiseConstants.ONLY_EVENTS;
import static com.nollpointer.dates.constants.PractiseConstants.TEST_MODE;
import static com.nollpointer.dates.constants.PractiseConstants.TYPE;


public class TestFragment extends Fragment implements ResultDialog.ResultDialogCallbackListener {
    private int RightButton, RightAnswers = 0, WrongAnswers = 0, type = 0;
    private Button answerButtons[];
    private TextView questionView;
    //private TextView rightAnswersView, wrongAnswersView;
    private Handler mHandler;
    private Runnable refreshRunnable;
    private ArrayList<Integer> uniqueQuestionIndexes;
    private int bestResult = 0;
    private ProgressBar progressBar;
    private ImageButton backButton, helpButton;

    private ArrayList<Date> dates;
    private ArrayList<Date> questions;

    private boolean testMode;
    private boolean isDateQuestion;
    private boolean clicked = false;

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CheckResult((int) view.getTag());
        }
    };

    public static TestFragment newInstance(ArrayList<Date> dates, int type, boolean testMode) {
        TestFragment test = new TestFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(TEST_MODE, testMode);
        bundle.putInt(TYPE, type);
        bundle.putParcelableArrayList(DATES, dates);
        test.setArguments(bundle);
        return test;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //if(Build.VERSION.SDK_INT == 19)
        //    view = inflater.inflate(R.layout.fragment_test_low_api, container, false);
        //else
        View view = inflater.inflate(R.layout.fragment_test, container, false);

        initViews(view);

        refreshAnswerCount();

        mHandler = new Handler();
        refreshRunnable = new Runnable() {
            @Override
            public void run() {
                if (testMode && (WrongAnswers + RightAnswers == 20))
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

        switch (type) {
            case ONLY_DATES:
                isDateQuestion = true;
                break;
            case ONLY_EVENTS:
                isDateQuestion = false;
                break;
        }
        if (testMode) {
            progressBar.setVisibility(View.VISIBLE);
            uniqueQuestionIndexes = new ArrayList<>(20);
        }
        setTestInfo();
        setQuestions();

        return view;
    }

    private void initViews(View view) {
        questionView = view.findViewById(R.id.test_info);
//        rightAnswersView = view.findViewById(R.id.right_answers);
//        wrongAnswersView = view.findViewById(R.id.wrong_answers);
        backButton = view.findViewById(R.id.test_back_button);
        helpButton = view.findViewById(R.id.test_help_button);
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

        backButton.setImageResource(R.drawable.ic_arrow_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        helpButton.setImageResource(R.drawable.ic_help);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
//        wrongAnswersView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.thumb_down_selector, 0);
//        rightAnswersView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.thumb_up_selector, 0, 0, 0);
    }

    private void setTestInfo() {
        Random random = new Random();
        int length = dates.size();
        RightButton = random.nextInt(4);
        for (int i = 0; i < 4; i++) {
            int r = random.nextInt(length);
            Date date = (dates.get(r));
            if (isDateAlreadyUsed(date, i))
                i--;                       //Повторить итерацию заново
            else
                questions.add(date);

            if (testMode && i == RightButton) { //В случае режима теста
                if (uniqueQuestionIndexes.contains(r))
                    i--;
                else
                    uniqueQuestionIndexes.add(r);
            }

        }
        if (type == MIXED)
            isDateQuestion = random.nextBoolean();
    }


    private boolean isDateAlreadyUsed(Date date, int index) {   //Проверить, если дата уже есть в списке или если событие с такой же датой уже присутствует
        if (questions.contains(date))
            return true;
        for (int i = 0; i < index; i++) {
            if (questions.get(i).isSameDate(date))
                return true;
        }
        return false;
    }

    public void setQuestions() {
        Date date;
        for (int i = 0; i < 4; i++) {
            date = questions.get(i);
            if (isDateQuestion)
                answerButtons[i].setText(date.getDate());
            else
                answerButtons[i].setText(date.getEvent());
            answerButtons[i].setBackgroundColor(getResources().getColor(android.R.color.white));
        }
        if (isDateQuestion)
            questionView.setText(questions.get(RightButton).getEvent());
        else
            questionView.setText(questions.get(RightButton).getDate());
        clicked = false;
    }


    public void CheckResult(int position) {
        if (clicked)
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
        if (testMode)
            progressBar.incrementProgressBy(1);
        answerButtons[RightButton].setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        questions.clear();
        mHandler.postDelayed(refreshRunnable, 950); ///////
        setTestInfo();
    }

    private void refreshAnswerCount() {
//        wrongAnswersView.setText(Integer.toString(WrongAnswers));
//        rightAnswersView.setText(Integer.toString(RightAnswers));
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

    @Override
    public void reset() {

        if (Appodeal.isLoaded(Appodeal.INTERSTITIAL))
            Appodeal.show(getActivity(), Appodeal.INTERSTITIAL);

        WrongAnswers = RightAnswers = 0;
//        rightAnswersView.setText(Integer.toString(WrongAnswers));
//        wrongAnswersView.setText(Integer.toString(RightAnswers));
        progressBar.setProgress(0);

        setQuestions();
    }

    @Override
    public void exit() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private void setResultScreen() {

        String mark;
        int color;

        if (RightAnswers < 5)
            mark = getString(R.string.mark_very_bad);
        else if (RightAnswers < 9)
            mark = getString(R.string.mark_bad);
        else if (RightAnswers < 13)
            mark = getString(R.string.mark_neutral);
        else if (RightAnswers < 17)
            mark = getString(R.string.mark_good);
        else
            mark = getString(R.string.mark_very_good);

        if (RightAnswers > 9)
            color = getResources().getColor(android.R.color.holo_green_light);
        else
            color = getResources().getColor(android.R.color.holo_red_light);

        new ResultDialog(RightAnswers, mark, color, this).showDialog(getActivity());

    }

}