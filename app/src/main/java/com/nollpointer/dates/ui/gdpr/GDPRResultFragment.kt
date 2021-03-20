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
import com.nollpointer.dates.databinding.FragmentGdprResultBinding
import com.nollpointer.dates.other.AppNavigator
import com.nollpointer.dates.other.Loader
import com.nollpointer.dates.ui.activity.MainActivity
import com.nollpointer.dates.ui.view.BaseFragment
import java.util.*
import javax.inject.Inject

/**
 * Экран "Результат выбора GDPR"
 *
 * @author Onanov Aleksey (@onanov)
 */
class GDPRResultFragment : BaseFragment() {

    private var _binding: FragmentGdprResultBinding? = null
    private val binding: FragmentGdprResultBinding
        get() = _binding!!

    @Inject
    lateinit var loader: Loader

    @Inject
    lateinit var navigator: AppNavigator

    override val statusBarColorRes = R.color.colorPrimary

    override val isStatusBarLight = false

    override val isBottomNavigationViewHidden = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View { // Inflate the layout for this fragment
        _binding = FragmentGdprResultBinding.inflate(inflater, container, false)

        binding.text.apply {
            movementMethod = LinkMovementMethod.getInstance()
            text = if (requireArguments().getBoolean(RESULT_GDPR)) {
                getString(R.string.gdpr_agree_text)
            } else {
                getString(R.string.gdpr_disagree_text)
            }
        }
        binding.closeText.text =
                SpannableString(getString(R.string.gdpr_close).toUpperCase(Locale.ROOT)).apply {
                    setSpan(UnderlineSpan(), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
        //TODO: Исправить этот момент и перенести в навигатор
        binding.close.setOnClickListener { (requireActivity() as MainActivity).replaceDatesFragment() }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val RESULT_GDPR = "RESULT_GDPR"

        @JvmStatic
        fun newInstance(result: Boolean) = GDPRResultFragment().apply {
            arguments = Bundle().apply {
                putBoolean(RESULT_GDPR, result)
            }
        }
    }
}