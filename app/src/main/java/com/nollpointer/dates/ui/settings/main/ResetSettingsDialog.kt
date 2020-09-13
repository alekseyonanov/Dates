package com.nollpointer.dates.ui.settings.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nollpointer.dates.R
import com.nollpointer.dates.other.Loader

/**
 * @author Onanov Aleksey (@onanov)
 */
class ResetSettingsDialog : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_reset_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<Button>(R.id.resetSettingsButton).setOnClickListener {
            Loader.clear(context as Context)
            dismiss()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = ResetSettingsDialog()
    }
}