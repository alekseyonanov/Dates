package com.nollpointer.dates.ui.game

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.nollpointer.dates.R
import com.nollpointer.dates.other.Misc

/**
 * @author Onanov Aleksey (@onanov)
 */
class GameCardsAdapter(private val listener: GameCardsListener?) : RecyclerView.Adapter<GameCardsAdapter.ViewHolder>() {
    private var isGameMode = false
    private val rightAnswered = 0
    private var openedCards = 0
    private val rightAnswers: List<Int> = Misc.getRightAnswersList(5)

    interface GameCardsListener {
        fun onGameStart()
        fun onGameEnd(result: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val c = LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false) as CardView
        return ViewHolder(c)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val textView = holder.itemView.findViewById<TextView>(R.id.game_card_text)
        val imageView = holder.itemView.findViewById<ImageView>(R.id.game_card_image)
        if (!isGameMode && position == 4) textView.text = "Играть"
        if (isGameMode) textView.text = "?"
        if (rightAnswers.contains(position)) imageView.setImageResource(R.drawable.ic_star) else imageView.setImageResource(R.drawable.ic_star_empty)
    }

    override fun getItemCount(): Int {
        return 9
    }

    fun startGameMode() {
        isGameMode = true
        notifyDataSetChanged()
    }

    inner class ViewHolder internal constructor(c: CardView) : RecyclerView.ViewHolder(c) {
        init {
            c.setOnClickListener {
                if (isGameMode) {
                    openedCards++
                    c.findViewById<View>(R.id.game_card_text).visibility = View.GONE
                    c.findViewById<View>(R.id.game_card_image).visibility = View.VISIBLE
                }
                if (!isGameMode && adapterPosition == 4) {
                    listener?.onGameStart()
                    startGameMode()
                }
            }
        }
    }


}