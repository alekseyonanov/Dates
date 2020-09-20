package com.nollpointer.dates.ui.link

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nollpointer.dates.R
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.ui.practise.PractiseSettingsFragment
import com.nollpointer.dates.ui.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_link.*

/**
 * @author Onanov Aleksey (@onanov)
 */
class LinkFragment : BaseFragment() {

    private lateinit var practise: Practise

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            practise = it.getParcelable<Practise>(LINK) as Practise
        }
    }

    override fun getStatusBarColorRes() = R.color.colorBackground

    override fun isStatusBarLight() = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_link, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        linkBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        linkSettings.setOnClickListener {
            requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frameLayout, PractiseSettingsFragment.newInstance(practise))
                    .addToBackStack(null)
                    .commit()
        }
    }

    companion object {

        private const val LINK = "Link"

        fun newInstance(practise: Practise) =
                LinkFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(LINK,practise)
                    }
                }
    }
}