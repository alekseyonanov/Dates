package com.nollpointer.dates.ui.analyze.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nollpointer.dates.R
import com.nollpointer.dates.annotation.AnswerViewType
import com.nollpointer.dates.annotation.TEST
import com.nollpointer.dates.annotation.TRUE_FALSE

/**
 *
 *
 * @author Onanov Aleksey (@onanov)
 */
class AnalyzeViewHolderFactory {
    fun createViewHolder(parent: ViewGroup, @AnswerViewType viewType: Int): AnswersAdapter.BaseAnswersViewHolder<*> {
        return when (viewType) {
            TEST -> AnswersAdapter.TestAnswersViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_test_answer, parent, false))
            TRUE_FALSE -> AnswersAdapter.TrueFalseAnswersViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_answers_true_false, parent, false))
            else -> throw IllegalArgumentException("View type: $viewType not supported");
        }
    }
}