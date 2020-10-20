package com.nollpointer.dates.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nollpointer.dates.R
import com.nollpointer.dates.api.WikipediaApi
import com.nollpointer.dates.app.App
import com.nollpointer.dates.di.DaggerAppComponent
import com.nollpointer.dates.model.Date
import com.nollpointer.dates.model.WikipediaResponseModel
import com.nollpointer.dates.ui.activity.MainActivity
import com.nollpointer.dates.ui.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_dates_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * @author Onanov Aleksey (@onanov)
 */
class DatesDetailsFragment : BaseFragment() {

    @Inject
    lateinit var api: WikipediaApi

    private var date = Date()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerAppComponent.create().inject(this)
        arguments?.let {
            date = it.getParcelable<Date>(DATE) as Date
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dates_details, container, false)
    }

    override fun getStatusBarColorRes() = android.R.color.white

    override fun isStatusBarLight() = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        datesDetailsSubjectTextView.text = getString(R.string.dates_details_year, date.date) //TODO месяц

        val specialText = "${date.event[0].toLowerCase()}${date.event.substring(1)}"

        datesDetailsDescriptionTextView.text = Html.fromHtml("<b>${date.date} год(а)</b> — $specialText")

        datesDetailsWikiLinkTextView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://ru.wikipedia.org/wiki/${date.request}"))
            startActivity(intent)
        }
        datesDetailsGoogleSearchLinkTextView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=${getLink(date.event)}"))
            startActivity(intent)
        }
        datesDetailsYandexSearchLinkTextView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://yandex.ru/search/?text=${getLink(date.event)}"))
            startActivity(intent)
        }
        datesDetailsArrowBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        api.getData(date.request)?.enqueue(object : Callback<WikipediaResponseModel?> {
            override fun onFailure(call: Call<WikipediaResponseModel?>, t: Throwable) {
            }

            override fun onResponse(call: Call<WikipediaResponseModel?>, response: Response<WikipediaResponseModel?>) {
                val data = response.body()?.extractHtml ?: "Получена ересь"
                if (datesDetailsWikiDescriptionTextView != null)
                    datesDetailsWikiDescriptionTextView.text = Html.fromHtml(data)
            }
        })
    }

    private fun getLink(originalText: String): String {
        val link = StringBuilder()

        originalText.split(" ").forEach {
            link.append(it)
            link.append("+")
        }

        return link.toString()
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