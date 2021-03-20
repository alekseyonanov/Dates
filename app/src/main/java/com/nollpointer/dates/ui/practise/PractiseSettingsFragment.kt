package com.nollpointer.dates.ui.practise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.nollpointer.dates.R
import com.nollpointer.dates.databinding.FragmentPractiseSettingsBinding
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.other.AppNavigator
import com.nollpointer.dates.ui.practiseselect.SingleSelectAdapter
import com.nollpointer.dates.ui.view.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Экран "Настройки практики"
 *
 * @author Onanov Aleksey (@onanov)
 */
@AndroidEntryPoint
class PractiseSettingsFragment : BaseFragment() {

    private var _binding: FragmentPractiseSettingsBinding? = null
    private val binding: FragmentPractiseSettingsBinding
        get() = _binding!!

    private lateinit var practise: Practise

    private lateinit var singleSelectAdapter: SingleSelectAdapter

    @Inject
    lateinit var navigator: AppNavigator

    override val statusBarColorRes = R.color.colorPrimary

    override val isStatusBarLight = false

    override val isBottomNavigationViewHidden = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            practise = it.getParcelable<Practise>(PRACTISE_SETTINGS) as Practise
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentPractiseSettingsBinding.inflate(inflater, container, false)

        binding.toolbar.setNavigationOnClickListener {
            navigator.navigateBack()
        }

        singleSelectAdapter = SingleSelectAdapter(resources.getTextArray(R.array.pick_type)).apply {
            onItemHasSelected = { _ ->

            }
            onItemSelected = { type ->
                practise.type = type
            }
            selectedItem = practise.type
        }

        binding.recyclerView.apply {
            adapter = singleSelectAdapter
            layoutManager = LinearLayoutManager(this.context)
            isNestedScrollingEnabled = false
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private const val PRACTISE_SETTINGS = "Practise_Settings"

        @JvmStatic
        fun newInstance(practise: Practise = Practise()) =
                PractiseSettingsFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(PRACTISE_SETTINGS, practise)
                    }
                }
    }
}