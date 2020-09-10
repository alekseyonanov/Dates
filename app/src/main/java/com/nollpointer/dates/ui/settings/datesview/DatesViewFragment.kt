package com.nollpointer.dates.ui.settings.datesview

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.nollpointer.dates.R
import com.nollpointer.dates.other.Loader
import kotlinx.android.synthetic.main.fragment_dates_view.*

class DatesViewFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dates_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        datesViewToolbar.setNavigationOnClickListener {
            fragmentManager!!.popBackStack()
        }

        datesViewStandard.setOnClickListener {
            if(datesViewStandard.isChecked)
                return@setOnClickListener
            else {
                datesViewAdaptive.isChecked = false
                datesViewStandard.isChecked = true
                datesViewContainer.removeAllViews()
                initView(layoutInflater.inflate(R.layout.item_dates,datesViewContainer))
            }
        }
        datesViewAdaptive.setOnClickListener {
            if(datesViewAdaptive.isChecked)
                return@setOnClickListener
            else {
                datesViewStandard.isChecked = false
                datesViewAdaptive.isChecked = true
                datesViewContainer.removeAllViews()
                initView(layoutInflater.inflate(R.layout.item_dates_2,datesViewContainer))
            }
        }

        when(Loader.getDatesViewType(context as Context)){
            0 -> {
                datesViewStandard.isChecked = true
                initView(layoutInflater.inflate(R.layout.item_dates,datesViewContainer))
            }
            else -> {
                datesViewAdaptive.isChecked = true
                initView(layoutInflater.inflate(R.layout.item_dates_2,datesViewContainer))
            }
        }

    }

    override fun onStop() {
        super.onStop()
        Loader.setDatesViewType(context as Context,
                if(datesViewStandard.isChecked)
                    0
                else
                    1)
    }

    private fun initView(view: View) {
        view.findViewById<TextView>(R.id.text1).text = getString(R.string.example_date)
        view.findViewById<TextView>(R.id.text2).text = getString(R.string.example_event)
        view.findViewById<TextView>(R.id.text3).apply {
            text = getString(R.string.example_month)
            visibility = View.VISIBLE
        }
    }

    companion object {
        fun newInstance() = DatesViewFragment()
    }
}