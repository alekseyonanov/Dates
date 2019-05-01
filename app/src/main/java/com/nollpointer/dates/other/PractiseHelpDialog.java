package com.nollpointer.dates.other;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.nollpointer.dates.R;
import com.nollpointer.dates.dates.MoreInfoDialog;

public class PractiseHelpDialog extends BottomSheetDialogFragment {


    private static final String TEXT_ID = "text_id";

    //public static MoreInfoDialog newInstance(int textId) {
    public static PractiseHelpDialog newInstance() {
        PractiseHelpDialog dialog = new PractiseHelpDialog();
        Bundle args = new Bundle();

        args.putInt(TEXT_ID,12);

        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.dialog_practise_help_layout, container, false);

//        int textId = getArguments().getInt(TEXT_ID);

        TextView textView = mainView.findViewById(R.id.practise_help_dialog_text);
        //textView.setText(textId);

        Button doneButton = mainView.findViewById(R.id.practise_help_dialog_done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PractiseHelpDialog.this.dismiss();
            }
        });

        return mainView;
    }
}