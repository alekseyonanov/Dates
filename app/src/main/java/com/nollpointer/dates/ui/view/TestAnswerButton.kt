package com.nollpointer.dates.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.nollpointer.dates.R
import com.nollpointer.dates.model.Date
import com.nollpointer.dates.model.Practise.Companion.TYPE_DATE
import kotlinx.android.synthetic.main.item_test_answer.view.*

class TestAnswerButton : LinearLayout {

    constructor(context: Context?) : super(context) {
        initialize()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initialize()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initialize()
    }

    var setOnAnswerButtonClickListener: ((TestAnswerButton) -> Unit)? = null
    var setOnDetailsClickListener: ((Date) -> Unit)? = null

    private var isDetailsMode = false
    private var currentDate = Date()

    private fun initialize() {
        val view = inflate(context, R.layout.item_test_answer, this)
        view.findViewById<LinearLayout>(R.id.testItemContainer).setOnClickListener {
            if (!this@TestAnswerButton.isClickable) return@setOnClickListener
            if (isDetailsMode)
                setOnDetailsClickListener?.invoke(currentDate)
            else
                setOnAnswerButtonClickListener?.invoke(this)
        }
    }

    fun setDate(type: Int, date: Date) {
        currentDate = date
        isDetailsMode = false
        testItemResult.visibility = View.INVISIBLE
        testItemButton.text =
                if (type == TYPE_DATE) {
                    date.event
                } else {
                    if (currentDate.containsMonth)
                        "${currentDate.date}, ${currentDate.month}"
                    else
                        currentDate.date
                }
        testItemButton.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)
    }

    fun setResult(result: Boolean) {
        testItemResult.visibility = View.VISIBLE
        testItemResult.setBackgroundResource(
                if (result)
                    R.color.colorTest
                else
                    R.color.colorPrimary
        )
    }

    fun setDetailsMode() {
        isDetailsMode = true
        testItemButton.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_help_gray, 0)
    }


}