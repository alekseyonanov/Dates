package com.nollpointer.dates;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appodeal.ads.Appodeal;
import com.crashlytics.android.Crashlytics;
import com.flurry.android.FlurryAgent;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nollpointer.dates.fragments.CalendarFragment;
import com.nollpointer.dates.fragments.DatesFragment;
import com.nollpointer.dates.fragments.GDPRFragment;
import com.nollpointer.dates.fragments.MenuFragment;
import com.nollpointer.dates.fragments.PractiseFragment;

import java.util.ArrayList;
import java.util.TreeMap;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    private int mode;

    private BottomNavigationViewEx bottomView;

    public static final String MODE = "MODE", PRACTISE = "PRACTISE", DATES = "DATES",
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
    private CalendarFragment calendarFragment;
    private MenuFragment menuFragment;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SharedPreferences prefs = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        //final Handler handler = new Handler();
        //mode = prefs.getInt(MODE, FULL_DATES_MODE);
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//
//                setDateList(Misc.getDates(FULL_DATES_MODE),FULL_DATES_MODE);
//                setDateList(Misc.getDates(EASY_DATES_MODE),EASY_DATES_MODE);
//
//
////                Cursor m_cursor = null;
////                Cursor e_cursor = null;
////                try (SQLiteDatabase sqLiteDatabase = new DatesDatabaseHelper(MainActivity.this).getReadableDatabase()) {
////                    String[] cells = new String[]{"DATE", "EVENT", "REQUEST", "CATEGORY"};
////                    m_cursor = sqLiteDatabase
////                            .query("D10", cells, null, null, null, null, null);
////                    e_cursor = sqLiteDatabase
////                            .query("D1", cells, null, null, null, null, null);
////                    m_cursor.moveToFirst();
////                    e_cursor.moveToFirst();
////                } catch (Exception e) {
////                    Log.e("SQl_DATABASE_EXCEPTION", e.toString());
////                }
////                handler.post(new FillDateArray(e_cursor, EASY_DATES_MODE));
////                new FillDateArray(m_cursor, FULL_DATES_MODE).run();
//
//            }
//        });

        datesFragment = new DatesFragment();
        practiseFragment = new PractiseFragment();
        calendarFragment = new CalendarFragment();
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
        new LoadData(bottomView, frameLayout).execute();

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
                        fragment = calendarFragment;
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

//                if (bottomView.getSelectedItemId() != id) {
//                    if (item.getItemId() == R.id.navigation_tests) {
//                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                        transaction.replace(R.id.frameLayout, practiseFragment, "TAG");
//                        transaction.addToBackStack(null);
//                        transaction.commit();
//                    } else
//                        getSupportFragmentManager().popBackStack(null, 0);
//                } else
//                    goToStartPosition();
                return true;
            }
        });
//        bottomView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                int id = item.getItemId();
//                if (bottomView.getSelectedItemId() != id) {
//                    if (item.getItemId() == R.id.navigation_tests) {
//                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                        transaction.replace(R.id.frameLayout, practiseFragment, "TAG");
//                        transaction.addToBackStack(null);
//                        transaction.commit();
//                    } else
//                        getSupportFragmentManager().popBackStack(null, 0);
//                } else
//                    goToStartPosition();
//                return true;
//            }
//        });
    }

    private void goToStartPosition() {
        StartPosition fragment = (StartPosition) getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        fragment.goToStartPosition();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomView.setVisibility(View.VISIBLE);
//        if (isFirstTime(GDPR_SHOW))
//            startGDPR();
    }

    public void startGDPR() {
//        Intent intent = new Intent(this, GDPRActivity.class);
//        startActivityForResult(intent, 1);
    }

    public void initializeAdds(boolean isGdprAgree) {
        Appodeal.disableLocationPermissionCheck();
        //Appodeal.disableNetwork(this, "mmedia");
        Appodeal.initialize(this, "106e01ac39306b040f6b1d290a5b5bae37ebbcf794bb3cb1", Appodeal.INTERSTITIAL | Appodeal.BANNER, isGdprAgree);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu,menu);
