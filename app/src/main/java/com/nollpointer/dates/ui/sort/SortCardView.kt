package com.nollpointer.dates.ui.sort

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.nollpointer.dates.R

class SortCardView internal constructor(private val card: CardView) {

    private val number: TextView = card.findViewById(R.id.text1)
    private val event: TextView = card.findViewById(R.id.text2)

    fun setBackgroundColor(color: Int) {
        card.setCardBackgroundColor(color)
    }

    fun setOnClickListener(listener: View.OnClickListener?) {
        card.setOnClickListener(listener)
    }

    fun setEvent(text: String?) {
        event.text = text
    }

    fun setNumber(number: Int) {
        this.number.text = number.toString()
    }

    //public void setResultScreenText(String)

}