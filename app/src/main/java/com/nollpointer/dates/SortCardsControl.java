package com.nollpointer.dates;

import java.util.List;

public interface SortCardsControl{
    void refresh();
    void setQuestions(List<String> list);
    void setAnswerSequence(int[] sequence);
    boolean check();
    //void setOnCardClickListener();
    void setResultScreen();
    int getAnswerSequence();

    void setCheckMode(boolean state);
}