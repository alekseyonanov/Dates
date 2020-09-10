package com.nollpointer.dates.ui.dates

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nollpointer.dates.R
import com.nollpointer.dates.app.App
import com.nollpointer.dates.model.WikipediaResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author Onanov Aleksey (@onanov)
 */
class MoreInfoDialog : BottomSheetDialogFragment() {

    private lateinit var request: String
    private lateinit var mainView: View
    private lateinit var contentTextView: TextView
    private lateinit var goToWikiButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var contentContainer: View
    private lateinit var noInternetView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainView = inflater.inflate(R.layout.dialog_more_info, container, false)
        contentTextView = mainView.findViewById(R.id.dialog_info)
        goToWikiButton = mainView.findViewById(R.id.dialog_go_to_source_button)
        progressBar = mainView.findViewById(R.id.dialog_progressbar)
        contentContainer = mainView.findViewById(R.id.dialog_content)
        noInternetView = mainView.findViewById(R.id.dialog_no_internet)
        request = arguments!!.getString(REQUEST_URL) as String
        //connectWikipedia();
        return mainView
    }

    fun setInfo(data: String?, wiki_url: String?) {
        noInternetView.visibility = View.INVISIBLE
        contentContainer.visibility = View.VISIBLE
        hideProgressBar()
        contentTextView.text = Html.fromHtml(data)
        goToWikiButton.setText(R.string.go_to_wikipedia)
        goToWikiButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(wiki_url)
            startActivity(intent)
        }
    }

    private fun hideProgressBar() {
        progressBar.isClickable = false
        progressBar.visibility = View.GONE
        progressBar.isEnabled = false
    }

    private fun showProgressBar() {
        progressBar.isClickable = true
        progressBar.visibility = View.VISIBLE
        progressBar.isEnabled = true
        contentContainer.visibility = View.INVISIBLE
        noInternetView.visibility = View.INVISIBLE
    }

    private fun connectWikipedia() {
        App.api!!.getData(request)!!.enqueue(object : Callback<WikipediaResponseModel?> {
            override fun onResponse(call: Call<WikipediaResponseModel?>, response: Response<WikipediaResponseModel?>) {
                val data: String
                val url: String
                val wiki: WikipediaResponseModel?
                try {
                    wiki = response.body()
                    data = wiki!!.extractHtml as String
                    url = wiki.contentUrls!!.mobile!!.page as String
                    setInfo(data, url)
                } catch (e: Exception) {
                    Log.wtf("ERROR_RESPONSE", e.toString())
                }
            }

            override fun onFailure(call: Call<WikipediaResponseModel?>, t: Throwable) {
                Log.wtf("ERROR", t.message)
                noInternetConnection()
            }
        })
    }

    fun noInternetConnection() {
        hideProgressBar()
        contentContainer.visibility = View.INVISIBLE
        noInternetView.visibility = View.VISIBLE
        noInternetView.findViewById<View>(R.id.dialog_try_again).setOnClickListener {
            connectWikipedia()
            showProgressBar()
        }
    }

    companion object {
        const val REQUEST_URL = "request_url"
        fun newInstance(url: String?): MoreInfoDialog {
            val dialog = MoreInfoDialog()
            val args = Bundle()
            args.putString(REQUEST_URL, url)
            dialog.arguments = args
            return dialog
        }
    }
}