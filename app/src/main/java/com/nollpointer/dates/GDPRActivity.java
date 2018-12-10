package com.nollpointer.dates;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

public class GDPRActivity extends AppCompatActivity {
    TextView yes_text;
    TextView no_text;
    TextView info_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gdpr);
        initViews();
        prepareForGDPR();
    }

    private void initViews() {
        yes_text = findViewById(R.id.yes_text);
        no_text = findViewById(R.id.no_text);
        info_text = findViewById(R.id.info_text);
    }

    private void prepareForGDPR() {
        String learnMore = getString(R.string.gdpr_learn_more);
        String mainText = getString(R.string.gdpr_main_text);
        int startPosition = mainText.indexOf(learnMore);
        int endPosition = startPosition + learnMore.length();
        SpannableString spannableMain = new SpannableString(mainText);
        spannableMain.setSpan(new URLSpan("https://www.appodeal.com/privacy-policy"), startPosition, endPosition, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        info_text.setMovementMethod(LinkMovementMethod.getInstance());
        info_text.setText(spannableMain);

        yes_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showResultView(true);
            }
        });

        String no = getString(R.string.gdpr_disagree).toUpperCase();
        SpannableString spannableNo = new SpannableString(no);
        spannableNo.setSpan(new UnderlineSpan(), 0, spannableNo.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        no_text.setText(spannableNo, TextView.BufferType.SPANNABLE);
        no_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showResultView(false);
            }
        });
    }

    private void showResultView(boolean result) {
        sendResult(result);
        startActivity(GDPRResultActivity.getIntent(this, result));
        finish();
    }

    private void sendResult(boolean result) {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.GDPR, result);
        setResult(1, intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        sendResult(false);
        super.onBackPressed();
    }
}
