package com.nollpointer.dates.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.nollpointer.dates.Date;
import com.nollpointer.dates.R;
import com.nollpointer.dates.dialogs.TestHelpDialog;
import com.nollpointer.dates.dialogs.TestSettingsDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.nollpointer.dates.constants.PractiseConstants.DATES;
import static com.nollpointer.dates.constants.PractiseConstants.DIFFICULTY;
import static com.nollpointer.dates.constants.PractiseConstants.TEST_MODE;
import static com.nollpointer.dates.constants.PractiseConstants.TYPE;

public class TrueFalseFragment extends Fragment {

    private Button trueButton;
    private Button falseButton;

    private TextView dateTextView;
    private TextView eventTextView;

    private Chip questionNumberChip;
    private Chip rightAnswersChip;
    private Chip wrongAnswersChip;

    private ImageView resultImage;

    private boolean isLocked = false;
    private boolean isTrue = false;

    private List<Date> dates;
    private int difficulty;
    private boolean isTestMode;

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(isLocked)
                return;

            lockAnswerButtons();


            int rightAnswersCount = Integer.parseInt(rightAnswersChip.getText().toString());
            int wrongAnswersCount = Integer.parseInt(wrongAnswersChip.getText().toString());

            if(isTrue && v.equals(trueButton) || !isTrue && v.equals(falseButton)){
                showCorrectImage();
                rightAnswersCount++;
            }else {
                showMistakeImage();
                wrongAnswersCount++;
            }

            if(rightAnswersCount + wrongAnswersCount == 20 && isTestMode)
                getFragmentManager().beginTransaction().replace(R.id.frameLayout, new PractiseResultFragment()).commit();


            rightAnswersChip.setText(Integer.toString(rightAnswersCount));
            wrongAnswersChip.setText(Integer.toString(wrongAnswersCount));

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    generateAndSetInfo();
                }
            },900);
        }
    };

    public static TrueFalseFragment newInstance(ArrayList<Date> dates, int difficulty, boolean testMode) {
        TrueFalseFragment fragment = new TrueFalseFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(TEST_MODE, testMode);
        bundle.putInt(DIFFICULTY, difficulty);
        bundle.putParcelableArrayList(DATES, dates);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_true_false, container, false);


        Bundle arguments = getArguments();

        dates = arguments.getParcelableArrayList(DATES);
        difficulty = arguments.getInt(DIFFICULTY);
        isTestMode = arguments.getBoolean(TEST_MODE);

        initializeViews(mainView);

        generateAndSetInfo();

        return mainView;
    }

    private void initializeViews(View mainView) {
        //Appodeal.setBannerViewId(R.id.appodealBannerView_true);
        dateTextView = mainView.findViewById(R.id.test_info_true_false_date);
        eventTextView = mainView.findViewById(R.id.test_info_true_false_event);

        ImageButton backButton = mainView.findViewById(R.id.true_false_back_button);
        ImageButton settingsButton = mainView.findViewById(R.id.true_false_settings_button);
        ImageButton helpButton = mainView.findViewById(R.id.true_false_help_button);

        trueButton = mainView.findViewById(R.id.true_button);
        trueButton.setOnClickListener(listener);

        falseButton = mainView.findViewById(R.id.false_button);
        falseButton.setOnClickListener(listener);

        backButton.setImageResource(R.drawable.ic_arrow_back_white);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        settingsButton.setImageResource(R.drawable.ic_settings);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestSettingsDialog settingsDialog = new TestSettingsDialog();
                settingsDialog.show(getActivity().getSupportFragmentManager(), null);
            }
        });

        helpButton.setImageResource(R.drawable.ic_help);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestHelpDialog helpDialog = new TestHelpDialog();
                helpDialog.show(getActivity().getSupportFragmentManager(), null);
            }
        });

        questionNumberChip = mainView.findViewById(R.id.trueFalseQuestionNumber);
        rightAnswersChip = mainView.findViewById(R.id.trueFalseRightAnswers);
        wrongAnswersChip = mainView.findViewById(R.id.trueFalseWrongAnswers);

        resultImage = mainView.findViewById(R.id.true_false_result_image);

    }

    private void lockAnswerButtons() {
        isLocked = true;
    }

    private void unlockAnswerButtons() {
        isLocked = false;
    }

    private void generateAndSetInfo() {

        isTrue = generateRandomBoolean();

        Date questionDate = generateDate();

        String date = questionDate.getDate();
        String event;

        if(isTrue)
            event = questionDate.getEvent();
        else{
            Date anotherDate = generateDate();
            while(anotherDate.equals(questionDate))
                anotherDate = generateDate();

            event = anotherDate.getEvent();
        }

        hideResultImage();

        setInfo(date,event);
        unlockAnswerButtons();
    }

    private boolean generateRandomBoolean(){

        Random random = new Random(System.currentTimeMillis());

        return random.nextBoolean();

    }

    private Date generateDate(){

        Random random = new Random(System.currentTimeMillis());

        return dates.get(random.nextInt(dates.size()));
    }

    private void setInfo(String date, String event){
        dateTextView.setText(date);
        eventTextView.setText(event);

        int rightAnswersCount = Integer.parseInt(rightAnswersChip.getText().toString());
        int wrongAnswersCount = Integer.parseInt(wrongAnswersChip.getText().toString());
        questionNumberChip.setText("#" + (rightAnswersCount + wrongAnswersCount + 1));
    }

    private void showCorrectImage(){
        resultImage.setImageResource(R.drawable.ic_correct);
        resultImage.setVisibility(View.VISIBLE);
    }

    private void showMistakeImage(){
        resultImage.setImageResource(R.drawable.ic_mistake);
        resultImage.setVisibility(View.VISIBLE);
    }

    private void hideResultImage(){
        resultImage.setVisibility(View.INVISIBLE);
    }

