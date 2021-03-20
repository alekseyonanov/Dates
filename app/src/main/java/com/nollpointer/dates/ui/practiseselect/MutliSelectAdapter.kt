package com.nollpointer.dates.ui.practiseselect

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import androidx.recyclerview.widget.RecyclerView
import com.nollpointer.dates.R
import java.util.*

/**
 * Адаптер для работы с элементами, поддерживающими мультивыбор
 *
 * @author Onanov Aleksey (@onanov)
 */
class MutliSelectAdapter(private val items: Array<CharSequence>) : RecyclerView.Adapter<MutliSelectAdapter.ViewHolder>() {

    var onAnyItemSelected: ((Boolean)->Unit)? = null

    var onItemsSelected: ((List<Int>)->Unit)? = null

    var selectedItems = ArrayList<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_select_multichoice, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val view = holder.itemView as CheckedTextView
        view.text = items[position]
    }

    override fun getItemCount() = items.size

    fun makeRandomValues() {
        val random = Random(System.currentTimeMillis())
        val size = random.nextInt(items.size)
        selectedItems.clear()
        if (size == items.size - 1) {
            for (i in items.indices) {
                selectedItems.add(i)
            }
        } else {
            for (i in 0 until size) selectedItems.add(random.nextInt(items.size))
        }

        notifyDataSetChanged()
    }

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener {
                val checkedTextView = it as CheckedTextView

                if (checkedTextView.isChecked)
                    selectedItems.remove(adapterPosition)
                else
                    selectedItems.add(adapterPosition)

                checkedTextView.isChecked = !checkedTextView.isChecked

                onAnyItemSelected?.invoke(selectedItems.isNotEmpty())
                onItemsSelected?.invoke(selectedItems)

            }
        }


    }

}