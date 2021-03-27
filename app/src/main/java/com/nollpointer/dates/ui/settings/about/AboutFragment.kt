package com.nollpointer.dates.ui.settings.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nollpointer.dates.BuildConfig
import com.nollpointer.dates.R
import com.nollpointer.dates.databinding.FragmentAboutBinding
import com.nollpointer.dates.other.AppNavigator
import com.nollpointer.dates.other.ExternalLinksManager
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

    @Inject
    lateinit var externalLinksManager: ExternalLinksManager

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)

        binding.toolbar.setNavigationOnClickListener {
            navigator.navigateBack()
        }

        binding.rateApp.setOnClickListener {
            externalLinksManager.navigateToRateApp()
        }

        binding.privacyPolicy.setOnClickListener {
            externalLinksManager.navigateToPrivacyPolicy()
        }

        binding.termsConditions.setOnClickListener {
            externalLinksManager.navigateToTermsConditions()
        }

        binding.licence.setOnClickListener {
            externalLinksManager.navigateToLicence()
        }

        binding.faq.setOnClickListener {
            externalLinksManager.navigateToFaq()
        }

        binding.betaTest.setOnClickListener {
            externalLinksManager.navigateToBetaTest()
        }

        binding.appVersion.text = getString(R.string.app_version, BuildConfig.VERSION_NAME)

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