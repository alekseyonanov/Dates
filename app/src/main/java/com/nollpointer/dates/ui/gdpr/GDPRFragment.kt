package com.nollpointer.dates.ui.gdpr

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flurry.android.FlurryAgent
import com.nollpointer.dates.BuildConfig
import com.nollpointer.dates.R
import com.nollpointer.dates.databinding.FragmentGdprBinding
import com.nollpointer.dates.other.AppNavigator
import com.nollpointer.dates.other.Loader
import com.nollpointer.dates.ui.view.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

/**
 * @author Onanov Aleksey (@onanov)
 */
@AndroidEntryPoint
class GDPRFragment : BaseFragment() {

    private var _binding: FragmentGdprBinding? = null
    private val binding: FragmentGdprBinding
        get() = _binding!!

    @Inject
    lateinit var loader: Loader

    @Inject
    lateinit var navigator: AppNavigator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentGdprBinding.inflate(inflater, container, false)

        binding.text.apply {
            val mainText = getString(R.string.gdpr_main_text)
            movementMethod = LinkMovementMethod.getInstance()

            text = SpannableString(mainText).apply {
                val learnMore = getString(R.string.gdpr_learn_more)
                val startPosition = mainText.indexOf(learnMore)
                val endPosition = startPosition + learnMore.length
                setSpan(URLSpan(BuildConfig.APPODEAL_PRIVACY_POLICY_LINK),
                        startPosition,
                        endPosition,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        binding.yes.setOnClickListener { showResultFragment(true) }
        binding.no.apply {
            text = SpannableString(
                    getString(R.string.gdpr_disagree).toUpperCase(Locale.ROOT)).apply {
                setSpan(UnderlineSpan(), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            setOnClickListener { showResultFragment(false) }
        }

        return binding.root
    }

    override fun getStatusBarColorRes() = R.color.colorPrimary

    override fun isStatusBarLight() = false

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showResultFragment(result: Boolean) {
        FlurryAgent.logEvent("IsGDPRAgree $result")
        loader.isFirstStart = false
        loader.isGdprAgree = result
        navigator.navigateToGdprResultFragment(result)
    }

    companion object {
        @JvmStatic
        fun newInstance() = GDPRFragment()
    }

}