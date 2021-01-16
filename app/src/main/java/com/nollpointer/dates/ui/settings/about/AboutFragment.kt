package com.nollpointer.dates.ui.settings.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nollpointer.dates.R
import com.nollpointer.dates.databinding.FragmentAboutBinding
import com.nollpointer.dates.ui.view.BaseFragment

/**
 * @author Onanov Aleksey (@onanov)
 */
class AboutFragment : BaseFragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding: FragmentAboutBinding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.rateApp.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.nollpointer.dates"))
            startActivity(intent)
        }

        binding.privacyPolicy.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://onanov.ru/app/dates/privacy-policy.html"))
            startActivity(intent)
        }

        binding.licence.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://onanov.ru/app/dates/privacy-policy.html"))
            startActivity(intent)
        }

        binding.faq.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://n0llpointer.github.io/Dates/"))
            startActivity(intent)
        }

        binding.betaTest.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/apps/testing/com.nollpointer.dates"))
            startActivity(intent)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getStatusBarColorRes() = R.color.colorPrimary

    override fun isStatusBarLight() = false

    companion object {
        fun newInstance() = AboutFragment()
    }
}