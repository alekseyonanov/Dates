package com.nollpointer.dates.ui.link

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.nollpointer.dates.R
import com.nollpointer.dates.databinding.FragmentLinkBinding
import com.nollpointer.dates.model.Practise
import com.nollpointer.dates.model.PractiseInfo
import com.nollpointer.dates.ui.view.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author Onanov Aleksey (@onanov)
 */
@AndroidEntryPoint
class LinkFragment : BaseFragment() {

    private var _binding: FragmentLinkBinding? = null
    private val binding: FragmentLinkBinding
        get() = _binding!!

    private val viewModel by viewModels<LinkViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentLinkBinding.inflate(inflater, container, false)

        binding.arrowBack.setOnClickListener {
            viewModel.onArrowBackClicked()
        }

        binding.settings.setOnClickListener {
            viewModel.onSettingsClicked()
        }

        binding.analyze.setOnClickListener {
            viewModel.onAnalyzeClicked()
        }

        binding.next.setOnClickListener {
            viewModel.onNextClicked()
        }

        binding.check.setOnClickListener {
            viewModel.onCheckClicked()
        }

        viewModel.apply {
            practise = requireArguments().getParcelable(LINK)!!
            controlsVisibilityLiveData.observe({ lifecycle }, ::showControls)
            infoLiveData.observe({ lifecycle }, ::setPractiseInfo)
            checkEnabilityLiveData.observe({ lifecycle }, ::setCheckEnable)
            start()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getStatusBarColorRes() = R.color.colorBackground

    override fun isStatusBarLight() = true

    private fun showControls(isVisible: Boolean) {
        if (isVisible) {
            binding.analyze.visibility = View.VISIBLE
            binding.next.visibility = View.VISIBLE
        } else {
            binding.analyze.visibility = View.INVISIBLE
            binding.next.visibility = View.INVISIBLE
        }
    }

    private fun setPractiseInfo(practiseInfo: PractiseInfo) {
        binding.wrongAnswers.text = practiseInfo.wrongAnswers.toString()
        binding.rightAnswers.text = practiseInfo.rightAnswers.toString()
        binding.questionNumber.text = practiseInfo.questionNumber.toString()
    }

    private fun setCheckEnable(isEnabled: Boolean) {
        binding.check.isEnabled = isEnabled
    }

    companion object {
        private const val LINK = "Link"

        @JvmStatic
        fun newInstance(practise: Practise) =
                LinkFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(LINK, practise)
                    }
                }
    }
}