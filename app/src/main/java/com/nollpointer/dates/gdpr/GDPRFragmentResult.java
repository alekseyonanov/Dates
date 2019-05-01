package com.nollpointer.dates.gdpr;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nollpointer.dates.R;
import com.nollpointer.dates.dates.DatesFragment;


public class GDPRFragmentResult extends Fragment {
    private static final String RESULT_GDPR = "result_gdpr";

    private TextView infoText;
    private LinearLayout closeButton;
    private TextView closeText;

    public static GDPRFragmentResult getInstance(boolean result) {
        GDPRFragmentResult fragmentResult = new GDPRFragmentResult();
        Bundle bundle = new Bundle();
        bundle.putBoolean(RESULT_GDPR, result);
        fragmentResult.setArguments(bundle);
        return fragmentResult;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gdpr_result, container, false);
        Bundle bundle = getArguments();
        boolean isGdprAgree = bundle.getBoolean(RESULT_GDPR);
        initViews(view);
        prepareResult(isGdprAgree);

        return view;
    }

    private void initViews(View mainView) {
        infoText = mainView.findViewById(R.id.info_text);
        closeButton = mainView.findViewById(R.id.close_button);
        closeText = mainView.findViewById(R.id.close_text);
    }

    private void prepareResult(boolean isGdprAgree) {
        infoText.setMovementMethod(LinkMovementMethod.getInstance());

        if (isGdprAgree) {
            infoText.setText(getString(R.string.gdpr_agree_text));
        } else {
            infoText.setText(getString(R.string.gdpr_disagree_text));
        }

        String close = getString(R.string.gdpr_close).toUpperCase();
        SpannableString spannableClose = new SpannableString(close);
        spannableClose.setSpan(new UnderlineSpan(), 0, spannableClose.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        closeText.setText(spannableClose);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout, new DatesFragment()).commit();
            }
        });
    }
}
