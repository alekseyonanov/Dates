package com.nollpointer.dates.fragments;


import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appodeal.ads.Appodeal;
import com.nollpointer.dates.Date;
import com.nollpointer.dates.MainActivity;
import com.nollpointer.dates.R;
import com.nollpointer.dates.dialogs.ResultDialog;
import com.nollpointer.dates.views.SortCards;
import com.nollpointer.dates.views.SortCardsControl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.shape.NoShape;

import static com.nollpointer.dates.MainActivity.DATES;
import static com.nollpointer.dates.MainActivity.SORT;
import static com.nollpointer.dates.MainActivity.SORT_CHECK;
import static com.nollpointer.dates.constants.PractiseConstants.TEST_MODE;

public class SortFragment extends Fragment implements ResultDialog.ResultDialogCallbackListener {

    private View mainView;
    private TextView RightAnswers, WrongAnswers;
    private TextView instructions;
    private ProgressBar progressBar;
    private SortCardsControl cardsControl;


    private int[] RightSequence = new int[3];
    private List<String> events;
    private int right_answers_count = 0, wrong_answers_count = 0, best_result = 0;


    private ArrayList<Date> dates;
    private Button check_button;


    //private boolean isResultScreenOn = false;
    private boolean isCheckMode = false;
    private boolean testMode;
    private boolean isFirstTimeCheck = true;


    public static SortFragment newInstance(ArrayList<Date> dates, boolean testMode) {
        SortFragment sort = new SortFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(TEST_MODE, testMode);
        bundle.putParcelableArrayList(DATES, dates);
        sort.setArguments(bundle);
        return sort;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.fragment_sort, container, false);
        MainActivity ctx = (MainActivity) getActivity();

        Bundle saved = getArguments();
        dates = saved.getParcelableArrayList(DATES);
        testMode = saved.getBoolean(TEST_MODE);

        initViews();

        if (testMode)
            progressBar.setVisibility(View.VISIBLE);

        setQuestionInfo();
        setQuestions();

        isFirstTimeCheck = ctx.isFirstTime(SORT_CHECK);
        if (ctx.isFirstTime(SORT))
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

    private void initViews() {
        RightAnswers = mainView.findViewById(R.id.right_answers);
        WrongAnswers = mainView.findViewById(R.id.wrong_answers);
        instructions = mainView.findViewById(R.id.instruction_sort);
        progressBar = mainView.findViewById(R.id.sort_progressbar);
        check_button = mainView.findViewById(R.id.sort_check);
        cardsControl = SortCards.newInstance(mainView);

        RightAnswers.setText("0");
        WrongAnswers.setText("0");

        check_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
            }
        });

        Resources resources = getResources();
        cardsControl.setColors(resources.getColor(android.R.color.holo_green_light), resources.getColor(android.R.color.holo_red_light));

        Appodeal.setBannerViewId(R.id.appodealBannerView_sort);

        WrongAnswers.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.thumb_down_selector, 0);
        RightAnswers.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.thumb_up_selector, 0, 0, 0);
    }

    public void check() {

        isCheckMode = !isCheckMode;

        if (isCheckMode) {
            if (isFirstTimeCheck)
                showFirstTimeCheck();
            boolean isCorrect = cardsControl.check();
            incrementScore(isCorrect);

            if (testMode && wrong_answers_count + right_answers_count == 20)
                setResultScreen();
            check_button.setText(R.string.next_sort);
            setQuestionInfo();
        } else {
            check_button.setText(R.string.check_button);
            setQuestions();
        }

        cardsControl.setCheckMode(isCheckMode);
    }

    public void showFirstTimeCheck() {
        new MaterialShowcaseView.Builder(getActivity())
                .setTarget(mainView)
                .setDelay(150)
                .setContentText(R.string.tutorial_sort_check)
                .setDismissText(R.string.got_it)
                .setDismissOnTouch(true)
                .setDismissTextColor(Color.GREEN)
                .setMaskColour(getResources().getColor(R.color.colorMask))
                .setShape(new NoShape())
                .show();
        isFirstTimeCheck = false;
    }

    public void incrementScore(boolean isCorrect) {
        if (isCorrect)
            right_answers_count++;
        else
            wrong_answers_count++;
        WrongAnswers.setText(Integer.toString(wrong_answers_count));
        RightAnswers.setText(Integer.toString(right_answers_count));
        if (testMode)
            progressBar.incrementProgressBy(1);
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

        wrong_answers_count = right_answers_count = 0;
        WrongAnswers.setText(Integer.toString(wrong_answers_count));
        RightAnswers.setText(Integer.toString(right_answers_count));
        progressBar.setProgress(0);

        setQuestions();
    }

    @Override
    public void exit() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private void setQuestionInfo() {
        Random random = new Random();
        ArrayList<Date> list = new ArrayList<>(3);
        int length = dates.size();
        for (int i = 0; i < 3; i++) {
            int rand = random.nextInt(length);
            Date date = dates.get(rand);
            if (list.contains(date) || date.isContinuous()) {
                i--;
                continue;
            }
            list.add(date);
            RightSequence[i] = rand;
        }
        prepareEventList(list);
        prepareRightSequence();
    }

    private void prepareEventList(List<Date> dateList) {
        ArrayList<String> eventList = new ArrayList<>(3);
        for (Date date : dateList) {
            eventList.add(date.getEvent());
        }
        events = eventList;
    }

    private void prepareRightSequence() {
        int maxIndex = 0;
        int minIndex = 0;
        int middleIndex = 0;
        for (int i = 0; i < 3; i++) {
            if (RightSequence[maxIndex] < RightSequence[i])
                maxIndex = i;
            if (RightSequence[minIndex] > RightSequence[i])
                minIndex = i;
        }
        for (int i = 0; i < 3; i++)
            if (i != minIndex && i != maxIndex) {
                middleIndex = i;
                break;
            }

        RightSequence[minIndex] = 1;
        RightSequence[middleIndex] = 2;
        RightSequence[maxIndex] = 3;
    }

    private void setQuestions() {
        cardsControl.setAnswerSequence(RightSequence);
        cardsControl.setQuestions(events);
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
