package com.nollpointer.dates.ui.gdpr

import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.flurry.android.FlurryAgent
import com.nollpointer.dates.R
import com.nollpointer.dates.other.Loader
import com.nollpointer.dates.ui.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_gdpr.*
import java.util.*

/**
 * @author Onanov Aleksey (@onanov)
 */
class GDPRFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gdpr, container, false)
    }

    override fun getStatusBarColorRes() = R.color.colorPrimary

    override fun isStatusBarLight() = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val learnMore = getString(R.string.gdpr_learn_more)
        val mainText = getString(R.string.gdpr_main_text)
        val startPosition = mainText.indexOf(learnMore)
        val endPosition = startPosition + learnMore.length
        val spannableMain = SpannableString(mainText)
        spannableMain.setSpan(URLSpan("https://www.appodeal.com/privacy-policy"), startPosition, endPosition, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        gdprText.apply {
            movementMethod = LinkMovementMethod.getInstance()
            text = spannableMain
        }
        gdprYes.setOnClickListener { showResultFragment(true) }
        val no = getString(R.string.gdpr_disagree).toUpperCase(Locale.ROOT)
        val spannableNo = SpannableString(no)
        spannableNo.setSpan(UnderlineSpan(), 0, spannableNo.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        gdprNo.apply{
            setText(spannableNo, TextView.BufferType.SPANNABLE)
            setOnClickListener { showResultFragment(false) }
        }
    }

    private fun showResultFragment(result: Boolean) {
        FlurryAgent.logEvent("Is_GDPR_Agree $result")
        Loader.setFirstStart(context as Context, false)
        Loader.setGdprAgree(context as Context, result)
        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.frameLayout, GDPRResultFragment.newInstance(result)).commit()
    }

    companion object{
        fun newInstance() = GDPRFragment()
    }

}