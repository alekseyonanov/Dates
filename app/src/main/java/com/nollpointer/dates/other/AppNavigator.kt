package com.nollpointer.dates.other

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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
 * Навигатор между экранами
 *
 * @author Onanov Aleksey (@onanov)
 */
class AppNavigator @Inject constructor(@ActivityContext private val context: Context) {

    private val activity: AppCompatActivity
        get() = context as AppCompatActivity

    fun navigateBack() {
        activity.supportFragmentManager.popBackStack()
    }

    fun navigateToDatesDetails(date: Date) {
        setFragmentWithBackStack(DatesDetailsFragment.newInstance(date))
    }

    fun navigateToTermsDetails(term: Term) {
        setFragmentWithBackStack(TermsDetailsFragment.newInstance(term))
    }

    fun navigateToPractiseSettings(practise: Practise) {
        setFragmentWithBackStack(PractiseSettingsFragment.newInstance(practise))
    }

    fun navigateToAnalyze() {
        setFragmentWithBackStack(AnalyzeFragment())
    }

    fun navigateToIntroduction() {
        setRootFragment(IntroductionFragment.newInstance())
    }

    fun navigateToGdpr() {
        setFragment(GDPRFragment.newInstance())
    }

    fun navigateToGdprResultFragment(result: Boolean) {
        setFragment(GDPRResultFragment.newInstance(result))
    }

    fun navigateToGame() {
        setFragmentWithBackStack(GameFragment.newInstance())
    }

    fun navigateToSettings() {
        setFragmentWithBackStack(SettingsFragment.newInstance())
    }

    fun navigateToAboutApp() {
        setFragmentWithBackStack(AboutFragment.newInstance())
    }

    fun navigateToDatesViewSettings() {
        setFragmentWithBackStack(DatesViewFragment.newInstance())
    }

    fun navigateToTermsViewSettings() {
        setFragmentWithBackStack(TermsViewFragment.newInstance())
    }

    private fun setFragment(fragment: Fragment) {
        activity
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit()
    }

    private fun setFragmentWithBackStack(fragment: Fragment) {
        activity
                .supportFragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.frameLayout, fragment)
                .commit()
    }

    private fun setRootFragment(fragment: Fragment) {
        activity
                .supportFragmentManager
                .beginTransaction()
                .add(R.id.frameLayout, fragment)
                .commitAllowingStateLoss()
    }

}