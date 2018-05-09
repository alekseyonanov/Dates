package com.nollpointer.dates;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;


public class CenturyPickDialog extends DialogFragment {

    public static interface NoticeDialogListener {
        public void onButtonClicked(ArrayList<Integer> dialog);
    }
    private NoticeDialogListener NListner;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final ArrayList<Integer> mSelectedItems = new ArrayList<>();
        MainActivity m = (MainActivity) getActivity();
        NListner = m;
        int tag = m.getMode();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_title)//.setTitle(R.string.dialog_title)
                .setMultiChoiceItems(tag==0 ? R.array.centuries : R.array.centuries_easy, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                if(b)
                    mSelectedItems.add(i);
                else
                    mSelectedItems.remove(Integer.valueOf(i));

            }
        }).setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(mSelectedItems.isEmpty())
                    NListner.onButtonClicked(null);
                else
                    NListner.onButtonClicked(mSelectedItems);
            }
        }).setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                NListner.onButtonClicked(null);
            }
        });
        return builder.create();
    }
}
