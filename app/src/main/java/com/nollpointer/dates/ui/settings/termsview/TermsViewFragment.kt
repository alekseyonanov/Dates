package com.nollpointer.dates.ui.settings.termsview

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.nollpointer.dates.R
import com.nollpointer.dates.other.Loader
import kotlinx.android.synthetic.main.fragment_terms_view.*

/**
 * @author Onanov Aleksey (@onanov)
 */
class TermsViewFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_terms_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        termsViewToolbar.setNavigationOnClickListener {
            fragmentManager!!.popBackStack()
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

        when (Loader.getTermsViewType(context as Context)) {
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
        Loader.setTermsViewType(context as Context,
                if (termsViewStandard.isChecked)
                    0
                else
                    1)
    }

    private fun initView(view: View) {
        view.findViewById<TextView>(R.id.text1).text = getString(R.string.example_term)
        view.findViewById<TextView>(R.id.text2).text = getString(R.string.example_description)
    }

    companion object {
        fun newInstance() = TermsViewFragment()
    }
}