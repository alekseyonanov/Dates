package com.nollpointer.dates.ui.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nollpointer.dates.R
import com.nollpointer.dates.other.PractiseConstants.CARDS
import com.nollpointer.dates.other.PractiseConstants.DISTRIBUTE
import com.nollpointer.dates.other.PractiseConstants.SORT
import com.nollpointer.dates.other.PractiseConstants.TEST
import com.nollpointer.dates.other.PractiseConstants.TRUE_FALSE
import com.nollpointer.dates.other.PractiseConstants.VOICE
import com.nollpointer.dates.ui.practise.PractiseCardsAdapter

class PractiseCellView : RecyclerView {
    interface OnClickListener {
        fun onClicked(practise: String?, mode: Int)
    }

    private var practiseMode = TRAINING_MODE
    private var listener: OnClickListener? = null

    constructor(context: Context) : super(context) {
        initializeAdapter()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initializeAdapter()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        initializeAdapter()
    }

    private fun initializeAdapter() {
        val adapter = PractiseCardsAdapter(resources.getStringArray(R.array.practise_titles),
                resources.getStringArray(R.array.practise_description), intArrayOf(R.drawable.ic_cards, R.drawable.ic_cards_with_voice, R.drawable.ic_test, R.drawable.ic_true_false,
                R.drawable.ic_sort, R.drawable.ic_distribution), intArrayOf(R.drawable.ic_practise_background_cards, R.drawable.ic_practise_background_voice_cards, R.drawable.ic_practise_background_test, R.drawable.ic_practise_background_true_false,
                R.drawable.ic_practise_background_sort, R.drawable.ic_practise_background_distribution))
        adapter.setListener(object : PractiseCardsAdapter.Listener {
            override fun onClick(position: Int) {
                val practise = when (position) {
                    0 -> CARDS
                    1 -> VOICE
                    2 -> TEST
                    3 -> TRUE_FALSE
                    4 -> SORT
                    5 -> DISTRIBUTE
                    else -> CARDS
                }
                listener!!.onClicked(practise, practiseMode)
            }
        })
        setAdapter(adapter)
        layoutManager = LinearLayoutManager(context)
    }

    fun setListener(listener: OnClickListener?) {
        this.listener = listener
    }

    fun setPractiseMode(mode: Int) {
        practiseMode = mode
        val adapter = adapter as PractiseCardsAdapter?
        adapter!!.setMode(practiseMode)
    }

    fun setMarks(marks: IntArray?) {
        val adapter = adapter as PractiseCardsAdapter?
        adapter!!.setMarks(marks!!)
    }

    companion object {
        const val TRAINING_MODE = 0
        //const val TEST_MODE = 1
    }
}