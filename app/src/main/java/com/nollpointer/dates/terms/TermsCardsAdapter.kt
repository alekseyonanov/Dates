package com.nollpointer.dates.terms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nollpointer.dates.R
import com.nollpointer.dates.other.Term

class TermsCardsAdapter //    private boolean searchMode = false;
(private val terms: List<Term>) : RecyclerView.Adapter<TermsCardsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.term_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cardView = holder.itemView
        val term = terms[position]
        val titleTextView = cardView.findViewById<TextView>(R.id.textTitle)
        val termTextView = cardView.findViewById<TextView>(R.id.text1)
        val descriptionTextView = cardView.findViewById<TextView>(R.id.text2)
        termTextView.text = term.term
        descriptionTextView.text = term.description
    }

    //    public void startSearchMode() {
//        searchMode = true;
//        for (Integer position : main_top_texts.keySet())
//            notifyItemChanged(position);
//        for (Integer position : add_top_texts.keySet())
//            notifyItemChanged(position);
//    }
//
//    public void stopSearchMode(List<Date> dates) {
//        searchMode = false;
//        this.dates = dates;
//        notifyDataSetChanged();
//    }
    override fun getItemCount() = terms.size

    override fun getItemViewType(position: Int) = 0

    class ViewHolder internal constructor(c: View) : RecyclerView.ViewHolder(c)

}