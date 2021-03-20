package com.nollpointer.dates.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * Кастомная View для отображения голосового вывода
 *
 * @author Onanov Aleksey (@onanov)
 */
class VisualizerView @JvmOverloads constructor(
        context: Context?,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private val linePaint: Paint = Paint()
    private var amplitude = AMPLITUDE_MIN_VALUE
    private var currentWidth = 0
    private var currentHeight = 0
    private var isHidden = false

    init {
        linePaint.color = -0xbb7501
        linePaint.style = Paint.Style.FILL
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        currentWidth = w
        currentHeight = h
    }

    //выставление амплитуды
    fun setAmplitude(amplitude: Float) {
        this.amplitude = amplitude
        invalidate()
    }

    fun hide() {
        amplitude = AMPLITUDE_MIN_VALUE
        isHidden = true
        invalidate()
    }

    fun show() {
        isHidden = false
    }

    override fun onDraw(canvas: Canvas) { //super.onDraw(canvas);
        val halfWidth = currentWidth / 2
        var delta = 0f
        if (!isHidden) delta = halfWidth * (amplitude / AMPLITUDE_MAX_VALUE)
        for (i in 0 until height) canvas.drawLine(halfWidth - delta, i.toFloat(), halfWidth + delta, i.toFloat(), linePaint)
    }

    companion object {
        const val AMPLITUDE_MAX_VALUE = 15f
        const val AMPLITUDE_MIN_VALUE = 0f
    }

}