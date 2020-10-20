package com.nollpointer.dates.ui.details.terms

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nollpointer.dates.R
import com.nollpointer.dates.app.App
import com.nollpointer.dates.model.Term
import com.nollpointer.dates.ui.activity.MainActivity
import com.nollpointer.dates.ui.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_terms_details.*

/**
 * @author Onanov Aleksey (@onanov)
 */
class TermsDetailsFragment : BaseFragment() {

    private lateinit var viewModel: TermsDetailsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_terms_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        termsDetailsWikiLinkTextView.setOnClickListener {
            viewModel.onWikiLinkClicked()
        }
        termsDetailsGoogleSearchLinkTextView.setOnClickListener {
            viewModel.onGoogleLinkClicked()
        }
        termsDetailsYandexSearchLinkTextView.setOnClickListener {
            viewModel.onYandexLinkClicked()
        }
        termsDetailsArrowBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        viewModel = TermsDetailsViewModel(context as Context, App.api).apply {
            term = arguments?.getParcelable<Term>(TERM) as Term
            wikiDescriptionLiveData.observe({ lifecycle }, ::setWikiDescription)
            dataLiveData.observe({ lifecycle }, ::setData)
            errorLiveData.observe({lifecycle}, ::showError)
            loadingLiveData.observe({lifecycle}, ::showLoading)
            start()
        }
    }

    override fun getStatusBarColorRes() = android.R.color.white

    override fun isStatusBarLight() = true

    private fun setData(data: Pair<String, String>) {
        termsDetailsSubjectTextView.text = getString(R.string.dates_details_year, data.first) //TODO месяц
        termsDetailsDescriptionTextView.text = Html.fromHtml("<b>${data.first} год(а)</b> — ${data.second}")
    }

    private fun setWikiDescription(wikiDescription: String) {
        termsDetailsWikiDescriptionTextView.text = Html.fromHtml(wikiDescription)
    }

    private fun showError(error: String) {
        AlertDialog.Builder(context)
                .setMessage(error)
                .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
    }

    private fun showLoading(isLoading: Boolean) {

    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).hideBottomNavigationView()
    }

    companion object {

        private const val TERM = "Term"

        fun newInstance(term: Term) = TermsDetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(TERM, term)
            }
        }
    }
}
