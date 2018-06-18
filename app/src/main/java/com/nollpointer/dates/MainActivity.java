package com.nollpointer.dates;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CenturyPickDialog.NoticeDialogListener{
    private Cursor main_cursor = null;
    private Cursor easy_cursor = null;
    private boolean first_time = true;
    private BottomNavigationView bn;
    private int type_pick = -1;
    private int mode;
    private Menu menu;
    public static final int FULL_DATES_MODE = 0;
    public static final int EASY_DATES_MODE = 1;
    private LinearLayout linearLayout;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onButtonClicked(ArrayList<Integer> dialog) {
        if(dialog != null)
            ((PractiseFragment)getFragmentManager().findFragmentByTag("TAG")).startPractise(dialog,type_pick);

    }

    public void typePicked(int pos){
        type_pick = pos;
        new CenturyPickDialog().show(getSupportFragmentManager(), "1");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = findViewById(R.id.container_main);
        Toolbar tlb = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(tlb);
        SharedPreferences preferences = getSharedPreferences("mode_settings", Context.MODE_PRIVATE);
        final SQLiteDatabase sqLiteDatabase = new DatesDatabaseHelper(this).getReadableDatabase();
        Handler handler = new Handler();
        if(preferences.contains("Mode"))
            mode = preferences.getInt("Mode",-1);
        else
            mode = 0;
        handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Cursor m_cursor = null;
                            Cursor e_cursor = null;
                            try{
                                m_cursor = sqLiteDatabase
                                        .query("D10",new String[]{"DATE","EVENT"},null,null,null,null,null);
                                e_cursor = sqLiteDatabase
                                        .query("D1",new String[]{"DATE","EVENT"},null,null,null,null,null);
                                m_cursor.moveToFirst();
                                e_cursor.moveToFirst();
                            }catch (Exception e){
                                Log.e("ExceptionFUUUUUUUCK",e.toString());
                            }
                            setCursors(m_cursor,e_cursor);
                        }
                    });

        Appodeal.disableLocationPermissionCheck();
        Appodeal.disableNetwork(this, "mmedia");
        Appodeal.initialize(this, "106e01ac39306b040f6b1d290a5b5bae37ebbcf794bb3cb1", Appodeal.INTERSTITIAL | Appodeal.BANNER);
        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .build(this, "52ZN7BKTNFZ8M26Q2VPN");
        bn = findViewById(R.id.navigation);
        bn.inflateMenu(R.menu.navigation);
        bn.setSelectedItemId(R.id.navigetion_dates);
        bn.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (bn.getSelectedItemId() != id) {
                    if (item.getItemId() == R.id.navigation_tests) {
                        setFragment(new PractiseFragment());
                        //bn.setSelectedItemId(R.id.navigation_tests);
                    } else {
                        getFragmentManager().popBackStack(null,0);
                        //bn.setSelectedItemId(R.id.navigetion_dates);
                        first_time = true;
                    }
                }else{
                    if(id == R.id.navigetion_dates)
                        ((DatesFragment)getFragmentManager().findFragmentById(R.id.frameLayout)).setStartPosition();
                    else
                        ((PractiseFragment)getFragmentManager().findFragmentById(R.id.frameLayout)).setStartPosition();
                }
                return true;
            }
        });

        tlb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment frg = getFragmentManager().findFragmentById(R.id.frameLayout);
                if(frg instanceof DatesFragment)
                    ((DatesFragment)frg).setStartPosition();
                else
                    ((PractiseFragment)frg).setStartPosition();
            }
        });

        try {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }catch (Exception e){
            Log.e("Exception",e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        this.menu = menu;
        MenuItem mI = menu.findItem(R.id.mode_switch);
        mI.setActionView(R.layout.switch_layout);
        SwitchCompat s = (SwitchCompat) mI.getActionView();
        if(mode == EASY_DATES_MODE) {
            s.setChecked(true);
            refreshLook();
        }
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                Fragment frg = MainActivity.this.getFragmentManager().findFragmentById(R.id.frameLayout);
                try {
                    if (checked) {
                        mode = EASY_DATES_MODE;
                        refreshLook();
                    } else {
                        mode = FULL_DATES_MODE;
                        refreshLook();
                    }
                    //refreshCursor();
                    if(frg != null && frg instanceof DatesFragment)
                        ((DatesFragment) frg).refresh();
                }catch (Exception e){
                    Log.e("Switch_Exception",e.toString());
                }
            }
        });
        //menu.findItem(R.id.font_minus)
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


    private void setFragment(Fragment frg){
        FragmentManager fgM = getFragmentManager();
        FragmentTransaction frT = fgM.beginTransaction();
        frT.replace(R.id.frameLayout,frg,"TAG");
        if(first_time) {
            frT.addToBackStack(null);
            first_time = false;
        }
        frT.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        frT.commit();
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

    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
        new setNewPreferences(mode).execute(this);
    }

    public BottomNavigationView getBn(){
        return bn;
    }

    public void hide_bottom_navigation_view(){
        linearLayout.removeView(bn);
    }

    public void show_bottom_navigation_view() {
        linearLayout.addView(bn);
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
        int mode;
        setNewPreferences(int m){
            mode = m;
        }
        @Override
        protected Void doInBackground(MainActivity... mainActivities) {
            SharedPreferences.Editor editor = mainActivities[0].getSharedPreferences("mode_settings",Context.MODE_PRIVATE).edit();
            editor.putInt("Mode",mode).apply();
            return null;
        }
    }

}