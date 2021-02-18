package com.nollpointer.dates.ui.link

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nollpointer.dates.R
import com.nollpointer.dates.databinding.ItemLinkBinding
import com.nollpointer.dates.model.LinkModel

/**
 * @author Onanov Aleksey (@onanov)
 */
class TaskLinkAdapter : RecyclerView.Adapter<TaskLinkAdapter.ViewHolder>() {

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
            binding.root.isClickable = false
            binding.root.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0,
                    when (adapterPosition) {
                        LinkModel.BLUE -> R.drawable.ic_dot_blue
                        LinkModel.RED -> R.drawable.ic_dot_red
                        LinkModel.GREEN -> R.drawable.ic_dot_green
                        LinkModel.YELLOW -> R.drawable.ic_dot_yellow
                        else -> R.drawable.ic_dot_grey
                    }, 0)
        }

    }
}