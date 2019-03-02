package com.nollpointer.dates;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.appodeal.ads.Appodeal;
import com.crashlytics.android.Crashlytics;
import com.flurry.android.FlurryAgent;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nollpointer.dates.fragments.StatisticsFragment;
import com.nollpointer.dates.fragments.DatesFragment;
import com.nollpointer.dates.fragments.GDPRFragment;
import com.nollpointer.dates.fragments.MenuFragment;
import com.nollpointer.dates.fragments.PractiseFragment;

import java.util.ArrayList;
import java.util.TreeMap;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private int mode;

    private BottomNavigationViewEx bottomView;

    public static final String MODE = "MODE123", PRACTISE = "PRACTISE", DATES = "DATES",
            SORT = "SORT", SORT_CHECK = "SORT_CHECK", TRUE_FALSE = "TRUE_FALSE", CARDS = "CARDS";
    public static final String GDPR = "GDPR", GDPR_SHOW = "GDPR_SHOW";
    public static final String SETTINGS = "SETTINGS";

    public static final int FULL_DATES_MODE = 0;
    public static final int EASY_DATES_MODE = 1;

    private TreeMap<String, Boolean> preferences = new TreeMap<>();

    private ArrayList<Date> full_list;
    private ArrayList<Date> easy_list;

    private DatesFragment datesFragment;
    private PractiseFragment practiseFragment;
    private StatisticsFragment statisticsFragment;
    private MenuFragment menuFragment;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datesFragment = new DatesFragment();
        practiseFragment = new PractiseFragment();
        statisticsFragment = new StatisticsFragment();
        menuFragment = new MenuFragment();

//        preferences.put(DATES, prefs.getBoolean(DATES, true));
//        preferences.put(PRACTISE, prefs.getBoolean(PRACTISE, true));
//        preferences.put(SORT, prefs.getBoolean(SORT, true));
//        preferences.put(SORT_CHECK, prefs.getBoolean(SORT_CHECK, true));
//        preferences.put(CARDS, prefs.getBoolean(CARDS, true));
//        preferences.put(TRUE_FALSE, prefs.getBoolean(TRUE_FALSE, true));
//        preferences.put(GDPR_SHOW, prefs.getBoolean(GDPR_SHOW, true));

        initializeBottomView();
        FrameLayout frameLayout = findViewById(R.id.frameLayout);
        new LoadData(bottomView, datesFragment).execute();

        SharedPreferences preferences =   PreferenceManager.getDefaultSharedPreferences(this);

        mode = preferences.getInt("mode",FULL_DATES_MODE);

