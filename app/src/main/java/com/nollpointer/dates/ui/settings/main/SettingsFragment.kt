package com.nollpointer.dates.ui.settings.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nollpointer.dates.R
import com.nollpointer.dates.ui.activity.MainActivity
import com.nollpointer.dates.ui.settings.about.AboutFragment
import com.nollpointer.dates.ui.settings.datesview.DatesViewFragment
import com.nollpointer.dates.ui.settings.termsview.TermsViewFragment
import kotlinx.android.synthetic.main.fragment_settings.*

/**
 * @author Onanov Aleksey (@onanov)
 */
class SettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingsToolbar.setNavigationOnClickListener {
            fragmentManager?.popBackStack()
        }

        settingsDatesView.setOnClickListener {
            fragmentManager?.beginTransaction()?.addToBackStack(null)?.replace(R.id.frameLayout, DatesViewFragment.newInstance())?.commit()
        }

        settingsTermsView.setOnClickListener {
            fragmentManager?.beginTransaction()?.addToBackStack(null)?.replace(R.id.frameLayout, TermsViewFragment.newInstance())?.commit()
        }

        settingsAboutApp.setOnClickListener {
            fragmentManager?.beginTransaction()?.addToBackStack(null)?.replace(R.id.frameLayout, AboutFragment.newInstance())?.commit()
        }
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity?)!!.hideBottomNavigationView()
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}