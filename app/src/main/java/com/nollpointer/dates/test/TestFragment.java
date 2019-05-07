package com.nollpointer.dates.test;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.appodeal.ads.Appodeal;
import com.google.android.material.chip.Chip;
import com.nollpointer.dates.other.Date;
import com.nollpointer.dates.R;
import com.nollpointer.dates.other.ResultDialog;
import com.nollpointer.dates.other.PractiseHelpDialog;
import com.nollpointer.dates.other.PractiseSettingsDialog;
import com.nollpointer.dates.practise.PractiseResult;
import com.nollpointer.dates.practise.PractiseResultFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.nollpointer.dates.practise.PractiseConstants.DATES;
import static com.nollpointer.dates.practise.PractiseConstants.DIFFICULTY;
import static com.nollpointer.dates.practise.PractiseConstants.MIXED;
import static com.nollpointer.dates.practise.PractiseConstants.ONLY_DATES;
import static com.nollpointer.dates.practise.PractiseConstants.TEST;
import static com.nollpointer.dates.practise.PractiseConstants.TEST_MODE;
import static com.nollpointer.dates.practise.PractiseConstants.TYPE;


public class TestFragment extends Fragment implements ResultDialog.ResultDialogCallbackListener {

    private TextView questionTextView;

    private Chip questionNumberChip;
    private Chip rightAnswersChip;
    private Chip wrongAnswersChip;

    private List<Button> answerButtons;

    private List<Date> dates;
    private int type;
    private boolean isTestMode;
    private int difficulty;

    private ArrayList<PractiseResult> practiseResults = new ArrayList<>();

    private Date currentDate;

    private int rightAnswerColor = 0xFF43a047;
    private int wrongAnswerColor = 0xFFB71C1C;

    private boolean isLocked = false;

    private int delay = 900;

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(isLocked)
                return;

            lockAnswerButtons();

            Button button = (Button) v;

            int rightAnswersCount = Integer.parseInt(rightAnswersChip.getText().toString());
            int wrongAnswersCount = Integer.parseInt(wrongAnswersChip.getText().toString());

            String buttonText = button.getText().toString();

            boolean isCorrect = buttonText.contains(currentDate.getDate()) || buttonText.contains(currentDate.getEvent());
            if(currentDate.containsMonth())
                isCorrect &= buttonText.contains(currentDate.getMonth());

            if (isCorrect) {
                button.setBackgroundColor(rightAnswerColor);
                button.setTextColor(Color.WHITE);
                rightAnswersCount++;

            } else {
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

            if(isTestMode){
                PractiseResult practiseResult = new PractiseResult(questionTextView.getText().toString(),isCorrect);
                practiseResults.add(practiseResult);
            }

            if(rightAnswersCount + wrongAnswersCount == 20 && isTestMode)
                getFragmentManager().beginTransaction().replace(R.id.frameLayout, PractiseResultFragment.newInstance(TEST,practiseResults,getArguments())).commit();

            rightAnswersChip.setText(Integer.toString(rightAnswersCount));
            wrongAnswersChip.setText(Integer.toString(wrongAnswersCount));


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    generateAndSetInfo();
                }
            }, delay);
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
        isTestMode = arguments.getBoolean(TEST_MODE);

        delay = getDelay();

        generateAndSetInfo();

        return mainView;
    }

    private void initializeViews(View mainView) {

        ImageButton backButton = mainView.findViewById(R.id.testBackButton);
        final ImageButton settingsButton = mainView.findViewById(R.id.testSettingsButton);
        ImageButton helpButton = mainView.findViewById(R.id.testHelpButton);

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
                PractiseSettingsDialog settingsDialog = PractiseSettingsDialog.newInstance(delay);
                settingsDialog.setListener(new PractiseSettingsDialog.Listener() {
                    @Override
                    public void onDelayPicked(int delay) {
                        setDelay(delay);
                    }
                });
                settingsDialog.show(getActivity().getSupportFragmentManager(), null);
            }
        });

        helpButton.setImageResource(R.drawable.ic_help);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PractiseHelpDialog helpDialog = new PractiseHelpDialog();
                helpDialog.show(getActivity().getSupportFragmentManager(), null);
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
                Date date = answerDates.get(i);
                String text = date.getDate();
                if(date.containsMonth())
                    text += ", " + date.getMonth();

                answerButtons.get(i).setText(text);

            }
        } else {
            String text = questionDate.getDate();
            if(questionDate.containsMonth()){
                text += ", " + questionDate.getMonth();
            }
            questionTextView.setText(text);

            for (int i = 0; i < answerDates.size(); i++) {
                answerButtons.get(i).setText(answerDates.get(i).getEvent());
            }

        }

        int rightAnswersCount = Integer.parseInt(rightAnswersChip.getText().toString());
        int wrongAnswersCount = Integer.parseInt(wrongAnswersChip.getText().toString());
        questionNumberChip.setText("#" + (rightAnswersCount + wrongAnswersCount + 1));
    }

    private int getDelay(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        int delay = preferences.getInt("test delay", 900);

        return delay;

    }

    private void setDelay(int delay){
        this.delay = delay;

        SharedPreferences.Editor preferences = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();

        preferences.putInt("test delay", delay);

        preferences.apply();
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

    private boolean isTestMode;
    private boolean isDateQuestion;
    private boolean clicked = false;

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CheckResult((int) view.getTag());
        }
    };

    public static TestFragment newInstance(ArrayList<Date> dates, int type, boolean isTestMode) {
        TestFragment test = new TestFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(TEST_MODE, isTestMode);
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
                if (isTestMode && (WrongAnswers + RightAnswers == 20))
                    setResultScreen();
                else
                    setQuestions();
            }
        };

        Bundle saved = getArguments();
        type = saved.getInt(TYPE);
        isTestMode = saved.getBoolean(TEST_MODE);
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
        if (isTestMode) {
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

            if (isTestMode && i == RightButton) { //В случае режима теста
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
        if (isTestMode)
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
