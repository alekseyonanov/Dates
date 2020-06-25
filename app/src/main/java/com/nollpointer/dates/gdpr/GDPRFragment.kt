package com.nollpointer.dates.gdpr

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.flurry.android.FlurryAgent
import com.nollpointer.dates.R
import com.nollpointer.dates.gdpr.GDPRFragmentResult.Companion.getInstance


class GDPRFragment : Fragment() {

    private lateinit var yesText: TextView
    private lateinit var noText: TextView
    private lateinit var infoText: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_gdpr, container, false)
        initializeViews(view)
        prepareForGDPR()
        return view
    }

    private fun initializeViews(mainView: View) {
        yesText = mainView.findViewById(R.id.yes_text)
        noText = mainView.findViewById(R.id.no_text)
        infoText = mainView.findViewById(R.id.info_text)
    }

    private fun prepareForGDPR() {

        val learnMore = getString(R.string.gdpr_learn_more)
        val mainText = getString(R.string.gdpr_main_text)
        val startPosition = mainText.indexOf(learnMore)
        val endPosition = startPosition + learnMore.length
        val spannableMain = SpannableString(mainText)
        spannableMain.setSpan(URLSpan("https://www.appodeal.com/privacy-policy"), startPosition, endPosition, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        infoText.movementMethod = LinkMovementMethod.getInstance()
        infoText.text = spannableMain
        yesText.setOnClickListener { showResultFragment(true) }
        val no = getString(R.string.gdpr_disagree).toUpperCase()
        val spannableNo = SpannableString(no)
        spannableNo.setSpan(UnderlineSpan(), 0, spannableNo.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        noText.setText(spannableNo, TextView.BufferType.SPANNABLE)

        noText.setOnClickListener { showResultFragment(false) }
    }

    private fun showResultFragment(result: Boolean) {
        SaveGdprResult(context, result).execute()
        //sendResult(result);
        val fragmentResult = getInstance(result)
        fragmentManager!!.beginTransaction().replace(R.id.frameLayout, fragmentResult).commit()
    }

    private class SaveGdprResult internal constructor(var context: Context?, var result: Boolean) : AsyncTask<Void, Void?, Void>() {
        override fun onPreExecute() {
            FlurryAgent.logEvent("Is_GDPR_Agree $result")
        }


        override fun doInBackground(vararg p0: Void): Void? {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putBoolean("gdpr_result", result)
            editor.apply()
            return null
        }
    }
}