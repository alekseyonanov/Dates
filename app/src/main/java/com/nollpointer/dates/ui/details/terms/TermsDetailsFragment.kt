package com.nollpointer.dates.ui.details.terms

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.viewModels
import com.nollpointer.dates.R
import com.nollpointer.dates.databinding.FragmentDetailsBinding
import com.nollpointer.dates.model.Term
import com.nollpointer.dates.ui.view.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Экран "Подробности по термину"
 *
 * @author Onanov Aleksey (@onanov)
 */
@AndroidEntryPoint
class TermsDetailsFragment : BaseFragment() {

    private val viewModel by viewModels<TermsDetailsViewModel>()

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() = _binding!!

    override val statusBarColorRes = android.R.color.white

    override val isStatusBarLight = true

    override val isBottomNavigationViewHidden = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        binding.arrowBack.setOnClickListener {
            viewModel.onArrowBackClicked()
        }

        binding.report.setOnClickListener {
            viewModel.onReportClicked()
        }

        binding.wikiLink.setOnClickListener {
            viewModel.onWikiLinkClicked()
        }
        binding.googleLink.setOnClickListener {
            viewModel.onGoogleLinkClicked()
        }
        binding.yandexLink.setOnClickListener {
            viewModel.onYandexLinkClicked()
        }

        viewModel.apply {
            term = arguments?.getParcelable<Term>(TERM) as Term
            wikiDescriptionLiveData.observe({ lifecycle }, ::setWikiDescription)
            dataLiveData.observe({ lifecycle }, ::setData)
            errorLiveData.observe({ lifecycle }, ::showError)
            loadingLiveData.observe({ lifecycle }, ::showLoading)
            start()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setData(data: Pair<String, String>) {
        binding.subject.text = getString(R.string.dates_details_year, data.first) //TODO месяц
        binding.datesDescription.text = HtmlCompat.fromHtml("<b>${data.first} год(а)</b> — ${data.second}", HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun setWikiDescription(wikiDescription: String) {
        binding.wikiDescription.text = HtmlCompat.fromHtml(wikiDescription, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun showError(error: String) {
        AlertDialog.Builder(context)
                .setMessage(error)
                .setPositiveButton(getString(R.string.ok_button)) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
    }

    private fun showLoading(isLoading: Boolean) {

    }

    companion object {
        private const val TERM = "Term"

        @JvmStatic
        fun newInstance(term: Term) = TermsDetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(TERM, term)
            }
        }
    }
}
