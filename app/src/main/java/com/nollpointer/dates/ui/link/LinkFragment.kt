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
 * Экран практики "Связка"
 *
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

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
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
            taskAdapter.onTaskClickedListener = viewModel::onDateClicked
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
        }

        binding.answersRecyclerView.apply {
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            answersAdapter.onAnswerClickedListener = viewModel::onDateClicked
            answersAdapter.onAnswersListChanged = viewModel::onAnswersListChanged
            adapter = answersAdapter
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
        }

        viewModel.apply {
            practise = requireArguments().getParcelable(LINK)!!
            controlButtonsVisibilityLiveData.observe({ lifecycle }, ::showControls)
            infoLiveData.observe({ lifecycle }, ::setPractiseInfo)
            checkEnabilityLiveData.observe({ lifecycle }, ::setCheckEnable)
            possibleAnswersLiveData.observe({ lifecycle }, ::setPossibleAnswers)
            questionLiveData.observe({ lifecycle }, ::setQuestions)
            resultLiveData.observe({ lifecycle }, ::setResult)
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
            binding.analyze.show()
            binding.next.show()
        } else {
            binding.analyze.hide()
            binding.next.hide()
        }
        taskAdapter.isDetailsMode = isVisible
        answersAdapter.isDetailsMode = isVisible
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
        answersAdapter.items = answers
    }

    private fun setQuestions(questions: List<Date>) {
        taskAdapter.items = questions
    }

    private fun setResult(results: List<Int>) {
        answersAdapter.correctAnswers = results
        answersAdapter.notifyDataSetChanged()
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