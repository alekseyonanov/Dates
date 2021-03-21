package com.nollpointer.dates.ui.dates

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nollpointer.dates.R
import com.nollpointer.dates.annotation.FULL
import com.nollpointer.dates.model.Date
import java.util.*

/**
 * Адаптер для работы со списком дат на экране "Даты"
 *
 * @author Onanov Aleksey (@onanov)
 */
class DatesAdapter(val resources: Resources, var items: List<Date>, mode: Int) : RecyclerView.Adapter<DatesAdapter.ViewHolder>() {

    var isSearchMode = false

    private var titles = TreeMap<Int, String>()

    var itemWithTitleLayoutId = R.layout.item_dates_top_text
    var itemLayoutId: Int = R.layout.item_dates

    var onDateClickListener: ((Date) -> Unit)? = null

    init {
        val positions = when (mode) {
            FULL -> resources.getIntArray(R.array.dates_full_positions)
            else -> resources.getIntArray(R.array.dates_easy_positions)
        }
        val titles = when (mode) {
            FULL -> resources.getStringArray(R.array.centuries_full)
            else -> resources.getStringArray(R.array.centuries_easy)
        }

        for (i in positions.indices)
            this.titles[positions[i]] = titles[i]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = when (viewType) {
            DATE -> LayoutInflater.from(parent.context).inflate(itemLayoutId, parent, false)
            else -> LayoutInflater.from(parent.context).inflate(itemWithTitleLayoutId, parent, false)
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date = items[position]
        holder.bind(date)

        if (getItemViewType(position) == DATE_WITH_TITLE)
            holder.itemView.findViewById<TextView>(R.id.textTitle).text = titles[position]
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        return if (!isSearchMode && titles.containsKey(position))
            DATE_WITH_TITLE
        else
            DATE
    }

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        private val dateTextView = itemView.findViewById<TextView>(R.id.text1)
        private val eventTextView = itemView.findViewById<TextView>(R.id.text2)
        private val monthTextView = itemView.findViewById<TextView>(R.id.text3)

        init {
            itemView.setOnClickListener {
                onDateClickListener?.invoke(items[adapterPosition])
            }
        }

        fun bind(date: Date) {
            dateTextView.text = date.date
            eventTextView.text = date.event
            if (date.containsMonth) {
                monthTextView.visibility = View.VISIBLE
                monthTextView.text = date.month
            }
        }
    }

    companion object {
        private const val DATE = 0
        private const val DATE_WITH_TITLE = 1
    }
}