package com.nollpointer.dates.ui.link

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nollpointer.dates.R
import com.nollpointer.dates.databinding.FragmentLinkBinding
import com.nollpointer.dates.model.Date
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

    private val taskAdapter = TaskLinkAdapter()
    private val answersAdapter = AnswersLinkAdapter()

    override val statusBarColorRes = R.color.colorBackground

    override val isStatusBarLight = true

    override val isBottomNavigationViewHidden = true

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

        binding.taskRecyclerView.apply {
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
        }

        binding.answersRecyclerView.apply {
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            adapter = answersAdapter
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
        }

        viewModel.apply {
            practise = requireArguments().getParcelable(LINK)!!
            controlsVisibilityLiveData.observe({ lifecycle }, ::showControls)
            infoLiveData.observe({ lifecycle }, ::setPractiseInfo)
            checkEnabilityLiveData.observe({ lifecycle }, ::setCheckEnable)
            possibleAnswersLiveData.observe({ lifecycle }, ::setPossibleAnswers)
            questionsLiveData.observe({ lifecycle }, ::setQuestions)
            start()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

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

    private fun setPossibleAnswers(answers: List<Date>) {
        answersAdapter.items = answers.map { it.event }
    }

    private fun setQuestions(questions: List<Date>) {
        taskAdapter.items = questions.map { it.date }
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