//    private void checkResult(boolean opinion) {
//        if (isCorrect == opinion) {
//            right_answers_count++;
//            //RightAnswerCountView.setText(Integer.toString(right_answers_count));
//            QuestionDate.setTextColor(greenColor);
//            QuestionEvent.setTextColor(greenColor);
//        } else {
//            wrong_answers_count++;
//            //WrongAnswersCountView.setText(Integer.toString(wrong_answers_count));
//            QuestionDate.setTextColor(redColor);
//            QuestionEvent.setTextColor(redColor);
//        }
//        if (testMode)
//            progressBar.incrementProgressBy(1);
//        setTestInfo();
//        mHandler.postDelayed(RefreshRunnable, 850);
//
//    }
//
//    private void setTestInfo() {
//        Random random = new Random();
//        isCorrect = random.nextBoolean();
//        int r;
//        Date randomDate;
//        int size = dates.size();
//        r = random.nextInt(size);
//        if (testMode) {
//            while (uniqueDates.contains(r))
//                r = random.nextInt(size);
//            uniqueDates.add(r);
//        }
//        randomDate = dates.get(r);
//        date = randomDate.getDate();
//        if (isCorrect)
//            event = randomDate.getEvent();
//        else {
//            r = random.nextInt(size);
//            randomDate = dates.get(r);
//            while (date.equals(randomDate.getDate())) {
//                r = random.nextInt(dates.size());
//                randomDate = dates.get(r);
//            }
//            event = randomDate.getEvent();
//        }
//    }
//
//    private void setQuestions() {
//        QuestionDate.setText(date);
//        QuestionEvent.setText(event);
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mHandler.removeCallbacks(RefreshRunnable);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        Appodeal.hide(getActivity(), Appodeal.BANNER_VIEW);
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        Appodeal.show(getActivity(), Appodeal.BANNER_VIEW);
//    }
//
//    @Override
//    public void reset() {
//
//        if (Appodeal.isLoaded(Appodeal.INTERSTITIAL))
//            Appodeal.show(getActivity(), Appodeal.INTERSTITIAL);
//
//        wrong_answers_count = right_answers_count = 0;
////        WrongAnswersCountView.setText(Integer.toString(wrong_answers_count));
////        RightAnswerCountView.setText(Integer.toString(right_answers_count));
//        progressBar.setProgress(0);
//
//        setQuestions();
//    }
//
//    @Override
//    public void exit() {
//        getActivity().getSupportFragmentManager().popBackStack();
//    }
//
//    private void setResultScreen() {
//
//        String mark;
//        int color;
//
//        if (right_answers_count < 5)
//            mark = getString(R.string.mark_very_bad);
//        else if (right_answers_count < 9)
//            mark = getString(R.string.mark_bad);
//        else if (right_answers_count < 13)
//            mark = getString(R.string.mark_neutral);
//        else if (right_answers_count < 17)
//            mark = getString(R.string.mark_good);
//        else
//            mark = getString(R.string.mark_very_good);
//
//        if (right_answers_count > 9)
//            color = getResources().getColor(android.R.color.holo_green_light);
//        else
//            color = getResources().getColor(android.R.color.holo_red_light);
//
//        new ResultDialog(right_answers_count, mark, color, this).showDialog(getActivity());
//
//    }
}


/*
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


*/
