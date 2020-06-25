package com.nollpointer.dates.practise

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import androidx.recyclerview.widget.RecyclerView
import com.nollpointer.dates.R
import java.util.*

class PractiseDetailsPickerAdapter(private val titles: Array<CharSequence>, private val mode: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var previousPickedType: CheckedTextView
    private lateinit var titleCheckedTextView: CheckedTextView

    var type = 0
    private var centuries = ArrayList<Int>()
    private var isLocked = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = if (mode == TYPE)
            LayoutInflater.from(parent.context).inflate(R.layout.card_select_singlechoice, parent, false)
        else
            LayoutInflater.from(parent.context).inflate(R.layout.card_select_multichoice, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val view = holder.itemView.findViewById<CheckedTextView>(android.R.id.text1)
        view.text = titles[position]
        if (mode == TYPE) {
            if (position == type) {
                view.isChecked = true
                previousPickedType = view
            } else view.isChecked = false
        } else {
            view.isChecked = centuries.contains(position)
        }
        view.isEnabled = !isLocked
    }

    override fun getItemCount(): Int {
        return titles.size
    }

    fun setTitleCheckedTextView(titleCheckedTextView: CheckedTextView) {
        this.titleCheckedTextView = titleCheckedTextView
    }

    fun setCenturies(centuries: ArrayList<Int>) {
        this.centuries = centuries
    }

    fun getCenturies(): List<Int> {
        centuries.sort()
        Log.e("ADAPTER", "getCenturies: $centuries")
        return centuries
    }

    fun setLocked() {
        isLocked = true
    }

    fun makeRandomValues() {
        val random = Random(System.currentTimeMillis())
        type = random.nextInt(3)
        val size = random.nextInt(titles.size)
        centuries.clear()
        if (size == titles.size - 1) {
            for (i in titles.indices) {
                centuries.add(i)
            }
        } else {
            for (i in 0 until size) centuries.add(random.nextInt(titles.size))
        }
        if (mode == CENTURY) titleCheckedTextView.isChecked = size == titles.size - 1
        notifyDataSetChanged()
    }

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        init {
            val textView = view.findViewById<View>(android.R.id.text1)
            textView.setOnClickListener {
                val checkedTextView = it as CheckedTextView
                if (mode == TYPE)
                    if (!checkedTextView.isChecked) {
                        checkedTextView.isChecked = true
                        previousPickedType.isChecked = false
                        previousPickedType = checkedTextView
                        type = adapterPosition
                    } else {
                        if (checkedTextView.isChecked) {
                            checkedTextView.isChecked = false
                            centuries.remove(Integer.valueOf(adapterPosition))
                        } else {
                            checkedTextView.isChecked = true
                            centuries.add(adapterPosition)
                        }
                        titleCheckedTextView.isChecked = centuries.size == 10
                    }

            }
        }
    }

    companion object {
        const val CENTURY = 1
        const val TYPE = 0
    }

}