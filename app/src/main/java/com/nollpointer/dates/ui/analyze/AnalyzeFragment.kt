package com.nollpointer.dates.ui.analyze

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nollpointer.dates.R
import com.nollpointer.dates.databinding.FragmentAnalyzeBinding
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.model.PractiseResult
import com.nollpointer.dates.ui.view.BaseFragment

/**
 * @author Onanov Aleksey (@onanov)
 */
class AnalyzeFragment : BaseFragment() {

    private var _binding: FragmentAnalyzeBinding? = null
    private val binding: FragmentAnalyzeBinding
        get() = _binding!!

    override val statusBarColorRes = R.color.colorPrimary

    override val isStatusBarLight = false

    override val isBottomNavigationViewHidden = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentAnalyzeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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