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
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.nollpointer.dates.R
import com.nollpointer.dates.model.FragmentPage
import com.nollpointer.dates.ui.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_practise.*

/**
 * @author Onanov Aleksey (@onanov)
 */
class PractiseFragment : BaseFragment() {

    private lateinit var fragmentPages: List<FragmentPage>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentPages = listOf(
                FragmentPage(getString(R.string.practise_dates_title), DatesPractiseFragment(), FragmentPage.PRACTISE_DATES),
                FragmentPage(getString(R.string.practise_terms_title), TermsPractiseFragment(), FragmentPage.PRACTISE_TERMS))
    }

    override fun getStatusBarColorRes() = R.color.colorPrimary

    override fun isStatusBarLight() = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_practise, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        practiseViewPager.adapter = PageAdapter(fragmentPages, childFragmentManager)

        practiseViewPager.pageMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16F, resources.displayMetrics).toInt()

        practiseTabLayout.apply {
            tabGravity = TabLayout.GRAVITY_FILL
            setupWithViewPager(practiseViewPager)

            addOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    practiseViewPager.currentItem = tab.position
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {}
                override fun onTabReselected(tab: TabLayout.Tab) {
                    val fragmentPage = fragmentPages[tab.position]
                    if (fragmentPage.isScrollable) {
                        fragmentPage.scrollTop()
                    }
                }
            })
        }
    }

    fun scrollToTop() {

    }

    internal class PageAdapter(private val fragmentPages: List<FragmentPage>, val fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
        private var currentFragment: Fragment? = null

        override fun getCount(): Int {
            return fragmentPages.size
        }

        override fun getPageTitle(position: Int): String? {
            return fragmentPages[position].title
        }

        override fun getItem(position: Int): Fragment {
            return fragmentPages[position].fragment
        }

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