package com.nollpointer.dates.ui.settings.termsview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nollpointer.dates.R
import com.nollpointer.dates.other.Loader
import com.nollpointer.dates.ui.view.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_terms_view.*
import javax.inject.Inject

/**
 * @author Onanov Aleksey (@onanov)
 */
@AndroidEntryPoint
class TermsViewFragment : BaseFragment() {

    @Inject
    lateinit var loader: Loader

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_terms_view, container, false)
    }

    override fun getStatusBarColorRes() = R.color.colorPrimary

    override fun isStatusBarLight() = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        termsViewToolbar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        termsViewStandard.setOnClickListener {
            if (termsViewStandard.isChecked)
                return@setOnClickListener
            else {
                termsViewAdaptive.isChecked = false
                termsViewStandard.isChecked = true
                termsViewContainer.removeAllViews()
                initView(layoutInflater.inflate(R.layout.item_term, termsViewContainer))
            }
        }
        termsViewAdaptive.setOnClickListener {
            if (termsViewAdaptive.isChecked)
                return@setOnClickListener
            else {
                termsViewStandard.isChecked = false
                termsViewAdaptive.isChecked = true
                termsViewContainer.removeAllViews()
                initView(layoutInflater.inflate(R.layout.item_term_2, termsViewContainer))
            }
        }

        when (loader.termsViewType) {
            0 -> {
                termsViewStandard.isChecked = true
                initView(layoutInflater.inflate(R.layout.item_term, termsViewContainer))
            }
            else -> {
                termsViewAdaptive.isChecked = true
                initView(layoutInflater.inflate(R.layout.item_term_2, termsViewContainer))
            }
        }

    }

    override fun onStop() {
        super.onStop()
        loader.termsViewType = if (termsViewStandard.isChecked) 0 else 1
    }

    private fun initView(view: View) {
        view.findViewById<TextView>(R.id.text1).text = getString(R.string.example_term)
        view.findViewById<TextView>(R.id.text2).text = getString(R.string.example_description)
    }

    companion object {
        fun newInstance() = TermsViewFragment()
    }
}