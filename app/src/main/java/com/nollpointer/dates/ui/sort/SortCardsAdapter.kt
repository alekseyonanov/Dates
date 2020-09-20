package com.nollpointer.dates.ui.sort

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nollpointer.dates.R
import com.nollpointer.dates.model.Date

/**
 * @author Onanov Aleksey (@onanov)
 */
class SortCardsAdapter : RecyclerView.Adapter<SortCardsAdapter.ViewHolder>() {

    var dates: List<Date> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sort, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dates[position])
    }

    override fun getItemCount() = COUNT

    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(date: Date) {
            itemView.findViewById<TextView>(R.id.sortItemText).text = date.event
        }
    }

    companion object {
        private const val COUNT = 3
    }
}