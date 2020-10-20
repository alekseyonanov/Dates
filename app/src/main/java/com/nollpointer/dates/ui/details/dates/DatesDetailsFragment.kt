package com.nollpointer.dates.ui.details.dates

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nollpointer.dates.R
import com.nollpointer.dates.app.App
import com.nollpointer.dates.model.Date
import com.nollpointer.dates.ui.activity.MainActivity
import com.nollpointer.dates.ui.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_dates_details.*

/**
 * @author Onanov Aleksey (@onanov)
 */
class DatesDetailsFragment : BaseFragment() {

    private lateinit var viewModel: DatesDetailsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dates_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        datesDetailsArrowBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        datesDetailsWikiLinkTextView.setOnClickListener {
            viewModel.onWikiLinkClicked()
        }
        datesDetailsGoogleSearchLinkTextView.setOnClickListener {
            viewModel.onGoogleLinkClicked()
        }
        datesDetailsYandexSearchLinkTextView.setOnClickListener {
            viewModel.onYandexLinkClicked()
        }

        viewModel = DatesDetailsViewModel(context as Context, App.api).apply {
            date = arguments?.getParcelable<Date>(DATE) as Date
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
        datesDetailsSubjectTextView.text = getString(R.string.dates_details_year, data.first) //TODO месяц
        datesDetailsDescriptionTextView.text = Html.fromHtml("<b>${data.first} год(а)</b> — ${data.second}")
    }

    private fun setWikiDescription(wikiDescription: String) {
        datesDetailsWikiDescriptionTextView.text = Html.fromHtml(wikiDescription)
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

        private const val DATE = "Date"

        fun newInstance(date: Date) = DatesDetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(DATE, date)
            }
        }
    }
}