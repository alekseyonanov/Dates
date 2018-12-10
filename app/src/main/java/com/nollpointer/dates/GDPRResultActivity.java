package com.nollpointer.dates;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GDPRResultActivity extends AppCompatActivity {

    private static final String RESULT_GDPR = "result_gdpr";

    private TextView info_text;
    private LinearLayout close_button;
    private TextView close_text;


    public static Intent getIntent(Context context, boolean resultGDPR) {
        Intent intent = new Intent(context, GDPRResultActivity.class);
        intent.putExtra(RESULT_GDPR, resultGDPR);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gdpr_result);
        boolean isGdprAgree = getIntent().getBooleanExtra(RESULT_GDPR, false);
        initViews();
        prepareResult(isGdprAgree);
    }

    private void initViews() {
        info_text = findViewById(R.id.info_text);
        close_button = findViewById(R.id.close_button);
        close_text = findViewById(R.id.close_text);
    }

    private void prepareResult(boolean isGdprAgree) {
        info_text.setMovementMethod(LinkMovementMethod.getInstance());

        if (isGdprAgree) {
            info_text.setText(getString(R.string.gdpr_agree_text));
        } else {
            info_text.setText(getString(R.string.gdpr_disagree_text));
        }

        String close = getString(R.string.gdpr_close).toUpperCase();
        SpannableString spannableClose = new SpannableString(close);
        spannableClose.setSpan(new UnderlineSpan(), 0, spannableClose.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        close_text.setText(spannableClose);
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
