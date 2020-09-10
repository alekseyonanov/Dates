package com.nollpointer.dates.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class DividerView : View {

    private val paintWhite = Paint().apply {
        color = Color.WHITE
    }

    private val paintRed = Paint().apply {
        color = Color.rgb(0xB7, 0x1C, 0x1C)
    }

    var type: Int = BOTTOM_POSITION

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)


    override fun onDraw(canvas: Canvas?) {
        when (type) {
            TOP_POSITION -> canvas?.drawOval((-width / 4).toFloat(), (height * 3 / 4).toFloat(), (width * 5 / 4).toFloat(), (-height * 7 / 4).toFloat(), paintRed)
            BOTTOM_POSITION -> canvas?.drawOval((-width / 4).toFloat(), (height * 1/ 4).toFloat(), (width * 5 / 4).toFloat(), (height * 7 / 4).toFloat(), paintWhite)
        }
        super.onDraw(canvas)
    }

    /*
    * when (type) {
            TOP_POSITION -> canvas?.drawOval((-width / 4).toFloat(), (height / 2).toFloat(), (width * 5 / 4).toFloat(), (-height * 3 / 2).toFloat(), paintRed)
            BOTTOM_POSITION -> canvas?.drawOval((-width / 4).toFloat(), (height / 2).toFloat(), (width * 5 / 4).toFloat(), (height * 3 / 2).toFloat(), paintWhite)
        }*/

    companion object {
        const val TOP_POSITION = 0
        const val BOTTOM_POSITION = 1
    }
}
