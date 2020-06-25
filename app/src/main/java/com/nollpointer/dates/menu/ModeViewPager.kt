package com.nollpointer.dates.menu

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager

class ModeViewPager(context: Context, attrs: AttributeSet? = null) : ViewPager(context,attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpecChanged = heightMeasureSpec
        var height = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
            val h = child.measuredHeight
            if (h > height) height = h
        }
        if (height != 0) {
            heightMeasureSpecChanged = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpecChanged)
    }
}