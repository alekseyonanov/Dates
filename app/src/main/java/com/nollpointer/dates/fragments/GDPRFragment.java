package com.nollpointer.dates.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nollpointer.dates.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GDPRFragment extends Fragment {
    TextView yesText;
    TextView noText;
    TextView infoText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gdpr, container, false);
        initViews(view);
        prepareForGDPR();
        return view;
    }

    private void initViews(View mainView) {
        yesText = mainView.findViewById(R.id.yes_text);
        noText = mainView.findViewById(R.id.no_text);
        infoText = mainView.findViewById(R.id.info_text);
    }

    private void prepareForGDPR() {
        String learnMore = getString(R.string.gdpr_learn_more);
        String mainText = getString(R.string.gdpr_main_text);
        int startPosition = mainText.indexOf(learnMore);
        int endPosition = startPosition + learnMore.length();
        SpannableString spannableMain = new SpannableString(mainText);
        spannableMain.setSpan(new URLSpan("https://www.appodeal.com/privacy-policy"), startPosition, endPosition, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        infoText.setMovementMethod(LinkMovementMethod.getInstance());
        infoText.setText(spannableMain);

        yesText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showResultView(true);
            }
        });

        String no = getString(R.string.gdpr_disagree).toUpperCase();
        SpannableString spannableNo = new SpannableString(no);
        spannableNo.setSpan(new UnderlineSpan(), 0, spannableNo.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        noText.setText(spannableNo, TextView.BufferType.SPANNABLE);
        noText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showResultView(false);
            }
        });
    }

    private void showResultView(boolean result) {
        //sendResult(result);
        GDPRFragmentResult fragmentResult = GDPRFragmentResult.getInstance(result);
        getFragmentManager().beginTransaction().replace(R.id.frameLayout,fragmentResult).commit();
    }

    private void sendResult(boolean result) {
//        Intent intent = new Intent();
//        intent.putExtra(MainActivity.GDPR, result);
//        setResult(1, intent);
    }

}
