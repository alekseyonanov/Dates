package com.nollpointer.dates.ui.practise

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.nollpointer.dates.R
import com.nollpointer.dates.databinding.FragmentPractiseBinding
import com.nollpointer.dates.model.FragmentPage
import com.nollpointer.dates.other.SimpleOnTabSelectedListener
import com.nollpointer.dates.ui.view.BaseFragment

/**
 * Экран "Практика"
 *
 * @author Onanov Aleksey (@onanov)
 */
class PractiseFragment : BaseFragment() {

    private var _binding: FragmentPractiseBinding? = null
    private val binding: FragmentPractiseBinding
        get() = _binding!!

    private lateinit var fragmentPages: List<FragmentPage>

    override val statusBarColorRes = R.color.colorPrimary

    override val isStatusBarLight = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentPages = listOf(
                FragmentPage(getString(R.string.practise_dates_title), DatesPractiseFragment(), FragmentPage.PRACTISE_DATES),
                FragmentPage(getString(R.string.practise_terms_title), TermsPractiseFragment(), FragmentPage.PRACTISE_TERMS))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentPractiseBinding.inflate(inflater, container, false)

        binding.viewPager.apply {
            adapter = PageAdapter(fragmentPages, childFragmentManager)
            binding.viewPager.pageMargin =
                    TypedValue
                            .applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                    16F,
                                    resources.displayMetrics)
                            .toInt()
        }

        binding.tabLayout.apply {
            tabGravity = TabLayout.GRAVITY_FILL
            setupWithViewPager(binding.viewPager)

            addOnTabSelectedListener(object : SimpleOnTabSelectedListener() {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    binding.viewPager.currentItem = tab.position
                }

                override fun onTabReselected(tab: TabLayout.Tab) {
                    val fragmentPage = fragmentPages[tab.position]
                    if (fragmentPage.isScrollable) {
                        fragmentPage.scrollTop()
                    }
                }
            })
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun scrollToTop() {

    }

    internal class PageAdapter(
            private val fragmentPages: List<FragmentPage>,
            fragmentManager: FragmentManager)
        : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private var currentFragment: Fragment? = null

        override fun getCount() = fragmentPages.size

        override fun getPageTitle(position: Int) = fragmentPages[position].title

        override fun getItem(position: Int) = fragmentPages[position].fragment

        override fun setPrimaryItem(container: ViewGroup, position: Int, obj: Any) {
            super.setPrimaryItem(container, position, obj)
            val fragmentPage: FragmentPage = fragmentPages[position]
            currentFragment = obj as Fragment
            if (currentFragment == fragmentPage.fragment) {
                return
            }
            fragmentPage.fragment = currentFragment as Fragment
        }

    }

    companion object {

        @JvmStatic
        fun newInstance() = PractiseFragment()
    }
}