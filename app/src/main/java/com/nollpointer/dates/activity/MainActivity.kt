package com.nollpointer.dates.activity

import android.content.Context
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.appodeal.ads.Appodeal
import com.flurry.android.FlurryAgent
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import com.nollpointer.dates.R
import com.nollpointer.dates.dates.DatesFragment
import com.nollpointer.dates.gdpr.GDPRFragment
import com.nollpointer.dates.menu.MenuFragment
import com.nollpointer.dates.other.*
import com.nollpointer.dates.other.Date
import com.nollpointer.dates.practise.PractiseFragment
import com.nollpointer.dates.statistics.StatisticsFragment
import com.nollpointer.dates.terms.TermsFragment
import java.util.*

class MainActivity : AppCompatActivity() {


    companion object {
        const val TAG = "MainActivity"
        const val MODE = "MODE123"
        const val PRACTISE = "PRACTISE"
        const val DATES = "DATES"
        const val SORT = "SORT"
        const val SORT_CHECK = "SORT_CHECK"
        const val TRUE_FALSE = "TRUE_FALSE"
        const val CARDS = "CARDS"
        const val GDPR = "GDPR"
        const val GDPR_SHOW = "GDPR_SHOW"
        const val SETTINGS = "SETTINGS"
        const val FULL_DATES_MODE = 0
        const val EASY_DATES_MODE = 1

//        init {
//            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
//        }
    }


    var mode = 0
    private lateinit var bottomView: BottomNavigationViewEx
    private val preferences = TreeMap<String, Boolean>()
    lateinit var dates: ArrayList<Date>
    lateinit var terms: ArrayList<Term>
    private lateinit var datesFragment: DatesFragment
    private lateinit var practiseFragment: PractiseFragment
    private lateinit var statisticsFragment: StatisticsFragment
    private lateinit var menuFragment: MenuFragment
    private lateinit var termsFragment: TermsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        mode = preferences.getInt("mode", FULL_DATES_MODE)
        val isAppodealInitialized = preferences.contains("gdpr_result")
        val isGdprAgree = preferences.getBoolean("gdpr_result", false)
        initializeAdds(isGdprAgree)
        datesFragment = DatesFragment()
        termsFragment = TermsFragment()
        practiseFragment = PractiseFragment()
        statisticsFragment = StatisticsFragment()
        menuFragment = MenuFragment()
        initializeBottomView()
        InitialLoadData(mode, isAppodealInitialized, bottomView, datesFragment).execute()
        //        if (prefs.contains(GDPR)) {
//            boolean isGDPRAgree = prefs.getBoolean(GDPR, false);
//            preferences.put(GDPR, isGDPRAgree);
//            initializeAdds(isGDPRAgree);
//        } else {
//            preferences.put(GDPR, false);
//            initializeAdds(false);
//        }
    }

    private fun initializeBottomView() {
        bottomView = findViewById<View>(R.id.navigation) as BottomNavigationViewEx
        bottomView.selectedItemId = R.id.navigation_dates
        bottomView.enableAnimation(false)
        bottomView.enableShiftingMode(false)
        bottomView.enableItemShiftingMode(false)
        bottomView.setTextVisibility(false)
        bottomView.onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.navigation_dates -> datesFragment
                R.id.navigation_terms -> termsFragment
                R.id.navigation_practise -> practiseFragment
                R.id.navigation_calendar -> statisticsFragment
                R.id.navigation_menu -> menuFragment
                else -> datesFragment
            }
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, fragment, TAG)
            //transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss()
            true
        }
    }

    private fun initializeAdds(isGdprAgree: Boolean) {
        Appodeal.disableLocationPermissionCheck()
        Appodeal.initialize(this, "106e01ac39306b040f6b1d290a5b5bae37ebbcf794bb3cb1", Appodeal.INTERSTITIAL or Appodeal.BANNER or Appodeal.NON_SKIPPABLE_VIDEO, isGdprAgree)
    }

    override fun onStart() {
        super.onStart()
        FlurryAgent.onStartSession(this)
    }

    override fun onStop() {
        super.onStop()
        FlurryAgent.onEndSession(this)
    }

    fun hideBottomNavigationView() {
        bottomView.visibility = View.GONE
    }

    fun showBottomNavigationView() {
        bottomView.visibility = View.VISIBLE
    }

    fun updateBottomNavigationView(id: Int) {
        bottomView.selectedItemId = id
    }

    fun updateMode(mode: Int, listener: OnDatesLoadListener) {
        this.mode = mode
        LoadDates(mode, listener).execute()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Разрешение получено", Toast.LENGTH_SHORT).show()
            } else {
                val helpDialog = PractiseHelpDialog()
                helpDialog.show(this.supportFragmentManager, null)
            }
        }
    }

    /*    public void checkLocaleSettings() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultLocale = getString(R.string.locale);
        String locale = preferences.getString("Locale", defaultLocale);
        if (!locale.equals(defaultLocale))
            setLocale(locale);
    }*/
/*    //TODO доделать изменение языка в приложении
    //Выставление языка приложения
    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        //new setNewPreferences(this, lang).execute();
    }*/
    protected class setNewPreferences internal constructor(var mode: Int, var prefs: TreeMap<String, Boolean?>) : AsyncTask<MainActivity, Void?, Void?>() {

        override fun doInBackground(vararg mainActivity: MainActivity): Void? {
            val editor = mainActivity[0].getSharedPreferences(SETTINGS, Context.MODE_PRIVATE).edit()
            editor.putInt(MODE, mode)
            if (prefs.containsKey(GDPR)) editor.putBoolean(GDPR, prefs[GDPR]!!)
            editor.apply()
            return null
        }
    }

    internal inner class InitialLoadData(var mode: Int, var isAppodealInitialized: Boolean, var bottomNavigationView: BottomNavigationView?, var datesFragment: DatesFragment) : AsyncTask<Void?, Void?, Void?>() {

        override fun onPreExecute() {
            hideBottomNavigationView()
        }

        override fun onPostExecute(aVoid: Void?) {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            if (isAppodealInitialized) fragmentTransaction.add(R.id.frameLayout, datesFragment, null) else fragmentTransaction.add(R.id.frameLayout, GDPRFragment(), null)
            fragmentTransaction.commitAllowingStateLoss()
        }

        override fun doInBackground(vararg p0: Void?): Void? {
            dates = Misc.getDates(mode)
            terms = Misc.getTerms()
            return null
        }
    }

    internal inner class LoadDates(var mode: Int, var listener: OnDatesLoadListener) : AsyncTask<Void?, Void?, Void?>() {
        override fun onPreExecute() {
            listener.onLoadStart()
        }

        override fun onPostExecute(aVoid: Void?) {
            listener.onLoadEnd()
        }

        override fun doInBackground(vararg p0: Void?): Void? {
            dates = Misc.getDates(mode)
            return null
        }
    }
}