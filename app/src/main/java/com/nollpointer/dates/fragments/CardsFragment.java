package com.nollpointer.dates.fragments;


import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nollpointer.dates.Date;
import com.nollpointer.dates.MainActivity;
import com.nollpointer.dates.R;

import java.util.ArrayList;
import java.util.Random;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.shape.NoShape;

import static com.nollpointer.dates.constants.PractiseConstants.DATES;
import static com.nollpointer.dates.constants.PractiseConstants.MIXED;
import static com.nollpointer.dates.constants.PractiseConstants.ONLY_DATES;
import static com.nollpointer.dates.constants.PractiseConstants.ONLY_EVENTS;
import static com.nollpointer.dates.constants.PractiseConstants.TYPE;

public class CardsFragment extends Fragment {
    private int type;
    private ArrayList<Date> dates;
    private Date currentDate;
    private TextView mainTextView;
    private boolean isDateQuestion = false;

    public static CardsFragment newInstance(ArrayList<Date> dates, int type) {
        CardsFragment test = new CardsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE, type);
        bundle.putParcelableArrayList(DATES, dates);
        test.setArguments(bundle);
        return test;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.fragment_cards, container, false);
        MainActivity ctx = (MainActivity) getActivity();

        Bundle saved = getArguments();
        type = saved.getInt(TYPE);
        dates = saved.getParcelableArrayList(DATES);
        switch (type) {
            case ONLY_DATES:
                isDateQuestion = true;
                break;
            case ONLY_EVENTS:
                isDateQuestion = false;
                break;
        }

        initializeViews(mainView);

        setQuestion();

        if (ctx.isFirstTime(MainActivity.CARDS))
            new MaterialShowcaseView.Builder(ctx)
                    .setTarget(mainView)
                    .setDelay(200)
                    .setContentText(R.string.tutorial_cards)
                    .setDismissText(R.string.got_it)
                    .setDismissOnTouch(true)
                    .setDismissTextColor(Color.GREEN)
                    .setMaskColour(getResources().getColor(R.color.colorMask))
                    .setShape(new NoShape())
                    .show();
        return mainView;
    }

    private void initializeViews(View mainView){
        mainTextView = mainView.findViewById(R.id.date_cards_text);

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            mainTextView.setBreakStrategy(Layout.BREAK_STRATEGY_SIMPLE);

        Button nextButton = mainView.findViewById(R.id.cards_next_date);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setQuestion();
            }
        });

        Button descriptionButton = mainView.findViewById(R.id.cards_description_date);
        descriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAnswer();
            }
        });

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            Toast.makeText(getContext(), "landscape", Toast.LENGTH_SHORT).show();
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            Toast.makeText(getContext(), "portrait", Toast.LENGTH_SHORT).show();
//        }

        try {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAnswer() {
        mainTextView.setText(currentDate.getDate() + "\n" + currentDate.getEvent());
        //mainTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 40);
    }

    public void setQuestion() {
        setRandomDate();
        //mainTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 45);
        if (isDateQuestion)
            mainTextView.setText(currentDate.getDate());
        else
            mainTextView.setText(currentDate.getEvent());
    }

    public void setRandomDate() {
        Random random = new Random();
        int x = random.nextInt(dates.size());
        currentDate = dates.get(x);
        if (type == MIXED)
            isDateQuestion = random.nextBoolean();
    }
}
