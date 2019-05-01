package com.nollpointer.dates.gdpr;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.fragment.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
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
        initializeViews(view);
        prepareForGDPR();
        return view;
    }

    private void initializeViews(View mainView) {
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
                showResultFragment(true);
            }
        });

        String no = getString(R.string.gdpr_disagree).toUpperCase();
        SpannableString spannableNo = new SpannableString(no);
        spannableNo.setSpan(new UnderlineSpan(), 0, spannableNo.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        noText.setText(spannableNo, TextView.BufferType.SPANNABLE);
        noText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showResultFragment(false);
            }
        });
    }

    private void showResultFragment(boolean result) {

        new SaveGdprResult(getContext(),result).execute();

        //sendResult(result);
        GDPRFragmentResult fragmentResult = GDPRFragmentResult.getInstance(result);
        getFragmentManager().beginTransaction().replace(R.id.frameLayout,fragmentResult).commit();
    }

    protected static class SaveGdprResult extends AsyncTask<Void, Void, Void> {
        boolean result;
        Context context;

        SaveGdprResult(Context context,boolean result) {
            this.context = context;
            this.result = result;
        }

        @Override
        protected void onPreExecute() {
            FlurryAgent.logEvent("Is_GDPR_Agree " + result);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

            editor.putBoolean("gdpr_result",result);

            editor.apply();
            return null;
        }
    }

}
