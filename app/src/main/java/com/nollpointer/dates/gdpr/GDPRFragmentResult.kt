package com.nollpointer.dates.gdpr

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.nollpointer.dates.R
import com.nollpointer.dates.dates.DatesFragment

class GDPRFragmentResult : Fragment() {

    private lateinit var infoText: TextView
    private lateinit var closeButton: LinearLayout
    private lateinit var closeText: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_gdpr_result, container, false)
        val bundle = arguments
        val isGdprAgree = bundle!!.getBoolean(RESULT_GDPR)
        initViews(view)
        prepareResult(isGdprAgree)
        return view
    }

    private fun initViews(mainView: View) {
        infoText = mainView.findViewById(R.id.info_text)
        closeButton = mainView.findViewById(R.id.close_button)
        closeText = mainView.findViewById(R.id.close_text)
    }

    private fun prepareResult(isGdprAgree: Boolean) {
        infoText.movementMethod = LinkMovementMethod.getInstance()
        if (isGdprAgree) {
            infoText.text = getString(R.string.gdpr_agree_text)
        } else {
            infoText.text = getString(R.string.gdpr_disagree_text)
        }
        val close = getString(R.string.gdpr_close).toUpperCase()
        val spannableClose = SpannableString(close)
        spannableClose.setSpan(UnderlineSpan(), 0, spannableClose.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        closeText.text = spannableClose
        closeButton.setOnClickListener { fragmentManager!!.beginTransaction().replace(R.id.frameLayout, DatesFragment()).commit() }
    }

    companion object {
        private const val RESULT_GDPR = "result_gdpr"
        @JvmStatic
        fun getInstance(result: Boolean): GDPRFragmentResult {
            val fragmentResult = GDPRFragmentResult()
            val bundle = Bundle()
            bundle.putBoolean(RESULT_GDPR, result)
            fragmentResult.arguments = bundle
            return fragmentResult
        }
    }
}