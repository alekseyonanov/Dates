package com.nollpointer.dates.ui.intro

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.nollpointer.dates.R
import com.nollpointer.dates.model.IntroPage
import com.nollpointer.dates.ui.gdpr.GDPRFragment
import com.nollpointer.dates.ui.view.DividerView
import com.nollpointer.dates.ui.view.DividerView.Companion.TOP_POSITION
import kotlinx.android.synthetic.main.fragment_introduction.*

class IntroductionFragment : Fragment() {

    private var introPages = mutableListOf<IntroPage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val titles = resources.getStringArray(R.array.intro_titles)
        val descriptions = resources.getStringArray(R.array.intro_descriptions)
        for (index in titles.indices) {
            introPages.add(
                    IntroPage(titles[index],
                            descriptions[index],
                            when (index) {
                                0 -> R.drawable.ic_intro_app
                                1 -> R.drawable.ic_intro_dates
                                2 -> R.drawable.ic_intro_terms
                                3 -> R.drawable.ic_intro_practise
                                4 -> R.drawable.ic_intro_statistics
                                else -> R.drawable.ic_intro_wiki
                            }))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_introduction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        introViewPager.adapter = PractiseCellAdapter(introPages)
        introDots.setupWithViewPager(introViewPager)

        introViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                introNextButton.text = when (position) {
                    introPages.lastIndex -> getString(R.string.ready)
                    else -> getString(R.string.next)
                }
            }
        })

        introSkipButton.setOnClickListener {
            fragmentManager!!.beginTransaction().replace(R.id.frameLayout, GDPRFragment.newInstance()).commit()
        }

        introNextButton.setOnClickListener {
            if (introViewPager.currentItem == introPages.lastIndex)
                fragmentManager!!.beginTransaction().replace(R.id.frameLayout, GDPRFragment.newInstance()).commit()
            else
                introViewPager.setCurrentItem(introViewPager.currentItem + 1, true)
        }
    }

    inner class PractiseCellAdapter(val introPages: List<IntroPage>) : PagerAdapter() {
        override fun instantiateItem(collection: ViewGroup, position: Int): Any {
            val view = layoutInflater.inflate(R.layout.intro_page, null)
            view.findViewById<TextView>(R.id.introTitle).text = introPages[position].title
            view.findViewById<TextView>(R.id.introDescription).text = introPages[position].description
            view.findViewById<ImageView>(R.id.introImage).setImageResource(introPages[position].imageRes)
            view.findViewById<DividerView>(R.id.introDivider).apply {
                type = (position + 1) % 2
                when (type) {
                    TOP_POSITION -> {
                        setBackgroundColor(Color.WHITE)
                    }
                    else -> {
                        setBackgroundColor(resources.getColor(R.color.colorPrimary))
                    }
                }
            }

            collection.addView(view)
            return view
        }

        override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
            collection.removeView(view as View)
        }

        override fun getCount(): Int {
            return introPages.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return ""
        }
    }


    companion object {

        @JvmStatic
        fun newInstance() = IntroductionFragment()
    }
}