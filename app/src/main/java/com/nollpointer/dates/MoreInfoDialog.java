package com.nollpointer.dates;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MoreInfoDialog extends BottomSheetDialogFragment{
    public static final String REQUEST_URL = "request_url";

    String request;
    View main_view;
    TextView textView_info;
    Button goToSourceButton;
    ProgressBar progressBar;
    View content;
    View no_internet;

    public static MoreInfoDialog newInstance(String url){
        MoreInfoDialog dialog = new MoreInfoDialog();
        Bundle args = new Bundle();
        args.putString(REQUEST_URL,url);
        dialog.setArguments(args);
        return dialog;
    }


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
        no_internet =main_view.findViewById(R.id.dialog_no_internet);
        request = getArguments().getString(REQUEST_URL);
        connectWikipedia();
        return main_view;
    }

    public void setInfo(String data,final String wiki_url){
        no_internet.setVisibility(View.INVISIBLE);
        content.setVisibility(View.VISIBLE);
        hideProgressBar();
        textView_info.setText(Html.fromHtml(data));
        goToSourceButton.setText(R.string.go_to_wikipedia);
        goToSourceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(wiki_url));
                startActivity(intent);
            }
        });
    }

    private void hideProgressBar(){
        progressBar.setClickable(false);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.setEnabled(false);
    }

    private void showProgressBar(){
        progressBar.setClickable(true);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setEnabled(true);
        content.setVisibility(View.INVISIBLE);
        no_internet.setVisibility(View.INVISIBLE);
    }

    private void connectWikipedia(){
        App.getApi().getData(request).enqueue(new Callback<WikipediaResponseModel>() {
            @Override
            public void onResponse(Call<WikipediaResponseModel> call, Response<WikipediaResponseModel> response) {
                String data, url;
                WikipediaResponseModel wiki;
                try {
                    wiki = response.body();
                    data = wiki.getExtractHtml();
                    url = wiki.getContentUrls().getMobile().getPage();
                    setInfo(data,url);
                }catch (Exception e){
                    Log.wtf("ERROR_RESPONSE",e.toString());
                }
            }

            @Override
            public void onFailure(Call<WikipediaResponseModel> call, Throwable t) {
                Log.wtf("ERROR", t.getMessage());
                noInternetConnection();
            }
        });
    }

    public void noInternetConnection(){
        hideProgressBar();
        content.setVisibility(View.INVISIBLE);
        no_internet.setVisibility(View.VISIBLE);
        no_internet.findViewById(R.id.dialog_try_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectWikipedia();
                showProgressBar();
            }
        });
        no_internet.findViewById(R.id.dialog_close_button_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }
}