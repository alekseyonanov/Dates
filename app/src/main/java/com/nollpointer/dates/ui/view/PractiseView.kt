package com.nollpointer.dates.ui.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView
import com.nollpointer.dates.R
import com.nollpointer.dates.other.PractiseEnum
import kotlinx.android.synthetic.main.practise_items.view.*

class PractiseView : ScrollView {

    var onPractiseClickListener: ((PractiseEnum) -> Unit)? = null

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        initView()
    }

    private fun initView() {
        inflate(context, R.layout.practise_items, this)

        practiseCards.setOnClickListener {
            onPractiseClickListener?.invoke(PractiseEnum.CARDS)
        }

        practiseVoice.setOnClickListener {
            onPractiseClickListener?.invoke(PractiseEnum.VOICE)
        }

        practiseTest.setOnClickListener {
            onPractiseClickListener?.invoke(PractiseEnum.TEST)
        }

        practiseTrueFalse.setOnClickListener {
            onPractiseClickListener?.invoke(PractiseEnum.TRUE_FALSE)
        }

        practiseLink.setOnClickListener {
            onPractiseClickListener?.invoke(PractiseEnum.LINK)
        }

        practiseSort.setOnClickListener {
            onPractiseClickListener?.invoke(PractiseEnum.SORT)
        }

        practiseDistribution.setOnClickListener {
            onPractiseClickListener?.invoke(PractiseEnum.DISTRIBUTION)
        }

    }
}
