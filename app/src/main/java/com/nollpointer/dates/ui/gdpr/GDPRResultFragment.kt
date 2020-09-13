package com.nollpointer.dates.ui.gdpr

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nollpointer.dates.R
import com.nollpointer.dates.ui.dates.DatesFragment
import com.nollpointer.dates.ui.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_gdpr_result.*
import java.util.*

/**
 * @author Onanov Aleksey (@onanov)
 */
class GDPRResultFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gdpr_result, container, false)
    }

    override fun getStatusBarColorRes() = R.color.colorPrimary

    override fun isStatusBarLight() = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gdprResultText.apply {
            movementMethod = LinkMovementMethod.getInstance()
            text = if (arguments?.getBoolean(RESULT_GDPR) == true)
                getString(R.string.gdpr_agree_text)
            else
                getString(R.string.gdpr_disagree_text)
        }
        val close = getString(R.string.gdpr_close).toUpperCase(Locale.ROOT)
        val spannableClose = SpannableString(close)
        spannableClose.setSpan(UnderlineSpan(), 0, spannableClose.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        gdprResultCloseText.text = spannableClose
        gdprResultClose.setOnClickListener { requireActivity().supportFragmentManager.beginTransaction().replace(R.id.frameLayout, DatesFragment()).commit() }
    }

    companion object {

        private const val RESULT_GDPR = "result_gdpr"

        fun newInstance(result: Boolean) = GDPRResultFragment().apply {
            arguments = Bundle().apply {
                putBoolean(RESULT_GDPR, result)
            }
        }
    }
}