package com.nollpointer.dates.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nollpointer.dates.R
import com.nollpointer.dates.databinding.FragmentStatisticsBinding
import com.nollpointer.dates.ui.view.BaseFragment

/**
 * @author Onanov Aleksey (@onanov)
 */
class StatisticsFragment : BaseFragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding: FragmentStatisticsBinding
        get() = _binding!!

    override val statusBarColorRes = R.color.colorPrimary

    override val isStatusBarLight = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}