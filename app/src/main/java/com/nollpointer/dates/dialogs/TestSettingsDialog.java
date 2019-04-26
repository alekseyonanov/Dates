package com.nollpointer.dates.dialogs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.nollpointer.dates.App;
import com.nollpointer.dates.R;
import com.nollpointer.dates.api.WikipediaResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestSettingsDialog extends BottomSheetDialogFragment {

    public static MoreInfoDialog newInstance() {
        MoreInfoDialog dialog = new MoreInfoDialog();
        Bundle args = new Bundle();
        dialog.setArguments(args);
        return dialog;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.test_settings_dialog_layout, container, false);



        return mainView;
    }
}