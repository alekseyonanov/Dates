package com.nollpointer.dates.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.nollpointer.dates.R

class ResultDialog(var rightAnswers: Int, var mark: String, var markColor: Int, var listener: ResultDialogCallbackListener) {

    interface ResultDialogCallbackListener {
        fun reset()
        fun exit()
    }

    fun showDialog(context: Context?) {
        val factory = LayoutInflater.from(context)
        val dialogView = factory.inflate(R.layout.dialog_result_layout, null)
        val dialog = AlertDialog.Builder(context!!).create()
        dialog.setCancelable(false)
        dialog.setView(dialogView)
        dialogView.findViewById<View>(R.id.result_exit).setOnClickListener {
            dialog.dismiss()
            listener.exit()
        }
        dialogView.findViewById<View>(R.id.result_reset).setOnClickListener {
            dialog.dismiss()
            listener.reset()
        }
        var textView = dialogView.findViewById<TextView>(R.id.result_mark)
        textView.text = mark
        textView.setTextColor(markColor)
        val imageId = imageId
        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(imageId, 0, 0, 0)
        textView = dialogView.findViewById(R.id.result_right_answers)
        val answers = StringBuffer(rightAnswers.toString())
        if (rightAnswers == 0) answers.append(" правильных ответов") else if (rightAnswers == 1) answers.append(" правильный ответ") else if (rightAnswers < 5) answers.append(" правильный ответа") else answers.append(" правильных ответов")
        textView.text = answers
        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_check_box, 0, 0, 0)
        dialog.show()
    }

    private val imageId =
        when(rightAnswers) {
            in 0..4 -> R.drawable.ic_sentiment_very_bad
            in 5..8 -> R.drawable.ic_sentiment_bad
            in 9..12 -> R.drawable.ic_sentiment_neutral
            in 13..16 -> R.drawable.ic_sentiment_good
            else -> R.drawable.ic_sentiment_very_good
        }

}