//        if (prefs.contains(GDPR)) {
//            boolean isGDPRAgree = prefs.getBoolean(GDPR, false);
//            preferences.put(GDPR, isGDPRAgree);
//            initializeAdds(isGDPRAgree);
//        } else {
//            preferences.put(GDPR, false);
//            initializeAdds(false);
//        }

        Fabric.with(this, new Crashlytics());
    }

    private void initializeBottomView() {
        //toolbar = findViewById(R.id.toolbar_actionbar);
        bottomView = (BottomNavigationViewEx) findViewById(R.id.navigation);

        //setSupportActionBar(toolbar);

        //bottomView.inflateMenu(R.menu.navigation);
        bottomView.setSelectedItemId(R.id.navigation_dates);
        bottomView.enableAnimation(false);
        bottomView.enableShiftingMode(false);
        bottomView.enableItemShiftingMode(false);
        bottomView.setTextVisibility(false);
        bottomView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.navigation_dates:
                        fragment = datesFragment;
                        break;
                    case R.id.navigation_practise:
                        fragment = practiseFragment;
                        break;
                    case R.id.navigation_calendar:
                        fragment = statisticsFragment;
                        break;
                    case R.id.navigation_menu:
                        fragment = menuFragment;
                        break;
                    default:
                        fragment = datesFragment;
                }

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frameLayout, fragment, "TAG");
                //transaction.addToBackStack(null);
                transaction.commit();

                return true;
            }
        });
    }

    private void goToStartPosition() {
        StartPosition fragment = (StartPosition) getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        fragment.goToStartPosition();
    }

    public void initializeAdds(boolean isGdprAgree) {
        Appodeal.disableLocationPermissionCheck();
        //Appodeal.disableNetwork(this, "mmedia");
        Appodeal.initialize(this, "106e01ac39306b040f6b1d290a5b5bae37ebbcf794bb3cb1", Appodeal.INTERSTITIAL | Appodeal.BANNER | Appodeal.NON_SKIPPABLE_VIDEO, isGdprAgree);
    }

    public int getCurrentColor() {
        if (mode == FULL_DATES_MODE)
            return getResources().getColor(R.color.colorPrimary);
        return getResources().getColor(R.color.colorPrimaryEasy);
    }

    public void setDateList(ArrayList<Date> array, int mode) {
        if (mode == FULL_DATES_MODE)
            full_list = array;
        else
            easy_list = array;
//        if (full_list != null && easy_list != null)
////            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, datesFragment, "TAG").commit();
    }

    public ArrayList<Date> getDateList() {
        if (mode == FULL_DATES_MODE)
            return full_list;
        else
            return easy_list;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //new setNewPreferences(mode, preferences).execute(this);
        FlurryAgent.onEndSession(this);
    }

    public boolean isFirstTime(String key) {
//        boolean is = preferences.get(key);
//        if (is)
//            preferences.put(key, false);
//        return is;
        return false;
    }


    public void hideBottomNavigationView() {
        bottomView.setVisibility(View.GONE);
    }

    public void showBottomNavigationView() {
        bottomView.setVisibility(View.VISIBLE);
    }

    public void updateBottomNavigationView(int id) {
        bottomView.setSelectedItemId(id);
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode){
        this.mode = mode;
    }

    protected static class setNewPreferences extends AsyncTask<MainActivity, Void, Void> {
        TreeMap<String, Boolean> prefs;
        int mode;

        setNewPreferences(int mode, TreeMap<String, Boolean> prefs) {
            this.mode = mode;
            this.prefs = prefs;
        }

        @Override
        protected Void doInBackground(MainActivity... mainActivities) {
            SharedPreferences.Editor editor = mainActivities[0].getSharedPreferences(SETTINGS, Context.MODE_PRIVATE).edit();
            editor.putInt(MODE, mode);
            if (prefs.containsKey(GDPR))
                editor.putBoolean(GDPR, prefs.get(GDPR));

            editor.putBoolean(GDPR_SHOW, prefs.get(GDPR_SHOW));
            editor.putBoolean(PRACTISE, prefs.get(PRACTISE));
            editor.putBoolean(DATES, prefs.get(DATES));
            editor.putBoolean(SORT, prefs.get(SORT));
            editor.putBoolean(SORT_CHECK, prefs.get(SORT_CHECK));
            editor.putBoolean(TRUE_FALSE, prefs.get(TRUE_FALSE));
            editor.putBoolean(CARDS, prefs.get(CARDS));
            editor.apply();
            return null;
        }
    }

    class LoadData extends AsyncTask<Void, Void, Void> {

        BottomNavigationView bottomNavigationView;
        DatesFragment datesFragment;

        public LoadData(BottomNavigationView bottomNavigationView, DatesFragment datesFragment) {
            this.bottomNavigationView = bottomNavigationView;
            this.datesFragment = datesFragment;
        }

        @Override
        protected void onPreExecute() {
            //frameLayout.addView(loader);
            hideBottomNavigationView();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //bottomNavigationView.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, datesFragment, "TAG").commit();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            setDateList(Misc.getDates(FULL_DATES_MODE), FULL_DATES_MODE);
            setDateList(Misc.getDates(EASY_DATES_MODE), EASY_DATES_MODE);
            return null;
        }
    }
}