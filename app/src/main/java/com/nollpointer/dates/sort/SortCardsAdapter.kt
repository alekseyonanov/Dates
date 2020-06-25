package com.nollpointer.dates.sort

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nollpointer.dates.R
import com.nollpointer.dates.other.Date
import java.util.*

class SortCardsAdapter : RecyclerView.Adapter<SortCardsAdapter.ViewHolder>() {
    private lateinit var listener: Listener
    private var itemCount = 3
    private lateinit var dates: List<Date>
    private val sequence = ArrayList<Int>()

    interface Listener {
        fun onClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_sort, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val view = holder.itemView
        val mainTextView = view.findViewById<TextView>(R.id.sortTextMain)
        val numberTextView = view.findViewById<TextView>(R.id.sortTextNumber)
        val imageView = view.findViewById<ImageView>(R.id.sortImage)
        numberTextView.text = (position + 1).toString()
        mainTextView.text = dates[position].event
        sequence.add(position)
        imageView.visibility = View.INVISIBLE
    }

    override fun getItemCount(): Int {
        return itemCount
    }

    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        notifyItemMoved(fromPosition, toPosition)
        val fromNumber = sequence[fromPosition]
        val toNumber = sequence[toPosition]
        sequence[toPosition] = fromNumber
        sequence[fromPosition] = toNumber
        return true
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun setItemCount(count: Int) {
        itemCount = count
    }

    fun setDates(dates: List<Date>) {
        this.dates = dates
        sequence.clear()
    }

    val answerSequence: List<Int>
        get() = sequence

    inner class ViewHolder internal constructor(view: View?) : RecyclerView.ViewHolder(view!!)
}