package com.nollpointer.dates.dialogs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nollpointer.dates.R;


public class MessageDeveloperDialog extends BottomSheetDialogFragment {

    View main_view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.message_developer_dialog, container, false);
//        main_view.findViewById(R.id.message_close_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                MessageDeveloperDialog.this.dismiss();
//            }
//        });

//        CardView card = main_view.findViewById(R.id.telegram_message);
//        ((ImageView)card.findViewById(R.id.message_icon)).setImageResource(R.drawable.ic_telegram_message);
//        ((TextView)card.findViewById(R.id.message_title)).setText("Telegram");
//        ((TextView)card.findViewById(R.id.message_subtitle)).setText("@nollpointer");

        TextView telegram_message = main_view.findViewById(R.id.telegram_message);
        TextView mail_message = main_view.findViewById(R.id.mail_message);

        telegram_message.setText("@nollpointer");
        telegram_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=nollpointer"));
                startActivity(intent);
            }
        });

        mail_message.setText("nollpointer@gmail.com");
        mail_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"nollpointer@gmail.com"});
                startActivity(intent);
            }
        });


//        card = main_view.findViewById(R.id.mail_message);
//        ((ImageView)card.findViewById(R.id.message_icon)).setImageResource(R.drawable.ic_mail_message);
//        ((TextView)card.findViewById(R.id.message_title)).setText("Email");
//        ((TextView)card.findViewById(R.id.message_subtitle)).setText("nollpointer@gmail.com");


        return main_view;
    }
}
