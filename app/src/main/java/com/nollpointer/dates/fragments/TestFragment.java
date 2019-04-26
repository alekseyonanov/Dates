package com.nollpointer.dates.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.appodeal.ads.Appodeal;
import com.google.android.material.chip.Chip;
import com.nollpointer.dates.Date;
import com.nollpointer.dates.R;
import com.nollpointer.dates.dialogs.ResultDialog;
import com.nollpointer.dates.dialogs.TestSettingsDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.nollpointer.dates.constants.PractiseConstants.DATES;
import static com.nollpointer.dates.constants.PractiseConstants.DIFFICULTY;
import static com.nollpointer.dates.constants.PractiseConstants.MIXED;
import static com.nollpointer.dates.constants.PractiseConstants.ONLY_DATES;
import static com.nollpointer.dates.constants.PractiseConstants.TEST_MODE;
import static com.nollpointer.dates.constants.PractiseConstants.TYPE;


public class TestFragment extends Fragment implements ResultDialog.ResultDialogCallbackListener {

    private TextView questionTextView;

    private Chip questionNumberChip;
    private Chip rightAnswersChip;
    private Chip wrongAnswersChip;

    private List<Button> answerButtons;

    private List<Date> dates;
    private int type;
    private boolean testMode;
    private int difficulty;

    private Date currentDate;

    private int rightAnswerColor = 0xFF43a047;
    private int wrongAnswerColor = 0xFFB71C1C;

    private boolean isLocked = false;

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(isLocked)
                return;

            lockAnswerButtons();

            Button button = (Button) v;

            int rightAnswersCount = Integer.parseInt(rightAnswersChip.getText().toString());
            int wrongAnswersCount = Integer.parseInt(wrongAnswersChip.getText().toString());

            if (currentDate.getDate().equals(button.getText().toString()) || currentDate.getEvent().equals(button.getText().toString())) {
                //Toast.makeText(getContext(), "Correct", Toast.LENGTH_SHORT).show();
                button.setBackgroundColor(rightAnswerColor);
                button.setTextColor(Color.WHITE);
                rightAnswersCount++;

            } else {
                //Toast.makeText(getContext(), "InCorrect", Toast.LENGTH_SHORT).show();
                button.setBackgroundColor(wrongAnswerColor);
                button.setTextColor(Color.WHITE);
                wrongAnswersCount++;

                for (Button answerButton : answerButtons) {
                    if (currentDate.getDate().equals(answerButton.getText().toString()) || currentDate.getEvent().equals(answerButton.getText().toString())) {
                        answerButton.setBackgroundColor(rightAnswerColor);
                        answerButton.setTextColor(Color.WHITE);
                    }
                }
            }

            rightAnswersChip.setText(Integer.toString(rightAnswersCount));
            wrongAnswersChip.setText(Integer.toString(wrongAnswersCount));


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    generateAndSetInfo();
                }
            }, 800);
        }
    };

    public static TestFragment newInstance(ArrayList<Date> dates, int type, int difficulty, boolean testMode) {
        TestFragment test = new TestFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(TEST_MODE, testMode);
        bundle.putInt(TYPE, type);
        bundle.putInt(DIFFICULTY, difficulty);
        bundle.putParcelableArrayList(DATES, dates);
        test.setArguments(bundle);
        return test;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_test, container, false);

        initializeViews(mainView);

        Bundle arguments = getArguments();

        type = arguments.getInt(TYPE);
        dates = arguments.getParcelableArrayList(DATES);
        difficulty = arguments.getInt(DIFFICULTY);
        testMode = arguments.getBoolean(TEST_MODE);

        generateAndSetInfo();

        return mainView;
    }

    private void initializeViews(View mainView) {

        ImageButton backButton = mainView.findViewById(R.id.testBackButton);
        ImageButton settingsButton = mainView.findViewById(R.id.testHelpButton);

        Appodeal.setBannerViewId(R.id.appodealBannerView);

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

        questionTextView = mainView.findViewById(R.id.testQuestion);
        questionNumberChip = mainView.findViewById(R.id.testQuestionNumber);
        rightAnswersChip = mainView.findViewById(R.id.testRightAnswers);
        wrongAnswersChip = mainView.findViewById(R.id.testWrongAnswers);

        ArrayList<Button> list = new ArrayList<>();

        ViewGroup group = mainView.findViewById(R.id.testButtonContainer);
        for (int i = 0; i < group.getChildCount(); i++) {
            Button button = (Button) group.getChildAt(i);
            button.setOnClickListener(listener);
            list.add(button);
        }

        answerButtons = list;
    }

    private void lockAnswerButtons() {
        isLocked = true;
    }

    private void unlockAnswerButtons() {
        isLocked = false;
    }

    private void generateAndSetInfo() {
        Date questionDate = generateQuestion();
        List<Date> answerDates = generateAnswers(questionDate);

        setDefaultColorForAnswerButtons();

        setInfo(questionDate, answerDates);
        unlockAnswerButtons();
    }

    private void setDefaultColorForAnswerButtons() {
        for (Button button : answerButtons) {
            button.setBackgroundColor(Color.WHITE);
            button.setTextColor(0xff808080);
        }
    }

    private Date generateQuestion() {
        Random random = new Random(System.currentTimeMillis());

        currentDate = dates.get(random.nextInt(dates.size()));

        return currentDate;
    }

    private List<Date> generateAnswers(Date questionDate) {
        Random random = new Random(System.currentTimeMillis());

        ArrayList<Date> answerDates = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Date answerDate = dates.get(random.nextInt(dates.size()));
            if (questionDate.equals(answerDate) || answerDates.contains(answerDate)) {    //TODO даты с одинаковым годом
                i--;
                continue;
            } else
                answerDates.add(answerDate);
        }

        answerDates.add(questionDate);

        Collections.shuffle(answerDates);

        return answerDates;
    }

    private void setInfo(Date questionDate, List<Date> answerDates) {

        int currentType;
        if (type == MIXED) {
            currentType = new Random().nextInt(2);
        } else
            currentType = type;

        if (currentType == ONLY_DATES) {
            questionTextView.setText(questionDate.getEvent());

            for (int i = 0; i < answerDates.size(); i++) {
                answerButtons.get(i).setText(answerDates.get(i).getDate());
            }
        } else {
            questionTextView.setText(questionDate.getDate());

            for (int i = 0; i < answerDates.size(); i++) {
                answerButtons.get(i).setText(answerDates.get(i).getEvent());
            }

        }

        int rightAnswersCount = Integer.parseInt(rightAnswersChip.getText().toString());
        int wrongAnswersCount = Integer.parseInt(wrongAnswersChip.getText().toString());
        questionNumberChip.setText("#" + (rightAnswersCount + wrongAnswersCount + 1));
    }


    @Override
    public void reset() {
        if (Appodeal.isLoaded(Appodeal.INTERSTITIAL))
            Appodeal.show(getActivity(), Appodeal.INTERSTITIAL);

    }

    @Override
    public void exit() {
        getActivity().getSupportFragmentManager().popBackStack();
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

}



/*
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

}*/
