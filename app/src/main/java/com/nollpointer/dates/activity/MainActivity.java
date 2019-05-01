package com.nollpointer.dates.activity;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.appodeal.ads.Appodeal;
import com.crashlytics.android.Crashlytics;
import com.flurry.android.FlurryAgent;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nollpointer.dates.other.OnDatesLoadListener;
import com.nollpointer.dates.R;
import com.nollpointer.dates.other.StartPosition;
import com.nollpointer.dates.dates.DatesFragment;
import com.nollpointer.dates.gdpr.GDPRFragment;
import com.nollpointer.dates.menu.MenuFragment;
import com.nollpointer.dates.other.Date;
import com.nollpointer.dates.other.Misc;
import com.nollpointer.dates.practise.PractiseFragment;
import com.nollpointer.dates.statistics.StatisticsFragment;
import com.nollpointer.dates.other.PractiseHelpDialog;

import java.util.ArrayList;
import java.util.Locale;
import java.util.TreeMap;

import io.fabric.sdk.android.Fabric;

//TODO обновить appodeal
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

    private ArrayList<Date> dates;

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


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        mode = preferences.getInt("mode", FULL_DATES_MODE);
        boolean isAppodealInitialized = preferences.contains("gdpr_result");
        boolean isGdprAgree = preferences.getBoolean("gdpr_result", false);

        initializeAdds(isGdprAgree);


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
        new InitialLoadData(mode, isAppodealInitialized, bottomView, datesFragment).execute();


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
                transaction.commitAllowingStateLoss();

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
        Appodeal.initialize(this, "106e01ac39306b040f6b1d290a5b5bae37ebbcf794bb3cb1", Appodeal.INTERSTITIAL | Appodeal.BANNER | Appodeal.NON_SKIPPABLE_VIDEO, isGdprAgree);
    }

    public void setDates(ArrayList<Date> dates) {
        this.dates = dates;
    }

    public ArrayList<Date> getDates() {
        return dates;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
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

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void updateMode(int mode, OnDatesLoadListener listener) {
        this.mode = mode;
        new LoadDates(mode, listener).execute();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Разрешение получено", Toast.LENGTH_SHORT).show();
            } else {
                PractiseHelpDialog helpDialog = new PractiseHelpDialog();
                helpDialog.show(this.getSupportFragmentManager(), null);
            }
        }
    }

    public void checkLocaleSettings() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultLocale = getString(R.string.locale);
        String locale = preferences.getString("Locale", defaultLocale);
        if (!locale.equals(defaultLocale))
            setLocale(locale);
    }

    //TODO доделать изменение языка в приложении
    //Выставление языка приложения
    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        //new setNewPreferences(this, lang).execute();
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


    class InitialLoadData extends AsyncTask<Void, Void, Void> {

        BottomNavigationView bottomNavigationView;
        DatesFragment datesFragment;
        int mode;
        boolean isAppodealInitialized;

        public InitialLoadData(int mode, boolean isAppodealInitialized, BottomNavigationView bottomNavigationView, DatesFragment datesFragment) {
            this.mode = mode;
            this.isAppodealInitialized = isAppodealInitialized;
            this.bottomNavigationView = bottomNavigationView;
            this.datesFragment = datesFragment;
        }

        @Override
        protected void onPreExecute() {
            hideBottomNavigationView();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            if (isAppodealInitialized)
                fragmentTransaction.add(R.id.frameLayout, datesFragment, null);
            else
                fragmentTransaction.add(R.id.frameLayout, new GDPRFragment(), null);

            fragmentTransaction.commitAllowingStateLoss();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            setDates(Misc.getDates(mode));
            return null;
        }
    }


    class LoadDates extends AsyncTask<Void, Void, Void> {

        OnDatesLoadListener listener;
        int mode;

        public LoadDates(int mode, OnDatesLoadListener listener) {
            this.listener = listener;
            this.mode = mode;
        }

        @Override
        protected void onPreExecute() {
            listener.onLoadStart();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            listener.onLoadEnd();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            setDates(Misc.getDates(mode));

            return null;
        }
    }
}