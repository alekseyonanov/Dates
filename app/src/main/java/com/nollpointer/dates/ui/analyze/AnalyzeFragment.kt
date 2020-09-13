package com.nollpointer.dates.ui.analyze

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nollpointer.dates.R
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.model.PractiseResult
import com.nollpointer.dates.ui.view.BaseFragment

/**
 * @author Onanov Aleksey (@onanov)
 */
class AnalyzeFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_analyze, container, false)
    }

    override fun getStatusBarColorRes() = R.color.colorPrimary

    override fun isStatusBarLight() = false

    companion object {

        private const val PRACTISE = "Practise"
        private const val RESULTS = "Results"

        @JvmStatic
        fun newInstance(practise: Practise, results: List<PractiseResult>) = AnalyzeFragment().apply {
            arguments = Bundle().apply {
                putParcelable(PRACTISE, practise)
                putParcelableArrayList(RESULTS, ArrayList(results))
            }
        }
    }
}