package com.nollpointer.dates.dialogs;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.nollpointer.dates.R;


public class ResultDialog {

    public static interface ResultDialogCallbackListener {
        void reset();

        void exit();
    }

    ResultDialogCallbackListener listener;

    int rightAnswers;
    String mark;
    int markColor;

    public ResultDialog(int rightAnswers, String mark, int markColor, ResultDialogCallbackListener listener) {
        this.rightAnswers = rightAnswers;
        this.mark = mark;
        this.markColor = markColor;
        this.listener = listener;
    }

    public void showDialog(Context context) {
        LayoutInflater factory = LayoutInflater.from(context);
        View dialogView = factory.inflate(R.layout.result_dialog_layout, null);
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setCancelable(false);
        dialog.setView(dialogView);


        dialogView.findViewById(R.id.result_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                listener.exit();
            }
        });

        dialogView.findViewById(R.id.result_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                listener.reset();
            }
        });

        TextView textView = dialogView.findViewById(R.id.result_mark);
        textView.setText(mark);
        textView.setTextColor(markColor);
        int imageId = getImageId();
        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(imageId, 0, 0, 0);

        textView = dialogView.findViewById(R.id.result_right_answers);
        StringBuffer answers = new StringBuffer(Integer.toString(rightAnswers));

        if (rightAnswers == 0)
            answers.append(" правильных ответов");
        else if (rightAnswers == 1)
            answers.append(" правильный ответ");
        else if (rightAnswers < 5)
            answers.append(" правильный ответа");
        else
            answers.append(" правильных ответов");
        textView.setText(answers);
        textView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_check_box, 0, 0, 0);


        dialog.show();
    }

    private int getImageId() {
        int imageId;
        if (rightAnswers < 5)
            imageId = R.drawable.ic_sentiment_very_bad;
        else if (rightAnswers < 9)
            imageId = R.drawable.ic_sentiment_bad;
        else if (rightAnswers < 13)
            imageId = R.drawable.ic_sentiment_neutral;
        else if (rightAnswers < 17)
            imageId = R.drawable.ic_sentiment_good;
        else
            imageId = R.drawable.ic_sentiment_very_good;
        return imageId;
    }
}
