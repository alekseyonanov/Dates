package com.nollpointer.dates.ui.analyze.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nollpointer.dates.databinding.ItemAnswersTestBinding
import com.nollpointer.dates.databinding.ItemAnswersTrueFalseBinding

/**
 * Адаптер для работы с ответами на экране "Анализ"
 *
 * @author Onanov Aleksey (@onanov)
 */
class AnswersAdapter : RecyclerView.Adapter<AnswersAdapter.BaseAnswersViewHolder<*>>() {

    private val factory = AnalyzeViewHolderFactory()

    var items: List<BasePractiseResult> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAnswersViewHolder<*> {
        return factory.createViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseAnswersViewHolder<*>, position: Int) {
        //holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int) = items[position].type


    abstract class BaseAnswersViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {

        abstract fun bind(result: T)

    }

    class TestAnswersViewHolder(view: View) : BaseAnswersViewHolder<TestPractiseResult>(view) {
        val binding = ItemAnswersTestBinding.bind(itemView)

        override fun bind(result: TestPractiseResult) {

        }
    }

    class TrueFalseAnswersViewHolder(view: View) : BaseAnswersViewHolder<TrueFalsePractiseResult>(view) {
        val binding = ItemAnswersTrueFalseBinding.bind(itemView)

        override fun bind(result: TrueFalsePractiseResult) {

        }
    }

}