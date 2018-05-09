package com.nollpointer.dates;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

public class TypePickDialog extends DialogFragment {
    private int mPosition = 0;

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final MainActivity mnc = (MainActivity) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(mnc);
        builder.setSingleChoiceItems(R.array.pick_type, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setPosition(i);
            }
        })
                .setTitle(R.string.type_pick_title)
        .setNegativeButton(R.string.cancel_button, null).setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mnc.typePicked(getPosition());
            }
        });
        return builder.create();
    }
}
