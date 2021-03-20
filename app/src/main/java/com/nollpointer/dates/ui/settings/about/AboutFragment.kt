package com.nollpointer.dates.ui.settings.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nollpointer.dates.R
import com.nollpointer.dates.databinding.FragmentAboutBinding
import com.nollpointer.dates.other.AppNavigator
import com.nollpointer.dates.ui.view.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Экран "О приложении"
 *
 * @author Onanov Aleksey (@onanov)
 */
@AndroidEntryPoint
class AboutFragment : BaseFragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding: FragmentAboutBinding
        get() = _binding!!

    override val statusBarColorRes = R.color.colorPrimary

    override val isStatusBarLight = false

    override val isBottomNavigationViewHidden = true

    @Inject
    lateinit var navigator: AppNavigator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)

        binding.toolbar.setNavigationOnClickListener {
            navigator.navigateBack()
        }

        binding.rateApp.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.nollpointer.dates"))
            startActivity(intent)
        }

        binding.privacyPolicy.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://onanov.ru/app/dates/privacy-policy.html"))
            startActivity(intent)
        }

        binding.termsConditions.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://onanov.ru/app/dates/terms-conditions.html"))
            startActivity(intent)
        }

        binding.licence.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://onanov.ru/app/dates/licences.html"))
            startActivity(intent)
        }

        binding.faq.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://onanov.ru/app/dates/faq.html"))
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

    companion object {
        @JvmStatic
        fun newInstance() = AboutFragment()
    }
}