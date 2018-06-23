package com.nollpointer.dates;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.appodeal.ads.Appodeal;
import com.flurry.android.FlurryAgent;

import java.util.TreeMap;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.shape.NoShape;
import uk.co.deanwild.materialshowcaseview.shape.RectangleShape;

public class MainActivity extends AppCompatActivity{
    private Cursor main_cursor = null;
    private Cursor easy_cursor = null;
    private BottomNavigationView BottomView;
    private int mode;
    private Menu menu;
    public static final int FULL_DATES_MODE = 0;
    public static final int EASY_DATES_MODE = 1;
    private LinearLayout linearLayout;
    public static final String MODE = "MODE", PRACTISE = "PRACTISE", DATES = "DATES", SORT = "SORT",SORT_CHECK = "SORT_CHECK", TRUE_FALSE = "TRUE_FALSE", CARDS = "CARDS";
    public static final String SETTINGS = "SETTINGS";
    private TreeMap<String,Boolean> preferences = new TreeMap<>();


    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = findViewById(R.id.container_main);
        Toolbar toolbar = findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);
        SharedPreferences prefs = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        Handler handler = new Handler();
        //this.preferences = prefs.getAll();
        mode = prefs.getInt(MODE,FULL_DATES_MODE);
        handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Cursor m_cursor = null;
                            Cursor e_cursor = null;
                            try(SQLiteDatabase sqLiteDatabase = new DatesDatabaseHelper(MainActivity.this).getReadableDatabase()){
                                m_cursor = sqLiteDatabase
                                        .query("D10",new String[]{"DATE","EVENT","REQUEST"},null,null,null,null,null);
                                e_cursor = sqLiteDatabase
                                        .query("D1",new String[]{"DATE","EVENT","REQUEST"},null,null,null,null,null);
                                m_cursor.moveToFirst();
                                e_cursor.moveToFirst();
                            }catch (Exception e){
                                Log.e("ExceptionFUUUUUUUCK",e.toString());
                            }
                            setCursors(m_cursor,e_cursor);
                        }
                    });
        preferences.put(DATES,prefs.getBoolean(DATES,true));
        preferences.put(PRACTISE,prefs.getBoolean(PRACTISE,true));
        preferences.put(SORT,prefs.getBoolean(SORT,true));
        preferences.put(SORT_CHECK,prefs.getBoolean(SORT_CHECK,true));
        preferences.put(CARDS,prefs.getBoolean(CARDS,true));
        preferences.put(TRUE_FALSE,prefs.getBoolean(TRUE_FALSE,true));
        Appodeal.disableLocationPermissionCheck();
        Appodeal.disableNetwork(this, "mmedia");
        Appodeal.initialize(this, "106e01ac39306b040f6b1d290a5b5bae37ebbcf794bb3cb1", Appodeal.INTERSTITIAL | Appodeal.BANNER);
        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .build(this, "52ZN7BKTNFZ8M26Q2VPN");

        BottomView = findViewById(R.id.navigation);
        BottomView.inflateMenu(R.menu.navigation);
        BottomView.setSelectedItemId(R.id.navigetion_dates);
        BottomView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (BottomView.getSelectedItemId() != id) {
                    if (item.getItemId() == R.id.navigation_tests) {
                        FragmentTransaction frT = getFragmentManager().beginTransaction();
                        frT.replace(R.id.frameLayout,new PractiseFragment(),"TAG");
                        frT.addToBackStack(null);
                        frT.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        frT.commit();
                    } else
                        getFragmentManager().popBackStack(null,0);
                }else
                    goToStartPosition();
                return true;
            }
        });

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToStartPosition();
            }
        });

        try {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }catch (Exception e){
            Log.e("Exception",e.toString());
        }
    }

    private void goToStartPosition(){
        StartPosition fragment=(StartPosition) getFragmentManager().findFragmentById(R.id.frameLayout);
        fragment.goToStartPosition();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        this.menu = menu;
        MenuItem mI = menu.findItem(R.id.mode_switch);
        mI.setActionView(R.layout.switch_layout);
        SwitchCompat switchCompat = (SwitchCompat) mI.getActionView();
        if(mode == EASY_DATES_MODE) {
            switchCompat.setChecked(true);
            refreshLook();
        }
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                Fragment frg = MainActivity.this.getFragmentManager().findFragmentById(R.id.frameLayout);
                int string_resource_id = 0;
                try {
                    if (checked) {
                        mode = EASY_DATES_MODE;
                        string_resource_id = R.string.easy_mode;
                        refreshLook();
                    } else {
                        mode = FULL_DATES_MODE;
                        string_resource_id = R.string.full_mode;
                        refreshLook();
                    }
                    if(frg != null && frg instanceof DatesFragment)
                        ((DatesFragment) frg).refresh();
                }catch (Exception e){
                    Log.e("Switch_Exception",e.toString());
                }
                Snackbar.make(MainActivity.this.findViewById(R.id.frameLayout), string_resource_id,Snackbar.LENGTH_SHORT).show();

            }
        });
        if(isFirstTime(DATES))
            startFirstTimeUserTutorial();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int x;
        Fragment frg = getFragmentManager().findFragmentById(R.id.frameLayout);
        switch (item.getItemId()){
            case R.id.font_plus:
                x = 2;
                break;
            case R.id.font_minus:
                x = -2;
                break;
            default:
                x = 0;
        }
        if(frg instanceof DatesFragment) {
            if(((DatesFragment) frg).setAdapterFontSize(x))
                menu.findItem(R.id.font_minus).setVisible(false);
            else
                menu.findItem(R.id.font_minus).setVisible(true);
        }
        return super.onOptionsItemSelected(item);
    }

    public Cursor getCursor(){
        if(mode == FULL_DATES_MODE)
            return main_cursor;
        else
            return easy_cursor;
    }

    public void setCursors(Cursor main_cursor,Cursor easy_cursor){
        this.main_cursor = main_cursor;
        this.easy_cursor = easy_cursor;
        getFragmentManager().beginTransaction().add(R.id.frameLayout,new DatesFragment(),"TAG").commit();
    }


    public void changeToolbarItemsVisibility(boolean font_plus,boolean font_minus) {
        if (menu != null) {
            menu.findItem(R.id.font_minus).setVisible(font_minus);
            menu.findItem(R.id.font_plus).setVisible(font_plus);
        }
    }

    public void startFirstTimeUserTutorial(){
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this);
        View switch_compat = menu.findItem(R.id.mode_switch).getActionView();
        View main_frame = findViewById(R.id.frameLayout);
        MaterialShowcaseView bottom_showcase,switch_showcase,dates_showcase;
        int mask_color = getResources().getColor(R.color.colorMask);
        int dismiss_color = Color.GREEN;
        bottom_showcase = new MaterialShowcaseView.Builder(this)
                .setTarget(BottomView)
                .setContentText(R.string.tutorial_bottom_view)
                .setDismissText(R.string.got_it)
                .setDismissOnTouch(true)
                .setShape(new RectangleShape(BottomView.getWidth(),BottomView.getHeight()))
                .setMaskColour(mask_color)
                .setDismissTextColor(dismiss_color)
                .build();
        sequence.addSequenceItem(bottom_showcase);
        switch_showcase = new MaterialShowcaseView.Builder(this)
                .setTarget(switch_compat)
                .setContentText(R.string.tutorial_switch)
                .setDismissText(R.string.got_it)
                .setTargetTouchable(true)
                .setDismissOnTouch(true)
                .setMaskColour(mask_color)
                .setDismissTextColor(dismiss_color)
                .build();
        sequence.addSequenceItem(switch_showcase);
        dates_showcase = new MaterialShowcaseView.Builder(this)
                .setTarget(main_frame)
                .setContentText(R.string.tutorial_dates)
                .setDismissText(R.string.got_it)
                .setDismissOnTouch(true)
                .setShape(new NoShape())
                .setMaskColour(mask_color)
                .setDismissTextColor(dismiss_color)
                .build();
        sequence.addSequenceItem(dates_showcase);
        sequence.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this);
    }

    public boolean isFirstTime(String key){
        boolean is = preferences.get(key);
        if(is)
            preferences.put(key,false);
        return is;
    }


    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
        new setNewPreferences(mode,preferences).execute(this);

    }

    public void hide_bottom_navigation_view(){
        linearLayout.removeView(BottomView);
    }

    public void show_bottom_navigation_view() {
        linearLayout.addView(BottomView);
    }

    public void updateBottomNavigationView(int id){
        BottomView.setSelectedItemId(id);
    }

    public int getMode(){
        return mode;
    }

    public void refreshLook(){
        if(mode == FULL_DATES_MODE) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        }else{
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryEasy)));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        easy_cursor.close();
        main_cursor.close();
    }


    protected static class setNewPreferences extends AsyncTask<MainActivity,Void,Void>{
        TreeMap<String,Boolean> prefs;
        int mode;

        setNewPreferences(int mode, TreeMap<String,Boolean> prefs){
            this.mode = mode;
            this.prefs = prefs;
        }

        @Override
        protected Void doInBackground(MainActivity... mainActivities) {
            SharedPreferences.Editor editor = mainActivities[0].getSharedPreferences(SETTINGS,Context.MODE_PRIVATE).edit();
            editor.putInt(MODE,mode);
            editor.putBoolean(PRACTISE,prefs.get(PRACTISE));
            editor.putBoolean(DATES,prefs.get(DATES));
            editor.putBoolean(SORT,prefs.get(SORT));
            editor.putBoolean(SORT_CHECK,prefs.get(SORT_CHECK));
            editor.putBoolean(TRUE_FALSE,prefs.get(TRUE_FALSE));
            editor.putBoolean(CARDS,prefs.get(CARDS));
            editor.apply();
            return null;
        }
    }

}