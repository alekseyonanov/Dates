package com.nollpointer.dates.practise

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nollpointer.dates.R

class PractiseResultCardsAdapter(private val practiseResults: List<PractiseResult>) : RecyclerView.Adapter<PractiseResultCardsAdapter.ViewHolder>() {
    private lateinit var mListener: PractiseCardsAdapter.Listener

    interface Listener {
        fun onClick(position: Int)
    }

    fun setListener(listener: PractiseCardsAdapter.Listener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_result, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val numberTextView = holder.itemView.findViewById<TextView>(R.id.result_card_number)
        val questionTextView = holder.itemView.findViewById<TextView>(R.id.result_card_main_text)
        val imageView = holder.itemView.findViewById<ImageView>(R.id.result_card_image_view)
        numberTextView.text = (position + 1).toString()
        questionTextView.text = practiseResults[position].question
        if (practiseResults[position].isCorrect) imageView.setImageResource(R.drawable.ic_correct) else imageView.setImageResource(R.drawable.ic_mistake)
    }

    override fun getItemCount(): Int {
        return practiseResults.size
    }

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                mListener.onClick(adapterPosition)
            }
        }
    }

}