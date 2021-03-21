package com.nollpointer.dates.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.appodeal.ads.Appodeal
import com.flurry.android.FlurryAgent
import com.google.gson.Gson
import com.nollpointer.dates.BuildConfig
import com.nollpointer.dates.R
import com.nollpointer.dates.annotation.FULL
import com.nollpointer.dates.annotation.Mode
import com.nollpointer.dates.databinding.ActivityMainBinding
import com.nollpointer.dates.model.Date
import com.nollpointer.dates.model.Term
import com.nollpointer.dates.other.AppNavigator
import com.nollpointer.dates.other.Loader
import com.nollpointer.dates.ui.dates.DatesFragment
import com.nollpointer.dates.ui.menu.MenuFragment
import com.nollpointer.dates.ui.practise.PractiseFragment
import com.nollpointer.dates.ui.statistics.StatisticsFragment
import com.nollpointer.dates.ui.terms.TermsFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.InputStreamReader
import javax.inject.Inject

/**
 * Главная Activity приложения
 *
 * @author Onanov Aleksey (@onanov)
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Mode
    var mode = FULL
        set(value) {
            field = value
            loader.mode = value
            updateDates(value)
        }

    lateinit var dates: List<Date>
    lateinit var terms: List<Term>

    private lateinit var datesFragment: DatesFragment
    private lateinit var practiseFragment: PractiseFragment
    private lateinit var statisticsFragment: StatisticsFragment
    private lateinit var menuFragment: MenuFragment
    private lateinit var termsFragment: TermsFragment

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding!!

    private val isBottomNavigationViewVisible: Boolean
        get() = binding.navigation.visibility == View.VISIBLE

    @Inject
    lateinit var navigator: AppNavigator

    @Inject
    lateinit var loader: Loader

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mode = loader.mode
        val isFirstStart = loader.isFirstStart
        val isGdprAgree = loader.isGdprAgree

        initializeAdds(isGdprAgree)

        datesFragment = DatesFragment()
        termsFragment = TermsFragment()
        practiseFragment = PractiseFragment()
        statisticsFragment = StatisticsFragment()
        menuFragment = MenuFragment()

        binding.navigation.apply {
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
                transaction.replace(R.id.frameLayout, fragment, "")
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

        // TODO: 13.03.2021 Сделать рефактор
        loadDates(mode).subscribe({
            dates = it
            if (isFirstStart) {
                navigator.navigateToIntroduction()
            } else {
                showDatesFragment()
            }
        }, {

        })
        loadTerms().subscribe({
            terms = it
        }, {})

        FlurryAgent.onStartSession(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        FlurryAgent.onEndSession(this)
        _binding = null
    }

    private fun initializeAdds(isGdprAgree: Boolean) {
        Appodeal.disableLocationPermissionCheck()
        Appodeal.initialize(this,
                BuildConfig.APPODEAL_KEY,
                Appodeal.INTERSTITIAL or Appodeal.BANNER,
                isGdprAgree)
    }

    fun hideBottomNavigationView() {
        if (isBottomNavigationViewVisible) {
            binding.navigation.visibility = View.GONE
        }
    }

    fun showBottomNavigationView() {
        if (!isBottomNavigationViewVisible) {
            binding.navigation.visibility = View.VISIBLE
        }
    }

    private fun updateDates(@Mode mode: Int) {
        val datesList = Gson().fromJson(
                InputStreamReader(
                        resources.openRawResource(
                                when (mode) {
                                    FULL -> R.raw.dates_full
                                    else -> R.raw.dates_easy
                                })),
                Array<Date>::class.java)

        dates = ArrayList(datesList.toList())
    }

    //TODO: Исправить этот момент и перенести в навигатор
    private fun showDatesFragment() {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.frameLayout, datesFragment, null)
                .commitAllowingStateLoss()
    }

    fun replaceDatesFragment() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout, datesFragment, null)
                .commitAllowingStateLoss()
    }

    private fun loadTerms(): Single<List<Term>> {
        return Single
                .fromCallable {
                    Gson().fromJson(InputStreamReader(resources.openRawResource(R.raw.terms)),
                            Array<Term>::class.java)
                            .toList()
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    }

    private fun loadDates(@Mode mode: Int): Single<List<Date>> {
        return Single
                .fromCallable {
                    Gson().fromJson(InputStreamReader(resources.openRawResource(
                            when (mode) {
                                FULL -> R.raw.dates_full
                                else -> R.raw.dates_easy
                            })),
                            Array<Date>::class.java)
                            .toList()
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}