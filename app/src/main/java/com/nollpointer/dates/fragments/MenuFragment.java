package com.nollpointer.dates.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nollpointer.dates.R;


public class MenuFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mainView = inflater.inflate(R.layout.fragment_menu, container, false);

        TextView telegram_message = mainView.findViewById(R.id.telegram_message);
        TextView twitter_message = mainView.findViewById(R.id.twitter_message);
        TextView mail_message = mainView.findViewById(R.id.mail_message);

        telegram_message.setText("@nollpointer");
        telegram_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=nollpointer"));
                startActivity(intent);
            }
        });

        twitter_message.setText("@nollpointer");
        twitter_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                try {
                    // get the Twitter app if possible
                    getContext().getPackageManager().getPackageInfo("com.twitter.android", 0);
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=nollpointer"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } catch (Exception e) {
                    // no Twitter app, revert to browser
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/nollpointer"));
                }
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
                intent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.message_developer_title));
                startActivity(intent);
            }
        });

        return mainView;
    }

}
