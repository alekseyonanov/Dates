package com.nollpointer.dates.ui.intro

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.nollpointer.dates.R
import com.nollpointer.dates.databinding.FragmentIntroductionBinding
import com.nollpointer.dates.databinding.IntroPageBinding
import com.nollpointer.dates.model.IntroPage
import com.nollpointer.dates.other.AppNavigator
import com.nollpointer.dates.ui.view.BaseFragment
import com.nollpointer.dates.ui.view.DividerView.Companion.TOP_POSITION
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Экран "Приветствие нового пользователя"
 *
 * @author Onanov Aleksey (@onanov)
 */
@AndroidEntryPoint
class IntroductionFragment : BaseFragment() {

    @Inject
    lateinit var navigator: AppNavigator

    private var _binding: FragmentIntroductionBinding? = null
    private val binding: FragmentIntroductionBinding
        get() = _binding!!

    override val statusBarColorRes = R.color.colorPrimary

    override val isStatusBarLight = false

    override val isBottomNavigationViewHidden = true

    //TODO перенести локально
    private var introPages = emptyList<IntroPage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        introPages = resources.getStringArray(R.array.intro_titles)
                .zip(resources.getStringArray(R.array.intro_descriptions).withIndex()) { title, descriptionWithIndex ->
                    IntroPage(title, descriptionWithIndex.value,
                            when (descriptionWithIndex.index) {
                                0 -> R.drawable.ic_intro_app
                                1 -> R.drawable.ic_intro_dates
                                2 -> R.drawable.ic_intro_terms
                                3 -> R.drawable.ic_intro_practise
                                4 -> R.drawable.ic_intro_statistics
                                else -> R.drawable.ic_intro_wiki
                            })
                }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentIntroductionBinding.inflate(inflater, container, false)

        binding.viewPager.apply {
            adapter = PractiseCellAdapter(introPages)
            addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    binding.dots.onPageSelected(position)
                    binding.next.text = when (position) {
                        introPages.lastIndex -> getString(R.string.fragment_introduction_ready)
                        else -> getString(R.string.fragment_introduction_next)
                    }
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    binding.dots.onPageScrolled(position, positionOffset, positionOffsetPixels)
                }
            })
            binding.dots.setupWithViewPager(this)
        }

        binding.skip.setOnClickListener {
            navigator.navigateToGdpr()
        }

        binding.next.setOnClickListener {
            if (binding.viewPager.currentItem == introPages.lastIndex)
                navigator.navigateToGdpr()
            else
                binding.viewPager.setCurrentItem(binding.viewPager.currentItem + 1, true)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class PractiseCellAdapter(private val introPages: List<IntroPage>) : PagerAdapter() {
        override fun instantiateItem(collection: ViewGroup, position: Int): Any {
            val binding = IntroPageBinding.inflate(layoutInflater)
            binding.title.text = introPages[position].title
            binding.description.text = introPages[position].description
            binding.image.setImageResource(introPages[position].imageRes)
            binding.divider.apply {
                type = (position + 1) % 2
                when (type) {
                    TOP_POSITION -> {
                        setBackgroundColor(Color.WHITE)
                    }
                    else -> {
                        setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    }
                }
            }

            collection.addView(binding.root)
            return binding.root
        }

        override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
            collection.removeView(view as View)
        }

        override fun getCount() = introPages.size

        override fun isViewFromObject(view: View, other: Any) = view === other

        override fun getPageTitle(position: Int) = ""
    }

    companion object {

        @JvmStatic
        fun newInstance() = IntroductionFragment()
    }
}