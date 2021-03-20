package com.nollpointer.dates.ui.link

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.nollpointer.dates.R
import com.nollpointer.dates.databinding.ItemLinkAnswerBinding
import com.nollpointer.dates.model.Date
import com.nollpointer.dates.model.LinkModel

/**
 * Адаптер для работы с вариантами ответов на экране "Связка"
 *
 * @author Onanov Aleksey (@onanov)
 */
class AnswersLinkAdapter : RecyclerView.Adapter<AnswersLinkAdapter.ViewHolder>() {

    var items = listOf<Date>()
        set(value) {
            field = value
            positions = value.map { LinkModel.GREY }.toMutableList()
            notifyDataSetChanged()
        }

    var positions = mutableListOf<Int>()
    var correctAnswers = listOf<Int>()

    var onAnswerClickedListener: ((Date) -> Unit)? = null
    var onAnswersListChanged: ((List<Date>) -> Unit)? = null

    var isDetailsMode = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_link_answer, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    private fun createAnswersListAndNotify() {
        val list = mutableListOf<Date>()
        positions.forEach {
            list.add(items[it])
        }
        onAnswersListChanged?.invoke(list.distinct())
    }

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemLinkAnswerBinding.bind(itemView)

        fun bind(item: Date) {
            binding.answer.text = item.event
            binding.root.setOnClickListener {
                if (isDetailsMode) {
                    onAnswerClickedListener?.invoke(item)
                } else {
                    val position = positions[adapterPosition]
                    positions[adapterPosition] = (position + 1) % 5

                    createAnswersListAndNotify()
                    setColorDot()
                }
            }
            setColorDot()
            if (isDetailsMode) {
                binding.result.setBackgroundResource(getColorResult(correctAnswers[adapterPosition]))
                binding.result.visibility = View.VISIBLE
            } else {
                binding.result.visibility = View.INVISIBLE
            }
        }

        private fun setColorDot() {
            binding.answer.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0,
                    getColorDot(adapterPosition), 0)
        }

        @DrawableRes
        private fun getColorDot(position: Int): Int {
            return when (positions[position]) {
                LinkModel.BLUE -> R.drawable.ic_dot_blue
                LinkModel.RED -> R.drawable.ic_dot_red
                LinkModel.GREEN -> R.drawable.ic_dot_green
                LinkModel.YELLOW -> R.drawable.ic_dot_yellow
                else -> R.drawable.ic_dot_grey
            }
        }

        @ColorRes
        private fun getColorResult(result: Int): Int {
            return when (result) {
                LinkModel.BLUE -> R.color.colorAccent
                LinkModel.RED -> R.color.colorPrimary
                LinkModel.GREEN -> R.color.colorTrueButton
                LinkModel.YELLOW -> R.color.colorMarkNormal
                else -> R.color.colorUnselected
            }
        }
    }
}