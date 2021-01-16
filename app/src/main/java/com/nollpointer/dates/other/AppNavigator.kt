package com.nollpointer.dates.other

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.nollpointer.dates.R
import com.nollpointer.dates.model.Date
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.model.Term
import com.nollpointer.dates.ui.analyze.AnalyzeFragment
import com.nollpointer.dates.ui.details.dates.DatesDetailsFragment
import com.nollpointer.dates.ui.details.terms.TermsDetailsFragment
import com.nollpointer.dates.ui.game.GameFragment
import com.nollpointer.dates.ui.gdpr.GDPRFragment
import com.nollpointer.dates.ui.gdpr.GDPRResultFragment
import com.nollpointer.dates.ui.intro.IntroductionFragment
import com.nollpointer.dates.ui.practise.PractiseSettingsFragment
import com.nollpointer.dates.ui.settings.about.AboutFragment
import com.nollpointer.dates.ui.settings.datesview.DatesViewFragment
import com.nollpointer.dates.ui.settings.main.SettingsFragment
import com.nollpointer.dates.ui.settings.termsview.TermsViewFragment
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

/**
 * @author Onanov Aleksey (@onanov)
 */
class AppNavigator @Inject constructor(@ActivityContext private val context: Context) {

    private val activity: AppCompatActivity
        get() = context as AppCompatActivity

    /**
     * Переход к датам
     */
    fun navigateToDates() {

    }

    fun navigateBack() {
        activity.supportFragmentManager.popBackStack()
    }

    fun navigateToDatesDetails(date: Date) {
        activity
                .supportFragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.frameLayout, DatesDetailsFragment.newInstance(date))
                .commit()
    }

    fun navigateToTermsDetails(term: Term) {
        activity
                .supportFragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.frameLayout, TermsDetailsFragment.newInstance(term))
                .commit()
    }

    fun navigateToPractiseSettings(practise: Practise) {
        activity
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout, PractiseSettingsFragment.newInstance(practise))
                .addToBackStack(null)
                .commit()
    }

    fun navigateToAnalyze() {
        activity
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout, AnalyzeFragment())
                .addToBackStack(null)
                .commit()
    }

    fun navigateToIntroduction() {
        activity
                .supportFragmentManager
                .beginTransaction()
                .add(R.id.frameLayout, IntroductionFragment.newInstance(), null)
                .commitAllowingStateLoss()
    }

    fun navigateToGdpr() {
        activity
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout, GDPRFragment.newInstance())
                .commit()
    }

    fun navigateToGdprResultFragment(result: Boolean) {
        activity
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout, GDPRResultFragment.newInstance(result))
                .commit()
    }

    fun navigateToGame() {
        activity
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout, GameFragment.newInstance())
                .addToBackStack(null)
                .commit()
    }

    fun navigateToSettings() {
        activity
                .supportFragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.frameLayout, SettingsFragment.newInstance())
                .commit()
    }

    fun navigateToAboutApp() {
        activity
                .supportFragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.frameLayout, AboutFragment.newInstance())
                .commit()
    }

    fun navigateToDatesViewSettings() {
        activity
                .supportFragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.frameLayout, DatesViewFragment.newInstance())
                .commit()
    }

    fun navigateToTermsViewSettings() {
        activity
                .supportFragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.frameLayout, TermsViewFragment.newInstance())
                .commit()
    }
}