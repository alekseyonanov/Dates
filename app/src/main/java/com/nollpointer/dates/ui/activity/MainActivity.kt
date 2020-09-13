package com.nollpointer.dates.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appodeal.ads.Appodeal
import com.flurry.android.FlurryAgent
import com.google.gson.Gson
import com.nollpointer.dates.R
import com.nollpointer.dates.model.Date
import com.nollpointer.dates.model.DatesList
import com.nollpointer.dates.model.Term
import com.nollpointer.dates.model.TermsList
import com.nollpointer.dates.other.Loader
import com.nollpointer.dates.ui.dates.DatesFragment
import com.nollpointer.dates.ui.intro.IntroductionFragment
import com.nollpointer.dates.ui.menu.MenuFragment
import com.nollpointer.dates.ui.practise.PractiseFragment
import com.nollpointer.dates.ui.statistics.StatisticsFragment
import com.nollpointer.dates.ui.terms.TermsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import java.io.InputStreamReader

/**
 * @author Onanov Aleksey (@onanov)
 */
class MainActivity : AppCompatActivity() {

    var mode = 0

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

        mode = Loader.getMode(this)
        val isFirstStart = Loader.isFirstStart(this)
        val isGdprAgree = Loader.isGDPRAgree(this)

        initializeAdds(isGdprAgree)

        datesFragment = DatesFragment()
        termsFragment = TermsFragment()
        practiseFragment = PractiseFragment()
        statisticsFragment = StatisticsFragment()
        menuFragment = MenuFragment()

        navigation.apply {
            setOnNavigationItemSelectedListener {
                val fragment = when (it.itemId) {
                    R.id.navigation_dates -> datesFragment
                    R.id.navigation_terms -> termsFragment
                    R.id.navigation_practise -> practiseFragment
                    R.id.navigation_calendar -> statisticsFragment
                    R.id.navigation_menu -> menuFragment
                    else -> datesFragment
                }
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.frameLayout, fragment, TAG)
                transaction.commitAllowingStateLoss()
                true
            }
            setOnNavigationItemReselectedListener {
                when (it.itemId) {
                    R.id.navigation_dates -> datesFragment.scrollToTop()
                    R.id.navigation_terms -> termsFragment.scrollToTop()
                    R.id.navigation_practise -> practiseFragment.scrollToTop()
                }
            }
        }

        if (isFirstStart)
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.frameLayout, IntroductionFragment.newInstance(), null)
                    .commitAllowingStateLoss()

        DateLoadViewModel().loadDates(mode) {
            if (!isFirstStart)
                showDatesFragment()
        }
        TermsLoadViewModel().loadTerms()

        //InitialLoadData(mode, isFirstStart, datesFragment).execute()
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
        navigation.visibility = View.GONE
    }

    fun showBottomNavigationView() {
        navigation.visibility = View.VISIBLE
    }

    fun updateMode(mode: Int) {
        this.mode = mode
        UpdateDates().updateDates(mode)
    }

    private fun showDatesFragment() {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.frameLayout, datesFragment, null)
                .commitAllowingStateLoss()
    }

    @SuppressLint("StaticFieldLeak")
    inner class TermsLoadViewModel : ViewModel() {
        fun loadTerms() {
            viewModelScope.launch {
                val gson = Gson()
                val termsList = gson.fromJson(InputStreamReader(resources.openRawResource(R.raw.terms)), TermsList::class.java)

                terms = ArrayList(termsList.terms)
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class DateLoadViewModel : ViewModel() {
        fun loadDates(mode: Int, listener: (() -> Unit)? = null) {
            viewModelScope.launch {
                val gson = Gson()
                val datesList = gson.fromJson(
                        InputStreamReader(
                                resources.openRawResource(
                                        when (mode) {
                                            FULL_DATES_MODE -> R.raw.dates_full
                                            else -> R.raw.dates_easy
                                        })),
                        DatesList::class.java)

                dates = ArrayList(datesList.dates)
                listener?.invoke()
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class UpdateDates : ViewModel() {
        fun updateDates(mode: Int) {
            viewModelScope.launch {
                val gson = Gson()
                val datesList = gson.fromJson(
                        InputStreamReader(
                                resources.openRawResource(
                                        when (mode) {
                                            FULL_DATES_MODE -> R.raw.dates_full
                                            else -> R.raw.dates_easy
                                        })),
                        DatesList::class.java)

                dates = ArrayList(datesList.dates)
            }
        }
    }

    companion object {
        const val TAG = "MainActivity"
        const val SORT = "SORT"
        const val TRUE_FALSE = "TRUE_FALSE"
        const val CARDS = "CARDS"
        const val FULL_DATES_MODE = 0
        const val EASY_DATES_MODE = 1
    }

}