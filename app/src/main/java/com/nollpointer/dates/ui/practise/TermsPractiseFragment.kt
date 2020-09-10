package com.nollpointer.dates.ui.practise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nollpointer.dates.R

/**
 * @author Onanov Aleksey (@onanov)
 */
class TermsPractiseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_terms_practise, container, false)
    }

    

    companion object {
        @JvmStatic
        fun newInstance() =
                TermsPractiseFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}