package com.nollpointer.dates.sort

import android.graphics.Color
import android.view.View
import androidx.cardview.widget.CardView
import java.util.*

class SortCards : SortCardsControl {
    private lateinit var cards: ArrayList<SortCardView>
    private val currentSequence = IntArray(MAX_COUNT)
    private val answerSequence = IntArray(MAX_COUNT)
    private var isCheckMode = false
    private var green = Color.GREEN
    private var red = Color.RED
    private val listener = View.OnClickListener { view ->
        val pos = getPosition(view.id)
        onCardClick(pos)
    }

    override fun setColors(g: Int, r: Int) {
        green = g
        red = r
    }

    private fun initialize() {
        for (i in 0 until MAX_COUNT) {
            val card = cards[i]
            card.setOnClickListener(listener)
            card.setNumber(i + 1)
            currentSequence[i] = i + 1
        }
    }

    private fun onCardClick(position: Int) {
        var sequenceNumber = currentSequence[position]
        sequenceNumber %= MAX_COUNT
        sequenceNumber++
        currentSequence[position] = sequenceNumber
        cards[position].setNumber(sequenceNumber)
        if (isCheckMode) singleCardCheck(position)
    }

    override fun setQuestions(list: List<String?>?) {
        for (i in 0 until MAX_COUNT) {
            val card = cards[i]
            card.setBackgroundColor(Color.WHITE)
            card.setEvent(list!![i])
            card.setNumber(i + 1)
            currentSequence[i] = i + 1
        }
    }

    override fun setAnswerSequence(sequence: IntArray?) {
        System.arraycopy(sequence!!, 0, answerSequence, 0, MAX_COUNT)
    }

    private fun getPosition(id: Int): Int {
        var position = -1
        when (id) {
            0 -> position = 0
            1 -> position = 1
            2 -> position = 2
        }
        return position
    }

    override fun setCheckMode(state: Boolean) {
        isCheckMode = state
    }

    override fun check(): Boolean {
        val isCorrect = answerSequence.contentEquals(currentSequence)
        if (isCorrect) {
            for (card in cards) card.setBackgroundColor(Color.GREEN)
        } else {
            for (i in 0 until MAX_COUNT) {
                if (currentSequence[i] == answerSequence[i]) cards[i].setBackgroundColor(green) else cards[i].setBackgroundColor(red)
            }
        }
        return isCorrect
    }

    private fun singleCardCheck(position: Int) {
        if (answerSequence[position] == currentSequence[position]) cards[position].setBackgroundColor(green) else cards[position].setBackgroundColor(red)
    }

    companion object {
        private const val MAX_COUNT = 3
        fun newInstance(mainView: View): SortCardsControl {
            val sorts = SortCards()
            sorts.cards = ArrayList(MAX_COUNT)
            var cardView: CardView = mainView.findViewById(0)
            sorts.cards.add(SortCardView(cardView))
            cardView = mainView.findViewById(0)
            sorts.cards.add(SortCardView(cardView))
            cardView = mainView.findViewById(0)
            sorts.cards.add(SortCardView(cardView))
            sorts.initialize()
            return sorts
        }
    }
}