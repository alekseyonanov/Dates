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

import com.appodeal.ads.Appodeal;
import com.flurry.android.FlurryAgent;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CenturyPickDialog.NoticeDialogListener{
    private Cursor crs = null;
    private Cursor crsAdd = null;
    private boolean first_time = true;
    private BottomNavigationView bn;
    private int type_pick = -1;
    private int mode;

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

    protected static class setup extends AsyncTask<MainActivity,Void,Cursor>{
        MainActivity ctx;
        String name;
        boolean main;
        SQLiteDatabase sql;
        setup(String n,boolean m,SQLiteDatabase s){
            name = n;
            main = m;
            sql = s;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if(main)
                ctx.setCursor(cursor);
            else
                ctx.setAddCursor(cursor);
        }

        @Override
        protected Cursor doInBackground(MainActivity... voids) {
            Cursor crs = null;
            ctx = voids[0];
            try{
                crs = sql
                .query(name,new String[]{"DATE","EVENT"},null,null,null,null,null);
            }catch (Exception e){
                Log.e("ExceptionFUUUUUUUCK",e.toString());
            }
            //Log.e("asda",""+crs.getCount());
            return crs;
        }
    }

    protected static class setNewPreferences extends AsyncTask<MainActivity,Void,Integer>{
        int mode;
        setNewPreferences(int m){
            mode = m;
        }
        @Override
        protected Integer doInBackground(MainActivity... mainActivities) {
            SharedPreferences.Editor editor = mainActivities[0].getSharedPreferences("mode_settings",Context.MODE_PRIVATE).edit();
                editor.putInt("Mode",mode).apply();
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar tlb = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(tlb);

        SharedPreferences preferences = getSharedPreferences("mode_settings", Context.MODE_PRIVATE);
        SQLiteDatabase sqLiteDatabase = new DatesDatabaseHelper(this).getReadableDatabase();
        if(preferences.contains("Mode"))
            switch (mode = preferences.getInt("Mode",-1)){
                case 0:
                    new setup("D10",true,sqLiteDatabase).execute(this);
                    new setup("D1",false,sqLiteDatabase).execute(this);
                    refreshLook();
                    break;
                case 1:
                    new setup("D10",false,sqLiteDatabase).execute(this);
                    new setup("D1",true,sqLiteDatabase).execute(this);
                    refreshLook();
                    break;
                default:
                    new setup("D10",true,sqLiteDatabase).execute(this);
                    new setup("D1",false,sqLiteDatabase).execute(this);
                    mode = 0;
                    refreshLook();
            }
        else{
            new setup("D10",true,sqLiteDatabase).execute(this);
            new setup("D1",false,sqLiteDatabase).execute(this);
            mode = 0;
            new setNewPreferences(mode).execute(this);
            refreshLook();
        }
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
                    } else {
                        getFragmentManager().popBackStack(null,0);
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
        MenuItem mI = menu.findItem(R.id.mode_switch);
        mI.setActionView(R.layout.switch_layout);
        SwitchCompat s = (SwitchCompat) mI.getActionView();
        if(mode == 1)
            s.setChecked(true);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Fragment frg = MainActivity.this.getFragmentManager().findFragmentByTag("TAG");
                try {
                    if (b) {
                        mode = 1;
                        refreshLook();
                    } else {
                        mode = 0;
                        refreshLook();
                    }
                    refreshCursor();
                    if(frg != null && frg instanceof DatesFragment)
                        ((DatesFragment) frg).refresh();
                    new setNewPreferences(mode).execute(MainActivity.this);
                }catch (Exception e){
                    Log.e("Switch_Exception",e.toString());
                }
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    protected Cursor getCursor(){
        return crs;
    }
    public void setCursor(Cursor crs){
        this.crs = crs;
        getFragmentManager().beginTransaction().add(R.id.frameLayout,new DatesFragment(),"TAG").commit();
    }

    public void setAddCursor(Cursor cur){
        crsAdd = cur;
    }

    public void refreshCursor(){
        Cursor c = crs;
        crs = crsAdd;
        crsAdd = c;
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
    public BottomNavigationView getBn(){
        return bn;
    }

    public int getMode(){
        return mode;
    }

    public void refreshLook(){
        if(mode ==0) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
            getSupportActionBar().setIcon(R.mipmap.ic_launcher_icon);
        }else{
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryEasy)));
            getSupportActionBar().setIcon(R.mipmap.ic_launcher_easy);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        crsAdd.close();
        crs.close();
    }
}
