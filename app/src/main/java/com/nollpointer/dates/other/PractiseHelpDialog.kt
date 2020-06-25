package com.nollpointer.dates.other

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nollpointer.dates.R

class PractiseHelpDialog : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mainView = inflater.inflate(R.layout.dialog_practise_help_layout, container, false)
        val textId = arguments!!.getInt(TEXT_ID)
        val textView = mainView.findViewById<TextView>(R.id.practise_help_dialog_text)
        textView.setText(textId)
        val doneButton = mainView.findViewById<Button>(R.id.practise_help_dialog_done_button)
        doneButton.setOnClickListener { dismiss() }
        return mainView
    }

    companion object {
        private const val TEXT_ID = "text_id"
        @JvmStatic
        fun newInstance(textId: Int): PractiseHelpDialog {
            val dialog = PractiseHelpDialog()
            val args = Bundle()
            args.putInt(TEXT_ID, textId)
            dialog.arguments = args
            return dialog
        }
    }
}