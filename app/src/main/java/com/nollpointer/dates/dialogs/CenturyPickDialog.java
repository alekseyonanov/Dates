package com.nollpointer.dates.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;

import com.nollpointer.dates.R;

import java.util.ArrayList;


public class CenturyPickDialog extends DialogFragment {

    public static interface NoticeDialogListener {
        void onButtonClicked(ArrayList<Integer> dialog);
    }

    private NoticeDialogListener NListener;

    public void setListener(NoticeDialogListener listener) {
        NListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final ArrayList<Integer> mSelectedItems = new ArrayList<>();
        int mode = Integer.parseInt(getTag());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_title)
                .setMultiChoiceItems(mode == 0 ? R.array.centuries : R.array.centuries_easy, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b)
                            mSelectedItems.add(i);
                        else
                            mSelectedItems.remove(Integer.valueOf(i));

                    }
                }).setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (mSelectedItems.isEmpty())
                    NListener.onButtonClicked(null);
                else
                    NListener.onButtonClicked(mSelectedItems);
            }
        }).setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                NListener.onButtonClicked(null);
            }
        });
        return builder.create();
    }
}
