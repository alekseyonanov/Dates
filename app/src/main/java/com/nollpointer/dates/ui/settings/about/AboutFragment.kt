package com.nollpointer.dates.ui.settings.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nollpointer.dates.R
import kotlinx.android.synthetic.main.fragment_about.*

class AboutFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        aboutToolbar.setNavigationOnClickListener {
            fragmentManager!!.popBackStack()
        }

        aboutRateApp.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.nollpointer.dates"))
            startActivity(intent)
        }

        aboutPrivacyPolicy.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://onanov.ru/app/dates/privacy-policy.html"))
            startActivity(intent)
        }

        aboutLicence.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://onanov.ru/app/dates/privacy-policy.html"))
            startActivity(intent)
        }

        aboutFAQ.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://n0llpointer.github.io/Dates/"))
            startActivity(intent)
        }

        aboutBetaTest.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/apps/testing/com.nollpointer.dates"))
            startActivity(intent)
        }
    }

    companion object {
        fun newInstance() = AboutFragment()
    }
}