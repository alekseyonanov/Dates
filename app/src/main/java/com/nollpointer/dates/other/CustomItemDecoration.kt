package com.nollpointer.dates.other

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.nollpointer.dates.R

/**
 * Кастомный ItemDecoration для RecyclerView с датами. Используется в связке с массивом позиций,
 * которые разделяют разные группы дат.
 *
 * @author Onanov Aleksey (@onanov)
 */
class CustomItemDecoration(
        private val context: Context,
        orientation: Int,
        private var items: IntArray,
) : DividerItemDecoration(context, orientation) {

    private val divider: Drawable = ContextCompat.getDrawable(context, R.drawable.divider)!!

    override fun getItemOffsets(
            outRect: Rect, view: View,
            parent: RecyclerView, state: RecyclerView.State,
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position != 0 && items.contains(parent.getChildAdapterPosition(view)))
            outRect.top = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    16f,
                    context.resources.displayMetrics
            ).toInt()
        outRect.bottom = divider.intrinsicHeight
    }


}