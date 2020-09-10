package com.nollpointer.dates.ui.terms

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nollpointer.dates.R
import com.nollpointer.dates.model.Term
import java.util.*

/**
 * @author Onanov Aleksey (@onanov)
 */
class TermsAdapter(val resources: Resources, var items: List<Term>) : RecyclerView.Adapter<TermsAdapter.ViewHolder>() {

    var onTermClickListener: ((Term) -> Unit)? = null

    var isSearchMode = false

    private var titles = TreeMap<Int,String>()

    var itemWithTitleLayoutId = R.layout.item_term_top_text
    var itemLayoutId: Int = R.layout.item_term

    init {
        val positions = resources.getIntArray(R.array.terms_positions)
        val titles = resources.getStringArray(R.array.terms_titles)

        for (i in positions.indices)
            this.titles[positions[i]] = titles[i]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = when(viewType){
            TERM_WITH_TITLE -> LayoutInflater.from(parent.context).inflate(itemWithTitleLayoutId, parent, false)
            else -> LayoutInflater.from(parent.context).inflate(itemLayoutId, parent, false)
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val term = items[position]
        holder.bind(term)

        if (getItemViewType(position) == TERM_WITH_TITLE)
            holder.itemView.findViewById<TextView>(R.id.textTitle).text = titles[position]

    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        return if (!isSearchMode && titles.containsKey(position))
            TERM_WITH_TITLE
        else
            TERM
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val termTextView = itemView.findViewById<TextView>(R.id.text1)
        private val descriptionTextView = itemView.findViewById<TextView>(R.id.text2)

        init {
            itemView.setOnClickListener {
                onTermClickListener?.invoke(items[adapterPosition])
            }
        }

        fun bind(term: Term) {
            termTextView.text = term.term
            descriptionTextView.text = term.description
        }
    }

    companion object {
        private const val TERM = 0
        private const val TERM_WITH_TITLE = 1
    }
}