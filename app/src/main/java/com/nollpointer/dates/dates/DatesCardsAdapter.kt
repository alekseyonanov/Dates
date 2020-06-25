package com.nollpointer.dates.dates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nollpointer.dates.R
import com.nollpointer.dates.activity.MainActivity
import com.nollpointer.dates.other.Date
import java.util.*

class DatesCardsAdapter(private var dates: List<Date>, private val mode: Int, main_text_tops: Array<String>, additional_text_tops: Array<String>) : RecyclerView.Adapter<DatesCardsAdapter.ViewHolder>() {
    private var main_top_texts = TreeMap<Int, String>()
    private var add_top_texts = TreeMap<Int, String>()
    private lateinit var listener: Listener
    private  var searchMode = false
    private  var dateCardId = 0
    private  var dateCardTopTextId = 0

    init {
        fillTopTexts(main_text_tops, additional_text_tops)
    }

    interface Listener {
        fun onItemClick(clickedDate: Date?)
    }

    fun setViewIds(dateCardId: Int, dateCardTopTextId: Int) {
        this.dateCardId = dateCardId
        this.dateCardTopTextId = dateCardTopTextId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = when (viewType) {
            DATE -> LayoutInflater.from(parent.context).inflate(dateCardId, parent, false)
            else -> LayoutInflater.from(parent.context).inflate(dateCardTopTextId, parent, false)
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date = dates[position]
        val cardView = holder.itemView
        var textView = cardView.findViewById<TextView>(R.id.text1)
        textView.text = date.date
        textView = cardView.findViewById(R.id.text2)
        textView.text = date.event
        textView = cardView.findViewById(R.id.text3)
        if (date.containsMonth()) textView.text = date.month else textView.text = ""
        if (getItemViewType(position) == DATE_WITH_MARGIN) {
            textView = cardView.findViewById(R.id.textTitle)
            textView.text = main_top_texts[Integer.valueOf(position)]
            //textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        }
        cardView.setOnLongClickListener {
            listener.onItemClick(dates[position])
            true
        }
    }

    private fun fillTopTexts(texts: Array<String>, add_texts: Array<String>) {
        val positionsMain = intArrayOf(0, 21, 41, 85, 118, 160, 209, 256, 298, 348)
        val positionsEasy = intArrayOf(0, 48)
        for (i in positionsMain.indices) main_top_texts[positionsMain[i]] = texts[i]
        for (i in positionsEasy.indices) add_top_texts[positionsEasy[i]] = add_texts[i]
        if (mode == MainActivity.EASY_DATES_MODE) changeTopTexts()
    }

    fun changeTopTexts() {
        val tree = main_top_texts
        main_top_texts = add_top_texts
        add_top_texts = tree
    }

    fun startSearchMode() {
        searchMode = true
        for (position in main_top_texts.keys) notifyItemChanged(position)
        for (position in add_top_texts.keys) notifyItemChanged(position)
    }

    fun stopSearchMode(dates: List<Date>) {
        searchMode = false
        this.dates = dates
        notifyDataSetChanged()
    }

    fun refresh(dates: List<Date>) {
        changeTopTexts()
        this.dates = dates
        //refreshMarginDates();
        notifyDataSetChanged()
    }

    fun refresh(dates: List<Date>, category: Int) {
        searchMode = category != DatesCategoryConstants.ALL
        this.dates = dates
        refreshMarginDates()
        notifyDataSetChanged()
    }

    private fun refreshMarginDates() {
        for (position in main_top_texts.keys) {
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return dates.size
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun setDates(dates: List<Date>) {
        this.dates = dates
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (!searchMode && main_top_texts.containsKey(position)) DATE_WITH_MARGIN else DATE
    }

    class ViewHolder internal constructor(c: View?) : RecyclerView.ViewHolder(c!!)
    companion object {
        private const val DATE = 0
        private const val DATE_WITH_MARGIN = 1
    }


}