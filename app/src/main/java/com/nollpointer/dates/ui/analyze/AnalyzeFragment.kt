package com.nollpointer.dates.ui.analyze

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nollpointer.dates.R
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.model.PractiseResult


class AnalyzeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_analyze, container, false)
    }

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