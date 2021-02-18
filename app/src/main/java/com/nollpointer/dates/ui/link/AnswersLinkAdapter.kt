package com.nollpointer.dates.ui.link

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nollpointer.dates.R
import com.nollpointer.dates.databinding.ItemLinkBinding

/**
 * @author Onanov Aleksey (@onanov)
 */
class AnswersLinkAdapter : RecyclerView.Adapter<AnswersLinkAdapter.ViewHolder>() {

    var items = listOf<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_link, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemLinkBinding.bind(itemView)

        fun bind(item: String) {
            binding.root.text = item
        }
    }
}