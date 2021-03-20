package com.nollpointer.dates.ui.settings.termsview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nollpointer.dates.R
import com.nollpointer.dates.databinding.FragmentTermsViewBinding
import com.nollpointer.dates.other.AppNavigator
import com.nollpointer.dates.other.Loader
import com.nollpointer.dates.ui.view.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Экран "Настройка терминов"
 *
 * @author Onanov Aleksey (@onanov)
 */
@AndroidEntryPoint
class TermsViewFragment : BaseFragment() {

    private var _binding: FragmentTermsViewBinding? = null
    private val binding: FragmentTermsViewBinding
        get() = _binding!!

    override val statusBarColorRes = R.color.colorPrimary

    override val isStatusBarLight = false

    override val isBottomNavigationViewHidden = true

    @Inject
    lateinit var loader: Loader

    @Inject
    lateinit var navigator: AppNavigator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentTermsViewBinding.inflate(inflater, container, false)

        binding.toolbar.setNavigationOnClickListener {
            navigator.navigateBack()
        }

        binding.viewDefault.setOnClickListener {
            if (binding.viewDefault.isChecked)
                return@setOnClickListener
            else {
                binding.viewAdaptive.isChecked = false
                binding.viewDefault.isChecked = true
                binding.viewContainer.removeAllViews()
                initView(layoutInflater.inflate(R.layout.item_term, binding.viewContainer))
            }
        }
        binding.viewAdaptive.setOnClickListener {
            if (binding.viewAdaptive.isChecked)
                return@setOnClickListener
            else {
                binding.viewDefault.isChecked = false
                binding.viewAdaptive.isChecked = true
                binding.viewContainer.removeAllViews()
                initView(layoutInflater.inflate(R.layout.item_term_2, binding.viewContainer))
            }
        }

        when (loader.termsViewType) {
            0 -> {
                binding.viewDefault.isChecked = true
                initView(layoutInflater.inflate(R.layout.item_term, binding.viewContainer))
            }
            else -> {
                binding.viewAdaptive.isChecked = true
                initView(layoutInflater.inflate(R.layout.item_term_2, binding.viewContainer))
            }
        }

        return binding.root
    }

    override fun onStop() {
        super.onStop()
        loader.termsViewType = if (binding.viewDefault.isChecked) 0 else 1
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView(view: View) {
        view.findViewById<TextView>(R.id.text1).text = getString(R.string.example_term)
        view.findViewById<TextView>(R.id.text2).text = getString(R.string.example_description)
    }

    companion object {
        fun newInstance() = TermsViewFragment()
    }
}