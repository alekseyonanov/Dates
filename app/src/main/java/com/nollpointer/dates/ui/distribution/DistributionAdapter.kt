package com.nollpointer.dates.ui.distribution

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nollpointer.dates.R
import com.nollpointer.dates.databinding.ItemDistributionBinding

/**
 * Адаптер для работы с экраном практики "Распределение"
 *
 * @author Onanov Aleksey (@onanov)
 */
class DistributionAdapter : RecyclerView.Adapter<DistributionAdapter.ViewHolder>() {

    var items = mutableListOf<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_distribution, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun onItemDismiss(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemDistributionBinding.bind(itemView)

        fun bind(item: String) {
            binding.root.text = item
        }
    }
}