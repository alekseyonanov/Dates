package com.nollpointer.dates;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MoreInfoDialog extends BottomSheetDialogFragment {
    String data,url;
    View main_view;
    TextView textView_info;
    Button goToSourceButton;
    ProgressBar progressBar;
    View content;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.dialog_more_info,container,false);
        main_view.findViewById(R.id.dialog_close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoreInfoDialog.this.dismiss();
            }
        });
        textView_info = main_view.findViewById(R.id.dialog_info);
        goToSourceButton = main_view.findViewById(R.id.dialog_go_to_source_button);
        progressBar = main_view.findViewById(R.id.dialog_progressbar);
        content = main_view.findViewById(R.id.dialog_content);
        return main_view;
    }

    public void setInfo(String data,final String url){
        this.data = data;
        this.url = url;
        textView_info.setText(Html.fromHtml(data));
        goToSourceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        content.setVisibility(View.VISIBLE);
        progressBar.setClickable(false);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.setEnabled(false);
    }
}
