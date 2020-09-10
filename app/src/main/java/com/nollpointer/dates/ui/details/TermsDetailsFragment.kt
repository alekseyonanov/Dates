package com.nollpointer.dates.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nollpointer.dates.R
import com.nollpointer.dates.app.App.Companion.api
import com.nollpointer.dates.model.Term
import com.nollpointer.dates.model.WikipediaResponseModel
import com.nollpointer.dates.ui.activity.MainActivity
import kotlinx.android.synthetic.main.fragment_terms_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TermsDetailsFragment : Fragment() {
    private var term: Term = Term()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            term = it.getParcelable<Term>(TERM) as Term
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_terms_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        termsDetailsSubjectTextView.text = term.term

        val specialText = "${term.description[0].toLowerCase()}${term.description.substring(1)}"

        termsDetailsDescriptionTextView.text = Html.fromHtml("<b>${term.term}</b> — $specialText")

        termsDetailsWikiLinkTextView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://ru.wikipedia.org/wiki/${term.request}"))
            startActivity(intent)
        }
        termsDetailsGoogleSearchLinkTextView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=${getLink(term.term)}"))
            startActivity(intent)
        }
        termsDetailsYandexSearchLinkTextView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://yandex.ru/search/?text=${getLink(term.term)}"))
            startActivity(intent)
        }
        termsDetailsArrowBack.setOnClickListener {
            fragmentManager!!.popBackStack()
        }

        api?.getData(term.request)?.enqueue(object : Callback<WikipediaResponseModel?> {
            override fun onFailure(call: Call<WikipediaResponseModel?>, t: Throwable) {
            }

            override fun onResponse(call: Call<WikipediaResponseModel?>, response: Response<WikipediaResponseModel?>) {
                val data = response.body()?.extractHtml ?: "Получена ересь"
                termsDetailsWikiDescriptionTextView.text = Html.fromHtml(data)
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

        private const val TERM = "Term"

        fun newInstance(term: Term) = TermsDetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(TERM, term)
            }
        }
    }
}
