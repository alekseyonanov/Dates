package com.nollpointer.dates.ui.distribution

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nollpointer.dates.R

/**
 * @author Onanov Aleksey (@onanov)
 */
class DistributionCardsAdapter : RecyclerView.Adapter<DistributionCardsAdapter.ViewHolder>() {
    private lateinit var mListener: Listener

    interface Listener {
        fun onClick(position: Int)
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_distribution, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {}
    override fun getItemCount(): Int {
        return 16
    }

    fun onItemDismiss(position: Int) { //mItems.remove(position);
        notifyItemRemoved(position)
    }

    inner class ViewHolder internal constructor(view: View?) : RecyclerView.ViewHolder(view!!)
}