package com.nollpointer.dates;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

public class SortCardView{
    CardView card;

    TextView number;
    TextView event;

    SortCardView(CardView card) {
        this.card = card;
        number = card.findViewById(R.id.button_cardview);
        event = card.findViewById(R.id.textview_cardview);
    }

    public void setBackgroundColor(int color){
        card.setCardBackgroundColor(color);
    }

    public void setOnClickListener(View.OnClickListener listener){
        card.setOnClickListener(listener);
    }

    public void setEvent(String text){
        event.setText(text);
    }

    public void setNumber(int number){
        this.number.setText(Integer.toString(number));
    }




}
