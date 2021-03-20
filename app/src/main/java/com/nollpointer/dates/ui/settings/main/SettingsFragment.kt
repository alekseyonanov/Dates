package com.nollpointer.dates.ui.settings.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nollpointer.dates.R
import com.nollpointer.dates.databinding.FragmentSettingsBinding
import com.nollpointer.dates.other.AppNavigator
import com.nollpointer.dates.ui.view.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Экран "Настройки"
 *
 * @author Onanov Aleksey (@onanov)
 */
@AndroidEntryPoint
class SettingsFragment : BaseFragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding
        get() = _binding!!

    override val statusBarColorRes = R.color.colorPrimary

    override val isStatusBarLight = false

    override val isBottomNavigationViewHidden = true

    @Inject
    lateinit var navigator: AppNavigator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.toolbar.setNavigationOnClickListener {
            navigator.navigateBack()
        }

        binding.datesView.setOnClickListener {
            navigator.navigateToDatesViewSettings()
        }

        binding.termsView.setOnClickListener {
            navigator.navigateToTermsViewSettings()
        }

        binding.about.setOnClickListener {
            navigator.navigateToAboutApp()
        }

        binding.reset.setOnClickListener {
            ResetSettingsDialog.newInstance().show(childFragmentManager, null)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }
}