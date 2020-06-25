package com.nollpointer.dates.statistics

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.nollpointer.dates.R

class StatisticsCardsAdapter : RecyclerView.Adapter<StatisticsCardsAdapter.ViewHolder>() {

    private lateinit var mListener: Listener

    interface Listener {
        fun onClick(position: Int)
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val c = LayoutInflater.from(parent.context).inflate(R.layout.card_statistics, parent, false) as CardView
        return ViewHolder(c)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {}
    override fun getItemCount() = 16

    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean { //        if (fromPosition < toPosition) {
//            for (int i = fromPosition; i < toPosition; i++) {
//                Collections.swap(mItems, i, i + 1);
//            }
//        } else {
//            for (int i = fromPosition; i > toPosition; i--) {
//                Collections.swap(mItems, i, i - 1);
//            }
//        }
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    inner class ViewHolder internal constructor(c: CardView) : RecyclerView.ViewHolder(c)
}