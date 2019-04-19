package com.nollpointer.dates.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nollpointer.dates.MainActivity;
import com.nollpointer.dates.OnDatesLoadListener;
import com.nollpointer.dates.R;
import com.nollpointer.dates.SettingsActivity;


public class MenuFragment extends Fragment {

    private ViewPager modeViewPager;
    private int currentMode;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mainView = inflater.inflate(R.layout.fragment_menu, container, false);

        TextView telegram_message = mainView.findViewById(R.id.telegram_message);
        TextView twitter_message = mainView.findViewById(R.id.twitter_message);
        TextView mail_message = mainView.findViewById(R.id.mail_message);
        TextView game_start = mainView.findViewById(R.id.game_start);
        Toolbar toolbar = mainView.findViewById(R.id.menu_toolbar);

        telegram_message.setText("Telegram");
        telegram_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = null;
                try {
                    // get the Telegram app if possible
                    getContext().getPackageManager().getPackageInfo("org.telegram.messenger", 0);
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=nollpointer"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } catch (Exception e) {
                    // no Telegram app, revert to browser
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://t.me/nollpointer"));
                }
                startActivity(intent);
            }
        });

        twitter_message.setText("Twitter");
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

        mail_message.setText("Mail");
        mail_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"nollpointer@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.message_developer_title));
                startActivity(intent);
            }
        });

        game_start.setText("Game");
        game_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout, new GameFragment()).addToBackStack(null).commit();
            }
        });

        toolbar.inflateMenu(R.menu.menu_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
                return true;
            }
        });

        currentMode = ((MainActivity) getActivity()).getMode();

        modeViewPager = mainView.findViewById(R.id.menu_view_pager);
        final Button selectCurrentModeButton = mainView.findViewById(R.id.menu_select_current_mode);
        TabLayout tabLayout = (TabLayout) mainView.findViewById(R.id.menu_tab_dots);
        tabLayout.setupWithViewPager(modeViewPager, true);
        modeViewPager.setAdapter(new ModeAdapter(getContext(), R.array.mode_count, R.array.mode_titles));
        modeViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if(i ==  currentMode)
                    selectCurrentModeButton.setVisibility(View.GONE);
                else
                    selectCurrentModeButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        modeViewPager.setCurrentItem(currentMode);

        selectCurrentModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                currentMode = modeViewPager.getCurrentItem();
                mainActivity.updateMode(currentMode, new OnDatesLoadListener() {
                    @Override
                    public void onLoadStart() {
                        Log.e("TAG", "onLoadStart: " + System.currentTimeMillis());
                    }

                    @Override
                    public void onLoadEnd() {
                        Log.e("TAG", "onLoadEnd: " + System.currentTimeMillis());
                    }
                });

                Log.e("TAG", "onClick: " + currentMode);

                new SaveCurrentMode(getActivity(),currentMode).execute();

                v.setVisibility(View.GONE);
            }
        });

        return mainView;
    }

    private class ModeAdapter extends PagerAdapter {

        private Context context;
        private CharSequence[] modeCountArray;
        private CharSequence[] modeTitle;

        public ModeAdapter(Context context, int modeCountResourceId, int modeTitleResourceId) {
            this.context = context;
            Resources resources = context.getResources();

            this.modeCountArray = resources.getStringArray(modeCountResourceId);
            this.modeTitle = resources.getStringArray(modeTitleResourceId);
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            //CustomPagerEnum customPagerEnum = CustomPagerEnum.values()[position];
            LayoutInflater inflater = LayoutInflater.from(context);
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.mode_view_pager_cell, collection, false);
            collection.addView(layout);

            TextView modeTitleTextView = layout.findViewById(R.id.mode_title);
            TextView modeDatesCountTextView = layout.findViewById(R.id.mode_dates_count);

            modeTitleTextView.setText(modeTitle[position]);
            modeDatesCountTextView.setText(modeCountArray[position]);

            return layout;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    protected static class SaveCurrentMode extends AsyncTask<Void, Void, Void> {
        int mode;
        Context context;

        SaveCurrentMode(Context context,int mode) {
            this.context = context;
            this.mode = mode;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

            editor.putInt("mode",mode);

            Log.e("asd", "doInBackground: written");

            editor.apply();
            return null;
        }
    }

}
