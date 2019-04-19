package com.nollpointer.dates.dialogs;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nollpointer.dates.App;
import com.nollpointer.dates.R;
import com.nollpointer.dates.api.WikipediaResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MoreInfoDialog extends BottomSheetDialogFragment {
    public static final String REQUEST_URL = "request_url";

    String request;
    View mainView;
    TextView contentTextView;
    Button goToWikiButton;
    ProgressBar progressBar;
    View contentContainer;
    View noInternetView;

    public static MoreInfoDialog newInstance(String url) {
        MoreInfoDialog dialog = new MoreInfoDialog();
        Bundle args = new Bundle();
        args.putString(REQUEST_URL, url);
        dialog.setArguments(args);
        return dialog;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.dialog_more_info, container, false);

        contentTextView = mainView.findViewById(R.id.dialog_info);
        goToWikiButton = mainView.findViewById(R.id.dialog_go_to_source_button);
        progressBar = mainView.findViewById(R.id.dialog_progressbar);
        contentContainer = mainView.findViewById(R.id.dialog_content);
        noInternetView = mainView.findViewById(R.id.dialog_no_internet);
        request = getArguments().getString(REQUEST_URL);



        connectWikipedia();
        return mainView;
    }

    public void setInfo(String data, final String wiki_url) {
        noInternetView.setVisibility(View.INVISIBLE);
        contentContainer.setVisibility(View.VISIBLE);
        hideProgressBar();
        contentTextView.setText(Html.fromHtml(data));
        goToWikiButton.setText(R.string.go_to_wikipedia);
        goToWikiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(wiki_url));
                startActivity(intent);
            }
        });
    }

    private void hideProgressBar() {
        progressBar.setClickable(false);
        progressBar.setVisibility(View.GONE);
        progressBar.setEnabled(false);
    }

    private void showProgressBar() {
        progressBar.setClickable(true);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setEnabled(true);
        contentContainer.setVisibility(View.INVISIBLE);
        noInternetView.setVisibility(View.INVISIBLE);
    }

    private void connectWikipedia() {
        App.getApi().getData(request).enqueue(new Callback<WikipediaResponseModel>() {
            @Override
            public void onResponse(Call<WikipediaResponseModel> call, Response<WikipediaResponseModel> response) {
                String data, url;
                WikipediaResponseModel wiki;
                try {
                    wiki = response.body();
                    data = wiki.getExtractHtml();
                    url = wiki.getContentUrls().getMobile().getPage();
                    setInfo(data, url);
                } catch (Exception e) {
                    Log.wtf("ERROR_RESPONSE", e.toString());
                }
            }

            @Override
            public void onFailure(Call<WikipediaResponseModel> call, Throwable t) {
                Log.wtf("ERROR", t.getMessage());
                noInternetConnection();
            }
        });
    }

    public void noInternetConnection() {
        hideProgressBar();
        contentContainer.setVisibility(View.INVISIBLE);
        noInternetView.setVisibility(View.VISIBLE);
        noInternetView.findViewById(R.id.dialog_try_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectWikipedia();
                showProgressBar();
            }
        });



    }
}