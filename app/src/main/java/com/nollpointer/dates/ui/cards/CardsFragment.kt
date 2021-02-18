package com.nollpointer.dates.ui.cards

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.text.Layout.BREAK_STRATEGY_SIMPLE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.nollpointer.dates.databinding.FragmentCardsBinding
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.ui.view.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author Onanov Aleksey (@onanov)
 */
@AndroidEntryPoint
class CardsFragment : BaseFragment() {

    private var _binding: FragmentCardsBinding? = null
    private val binding: FragmentCardsBinding
        get() = _binding!!

    private val viewModel by viewModels<CardsViewModel>()

    override val statusBarColorRes = android.R.color.white

    override val isStatusBarLight = true

    override val isBottomNavigationViewHidden = true

    @SuppressLint("WrongConstant")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentCardsBinding.inflate(inflater, container, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.text.breakStrategy = BREAK_STRATEGY_SIMPLE
        }

        binding.next.setOnClickListener { viewModel.onNextClicked() }
        binding.description.setOnClickListener { viewModel.onDescriptionClicked() }
        binding.arrowBack.setOnClickListener { viewModel.onArrowBackClicked() }
        binding.settings.setOnClickListener { viewModel.onSettingsClicked() }

        viewModel.apply {
            this.practise = arguments?.getParcelable<Practise>(CARDS) as Practise
            questionLiveData.observe({ lifecycle }, ::setData)
            start()
        }

        return binding.root
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        try {
            parentFragmentManager
                    .beginTransaction()
                    .detach(this)
                    .attach(this)
                    .commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setData(data: String) {
        binding.text.text = data
    }

    companion object {
        private const val CARDS = "Cards"

        @JvmStatic
        fun newInstance(practise: Practise) = CardsFragment().apply {
            arguments = Bundle(1).apply {
                putParcelable(CARDS, practise)
            }
        }
    }
}