//        MenuItem mI = menu.findItem(R.id.mode_switch);
//        mI.setActionView(R.layout.switch_layout);
//        SwitchCompat switchCompat = (SwitchCompat) mI.getActionView();
//        if(mode == EASY_DATES_MODE) {
//            switchCompat.setChecked(true);
//            //refreshLook();
//        }
//        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
//                Fragment frg = MainActivity.this.getSupportFragmentManager().findFragmentById(R.id.frameLayout);
//                int string_resource_id = 0;
//                try {
//                    if (checked) {
//                        mode = EASY_DATES_MODE;
//                        string_resource_id = R.string.easy_mode;
//                    } else {
//                        mode = FULL_DATES_MODE;
//                        string_resource_id = R.string.full_mode;
//                    }
//                    //refreshLook();
//                    //animate(toolbar);
//                    if(frg != null && frg instanceof DatesFragment) {
//                        DatesFragment datesFragment = (DatesFragment) frg;
//                        datesFragment.setTabLayoutIndicatorColor(getCurrentColor());
//                        datesFragment.refresh();
//                    }
//                }catch (Exception e){
//                    Log.e("Switch_Exception",e.toString());
//                }
//                Snackbar.make(MainActivity.this.findViewById(R.id.frameLayout), string_resource_id,Snackbar.LENGTH_SHORT).show();
//
//            }
//        });
////        if(isFirstTime(DATES))
////            startFirstTimeUserTutorial();
//        return true;
//    }
//
//    public void animate(final View view){
//        int colorFrom,colorTo;
//        Resources resources = getResources();
//        if(mode == EASY_DATES_MODE){
//            colorFrom = resources.getColor(R.color.colorPrimary);
//            colorTo = resources.getColor(R.color.colorPrimaryEasy);
//        }else{
//            colorFrom = resources.getColor(R.color.colorPrimaryEasy);
//            colorTo = resources.getColor(R.color.colorPrimary);
//        }
//        final ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(),colorFrom,colorTo);
//        colorAnimator.setDuration(270);
//        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                view.setBackgroundColor((int)valueAnimator.getAnimatedValue());
//            }
//        });
//        colorAnimator.start();
//
//    }

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

//    public void startFirstTimeUserTutorial(){
//        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this);
//        View switch_compat = menu.findItem(R.id.mode_switch).getActionView();
//        View main_frame = findViewById(R.id.frameLayout);
//        MaterialShowcaseView bottom_showcase,switch_showcase,dates_showcase;
//        int mask_color = getResources().getColor(R.color.colorMask);
//        int dismiss_color = Color.GREEN;
//        bottom_showcase = new MaterialShowcaseView.Builder(this)
//                .setTarget(bottomView)
//                .setContentText(R.string.tutorial_bottom_view)
//                .setDismissText(R.string.got_it)
//                .setDismissOnTouch(true)
//                .setShape(new RectangleShape(bottomView.getWidth(),bottomView.getHeight()))
//                .setMaskColour(mask_color)
//                .setDismissTextColor(dismiss_color)
//                .build();
//        sequence.addSequenceItem(bottom_showcase);
//        switch_showcase = new MaterialShowcaseView.Builder(this)
//                .setTarget(switch_compat)
//                .setContentText(R.string.tutorial_switch)
//                .setDismissText(R.string.got_it)
//                .setTargetTouchable(true)
//                .setDismissOnTouch(true)
//                .setMaskColour(mask_color)
//                .setDismissTextColor(dismiss_color)
//                .build();
//        sequence.addSequenceItem(switch_showcase);
//        dates_showcase = new MaterialShowcaseView.Builder(this)
//                .setTarget(main_frame)
//                .setContentText(R.string.tutorial_dates)
//                .setDismissText(R.string.got_it)
//                .setDismissOnTouch(true)
//                .setShape(new NoShape())
//                .setMaskColour(mask_color)
//                .setDismissTextColor(dismiss_color)
//                .build();
//        sequence.addSequenceItem(dates_showcase);
//        sequence.start();
//    }


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

    public void refreshLook() {
        try {
            if (mode == FULL_DATES_MODE) {
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
            } else {
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryEasy)));
            }
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
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

        RelativeLayout loader;
        BottomNavigationView bottomNavigationView;
        FrameLayout frameLayout;

        TextView textView;

        public   LoadData(BottomNavigationView bottomNavigationView, FrameLayout frameLayout) {
            this.bottomNavigationView = bottomNavigationView;
            this.frameLayout = frameLayout;
        }

        @Override
        protected void onPreExecute() {
            loader = findViewById(R.id.loader);
            //frameLayout.addView(loader);
            textView = loader.findViewById(R.id.loaderText);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            frameLayout.removeView(loader);
            //bottomNavigationView.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, new GDPRFragment(), "TAG").commit();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            setDateList(Misc.getDates(FULL_DATES_MODE), FULL_DATES_MODE);
            setDateList(Misc.getDates(EASY_DATES_MODE), EASY_DATES_MODE);
            return null;
        }
    }

    class FillDateArray implements Runnable {
        Cursor cursor;
        int mode;

        FillDateArray(Cursor crs, int mode) {
            cursor = crs;
            this.mode = mode;
        }

        @Override
        public void run() {
            ArrayList<Date> list = new ArrayList<>();
            list.add(new Date(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3))); // нулевой элемент
            while (cursor.moveToNext())
                list.add(new Date(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3)));
            cursor.close();
            setDateList(list, mode);
        }
    }
}