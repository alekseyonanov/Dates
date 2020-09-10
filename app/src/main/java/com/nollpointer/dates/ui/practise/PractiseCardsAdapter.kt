package com.nollpointer.dates.ui.practise

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nollpointer.dates.R
import java.util.*

/**
 * @author Onanov Aleksey (@onanov)
 */
class PractiseCardsAdapter(private val title_texts: Array<String>, private val subtitle_texts: Array<String>, private val imageIds: IntArray, private val backgrounds: IntArray) : RecyclerView.Adapter<PractiseCardsAdapter.ViewHolder>() {
    private lateinit var marks: IntArray
    private var mListener: Listener? = null
    private var currentMode = 0

    private val mark: String
        get() {
            val random = Random()
            val number = random.nextInt(20) * 5.0 / 20
            return String.format("%.1f", number)
        }

    interface Listener {
        fun onClick(position: Int)
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun setMode(practiseMode: Int) {
        currentMode = practiseMode
    }

    fun setMarks(marks: IntArray) {
        this.marks = marks
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_practise, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cardView = holder.itemView
        val titleTextView = cardView.findViewById<TextView>(R.id.info_text_title)
        val subTitleTextView = cardView.findViewById<TextView>(R.id.info_text_subtitle)
        //val markImageView = cardView.findViewById<ImageView>(R.id.info_image_mark)
        val imageView = cardView.findViewById<ImageView>(R.id.info_image)
        titleTextView.text = title_texts[position]
        subTitleTextView.text = subtitle_texts[position]
        imageView.setImageResource(imageIds[position])
        imageView.setBackgroundResource(backgrounds[position])
        imageView.contentDescription = title_texts[position]
       // if (currentMode == 1) {
           // markImageView.visibility = View.VISIBLE
           // markImageView.setImageResource(getMarkImage(marks[position]))
        //}
    }

    override fun getItemCount(): Int {
        return imageIds.size
    }

    private fun getMarkImage(mark: Int): Int { //int colors[] = {0xFFB71C1C,0xFFFFEB3B, 0xFF43a047};
        val colors = intArrayOf(R.drawable.ic_practise_result_nothing, R.drawable.ic_practise_result_bad, R.drawable.ic_practise_result_neutral, R.drawable.ic_practise_result_good)
        return colors[mark + 1]
    }

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener { if (mListener != null) mListener!!.onClick(adapterPosition) }
        }
    }

}