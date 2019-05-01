package com.nollpointer.dates.other;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.nollpointer.dates.R;
import com.nollpointer.dates.dates.MoreInfoDialog;

import org.w3c.dom.Text;

public class PractiseSettingsDialog extends BottomSheetDialogFragment {

    private static final String DELAY = "delay";

    public interface Listener{
        void onDelayPicked(int delay);
    }

    private Listener listener;

    public static PractiseSettingsDialog newInstance(int delay) {
        PractiseSettingsDialog dialog = new PractiseSettingsDialog();
        Bundle args = new Bundle();

        args.putInt(DELAY,delay);

        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.dialog_practise_settings_layout, container, false);

        int delay = getArguments().getInt(DELAY)/100;

        final TextView textView = mainView.findViewById(R.id.practise_settings_dialog_delay);
        textView.setText(delay * 100 + " мс");

        final SeekBar seekBar = mainView.findViewById(R.id.practise_settings_dialog_delay_seekbar);
        seekBar.setProgress(delay);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setText(progress * 100 + " мс");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        Button doneButton = mainView.findViewById(R.id.practise_settings_dialog_done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDelayPicked(seekBar.getProgress()*100);
                PractiseSettingsDialog.this.dismiss();
            }
        });

        return mainView;
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

}