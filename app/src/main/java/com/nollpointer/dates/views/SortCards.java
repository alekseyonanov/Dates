package com.nollpointer.dates.views;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.View;

import com.nollpointer.dates.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SortCards implements SortCardsControl {

    private static final int MAX_COUNT = 3;

    private ArrayList<SortCardView> cards;
    private int[] currentSequence = new int[MAX_COUNT];
    private int[] answerSequence = new int[MAX_COUNT];

    private boolean isCheckMode = false;

    private int GREEN = Color.GREEN;
    private int RED = Color.RED;

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int pos = getPosition(view.getId());
            onCardClick(pos);
        }
    };

    @Override
    public void setColors(int green, int red) {
        GREEN = green;
        RED = red;
    }

    public static SortCardsControl newInstance(View mainView) {
        SortCards sorts = new SortCards();
        sorts.cards = new ArrayList<>(MAX_COUNT);

        CardView cardView = mainView.findViewById(R.id.cardView0);
        sorts.cards.add(new SortCardView(cardView));
        cardView = mainView.findViewById(R.id.cardView1);
        sorts.cards.add(new SortCardView(cardView));
        cardView = mainView.findViewById(R.id.cardView2);
        sorts.cards.add(new SortCardView(cardView));

        sorts.initialize();
        return sorts;
    }

    private void initialize() {
        for (int i = 0; i < MAX_COUNT; i++) {
            SortCardView card = cards.get(i);
            card.setOnClickListener(listener);
            card.setNumber(i + 1);
            currentSequence[i] = i + 1;
        }
    }

    private void onCardClick(int position) {
        int sequenceNumber = currentSequence[position];
        sequenceNumber %= MAX_COUNT;
        sequenceNumber++;
        currentSequence[position] = sequenceNumber;
        cards.get(position).setNumber(sequenceNumber);
        if (isCheckMode)
            singleCardCheck(position);
    }


    @Override
    public void setQuestions(List<String> list) {
        for (int i = 0; i < MAX_COUNT; i++) {
            SortCardView card = cards.get(i);
            card.setBackgroundColor(Color.WHITE);
            card.setEvent(list.get(i));
            card.setNumber(i + 1);
            currentSequence[i] = i + 1;
        }
    }

    @Override
    public void setAnswerSequence(int[] sequence) {
        System.arraycopy(sequence, 0, answerSequence, 0, MAX_COUNT);
    }

    private int getPosition(int id) {
        int position = -1;
        switch (id) {
            case R.id.cardView0:
                position = 0;
                break;
            case R.id.cardView1:
                position = 1;
                break;
            case R.id.cardView2:
                position = 2;
                break;
        }
        return position;
    }

    @Override
    public void setCheckMode(boolean state) {
        isCheckMode = state;
    }

    @Override
    public boolean check() {
        boolean isCorrect = Arrays.equals(answerSequence, currentSequence);
        if (isCorrect) {
            for (SortCardView card : cards)
                card.setBackgroundColor(Color.GREEN);
        } else {
            for (int i = 0; i < MAX_COUNT; i++) {
                if (currentSequence[i] == answerSequence[i])
                    cards.get(i).setBackgroundColor(GREEN);
                else
                    cards.get(i).setBackgroundColor(RED);
            }
        }
        return isCorrect;
    }

    private void singleCardCheck(int position) {
        if (answerSequence[position] == currentSequence[position])
            cards.get(position).setBackgroundColor(GREEN);
        else
            cards.get(position).setBackgroundColor(RED);

    }
}