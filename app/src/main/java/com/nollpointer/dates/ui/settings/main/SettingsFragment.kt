package com.nollpointer.dates.ui.settings.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nollpointer.dates.R
import com.nollpointer.dates.databinding.FragmentSettingsBinding
import com.nollpointer.dates.other.AppNavigator
import com.nollpointer.dates.ui.activity.MainActivity
import com.nollpointer.dates.ui.view.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * @author Onanov Aleksey (@onanov)
 */
@AndroidEntryPoint
class SettingsFragment : BaseFragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding
        get() = _binding!!

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

    override fun onStart() {
        super.onStart()
        (activity as MainActivity?)!!.hideBottomNavigationView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getStatusBarColorRes() = R.color.colorPrimary

    override fun isStatusBarLight() = false

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }
}