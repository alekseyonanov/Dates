package com.nollpointer.dates.ui.view

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

/**
 * Квадратный RelativeLayout
 *
 * @author Onanov Aleksey (@onanov)
 */
class SquareRelativeLayout @JvmOverloads constructor(
        context: Context?,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}