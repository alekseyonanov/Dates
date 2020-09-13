package com.nollpointer.dates.ui.practise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nollpointer.dates.R
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.ui.practiseselect.SingleSelectAdapter
import kotlinx.android.synthetic.main.fragment_practise_settings.*

/**
 * @author Onanov Aleksey (@onanov)
 */
class PractiseSettingsFragment : Fragment() {

    private lateinit var practise: Practise

    private lateinit var singleSelectAdapter: SingleSelectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            practise = it.getParcelable<Practise>(PRACTISE_SETTINGS) as Practise
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_practise_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        practiseSettingsToolbar.setNavigationOnClickListener {
            fragmentManager!!.popBackStack()
        }

        singleSelectAdapter = SingleSelectAdapter(resources.getTextArray(R.array.pick_type)).apply {
            onItemHasSelected = { isSelected ->

            }
            onItemSelected = { type ->
                practise.type = type
            }
            selectedItem = practise.type
        }

        practiseSettingsSingleRecyclerView.apply {
            adapter = singleSelectAdapter
            layoutManager = LinearLayoutManager(this.context)
            isNestedScrollingEnabled = false
        }
    }

    companion object {

        private const val PRACTISE_SETTINGS = "Practise_Settings"

        @JvmStatic
        fun newInstance(practise: Practise) =
                PractiseSettingsFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(PRACTISE_SETTINGS, practise)
                    }
                }
    }
}