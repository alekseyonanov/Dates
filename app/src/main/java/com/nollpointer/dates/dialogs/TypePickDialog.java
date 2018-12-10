package com.nollpointer.dates.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.nollpointer.dates.R;

public class TypePickDialog extends DialogFragment {
    private int mPosition = 0;
    private Listener listener;

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public static interface Listener {
        void typePicked(int type);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setSingleChoiceItems(R.array.pick_type, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setPosition(i);
            }
        }).setTitle(R.string.type_pick_title)
                .setNegativeButton(R.string.cancel_button, null).setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.typePicked(getPosition());
            }
        });
        return builder.create();
    }
}
