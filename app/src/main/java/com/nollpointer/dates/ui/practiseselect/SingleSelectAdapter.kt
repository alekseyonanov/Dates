package com.nollpointer.dates.ui.practiseselect

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import androidx.recyclerview.widget.RecyclerView
import com.nollpointer.dates.R
import java.util.*

/**
 * Адаптер для работы с элементами, поддерживающими единственный выбор
 *
 * @author Onanov Aleksey (@onanov)
 */
class SingleSelectAdapter(private val items: Array<CharSequence>) : RecyclerView.Adapter<SingleSelectAdapter.ViewHolder>() {

    var onItemHasSelected: ((Boolean) -> Unit)? = null

    var onItemSelected: ((Int) -> Unit)? = null

    private var previousSelectedTextView: CheckedTextView? = null

    var selectedItem = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_select_singlechoice, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val view = holder.itemView as CheckedTextView
        view.text = items[position]
        if (position == selectedItem) {
            view.isChecked = true
            previousSelectedTextView = view
        }
    }

    override fun getItemCount() = items.size

    fun makeRandomValues() {
        val random = Random(System.currentTimeMillis())
        selectedItem = random.nextInt(3)
        notifyDataSetChanged()
    }

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener {
                val checkedTextView = it as CheckedTextView
                if (checkedTextView != previousSelectedTextView) {
                    previousSelectedTextView?.isChecked = false
                    checkedTextView.isChecked = true
                    selectedItem = adapterPosition
                    previousSelectedTextView = checkedTextView
                    onItemSelected?.invoke(adapterPosition)
                }
                onItemHasSelected?.invoke(selectedItem != -1)
            }
        }
    }
}