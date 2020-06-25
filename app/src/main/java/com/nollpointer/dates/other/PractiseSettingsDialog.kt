package com.nollpointer.dates.other

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nollpointer.dates.R

class PractiseSettingsDialog : BottomSheetDialogFragment() {

    interface Listener {
        fun onDelayPicked(delay: Int)
    }

    private lateinit var listener: Listener
    private lateinit var seekBar: SeekBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mainView = inflater.inflate(R.layout.dialog_practise_settings_layout, container, false)
        val delay = arguments!!.getInt(DELAY) / 100
        val textView = mainView.findViewById<TextView>(R.id.practise_settings_dialog_delay)
        textView.setText((delay * 100).toString() + " мс")
        seekBar = mainView.findViewById(R.id.practise_settings_dialog_delay_seekbar)
        seekBar.progress = delay
        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                textView.text = (progress * 100).toString() + " мс"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        val doneButton = mainView.findViewById<Button>(R.id.practise_settings_dialog_done_button)
        doneButton.setOnClickListener {
            listener.onDelayPicked(seekBar.progress * 100)
            dismiss()
        }
        return mainView
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener.onDelayPicked(seekBar.progress * 100)
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    companion object {
        private const val DELAY = "delay"
        @JvmStatic
        fun newInstance(delay: Int): PractiseSettingsDialog {
            val dialog = PractiseSettingsDialog()
            val args = Bundle()
            args.putInt(DELAY, delay)
            dialog.arguments = args
            return dialog
        }
    }
}