package com.nollpointer.dates.ui.practise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nollpointer.dates.databinding.FragmentTermsPractiseBinding

/**
 * Экран со списком практики для терминов
 *
 * @author Onanov Aleksey (@onanov)
 */
class TermsPractiseFragment : Fragment() {

    private var _binding: FragmentTermsPractiseBinding? = null
    private val binding: FragmentTermsPractiseBinding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentTermsPractiseBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = TermsPractiseFragment()
    }
}