package com.nollpointer.dates.ui.dates

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nollpointer.dates.R

class DatesInfoDialog : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //        TextView textView = mainView.findViewById(R.id.practise_help_dialog_text);
//
//        Button doneButton = mainView.findViewById(R.id.practise_help_dialog_done_button);
//        doneButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DatesInfoDialog.this.dismiss();
//            }
//        });
        return inflater.inflate(R.layout.dialog_dates_info, container, false)
    }

    companion object {
        private const val TEXT_ID = "text_id"
        @JvmStatic
        fun newInstance(textId: Int): DatesInfoDialog {
            val dialog = DatesInfoDialog()
            val args = Bundle()
            args.putInt(TEXT_ID, textId)
            dialog.arguments = args
            return dialog
        }
    }
}