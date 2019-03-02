package com.nollpointer.dates.fragments;


import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appodeal.ads.Appodeal;
import com.nollpointer.dates.Date;
import com.nollpointer.dates.MainActivity;
import com.nollpointer.dates.R;
import com.nollpointer.dates.dialogs.ResultDialog;

import java.util.ArrayList;
import java.util.Random;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.shape.NoShape;

import static com.nollpointer.dates.constants.PractiseConstants.DATES;
import static com.nollpointer.dates.constants.PractiseConstants.TEST_MODE;

public class TrueFalseFragment extends Fragment implements ResultDialog.ResultDialogCallbackListener {

    private TextView QuestionDate, QuestionEvent;
//    private TextView RightAnswerCountView, WrongAnswersCountView;
    private Button trueButton, falseButton;
    private ProgressBar progressBar;
    private ImageButton backButton, helpButton;

    private int right_answers_count = 0, wrong_answers_count = 0;

    private Handler mHandler;
    private Runnable RefreshRunnable;

    private String date, event;
    private boolean isCorrect;

    private ArrayList<Date> dates;

    private ArrayList<Integer> uniqueDates;
    private int bestResult = 0;

    private boolean clicked = false;
    private boolean testMode;

    private int greenColor, redColor, blackColor;

    public static TrueFalseFragment newInstance(ArrayList<Date> dates, boolean testMode) {
        TrueFalseFragment fragment = new TrueFalseFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(TEST_MODE, testMode);
        bundle.putParcelableArrayList(DATES, dates);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //if(Build.VERSION.SDK_INT == 19)
        //    mainView =inflater.inflate(R.layout.fragment_true_false_low_api, container, false);
        //else
        View mainView = inflater.inflate(R.layout.fragment_true_false, container, false);
        initViews(mainView);
//        WrongAnswersCountView.setText("0");
//        RightAnswerCountView.setText("0");

        Bundle saved = getArguments();
        dates = saved.getParcelableArrayList(DATES);
        testMode = saved.getBoolean(TEST_MODE);
        if (testMode) {
            uniqueDates = new ArrayList<>(20);
            progressBar.setVisibility(View.VISIBLE);
        }
        mHandler = new Handler();
        RefreshRunnable = new Runnable() {
            @Override
            public void run() {
                clicked = false;
                QuestionDate.setTextColor(blackColor);
                QuestionEvent.setTextColor(blackColor);
                if (testMode && wrong_answers_count + right_answers_count == 20)
                    setResultScreen();
                else
                    setQuestions();
            }
        };
        Resources resources = getResources();
        redColor = resources.getColor(R.color.colorFalseButton);
        greenColor = resources.getColor(R.color.colorTrueButton);
        blackColor = resources.getColor(android.R.color.black);

        setTestInfo();
        setQuestions();

        MainActivity activity = (MainActivity) getActivity();
        if (activity.isFirstTime(MainActivity.TRUE_FALSE))
            new MaterialShowcaseView.Builder(activity)
                    .setTarget(mainView)
                    .setDelay(200)
                    .setContentText(R.string.tutorial_true_false)
                    .setDismissText(R.string.got_it)
                    .setDismissOnTouch(true)
                    .setDismissTextColor(Color.GREEN)
                    .setMaskColour(getResources().getColor(R.color.colorMask))
                    .setShape(new NoShape())
                    .show();
        return mainView;
    }

    private void initViews(View mainView) {
        progressBar = mainView.findViewById(R.id.true_false_progressbar);
        Appodeal.setBannerViewId(R.id.appodealBannerView_true);
        QuestionDate = mainView.findViewById(R.id.test_info_true_false_date);
        QuestionEvent = mainView.findViewById(R.id.test_info_true_false_event);
//        RightAnswerCountView = mainView.findViewById(R.id.right_answers_true_false);
//        WrongAnswersCountView = mainView.findViewById(R.id.wrong_answers_true_false);
        backButton = mainView.findViewById(R.id.true_false_back_button);
        helpButton = mainView.findViewById(R.id.true_false_help_button);
        progressBar = mainView.findViewById(R.id.true_false_progressbar);
        trueButton = mainView.findViewById(R.id.true_button);
        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!clicked) {
                    checkResult(true);
                    clicked = true;
                }
            }
        });
        falseButton = mainView.findViewById(R.id.false_button);
        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!clicked) {
                    checkResult(false);
                    clicked = true;
                }
            }
        });

//        WrongAnswersCountView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.thumb_down_selector, 0);
//        RightAnswerCountView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.thumb_up_selector, 0, 0, 0);
        backButton.setImageResource(R.drawable.ic_arrow_back_white);
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
    }

    private void checkResult(boolean opinion) {
        if (isCorrect == opinion) {
            right_answers_count++;
            //RightAnswerCountView.setText(Integer.toString(right_answers_count));
            QuestionDate.setTextColor(greenColor);
            QuestionEvent.setTextColor(greenColor);
        } else {
            wrong_answers_count++;
            //WrongAnswersCountView.setText(Integer.toString(wrong_answers_count));
            QuestionDate.setTextColor(redColor);
            QuestionEvent.setTextColor(redColor);
        }
        if (testMode)
            progressBar.incrementProgressBy(1);
        setTestInfo();
        mHandler.postDelayed(RefreshRunnable, 850);

    }

    private void setTestInfo() {
        Random random = new Random();
        isCorrect = random.nextBoolean();
        int r;
        Date randomDate;
        int size = dates.size();
        r = random.nextInt(size);
        if (testMode) {
            while (uniqueDates.contains(r))
                r = random.nextInt(size);
            uniqueDates.add(r);
        }
        randomDate = dates.get(r);
        date = randomDate.getDate();
        if (isCorrect)
            event = randomDate.getEvent();
        else {
            r = random.nextInt(size);
            randomDate = dates.get(r);
            while (date.equals(randomDate.getDate())) {
                r = random.nextInt(dates.size());
                randomDate = dates.get(r);
            }
            event = randomDate.getEvent();
        }
    }

    private void setQuestions() {
        QuestionDate.setText(date);
        QuestionEvent.setText(event);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mHandler.removeCallbacks(RefreshRunnable);
    }

    @Override
    public void onStop() {
        super.onStop();
        Appodeal.hide(getActivity(), Appodeal.BANNER_VIEW);
    }

    @Override
    public void onStart() {
        super.onStart();
        Appodeal.show(getActivity(), Appodeal.BANNER_VIEW);
    }

    @Override
    public void reset() {

        if (Appodeal.isLoaded(Appodeal.INTERSTITIAL))
            Appodeal.show(getActivity(), Appodeal.INTERSTITIAL);

        wrong_answers_count = right_answers_count = 0;
//        WrongAnswersCountView.setText(Integer.toString(wrong_answers_count));
//        RightAnswerCountView.setText(Integer.toString(right_answers_count));
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

        if (right_answers_count < 5)
            mark = getString(R.string.mark_very_bad);
        else if (right_answers_count < 9)
            mark = getString(R.string.mark_bad);
        else if (right_answers_count < 13)
            mark = getString(R.string.mark_neutral);
        else if (right_answers_count < 17)
            mark = getString(R.string.mark_good);
        else
            mark = getString(R.string.mark_very_good);

        if (right_answers_count > 9)
            color = getResources().getColor(android.R.color.holo_green_light);
        else
            color = getResources().getColor(android.R.color.holo_red_light);

        new ResultDialog(right_answers_count, mark, color, this).showDialog(getActivity());

    }
}
